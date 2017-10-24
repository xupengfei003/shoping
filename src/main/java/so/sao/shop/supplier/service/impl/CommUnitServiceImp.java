package so.sao.shop.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.dao.CommUnitDao;
import so.sao.shop.supplier.dao.SupplierCommodityDao;
import so.sao.shop.supplier.domain.CommUnit;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommUnitUpdateInput;
import so.sao.shop.supplier.service.CommUnitService;
import so.sao.shop.supplier.util.BeanMapper;

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
     * 添加库存单位
     * @param supplierId 供应商id
     * @param name 库存单位名称
     * @return 新增结果
     */
    @Transactional(rollbackFor = Exception.class)
    public Result saveCommUnit(Long supplierId, String  name){
        //判断新增库存单位与supplierId是否重复,不重复时进行新增，重复时返回重复信息
        Result result=checkUnitName(supplierId,name.trim());
        if(null!=result){
            return result;
        }else{
            //封装需要添加的对象数据
            CommUnit commUnit = new CommUnit();
            commUnit.setName(name);
            commUnit.setSupplierId(supplierId);
            commUnit.setCreatedAt(new Date());
            commUnit.setUpdatedAt(new Date());
            commUnitDao.save(commUnit);
            return Result.success("添加库存单位成功！");
        }
    }

    /**
     * 修改库存单位
     * 业务逻辑
     * 1.判断库存单位是否由管理员创建
     * 2.判断修改的库存单位名称有无和存在的库存单位重复
     * @param supplierId 供应商id
     * @param commUnitUpdateInput 库存单位入参
     * @return 修改结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateCommunit(Long supplierId, CommUnitUpdateInput commUnitUpdateInput)  {
        //库存单位是否存在与操作权限判断
        Result result=checkExistAndPermission(supplierId,commUnitUpdateInput.getId());
        if(null!=result){
            return result;
        }
        //根据修改的name和supplierId判断是否重复
        Result checkName=checkUnitName(supplierId,commUnitUpdateInput.getName().trim());
        if (null!=checkName){
            return checkName;
        }
        CommUnit commUnit= BeanMapper.map(commUnitUpdateInput,CommUnit.class);
        commUnit.setSupplierId(supplierId);
        commUnit.setUpdatedAt(new Date());
        //执行修改操作
        commUnitDao.update(commUnit);
        return Result.success("库存单位修改成功！");
    }

    /**
     * 删除库存单位
     * 业务逻辑
     * 1.判断要删除的库存单位是否被supplier_commodity表引用（deleted状态是否1，1表示已删除）
     * 2.判断是否为管理员添加
     * 3.供应商只能删除自己创建的商品单位（未关联业务）
     * @param supplierId 供应商id
     * @param id 商品单位id
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteCommUnit(Long  supplierId, Long  id) {
        //库存单位是否存在与操作权限判断
        Result result=checkExistAndPermission(supplierId,id);
        if (null!=result){
            return  result;
        }
        //根据该ID在供应商商品表中查询是是否在被使用（delete=0未删除状态），在使用状态时不能进行删除操作
        int count=supplierCommodityDao.countByUnitId(id);
        if (count>0) {
            return Result.fail("库存单位正在使用，暂时无法删除此库存单位！");
        }
        commUnitDao.deleteById(id);
        return Result.success("库存单位删除成功！");
    }
    /**
     * 查询库存单位集合（查询管理员和供应商单位）
     * @param supplierId 供应商id
     * @return 库存单位对象集合
     */
    @Override
    public Result searchCommUnit(Long supplierId) {
         List<CommUnit> commUnitList=commUnitDao.search(supplierId);
            return Result.success("查询库存单位成功！",commUnitList);
    }
    /**
     * 库存单位存在性校验
     * @param supplierId
     * @param name
     * @return
     */
    private Result checkUnitName(Long supplierId, String name ){
        int count=commUnitDao.countByNameAndSupplierId(supplierId,name);
        if(count>0){
            return Result.fail("库存单位已存在！");
        }
        return null;
    }
    private Result checkExistAndPermission(Long supplierId, Long id){
        CommUnit commUnit=commUnitDao.findOne(id);
        if(null==commUnit){
            return Result.fail("该库存单位不存在！");
        }
        if(!supplierId.equals(commUnit.getSupplierId())){
            return Result.fail("公共库存单位，不能操作！");
        }
        return null;
    }
}
