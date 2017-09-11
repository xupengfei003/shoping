package so.sao.shop.supplier.service;

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
  * 根据code69查找所有供应商列表
  * @param code69
  * @return 供应商列表
  */
 Result searchSuppliers(String code69);


 /**
  * 查询所有商品科属
  * @return Result结果集
  */
 Result searchCategories();

 /**
  * 模糊查询品牌名称
  * @param name
  * @return Result结果集
  */
 Result getBrandName(String name);

 /**
  * 根据供应商ID或名称查询供应商详情列表
  * @param accountId 供应商ID
  * @param providerName 供应商名称
  * @param pageNum 当前页码
  * @param pageSize 页面大小
  * @return Result Result对象（供应商详情列表）
  */
 Result getSuppliers(Long accountId, String providerName, Integer pageNum, Integer pageSize);

 /**
  * 根据商品名称或分类或品牌ID查询商品信息
  * @param commName 商品名称
  * @param categoryOneId 一级分类id
  * @param categoryTwoId 二级分类id
  * @param categoryThreeId 三级分类id
  * @param brandIds 品牌集合
  * @param pageNum 当前页码
  * @param pageSize 页面大小
  * @return
  */
 Result getCommodities(String commName, Long categoryOneId, Long categoryTwoId, Long categoryThreeId, Long[] brandIds, Integer pageNum, Integer pageSize);
}
