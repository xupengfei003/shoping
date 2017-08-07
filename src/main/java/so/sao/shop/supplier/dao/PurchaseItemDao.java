package so.sao.shop.supplier.dao;

import so.sao.shop.supplier.domain.PurchaseItem;
import so.sao.shop.supplier.pojo.vo.PurchaseItemVo;

import java.util.List;

/**
 * <p>
 * 订单信息  dao 接口
 * </p>
 *
 * @author 透云-中软-西安项目组
 * @since 2017-07-19
 */
public interface PurchaseItemDao {
    /**
     * 保存订单信息
     * @param listItem 订单列表详情对象
     * @return
     */
    public int savePurchaseItem(List<PurchaseItem> listItem) throws Exception;

    List<PurchaseItemVo> getOrderDetailByOId(String orderId);

    /**
     * 根据订单编号查找所有相关订单详情
     * @param orderId 订单编号
     * @return 查询到的相关记录
     */
    List<PurchaseItem> findPage(String orderId);

}