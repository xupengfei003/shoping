package so.sao.shop.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.CommUnitDao;
import so.sao.shop.supplier.dao.SupplierCommodityDao;
import so.sao.shop.supplier.domain.CommUnit;
import so.sao.shop.supplier.domain.SupplierCommodity;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommUnitUpdateInput;
import so.sao.shop.supplier.service.CommUnitService;
import so.sao.shop.supplier.util.Ognl;

import java.util.Date;
import java.util.List;

/**
 * Created by Renzhiping on 2017/8/14.
 */
@Service
public class CommUnitServiceImp implements CommUnitService {
    @Autowired
    private CommUnitDao commUnitDao;
    @Autowired
    private SupplierCommodityDao supplierCommodityDao;

    @Override
    /**
     * 添加商品单位
     * @param supplierId 供应商id
     * @param name 商品单位名称
     * @return 新增结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Result saveCommUnit(Long supplierId, String  name){
        //1.方法返回对象
        Result result = new Result();
        //3.校验输入name是否为空
        if( null == name || Ognl.isEmpty(name.trim())){
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品单位不能为空！");
            return result;
        }else {
            name = name.trim();
            if (name.length()> Constant.CheckMaxLength.MAX_UNIT_NAME_LENGTH){
                result.setCode(Constant.CodeConfig.CODE_FAILURE);
                result.setMessage("商品单位不能超过"+ Constant.CheckMaxLength.MAX_UNIT_NAME_LENGTH+"位！");
                return result;
            }
        }
        //2.封装需要添加的对象数据
        CommUnit commUnit = new CommUnit();
        commUnit.setName(name);
        commUnit.setSupplierId(supplierId);
        commUnit.setCreatedAt(new Date());
        commUnit.setUpdatedAt(new Date());
        //4.判断新增商品单位是否重复,不重复时进行新增，重复时返回重复信息
       List<CommUnit> unitList= commUnitDao.findNameAndSupplierId(supplierId,name);
        if (null==unitList || unitList.size()==0) {
            //5.判断新增成功或失败
            if (commUnitDao.save(commUnit)) {
                result.setCode(Constant.CodeConfig.CODE_SUCCESS);
                result.setMessage("商品单位添加增成功！");
            } else {
                result.setCode(Constant.CodeConfig.CODE_FAILURE);
                result.setMessage("商品单位添加失败！");
            }
            return result;
        } else {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品单位添加失败，新增的商品单位已存在！");
            return result;
        }
    }

    /**
     * 修改商品单位
     * 业务逻辑
     * 1.判断商品单位是否由管理员创建
     * 2.判断修改的商品单位名称有无和存在的商品单位重复
     * @param supplierId 供应商id
     * @param commUnitUpdateInput 商品单位入参
     * @return 修改结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateCommunit(Long supplierId, CommUnitUpdateInput commUnitUpdateInput) {
        // 1. 创建结果对象，封装结果数据使用
        Result result = new Result();
        CommUnit commUnit = new CommUnit();
        commUnit.setId( commUnitUpdateInput.getId());
        //2.修改时判断输商品单位是否为空
        if(Ognl.isEmpty(commUnitUpdateInput.getName().trim())){
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品单位不能为空！");
            return result;
        }
        commUnit.setName(commUnitUpdateInput.getName().trim());
        commUnit.setSupplierId(supplierId);
        commUnit.setUpdatedAt(new Date());
        //3.判断该ID对应的商品单位是否存在
        CommUnit one = commUnitDao.findOne(commUnitUpdateInput.getId());
        if (null == one) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品单位修改失败，该商品单位不存在！");
            return result;
        }
        //4.判断该ID对应的商品单位的supplierId与登录用户supplierId是否相同
        if (supplierId.longValue() != one.getSupplierId().longValue()) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("您无权修改此商品单位！");
            return result;
        }
       List<CommUnit> commUnitList= commUnitDao.findNameAndSupplierId(supplierId,commUnitUpdateInput.getName());
        if (null!= commUnitList && commUnitList.size()>0) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品单位重复,请重新定义！");
            return result;
        }
        //5.判断修改商品单位是否成功
        if (commUnitDao.update(commUnit)) {
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage("商品单位修改成功！");
            return result;
        } else {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品单位修改失败！");
            return result;
        }
    }

    /**
     * 删除商品单位
     * 业务逻辑
     * 1.判断要删除的商品单位是否被supplier_commodity表引用（deleted状态是否1，1表示可以删除）
     * 2.判断是否为管理员添加
     * 3.供应商只能删除自己创建的商品单位（未关联业务）
     * @param supplierId 供应商id
     * @param id 商品单位id
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteCommUnit(Long  supplierId, Long  id) {

        //1.方法返回对象
        Result result = new Result();
        //2.根据该ID在供应商商品表中查询是是否在被使用（未删除状态），在使用状态时不能进行删除操作
        List<SupplierCommodity> supplierCommodityList = supplierCommodityDao.findSupplierCommodityByUnitId(id);
        if (null != supplierCommodityList && supplierCommodityList.size() > 0) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品单位正在使用！");
            return result;
        }
        //3.判断该ID对应的商品单位是否存在
        CommUnit commUnit = commUnitDao.findOne(id);
        if (null == commUnit) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品单位删除失败，该商品单位不存在！");
            return result;
        }
        //4.判断ID对应的商品单位的supplierId与登录用户supplierId是否相同
        if (supplierId.longValue() != commUnit.getSupplierId().longValue()) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("您无权删除此商品单位！");
            return result;
        }
        //5.判断删除操作成功或失败
        if (commUnitDao.delete(id)) {
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage("商品单位删除成功！");
            return result;
        } else {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品单位删除失败！");
            return result;
        }
    }

    /**
     * 查询商品单位集合（查询管理员和供应商单位）
     * @param supplierId 供应商id
     * @return 商品单位对象集合
     */
    @Override
    public Result searchCommUnit(Long supplierId) {
         Result result=new Result();
         List<CommUnit> commUnitList=commUnitDao.search(supplierId);
        if (null == commUnitList || commUnitList.size() == 0) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("查询商品单位失败！");
        } else {
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage("查询商品单位成功！");
            result.setData(commUnitList);
        }
        return result;
    }
}
