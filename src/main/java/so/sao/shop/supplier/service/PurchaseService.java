package so.sao.shop.supplier.service;


import com.github.pagehelper.PageInfo;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.*;
import so.sao.shop.supplier.pojo.output.PurchaseItemPrintOutput;
import so.sao.shop.supplier.pojo.vo.PurchaseInfoVo;
import so.sao.shop.supplier.pojo.vo.PurchasesVo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.List;
import java.util.Map;

/**
 * 订单信息  service 接口
 * </p>
 *
 * @author 透云-中软-西安项目组
 * @since 2017-07-19
 */
public interface PurchaseService {
    /**
     * 保存订单信息
     *
     * @param purchase 订单对象
     * @return
     */
    public Map<String, Object> savePurchase(PurchaseInput purchase) throws Exception;

    /**
     * 根据订单ID获取订单详情
     *
     * @param orderId orderId
     * @return PurchaseInfoVo
     * @throws Exception Exception
     */
    PurchaseInfoVo findById(String orderId) throws Exception;

    /**
     * 查询订单列表，并分页
     *
     * @param pageNum
     * @param rows
     * @return list
     */
    PageInfo<PurchasesVo> searchOrders(Integer pageNum, Integer rows, PurchaseSelectInput purchaseSelectInput) throws Exception;

    /**
     * 发货接口
     *
     * @param orderId
     * @return
     */
    void deliverGoods(String orderId, Integer receiveMethod, String name, String number) throws Exception;

    /**
     * 删除订单，批量删除和单个删除
     *
     * @param orderIds
     * @return boolean
     */
    void deletePurchase(String orderIds);

    /**
     * POI导出(当前页/所选页/全部)订单列表
     *
     * @param request   request
     * @param response  response
     * @param pageNum   pageNum
     * @param pageSize  pageSize
     * @param accountId accountId
     */
    void exportExcel(HttpServletRequest request, HttpServletResponse response, String pageNum, Integer pageSize, Long accountId, PurchaseSelectInput purchaseSelectInput) throws Exception;

    /**
     * 根据商家编号及查询条件（起始创建订单-结束创建订单时间/起始下单时间-结束下单时间/起始-结束金额范围;支付流水号/订单编号/收货人名称/收货人联系方式）查找所有相关订单记录(分页)
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示条数
     * @param input    查询条件封装类
     * @param storeId  商家编号
     * @return 出参
     */
    Result<PageInfo> searchPurchasesHigh(Integer pageNum, Integer pageSize, AccountPurchaseInput input, Long storeId) throws ParseException;


    /**
     * 通过商户ID查询订单状态，并返回总金额
     *
     * @param storeId
     * @return
     */
    String findOrderStatus(Long storeId);

    /**
     * 根据订单ID获取订单状态
     *
     * @param orderId
     * @return
     */
    Integer findOrderStatus(String orderId);

    /**
     * 根据支付ID获取订单状态
     *
     * @param payId
     * @return
     */
    List<String> findOrderStatusByPayId(String payId);

    /**
     * 根据订单查询订单打印页面信息
     * <p>
     * 1.查询订单信息；
     * 2.查询商品条目；
     * 3.将订单信息和商品条目封装到output对象；
     * 4.在实体类属性的get方法中格式化数据。
     *
     * @param orderId 订单编号
     * @return 返回一个PurchaseItemPrintOutput对象
     * @throws Exception 异常
     */
    PurchaseItemPrintOutput getPrintItems(String orderId) throws Exception;

    /**
     * 根据订单编号生成收货二维码
     * <p>
     * 如果订单已经存在二维码返回false，生成二维码失败返回false。
     * 1.验证订单并判断订单编号是否存在关联的二维码；
     * 2.生成二维码图片。
     *      2.1.拼接二维码内容；
     *      2.2.生成二维码图片；
     *      2.3.将二维码图片上传到云端；
     *      2.4.将二维码信息保存到数据库；
     *      2.5.删除本地图片。
     *
     * @param orderId 订单编号
     * @throws Exception 异常
     */
    void createReceivingQrcode(String orderId) throws Exception;

    /**
     * 扫描收货二维码
     * <p>
     * 1.验证是否可以扫码收货
     * 2.将订单状态改为已收货
     * 3.将二维码状态改为失效，并记录失效时间
     *
     * @param orderId 订单编号
     * @return map 封装结果 键flag的值为true表示成功，false表示失败，message的值表示文字描述
     */
    Map sweepReceiving(String orderId) throws Exception;

    /**
     * 添加拒收货信息
     * <p>
     * 将拒收理由及相关图片保存
     *
     * @param refuseOrderInput 封装了订单编号，拒收理由，拒收图片
     * @return Map 封装结果 键flag的值为true表示成功，false表示失败，message的值表示文字描述
     */
    void refuseOrder(RefuseOrderInput refuseOrderInput) throws Exception;

    /**
     * 根据订单ID获取该订单的拒收原因
     *
     * @param orderId 订单ID
     * @return map 封装了所有订单拒收原因信息
     */
    Map<String, Object> searchRefuseReasonByOrderId(String orderId) throws Exception;

    /**
     * 根据商家编号及查询条件（起始创建订单-结束创建订单时间/支付流水号/订单编号/收货人名称）查找所有相关订单记录(分页)
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示条数
     * @param input    查询条件封装类
     * @param storeId  商家编号
     * @return 出参
     */
    Result<PageInfo> searchPurchasesLow(Integer pageNum, Integer pageSize, AccountPurchaseLowInput input, Long storeId) throws ParseException;

    /**
     * 添加取消订单信息
     *
     * @param cancelReasonInput 封装了订单编号，取消理由
     * @return Map 封装结果 键flag的值为true表示成功，false表示失败，message的值表示文字描述
     * @throws Exception
     */
    void cancelOrder(CancelReasonInput cancelReasonInput) throws Exception;

    /**
     * 根据订单编号查询取消订单原因
     *
     * @param orderId 订单编号
     * @return 取消订单原因
     * @throws Exception
     */
    String searchCancelReasonByOrderId(String orderId) throws Exception;

    /**
     * 根据订单编号实现退款逻辑
     * <p>
     * 根据订单编号验证订单、修改订单状态、调用退款接口、推送退款消息。
     * 1.根据订单状态验证是否可以退款（仅已取消（7）和已拒收（5）状态的订单可以退款，其他状态不可以退款）；
     * 2.修改订单状态为退款，修改退款时间为当前时间；
     * 3.调用退款接口实现真正的退款；
     * 4.推送退款消息。
     *
     * @param orderId 订单编号
     * @return 返回Map：flag：true|false,message:信息
     * @throws Exception 异常
     */
    Map refundByOrderId(String orderId) throws Exception;

    /**
     * 根据支付id，批量生成订单的二维码
     *
     * @param payId 支付id
     * @throws Exception 异常
     */
    void createReceivingQrcodeByPayId(String payId) throws Exception;

    /**
     * 根据订单状态查询订单ID
     *
     * @param orderStatus 订单状态
     * @return String 订单ID
     * @throws Exception 异常
     */
    List<String> findOrderIdByOrderStatus(Integer orderStatus) throws Exception;

}
