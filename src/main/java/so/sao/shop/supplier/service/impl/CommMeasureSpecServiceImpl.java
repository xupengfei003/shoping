package so.sao.shop.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.CommMeasureSpecDao;
import so.sao.shop.supplier.dao.SupplierCommodityDao;
import so.sao.shop.supplier.domain.CommMeasureSpec;
import so.sao.shop.supplier.domain.SupplierCommodity;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommMeasureSpecUpdateInput;
import so.sao.shop.supplier.service.CommMeasureSpecService;
import so.sao.shop.supplier.util.Ognl;

import java.util.Date;
import java.util.List;

/**
 * Created by XuPengFei on 2017/8/14.
 */
@Service
public class CommMeasureSpecServiceImpl implements CommMeasureSpecService {

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
        if( null != commMeasureSpecs && commMeasureSpecs.size()>0 ){
           return Result.success("查询计量规格成功！",commMeasureSpecs);
        }else {
            return Result.fail("暂无数据，请先添加！");
        }
    }

    /**
     * 新添加计量规格
     * @param supplierId 供应商ID
     * @param name 计量规格名称
     * @return Result 结果对象
     */
    @Transactional(rollbackFor = Exception.class)
    public Result saveCommMeasureSpec(long supplierId, String  name){
        //  效验输入的name = null ,"" 就抛出异常
        if( null == name || Ognl.isEmpty(name.trim()) ){
            return Result.fail("计量规格不能为空！");
        }else {
            name = name.trim();
           if(name.length() > Constant.CheckMaxLength.MAX_MEASURESPEC_NAME_LENGTH){
               return Result.fail("计量规格不能超过"+ Constant.CheckMaxLength.MAX_MEASURESPEC_NAME_LENGTH+"位!");
           }
        }
        // 封装需要新加的对象数据
        CommMeasureSpec commMeasureSp = new CommMeasureSpec();
        commMeasureSp.setName(name);
        commMeasureSp.setSupplierId(supplierId);
        commMeasureSp.setCreatedAt(new Date());
        commMeasureSp.setUpdatedAt(new Date());
        // 4. 判断新的名称是否重复： 通过name 查询comm_measure_spec表,name不重复时，执行新增操作
        List<CommMeasureSpec> commMeasureSpecList = commMeasureSpecDao.findByName(supplierId,name);
        if( null == commMeasureSpecList || commMeasureSpecList.size()<=0 ){
            if( commMeasureSpecDao.save(commMeasureSp) ){
                return  Result.success("添加计量规格成功",commMeasureSp);
            }else {
                return Result.success("添加计量规格失败！",commMeasureSp);
            }
        }else{
            return Result.fail("计量规格已存在！");
        }
    }

    /**
     * 根据计量规格主键id 删除对应的计量规格
     * @param supplierId 供应商ID
     * @param id 计量规格主键ID
     * @return Result 结果对象
     */
    @Transactional(rollbackFor = Exception.class)
    public Result deleteOneById(Long supplierId, Long id){
        //  效验id = null
        if(null == id){
            return  Result.fail("请选择需要删除的计量规格！");
        }
        //  查出id +delete=0 对应的规格在 supplier_commodity表是否正在使用： delete=1 标识已经被删除，delete=0 使用中
        List<SupplierCommodity> supplierCommodityList = supplierCommodityDao.findAllSupplierCommodityById(id);
        if( null != supplierCommodityList && supplierCommodityList.size()>0 ) {
            return  Result.fail("计量规格正在使用，暂时无法删除此计量规格！");
        } else{
            //   查出id 对应的comm_measure_spec: supplierId 是否 =0. =0是管理员添加的
            CommMeasureSpec commMeasureSpec = commMeasureSpecDao.findOne(id);
            if( null == commMeasureSpec){
                return  Result.fail("计量规格不存在！");
            }else {
                if( ! supplierId.equals(commMeasureSpec.getSupplierId()) ){
                    return  Result.fail("公共计量规格，不能删除！");
                }else {
                    if(commMeasureSpecDao.deleteOneById(id)){
                        return Result.success("删除计量规格成功!",commMeasureSpec);
                    }
                    return  Result.fail("删除此计量规格失败！");
                }
            }
        }
    }

    /**
     *根据计量规格的CommMeasureSpecUpdateInput更新计量规格
     * @param supplierId 供应商ID
     * @param commMeasureSpecUpdateInput 计量规格入参对象
     * @return Result 结果对象
     */
    @Transactional(rollbackFor = Exception.class)
    public Result update(Long supplierId, CommMeasureSpecUpdateInput commMeasureSpecUpdateInput ){
        // 封装需要更新的对象参数CommMeasureSpec
        CommMeasureSpec commMeasureSpec= new CommMeasureSpec();
        commMeasureSpec.setId(commMeasureSpecUpdateInput.getId());
        if(Ognl.isEmpty(commMeasureSpecUpdateInput.getName().trim())){
            return Result.fail("计量规格不能为空！");
        }
        commMeasureSpec.setName(commMeasureSpecUpdateInput.getName().trim());
        commMeasureSpec.setUpdatedAt(new Date());
        commMeasureSpec.setSupplierId(supplierId);
        //  查出id 对应的comm_measure_spec: supplierId 是否 =0. =0是管理员添加的
        CommMeasureSpec getCommMeasureSpec = commMeasureSpecDao.findOne(commMeasureSpecUpdateInput.getId());
        if(null != getCommMeasureSpec){
            if(  ! supplierId.equals(getCommMeasureSpec.getSupplierId()) ){
                return Result.fail("公共计量规格，不能修改！");
            }else {
                //  校验: 新的name 是否重复
                List<CommMeasureSpec> commMeasureSpecList = commMeasureSpecDao.findByName( supplierId,commMeasureSpecUpdateInput.getName().trim());
                if( null != commMeasureSpecList && commMeasureSpecList.size()>0 ){
                    return Result.fail("计量规格已存在！");
                }else {
                    if(commMeasureSpecDao.update(commMeasureSpec)){
                        return Result.success("更新计量规格成功！",commMeasureSpec);
                    }
                    return Result.fail("更新计量规格失败！");
                }
            }
        }else {
            return Result.fail("计量规格不存在！");
        }
    }


}
