package so.sao.shop.supplier.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.CommMeasureSpecDao;
import so.sao.shop.supplier.dao.SupplierCommodityDao;
import so.sao.shop.supplier.domain.CommMeasureSpec;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommMeasureSpecUpdateInput;
import so.sao.shop.supplier.service.CommMeasureSpecService;
import so.sao.shop.supplier.util.BeanMapper;

import java.util.Date;
import java.util.List;

/**
 * Created by XuPengFei on 2017/8/14.
 */
@Service
public class CommMeasureSpecServiceImpl implements CommMeasureSpecService {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommMeasureSpecDao commMeasureSpecDao;

    @Autowired
    private SupplierCommodityDao supplierCommodityDao;

    /**
     * 查询计量规格，查询出自己的和管理员前期添加的公共的
     * @param supplierId 供应商ID
     * @return Result 结果对象
     */
    @Override
    public Result getCommMeasureSpe(long supplierId) {
        // 根据用户ID(supplierId),查询到自己和管理员添加的所有的计量规格
        List<CommMeasureSpec> commMeasureSpecs = commMeasureSpecDao.find(supplierId);;
           return Result.success("查询商品计量规格成功！",commMeasureSpecs);
    }

    /**
     * 新添加计量规格
     * @param supplierId 供应商ID
     * @param name 计量规格名称
     * @return Result 结果对象
     */
    @Transactional(rollbackFor = Exception.class)
    public Result saveCommMeasureSpec(long supplierId, String name){
        //  判断新的名称是否重复： 通过name 查询comm_measure_spec表,name不重复时，执行新增操作
        Result result = checkIfExistcommMeasureSpecName(supplierId,name);
        if(null == result){
            // 封装需要新加的对象数据
            CommMeasureSpec commMeasureSp = new CommMeasureSpec();
            commMeasureSp.setName(name);
            commMeasureSp.setSupplierId(supplierId);
            commMeasureSp.setCreatedAt(new Date());
            commMeasureSp.setUpdatedAt(new Date());
            commMeasureSpecDao.save(commMeasureSp);
            return  Result.success("商品计量规格新增成功",commMeasureSp);
        }
        return  result;
    }

    /**
     * 根据计量规格主键id 删除对应的计量规格
     * @param supplierId 供应商ID
     * @param id 计量规格主键ID
     * @return Result 结果对象
     */
    @Transactional(rollbackFor = Exception.class)
    public Result deleteById(Long supplierId, Long id){
        // 判断计量规格是不是正在被使用
        int count = supplierCommodityDao.countSupplierCommodityById(id);
        if( count >0 ) {
            return  Result.fail("商品计量规格正在使用，暂时无法删除此计量规格！");
        }
        //判断计量规格是否存在 和  是否自己添加的(只能删除自己的)
        Result result = checkIfInsertByAdminOrExist(supplierId, id);
        if( null == result ){
            commMeasureSpecDao.deleteById(id);
            return Result.success("商品标计量规格除成功!");
        }
        return result;
    }

    /**
     *根据计量规格的CommMeasureSpecUpdateInput更新计量规格
     * @param supplierId 供应商ID
     * @param commMeasureSpecUpdateInput 计量规格入参对象
     * @return Result 结果对象
     */
    @Transactional(rollbackFor = Exception.class)
    public Result update(Long supplierId, CommMeasureSpecUpdateInput commMeasureSpecUpdateInput ) {
        //判断计量规格是否存在  和  是否自己添加的(只能删除自己的)
        Result result = checkIfInsertByAdminOrExist(supplierId, commMeasureSpecUpdateInput.getId());
        if (null == result) {
            //  校验新的name 是否重复
            Result resultName = checkIfExistcommMeasureSpecName(supplierId, commMeasureSpecUpdateInput.getName());
            if (null == resultName) {
                // 封装需要更新的对象参数CommMeasureSpec
                CommMeasureSpec commMeasureSpec = BeanMapper.map(commMeasureSpecUpdateInput, CommMeasureSpec.class);
                commMeasureSpec.setUpdatedAt(new Date());
                commMeasureSpec.setSupplierId(supplierId);
                commMeasureSpecDao.update(commMeasureSpec);
                return Result.success("商品计量规格修改成功！", commMeasureSpec);
            }
            return  resultName;
        }
        return  result;
    }

    /**
     * 判断新的名称是否重复： 通过name 查询comm_measure_spec表,name不重复时，执行新增操作
     * @param supplierId
     * @param name
     * @return
     */
    private  Result checkIfExistcommMeasureSpecName(Long supplierId, String name){
        int count = commMeasureSpecDao.countByNameAndSupplierId(supplierId,name);
        if( count >0 ) {
            return Result.fail("商品计量规格已存在！");
        }else {
            return  null;
        }
    }

    /**
     * 判断计量规格是否存在和 查出id 对应的comm_measure_spec: supplierId 是否 =0. =0是管理员添加的
     * @param supplierId
     * @param id
     * @return
     */
    private  Result checkIfInsertByAdminOrExist(Long supplierId, Long id) {
        CommMeasureSpec commMeasureSpec = commMeasureSpecDao.findOne(id);
        if (null == commMeasureSpec) {
            return Result.fail("该商品计量规格不存在！");
        } else {
            if (!supplierId.equals(commMeasureSpec.getSupplierId())) {
                return Result.fail("预置计量规格无权操作！");
            }
                return null;
        }
    }


}
