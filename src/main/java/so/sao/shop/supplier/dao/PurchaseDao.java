package so.sao.shop.supplier.dao;


import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.domain.Purchase;
import so.sao.shop.supplier.pojo.input.AccountPurchaseInput;
import so.sao.shop.supplier.pojo.input.AccountPurchaseLowInput;
import so.sao.shop.supplier.pojo.input.PurchaseSelectInput;
import so.sao.shop.supplier.pojo.output.OrderCancelReasonOutput;
import so.sao.shop.supplier.pojo.output.OrderRefuseReasonOutput;
import so.sao.shop.supplier.pojo.vo.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    Purchase findById(String orderId);

    /**
     * 查询订单列表
     *
     * @param purchaseInput
     * @return
     */
    List<PurchasesVo> findPage(PurchaseSelectInput purchaseInput);

    /**
     * 更改订单状态
     *
     * @param orderId
     * @param orderStatus
     * @return boolean
     */
    boolean updateOrder(@Param("orderId") String orderId, @Param("orderStatus") Integer orderStatus,@Param("updateDate") Date updateDate);

    /**
     * 发货接口
     *
     * @param orderStatus
     * @param receiveMethod
     * @param name
     * @param number
     * @return boolean
     */
    void deliverGoods(@Param("orderId") String orderId,@Param("orderStatus") Integer orderStatus,@Param("updateDate") Date date,
                      @Param("receiveMethod") Integer receiveMethod, @Param("name") String name, @Param("number") String number) throws Exception;

    /**
     * 删除订单，可批量删除
     *
     * @param orderIdArr
     * @return
     */
    void deleteByOrderId(@Param("orderIdArr") String[] orderIdArr, @Param("updateDate") Date updateDate);

    /**
     * 批量获取订单列表
     * @param accountId 供应商ID
     * @return List<Purchase>
     */
    List<Purchase> getOrderListByIds(@Param("purchaseSelectInput") PurchaseSelectInput purchaseSelectInput,@Param("accountId") Long accountId);

    /**
     * 根据商品ID查询供应商信息
     *
     * @param id 商品ID
     * @return
     */
    Account findAccountById(@Param("id") long id);

    /**
     * 更新订单中账户状态
     *
     * @param recordId 结算明细id
     * @param updateDate 更新时间
     * @return
     */
    int updateAccountStatus(@Param("recordId") String recordId, @Param("updateDate") Date updateDate);

    /**
     * 根据订单表商户ID，查询该表中订单状态为已收货，账户状态为未结算的订单结算金额累计之和,返回和数；
     *
     * @param storeId
     * @return
     */
    BigDecimal findUncountedMoney(@Param("storeId") Long storeId) throws Exception;

    /**
     * 根据商户ID查询订单状态并返回总金额
     *
     * @param storeId
     * @return
     */
    BigDecimal findOrderStatus(@Param("storeId") Long storeId);

    /**
     * 根据商家编号查找所有相关订单记录(高级搜索)
     *
     * @param storeId 商家编号
     * @return 查询的相关记录
     */
    List<Purchase> findPageByStoreId(@Param("input") AccountPurchaseInput input, @Param("storeId") Long storeId);

    /**
     * 根据商家编号查找所有相关订单记录(普通查询)
     *
     * @param storeId 商家编号
     * @return 查询的相关记录
     */
    List<Purchase> findPageByStoreIdLow(@Param("input")AccountPurchaseLowInput input,@Param("storeId") Long storeId);

    /**
     *
     * 获得订单状态
     * @param orderId
     * @return
     */
    Integer getOrderStatus(@Param("orderId") String orderId);

    /**
     * 根据订单编号查询订单打印页面信息
     *
     * @param orderId 订单编号
     * @return 订单页面信息封装的vo
     */
    PurchasePrintVo findPrintOrderInfo(@Param("orderId") String orderId);

    /**
     * 添加拒收货信息
     *
     * @param map 封装了所有拒收相关的信息
     * @return boolean 返回true则为成功，false为失败
     */
    void insertRefuseMessage(@Param("map") Map<String,Object> map) throws Exception;

    /**
     * 添加拒收货信息(图片)
     *
     * @param map 封装了所有拒收相关的信息
     * @param imgList 封装了所有图片相关的信息
     * @return boolean 返回true则为成功，false为失败
     */
    void insertRefuseImg(@Param("map")  Map<String, Object> map,@Param("imgList") List<CommBlobUpload> imgList) throws Exception;
    /**
     * 根据订单ID获取该订单的拒收原因
     *
     * @param orderId 订单ID
     * @return OrderRefuseReasonVo 封装了所有订单拒收原因信息
     */
    OrderRefuseReasonVo findRefuseReasonByOrderId(@Param("orderId") String orderId) throws Exception;
    /**
     * 根据订单ID获取该订单的拒收图片
     *
     * @param orderId 订单ID
     * @return OrderRefuseImageVo 封装了所有订单拒收原因信息
     */
    List<OrderRefuseImageVo> findRefuseImageByOrderId(@Param("orderId") String orderId) throws Exception;

    /**
     * 查询该商户下已完成且按自然月结算的订单列表
     * @param storeId 商户id
     * @param currentDate 当前时间
     * @return
     */
    List<Purchase> findPurchaseMonth(@Param("storeId") Long storeId, @Param("currentDate") Date currentDate);

    /**
     * 查询该商户下已完成且按固定时间段结算的订单列表
     * @param storeId 商户id
     * @param lastSettlementDate 上一次结算时间
     * @param remittanced  结算的时间间隔
     * @return
     */
    List<Purchase> findPurchaseFixedTime(@Param("storeId") Long storeId, @Param("lastSettlementDate") Date lastSettlementDate,  @Param("remittanced") String remittanced);

    /**
     * 添加取消订单信息
     *
     * @param cancelMap 封装了所有取消订单相关的信息
     * @return boolean 返回true则为成功，false为失败
     * @throws Exception
     */
    void insertCancelMessage(@Param("cancelMap") Map<String,Object> cancelMap) throws Exception;

    /**
     * 根据订单编号查询取消订单原因
     *
     * @param orderId 订单编号
     * @return
     */
    OrderCancelReasonOutput findCancelReason(@Param("orderId") String orderId);

    /**
     * 根据支付ID获取订单
     * @param payId 支付ID
     * @return List<Purchase>
     */
    List<Purchase> findByPayId(String payId);

    /**
     * 根据订单编号修改订单编号，退款时间
     *
     * @param refundMap 退款参数
     * @return 修改行
     * @throws Exception 异常
     */
    int refundByOrderId(@Param("refundMap") Map<String,Object> refundMap) throws Exception;

    /**
     * 根据订单状态查询订单ID
     *
     * @param orderStatus 订单状态
     * @return String 订单ID
     * @throws Exception 异常
     */
    List<String> findOrderIdByOrderStatus(@Param("orderStatus") Integer orderStatus) throws Exception;

    /**
     * 批量更新订单的账户状态
     * @param purchaseUpdateList
     */
    void updatePurchasesAccountStatusById(List<Purchase> purchaseUpdateList);

    /**
     * 根据订单编号和用户id查询订单
     *
     * @param orderId 订单编号
     * @param userId 用户id
     * @return 订单列表
     */
    List<Purchase> findPurchaseByUserId(@Param("orderId") String orderId, @Param("userId") String userId);
}