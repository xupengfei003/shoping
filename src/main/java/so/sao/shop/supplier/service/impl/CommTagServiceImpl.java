package so.sao.shop.supplier.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.CommTagDao;
import so.sao.shop.supplier.dao.SupplierCommodityDao;
import so.sao.shop.supplier.domain.CommTag;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommTagUpdateInput;
import so.sao.shop.supplier.service.CommTagService;
import so.sao.shop.supplier.util.BeanMapper;

import java.util.Date;
import java.util.List;

/**
 * 商品标签serviceImpl
 *
 * @author zhaoyan
 * @create 2017/8/14 16:15
 */
@Service
public class CommTagServiceImpl implements CommTagService {
    @Autowired
    private CommTagDao commTagDao;
    @Autowired
    private SupplierCommodityDao supplierCommodityDao;

    /**
     * 初始化日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 新增商品标签
     *
     * @param supplierId 供应商ID
     * @param name       商品标签名称
     * @return 新增结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveCommTag(Long supplierId, String name) {
        //判断新增标签名与supplierId是否重复,不重复时进行新增，重复时返回重复信息
        Result result = checkCommTagName(supplierId, name.trim());
        if (null != result) {
            return result;
        } else {
            CommTag commTag = new CommTag();
            commTag.setName(name.trim());
            commTag.setSupplierId(supplierId);
            commTag.setCreatedAt(new Date());
            commTag.setUpdatedAt(new Date());
            //新增操作
            commTagDao.save(commTag);
            return Result.success("商品标签新增成功！");
        }
    }

    /**
     * 根据id删除商品标签
     *
     * @param supplierId 供应商ID
     * @param id         商品标签id
     * @return 删除结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteCommTag(Long supplierId, Long id) {
        //商品标签是否存在与操作权限判断
        Result result = checkExistAndPermission(supplierId, id);
        if (null != result){
            return result;
        }
        //根据该ID在供应商商品表中查询是是否在被使用（未删除状态），在使用状态时不能进行删除操作
        int count = supplierCommodityDao.countSupplierCommodityByTagId(id);
        if (count > 0) {
            return Result.fail("商品标签正在使用，暂时无法删除此标签！");
        }
        //删除操作
        commTagDao.deleteById(id);
        return Result.success("商品标签删除成功！");
    }

    /**
     * 修改商品标签信息
     *
     * @param supplierId         供应商ID
     * @param commTagUpdateInput 商品标签修改入参对象
     * @return 修改结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateCommTag(Long supplierId, CommTagUpdateInput commTagUpdateInput) {
        //商品标签是否存在与操作权限判断
        Result result = checkExistAndPermission(supplierId, commTagUpdateInput.getId());
        if (null != result){
            return result;
        }
        //根据修改的name和supplierId判断是否重复
        Result checkNameResult = checkCommTagName(supplierId, commTagUpdateInput.getName().trim());
        if (null != checkNameResult) {
            return result;
        }
        CommTag commTag = BeanMapper.map(commTagUpdateInput, CommTag.class);
        commTag.setSupplierId(supplierId);
        commTag.setUpdatedAt(new Date());
        //进行修改操作
        commTagDao.update(commTag);
        return Result.success("商品标签修改成功！");
    }

    /**
     * 查询商品标签集合
     *
     * @param supplierId 供应商ID
     * @return 商品标签集合
     */
    @Override
    public Result searchCommTag(Long supplierId) {
        //商品标签查询操作
        List<CommTag> commTags = commTagDao.find(supplierId);
        return Result.success("查询商品标签成功！",commTags);
    }

    /**
     * 根据suppplierId与id进行存在性检验与操作权限校验
     *
     * @param supplierId 供应商ID
     * @param id 商品标签ID
     * @return 返回Result
     */
    private Result checkExistAndPermission(Long supplierId, Long id){
        //商品标签存在性判断
        CommTag commTag = commTagDao.findOne(id);
        if (null == commTag) {
            return Result.fail("该商品标签不存在！");
        }
        //判断ID对应的商品标签supplierId与登录用户supplierId是否相同
        if (!supplierId.equals(commTag.getSupplierId())) {
            return Result.fail("预置商品标签无权操作！");
        }
        return null;
    }

    /**
     * 根据suppplierId与name进行判断商品标签名称是否重复
     *
     * @param supplierId 供应商ID
     * @param name  商品标签名称
     * @return 返回Result
     */
    private Result checkCommTagName(Long supplierId, String name){
        //判断新增标签名是否重复
        int count = commTagDao.countByNameAndSupplierId(name.trim(), supplierId);
        if (count > 0) {
            return  Result.fail("商品标签已存在！");
        } else {
            return null;
        }
    }
}
