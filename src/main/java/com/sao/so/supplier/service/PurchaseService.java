package com.sao.so.supplier.service;


import com.sao.so.supplier.pojo.input.PurchaseInput;
import com.sao.so.supplier.pojo.input.PurchaseSelectInput;
import com.sao.so.supplier.pojo.output.PurchaseInfoOutput;
import com.sao.so.supplier.pojo.output.PurchaseSelectOutput;
import com.sao.so.supplier.pojo.output.RecordToPurchaseOutput;
import com.sao.so.supplier.pojo.output.SumIncome;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
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
    PurchaseInfoOutput findById(BigInteger orderId) throws Exception;

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
    boolean updateOrder(BigInteger orderId, Integer orderStatus,Integer receiveMethod,String name,String number);

    /**
     *
     * 删除订单，批量删除和单个删除
     * @param orderIds
     * @return boolean
     */
    boolean deletePurchase(String orderIds);

    /**
     * POI批量导出订单列表
     * @param request request
     * @param response response
     * @param orderIds orderIds
     * @param pageNum pageNum
     * @param pageSize pageSize
     */
    void exportExcel(HttpServletRequest request, HttpServletResponse response,String orderIds, Integer pageNum, Integer pageSize) throws Exception;

    /**
     * 根据商家编号查找所有相关订单记录(分页)
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @param storeId 商家编号
     * @return 相关记录的集合
     */
    RecordToPurchaseOutput searchPurchases(Integer pageNum, Integer pageSize, Long storeId);

    /**
     * 通过商户ID查询订单状态，并返回总金额
     * @param storeId
     * @return
     */
    SumIncome findOrderStatus(Long storeId);

}
