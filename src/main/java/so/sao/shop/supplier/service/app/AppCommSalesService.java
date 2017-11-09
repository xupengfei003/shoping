package so.sao.shop.supplier.service.app;

import so.sao.shop.supplier.pojo.input.AppCommSalesInput;

import java.util.List;

/**
 *<p>Version: shop-supplier V1.1.0 </p>
 *<p>Title: AppCommSalesService</p>
 *<p>Description: 商品销量Service</p>
 *@author: liugang
 *@Date: Created in 2017/10/30 15:54
 */
public interface AppCommSalesService {
    /**
     * 根据商品ID统计已销售商品数量
     *
     * @param goodsIds 商品ID列表
     * @return List<String> 统计数
     */
    List<String> countSoldCommNum(String[] goodsIds) throws Exception ;


    /**
     * 更新商品销量
     * @param commSalesInputs
     * @return
     */
    boolean updateSalesNum(List<AppCommSalesInput> commSalesInputs);

    /**
     * 添加商品销量
     * @param goodsIds 商品ID集合
     * @return
     */
    boolean saveCommSales(String goodsIds);
}
