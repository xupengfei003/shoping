package so.sao.shop.supplier.dao;


import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.AccountUser;
import so.sao.shop.supplier.domain.Purchase;
import so.sao.shop.supplier.pojo.input.AccountPurchaseInput;
import so.sao.shop.supplier.pojo.input.PurchaseSelectInput;
import so.sao.shop.supplier.pojo.vo.PurchasesVo;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * <p>
 * 订单信息  dao 接口
 * </p>
 *
 * @author 透云-中软-西安项目组
 * @since 2017-07-19
 */
public interface PurchaseDao {
    /**
     * 保存订单信息
     *
     * @param list 订单对象
     * @return
     */
    public int savePurchase(List<Purchase> list) throws Exception;


    /**
     * 根据订单ID获取订单信息.
     *
     * @param orderId
     * @return
     */
    Purchase findById(Long orderId);

    /**
     *
     * 查询订单列表
     * @param purchaseInput
     * @return
     */
    List<PurchasesVo> findPage(PurchaseSelectInput purchaseInput);

    /**
     * 更改订单状态
     * @param orderId
     * @param orderStatus
     * @return boolean
     */
    boolean updateOrder(@Param("orderId") BigInteger orderId, @Param("orderStatus") Integer orderStatus);

    /**
     *
     * 更改状态辅助操作（更改退款时间、插入物流配送方式）
     * @param drawbackTime
     * @param receiveMethod
     * @param name
     * @param number
     * @return boolean
     */
    boolean updateOrderAtr(@Param("orderId") BigInteger orderId,@Param("drawbackTime") Long drawbackTime,
                           @Param("receiveMethod") Integer receiveMethod, @Param("name") String name, @Param("number") String number);

    /**
     *
     * 删除订单，可批量删除
     * @param orderIdArr
     * @return
     */
    boolean deleteByOrderId(String[] orderIdArr);

    /**
     * 批量获取订单列表
     * @return List<Purchase>
     */
    List<Purchase> getOrderListByIds(@Param("orderIdList") List<String> orderIdList);

    /**
     * 根据商品ID查询供应商信息
     * @param id 商品ID
     * @return
     */
    AccountUser findAccountById(@Param("id")long id);

    /**
     *  更新订单中账户状态
     * @param userId 商家id
     */
    void updateAccountStatus(@Param("userId") Long userId);

    /**
     * 1.根据订单表（purchase）中的商户（store_id）字段查询该表中订单状态（order_status）为已完成，
     *   账户状态（account_status）为未统计的订单金额（order_price）之和,返回和数；
     * @param userId
     * @return
     */
    BigDecimal findUncountedMoney(@Param("userId") Long userId);
    /**
     * 1.根据订单表（purchase）中的商户（store_id）字段，将该表中订单状态（order_status）为已完成，
     *   账户状态（account_status）为未统计（状态码：0）的改为已统计（状态码：1）,返回受影响行数；
     * @param userId
     * @return
     */
    int updatePurchaseAccountStatus(@Param("userId") Long userId);

    /**
     * 根据商户ID查询订单状态并返回总金额
     * @param storeId
     * @return
     */
    Double findOrderStatus(@Param("storeId") Long storeId);




    /**
     * 查询该商户下已完成且已统计的订单id
     * @return 订单id,多个以逗号分隔
     */
    String findOrderIdsByStatus(@Param("storeId") Long storeId);


    /**
     * 根据提现申请记录查询该记录所对应的订单列表
     * @param orderIds
     * @return
     */
    List<Purchase> findPageOMRPurchaseDetails(String[] orderIds);
    /**
     * 根据商家编号查找所有相关订单记录
     * @param storeId 商家编号
     * @return 查询的相关记录
     */
    List<Purchase> findPageByStoreId(@Param("input") AccountPurchaseInput input, @Param("storeId") Long storeId);

    /**
     * 根据商家编号查询记录总条数
     * @param storeId 商家编号
     * @return 符合条件的记录条数
     */
    int countByStoreId(@Param("storeId") Long storeId);

    /**
     * 有条件根据商家编号查找所有相关订单记录
     * @param storeId 商家编号
     * @param input 查询条件封装类
     * @return
     */
    int countByStoreIdAndInput(@Param("storeId") Long storeId,@Param("input")AccountPurchaseInput input);
}