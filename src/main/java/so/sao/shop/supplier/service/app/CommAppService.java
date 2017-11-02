package so.sao.shop.supplier.service.app;

import com.github.pagehelper.PageInfo;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommAppInput;
import so.sao.shop.supplier.pojo.input.CommodityAppInput;
import so.sao.shop.supplier.pojo.output.CommAppOutput;

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
  * 根据code69或供应商ID或商品名称或商品分类或品牌ID查询商品信息
  * @param commAppInput
  * @return
  */
 Result getCommodities(CommAppInput commAppInput) throws  Exception;

 /**
 * 查询供应商主营分类
  *
  * @param supplierId 供应商ID
 */
 Result getMainCateGory(Long supplierId);


 /**
  * 根据科属的等级参数获取所有的2或3级科属
  * @param level
  * @return
  */
 Result getAllLevelTwoOrThreeCategories( Long supplierId, Integer level);

 /**
  * 根据条件 获取所属类型下面的 商品的全部品牌
  * @param categoryId
  * @return
  */
 Result getAllBrands(Long supplierId,  Integer categoryId );

 /**
  * 根据动态条件(供应商ID/分类/品牌ids/排序条件)查询商品
  * @param commodityAppInput
  * @return
  */
 PageInfo<CommAppOutput> searchCommodities( CommodityAppInput commodityAppInput);


 /**
  * 根据供应商ID和商品名称查询供应商列表
  * @param supplierId 供应商ID
  * @param commName   商品名称
  * @return
  */
 Result listCommodities(Long supplierId,String commName,Integer pageNum, Integer pageSize);

 /**
  * 根据商品名称模糊查询商品，返回商品列表
  *
  * @param goodsName 商品名称
  * @return
  */
 Result getGoods(String goodsName);

 /**
  * 根据供应商商品ID获取商品详细信息
  * @param id
  * @return
  */
 Result getCommodity(Long id);

 /**
  * 根据商品名称，品牌名称，供应商名称模糊搜索商品
  * @param name
  * @param pageNum
  * @param pageSize
  * @return
  */
 Result getComms(String name, Integer pageNum, Integer pageSize);


}
