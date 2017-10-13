package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommCartonUpdateInput;

/**
 * 商品箱规单位service
 * @author chensha
 */
public interface CommCartonService {

    /**
     * 新增商品箱规单位
     * @param supplierId 供应商ID
     * @param name 箱规单位名称
     * @return 新增结果
     */
    Result saveCommCarton(Long supplierId, String name);

    /**
     * 根据id删除箱规单位
     * @param supplierId 供应商ID
     * @param id 箱规单位id
     * @return 删除结果
     */
    Result deleteCommCarton(Long supplierId, Long id);

    /**
     * 修改商品箱规单位
     * @param supplierId 供应商ID
     * @param commCartonUpdateInput 商品标签修改入参
     * @return 修改结果
     */
    Result updateCommTag(Long supplierId, CommCartonUpdateInput commCartonUpdateInput);

    /**
     * 查询商品箱规单位列表
     * @param supplierId 供应商ID
     * @return 箱规单位集合
     */
    Result searchCommCartons(Long supplierId);
}
