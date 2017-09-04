package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommTagUpdateInput;

/**
 * 商品标签service
 *
 * @author zhaoyan
 * @create 2017/8/14 15:54
 */
public interface CommTagService {

    /**
     * 新增商品标签
     *
     * @param supplierId 供应商ID
     * @param name 商品标签名称
     * @return 新增结果
     */
    Result saveCommTag(Long supplierId, String name);

    /**
     * 根据id删除商品标签
     *
     * @param supplierId 供应商ID
     * @param id 商品标签id
     * @return 删除结果
     */
    Result deleteCommTag(Long supplierId, Long id);

    /**
     * 修改商品标签信息
     *
     * @param supplierId 供应商ID
     * @param commTagUpdateInput 商品标签修改入参
     * @return 修改结果
     */
    Result updateCommTag(Long supplierId, CommTagUpdateInput commTagUpdateInput);

    /**
     * 查询商品标签集合
     *
     * @param supplierId 供应商ID
     * @return 商品标签集合
     */
    Result searchCommTag(Long supplierId);
}
