package so.sao.shop.supplier.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.dao.CommCartonDao;
import so.sao.shop.supplier.dao.SupplierCommodityDao;
import so.sao.shop.supplier.domain.CommCarton;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommCartonUpdateInput;
import so.sao.shop.supplier.service.CommCartonService;
import so.sao.shop.supplier.util.BeanMapper;

import java.util.Date;
import java.util.List;

/**
 * 商品箱规单位serviceImpl
 * @author chensha
 */
@Service
public class CommCartonServiceImpl implements CommCartonService {
    @Autowired
    private CommCartonDao commCartonDao;
    @Autowired
    private SupplierCommodityDao supplierCommodityDao;

    /**
     * 初始化日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 新增商品箱规单位
     * @param supplierId 供应商ID
     * @param name 箱规单位名称
     * @return 新增结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveCommCarton(Long supplierId, String name) {
        //判断新增箱规单位名是否重复,不重复时进行新增，重复时返回重复信息
        Result result = checkCommCartonName(supplierId, name.trim());
        if (null != result) {
            return result;
        } else {
            CommCarton commCarton = new CommCarton();
            commCarton.setName(name.trim());
            commCarton.setSupplierId(supplierId);
            commCarton.setCreatedAt(new Date());
            commCarton.setUpdatedAt(new Date());

            //新增操作
            commCartonDao.save(commCarton);
            return Result.success("添加箱规单位成功！");
        }
    }

    /**
     * 根据id删除箱规单位
     * @param supplierId 供应商ID
     * @param id 箱规单位id
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteCommCarton(Long supplierId, Long id) {
        //箱规单位是否存在与操作权限判断
        Result result = checkExistAndPermission(supplierId, id);
        if (null != result){
            return result;
        }
        //根据该ID在供应商商品表中查询是是否在被使用，在使用状态时不能进行删除操作
        int count = supplierCommodityDao.countSupplierCommodityByCartonId(id);
        if (count > 0) {
            return Result.fail("该箱规单位正在使用，暂时无法删除！");
        }
        //删除操作
        commCartonDao.deleteById(id);
        return Result.success("箱规单位删除成功！");
    }

    /**
     * 修改商品箱规单位
     * @param supplierId 供应商ID
     * @param commCartonUpdateInput 商品标签修改入参
     * @return 修改结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateCommTag(Long supplierId, CommCartonUpdateInput commCartonUpdateInput) {
        //箱规单位是否存在与操作权限判断
        Result result = checkExistAndPermission(supplierId, commCartonUpdateInput.getId());
        if (null != result){
            return result;
        }
        //判断修改name是否重复
        Result checkNameResult = checkCommCartonName(supplierId, commCartonUpdateInput.getName().trim());
        if (null != checkNameResult) {
            return checkNameResult;
        }
        CommCarton commCarton = BeanMapper.map(commCartonUpdateInput,CommCarton.class);
        commCarton.setSupplierId(supplierId);
        commCarton.setUpdatedAt(new Date());
        //进行修改操作
        commCartonDao.update(commCarton);
        return Result.success("箱规单位修改成功！");
    }

    /**
     * 查询商品箱规单位集合
     * @param supplierId 供应商ID
     * @return 箱规单位集合
     */
    @Override
    public Result searchCommCartons(Long supplierId) {

        List<CommCarton> commCartons = commCartonDao.find(supplierId);
        return Result.success("查询成功！",commCartons);
    }

    /**
     * 根据suppplierId与id进行存在性检验与操作权限校验
     * @param supplierId 供应商ID
     * @param id 箱规单位ID
     * @return 返回Result
     */
    private Result checkExistAndPermission(Long supplierId, Long id){
        //存在性判断
        CommCarton commCarton = commCartonDao.findOne(id);
        if (null == commCarton) {
            return Result.fail("该箱规单位不存在！");
        }
        //判断ID对应的商品标签supplierId与登录用户supplierId是否相同
        if (!supplierId.equals(commCarton.getSupplierId())) {
            return Result.fail("您无权限操作此箱规单位！");
        }
        return null;
    }

    /**
     * 根据suppplierId与name进行判断箱规单位名称是否重复
     * @param supplierId 供应商ID
     * @param name  箱规单位名称
     * @return 返回Result
     */
    private Result checkCommCartonName(Long supplierId, String name){
        //判断新增是否重复
        int count = commCartonDao.countByNameAndSupplierId(name.trim(), supplierId);
        if (count > 0) {
            return  Result.fail("该箱规单位已存在！");
        }
        return null;
    }
}
