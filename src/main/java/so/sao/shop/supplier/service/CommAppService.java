package so.sao.shop.supplier.service;

import org.springframework.web.bind.annotation.RequestParam;
import so.sao.shop.supplier.pojo.Result;

import java.math.BigDecimal;

public interface CommAppService {

   /**
     * 根据查询条件查询商品详情
     * @param sku  SKU（商品ID,拼接生成）
     * @param supplierId 供应商ID
     * @param code69  商品编码
     * @param suppCommCode 商家商品编码
     * @param inputValue 输入值（商品名称或分类名称）
     * @param minPrice 价格（低）
     * @param maxPrice 价格（高）
     * @param pageNum 当前页号
     * @param pageSize 页面大小
     * @return Result Result对象
     */

    Result searchAllCommodities(Long supplierId, String sku, String code69, String suppCommCode, String inputValue, BigDecimal minPrice, BigDecimal maxPrice, Integer pageNum, Integer pageSize);

    /**
     * 根据code69查找所有的供应商列表
     * @param code69
     * @return
     */
    Result searchSuppliers(String code69);


    /**
     * 查询所有商品分类
     * @return Result结果集
     */
    Result searchCategories();
}
