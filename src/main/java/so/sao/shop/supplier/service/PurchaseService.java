package so.sao.shop.supplier.service;


import so.sao.shop.supplier.pojo.input.AccountPurchaseInput;
import so.sao.shop.supplier.pojo.input.PurchaseInput;
import so.sao.shop.supplier.pojo.input.PurchaseSelectInput;
import so.sao.shop.supplier.pojo.output.PurchaseInfoOutput;
import so.sao.shop.supplier.pojo.output.PurchaseSelectOutput;
import so.sao.shop.supplier.pojo.output.RecordToPurchaseOutput;
import so.sao.shop.supplier.pojo.output.SumIncome;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Map;

/**
 * <p>
 * 订单信息  service 接口
 * </p>
 *
 * @author 透云-中软-西安项目组
 * @since 2017-07-19
 */
public interface PurchaseService {
    /**
     * 保存订单信息
     * @param purchase 订单对象
     * @return
     */
    public Map<String,Object> savePurchase(PurchaseInput purchase) throws Exception;
    /**
     * 根据订单ID获取订单详情
     * @param orderId orderId
     * @return PurchaseOutput
     * @throws Exception
     */
    PurchaseInfoOutput findById(String orderId) throws Exception;

    /**
     *
     * 查询订单列表，并分页
     * @param pageNum
     * @param rows
     * @return list
     */
    PurchaseSelectOutput searchOrders(Integer pageNum, Integer rows, PurchaseSelectInput purchaseSelectInput);

    /**
     *
     * 更改订单状态
     * @param orderId
     * @param orderStatus
     * @return
     */
    boolean updateOrder(String orderId, Integer orderStatus,Integer receiveMethod,String name,String number);

    /**
     *
     * 删除订单，批量删除和单个删除
     * @param orderIds
     * @return boolean
     */
    boolean deletePurchase(String orderIds);

    /**
     * POI导出(当前页/所选页/全部)订单列表
     * @param request request
     * @param response response
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @param accountId accountId
     */
    void exportExcel(HttpServletRequest request, HttpServletResponse response, String pageNum, Integer pageSize, Long accountId) throws Exception;

    /**
     * 根据商家编号及查询条件（起始-结束时间范围；起始-结束金额范围）查找所有相关订单记录(分页)
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @param input 查询条件封装类
     * @param storeId 商家编号
     * @return 出参
     */
    RecordToPurchaseOutput searchPurchases(Integer pageNum, Integer pageSize, AccountPurchaseInput input, Long storeId) throws ParseException;


    /**
     * 通过商户ID查询订单状态，并返回总金额
     * @param storeId
     * @return
     */
    SumIncome findOrderStatus(Long storeId);

    /**
     * 根据订单ID获取订单状态
     * @param orderId
     * @return
     */
    Integer findOrderStatus(String orderId);

}
