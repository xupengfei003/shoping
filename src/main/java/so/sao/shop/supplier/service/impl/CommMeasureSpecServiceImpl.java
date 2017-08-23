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
        // 1. 创建结果对象，封装返回的消息和数据
        Result result = new Result();
        // 2.  根据用户ID(supplierId),查询到自己和管理员添加的所有的计量规格
        List<CommMeasureSpec> commMeasureSpecs = commMeasureSpecDao.find(supplierId);;
        if( null != commMeasureSpecs && commMeasureSpecs.size()>0 ){
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage("获取计量单位成功！");
            result.setData(commMeasureSpecs);
        }else {
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("目前您没有计量单位，请先添加计量单位！");
        }
        return result;

    }

    /**
     * 新添加计量规格
     * @param supplierId 供应商ID
     * @param name 计量规格名称
     * @return Result 结果对象
     */
    @Transactional(rollbackFor = Exception.class)
    public Result saveCommMeasureSpec(long supplierId, String  name){
        //1. 创建结果对象，封装返回的消息和数据
        Result result = new Result();
        // 2. 效验输入的name = null ,"" 就抛出异常
        if(Ognl.isEmpty(name.trim())){
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("请输入计量规格！");
            return result;
        }else {
            name = name.trim();
           if(name.length() > Constant.CheckMaxLength.MAX_MEASURESPEC_NAME_LENGTH){
               result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
               result.setMessage("计量规格不能超过"+ Constant.CheckMaxLength.MAX_MEASURESPEC_NAME_LENGTH+"位！");
               return result;
           }
        }
        // 3.封装需要新加的对象数据
        CommMeasureSpec commMeasureSp = new CommMeasureSpec();
        commMeasureSp.setName(name);
        commMeasureSp.setSupplierId(supplierId);
        commMeasureSp.setCreatedAt(new Date());
        commMeasureSp.setUpdatedAt(new Date());
        // 4. 判断新的名称是否重复： 通过name 查询comm_measure_spec表,name不重复时，执行新增操作
        List<CommMeasureSpec> commMeasureSpec = commMeasureSpecDao.findByName(supplierId,name);
        if( null == commMeasureSpec || commMeasureSpec.size()<=0 ){
            Boolean flag = commMeasureSpecDao.save(commMeasureSp);
            if(flag){
                result.setMessage("添加计量规格成功");
                result.setCode(Constant.CodeConfig.CODE_SUCCESS);
                result.setData(commMeasureSp);
            }else {
                result.setCode(Constant.CodeConfig.CODE_FAILURE);
                result.setMessage("添加计量规格异常，请稍后操作！");
                result.setData(commMeasureSp);
            }

        }else{
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("计量规格名称已经存在，请重新定义！");


        }
        return result;
    }

    /**
     * 根据计量规格主键id 删除对应的计量规格
     * @param supplierId 供应商ID
     * @param id 计量规格主键ID
     * @return Result 结果对象
     */
    @Transactional(rollbackFor = Exception.class)
    public Result deleteOneById(Long supplierId, Long id){
        // 1. 创建结果对象，封装结果数据使用
        Result result = new Result();
        // 2. 效验id = null
        if(null == id){
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("请选择需要删除的规格！");
            return  result;
        }
        // 3. 查出id +delete=0 对应的规格在 supplier_commodity表是否正在使用： delete=1 标识已经被删除，delete=0 使用中
        List<SupplierCommodity> supplierCommodityList = supplierCommodityDao.findAllSupplierCommodityById(id);
        if( null != supplierCommodityList && supplierCommodityList.size()>0 ) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("计量规格正在被使用！");
            result.setData(supplierCommodityList);
        } else{
            //  3. 查出id 对应的comm_measure_spec: supplierId 是否 =0. =0是管理员添加的
            CommMeasureSpec commMeasureSpec = commMeasureSpecDao.findOne(id);
            if( null == commMeasureSpec){
                result.setCode(Constant.CodeConfig.CODE_SUCCESS);
                result.setMessage("没有此规格!");
            }else {
                if( supplierId.longValue() != commMeasureSpec.getSupplierId().longValue() ){
                    result.setCode(Constant.CodeConfig.CODE_FAILURE);
                    result.setMessage("您无权删除此计量规格！");
                    result.setData(commMeasureSpec);
                }else {
                    Boolean flag = commMeasureSpecDao.deleteOneById(id);
                    if(flag){
                        result.setCode(Constant.CodeConfig.CODE_SUCCESS);
                        result.setMessage("删除计量规格成功!");
                        result.setData(commMeasureSpec);
                    }
                }
            }

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
    public Result update(Long supplierId, CommMeasureSpecUpdateInput commMeasureSpecUpdateInput ){
        // 1. a. 创建结果对象Result，封装结果数据使用; b. 封装需要更新的对象参数CommMeasureSpec
        Result result = new Result();
        CommMeasureSpec commMeasureSpec= new CommMeasureSpec();
        commMeasureSpec.setId(commMeasureSpecUpdateInput.getId());
        if(Ognl.isEmpty(commMeasureSpecUpdateInput.getName().trim())){
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("请输入计量规格！");
            return result;
        }
        commMeasureSpec.setName(commMeasureSpecUpdateInput.getName().trim());
        commMeasureSpec.setUpdatedAt(new Date());
        commMeasureSpec.setSupplierId(supplierId);
        // 2. 查出id 对应的comm_measure_spec: supplierId 是否 =0. =0是管理员添加的
        CommMeasureSpec commMeasureSpec01 = commMeasureSpecDao.findOne(commMeasureSpecUpdateInput.getId());
        if(null != commMeasureSpec01){
            if( supplierId.longValue() != commMeasureSpec01.getSupplierId().longValue() ){
                result.setCode(Constant.CodeConfig.CODE_FAILURE);
                result.setMessage("你无权操作！");
                result.setData(commMeasureSpec01);
            }else {
                // 3. 校验: 新的name 是否重复
                List<CommMeasureSpec> commMeasureSpec02 = commMeasureSpecDao.findByName( supplierId,commMeasureSpecUpdateInput.getName().trim());
                if( null != commMeasureSpec02 && commMeasureSpec02.size()>0 ){
                    result.setCode(Constant.CodeConfig.CODE_FAILURE);
                    result.setMessage("规格名称已存在,请重新输入！");
                    result.setData(commMeasureSpec02);
                }else {
                    Boolean flag = commMeasureSpecDao.update(commMeasureSpec);
                    if(flag){
                        result.setCode(Constant.CodeConfig.CODE_SUCCESS);
                        result.setMessage("更新计量规格成功!");
                        result.setData(commMeasureSpec);
                    }
                }
            }
        }else {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("计量规格不存在，请重新选择！");
        }
        return result;
    }


}
