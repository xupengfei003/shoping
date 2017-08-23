package so.sao.shop.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.CommTagDao;
import so.sao.shop.supplier.dao.SupplierCommodityDao;
import so.sao.shop.supplier.domain.CommTag;
import so.sao.shop.supplier.domain.SupplierCommodity;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommTagUpdateInput;
import so.sao.shop.supplier.service.CommTagService;
import so.sao.shop.supplier.util.Ognl;

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
     * 新增商品标签
     *
     * @param supplierId 供应商ID
     * @param name       商品标签名称
     * @return 新增结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveCommTag(Long supplierId, String name) {
        //方法返回对象
        Result result = new Result();
        CommTag commTag = new CommTag();
        commTag.setName(name.trim());
        commTag.setSupplierId(supplierId);
        commTag.setCreatedAt(new Date());
        commTag.setUpdatedAt(new Date());
        //判断标签名为null或空
        if (Ognl.isEmpty(name.trim())) {
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("请输入商品标签名称！");
            return result;
        }
        //校验商品标签名称长度
        if (name.trim().length() > Constant.CheckMaxLength.MAX_TAG_NAME_LENGTH) {
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品标签名称长度不能超过" + Constant.CheckMaxLength.MAX_TAG_NAME_LENGTH + "位！");
            return result;
        }
        //判断新增标签名余supplierId是否重复,不重复时进行新增，重复时返回重复信息
        List<CommTag> list = commTagDao.findByNameAndSupplierId(name.trim(), supplierId);
        if (null != list && list.size() == 0) {
            //判断新增成功或失败
            if (commTagDao.save(commTag)) {
                result.setCode(Constant.CodeConfig.CODE_SUCCESS);
                result.setMessage("商品标签新增成功！");
            } else {
                result.setCode(Constant.CodeConfig.CODE_FAILURE);
                result.setMessage("商品标签新增失败！");
            }
            return result;
        } else {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品标签新增失败，新增的商品标签已存在！");
            return result;
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
        //方法返回对象
        Result result = new Result();
        //判断该ID对应的商品标签是否存在
        CommTag commTag = commTagDao.findOne(id);
        if (null == commTag) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品标签删除失败，该标签不存在！");
            return result;
        }
        //判断ID对应的商品标签supplierId与登录用户supplierId是否相同
        if (supplierId.longValue() != commTag.getSupplierId().longValue()) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("您无权删除此商品标签！");
            return result;
        }
        //根据该ID在供应商商品表中查询是是否在被使用（未删除状态），在使用状态时不能进行删除操作
        List<SupplierCommodity> supplierCommodityList = supplierCommodityDao.findSupplierCommodityByTagId(id);
        if (null != supplierCommodityList && supplierCommodityList.size() > 0) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品标签正在使用，暂时无法删除此标签！");
            return result;
        }
        //判断删除操作成功或失败
        if (commTagDao.deleteById(id)) {
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage("商品标签删除成功！");
            return result;
        } else {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品标签删除失败！");
            return result;
        }
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
        //方法返回对象
        Result result = new Result();
        CommTag commTag = new CommTag();
        commTag.setId(commTagUpdateInput.getId());
        commTag.setName(commTagUpdateInput.getName().trim());
        commTag.setSupplierId(supplierId);
        commTag.setUpdatedAt(new Date());
        //判断标签名为null或空
        if (Ognl.isEmpty(commTagUpdateInput.getName().trim())) {
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("请输入商品标签名称！");
            return result;
        }
        //判断该ID对应的商品标签是否存在
        CommTag one = commTagDao.findOne(commTagUpdateInput.getId());
        if (null == one) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品标签修改失败，该标签不存在！");
            return result;
        }
        //判断该ID对应的商品标签supplierId与登录用户supplierId是否相同
        if (supplierId.longValue() != one.getSupplierId().longValue()) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("您无权修改此商品标签！");
            return result;
        }
        //根据修改的name和supplierId判断是否重复
        List<CommTag> list = commTagDao.findByNameAndSupplierId(commTagUpdateInput.getName().trim(), supplierId);
        if (null != list && list.size() > 0) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品标签修改失败，修改的商品标签名称重复！");
            return result;
        }
        //判断修改标签是否成功
        if (commTagDao.update(commTag)) {
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage("商品标签修改成功！");
            return result;
        } else {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品标签修改失败！");
            return result;
        }
    }

    /**
     * 查询商品标签集合
     *
     * @param supplierId 供应商ID
     * @return 商品标签集合
     */
    @Override
    public Result searchCommTag(Long supplierId) {
        //方法返回对象
        Result result = new Result();
        List<CommTag> commTags = commTagDao.find(supplierId);
        //判断商品标签查询是否成功
        if (null == commTags || commTags.size() == 0) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("查询商品标签失败！");
        } else {
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage("查询商品标签成功！");
            result.setData(commTags);
        }
        return result;
    }
}
