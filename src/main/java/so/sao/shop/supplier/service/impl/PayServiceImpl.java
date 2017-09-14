package so.sao.shop.supplier.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.NotificationDao;
import so.sao.shop.supplier.dao.PayDao;
import so.sao.shop.supplier.dao.PurchaseDao;
import so.sao.shop.supplier.domain.Notification;
import so.sao.shop.supplier.domain.Purchase;
import so.sao.shop.supplier.pojo.input.PayInput;
import so.sao.shop.supplier.service.PayService;
import so.sao.shop.supplier.service.PurchaseService;
import so.sao.shop.supplier.util.MD5Util;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by acer on 2017/8/15.
 */
@Service
public class PayServiceImpl implements PayService {
    @Resource
    private PayDao payDao;
    @Resource
    private NotificationDao notificationDao;
    @Resource
    private PurchaseDao purchaseDao;
    @Resource
    private PurchaseService purchaseService;

    /**
     * 支付回调接口
     *
     * @param payInput 封装了回调参数
     * @return Result 封装了结果
     * @throws Exception 异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePurchasePayment(PayInput payInput) throws Exception {
        boolean flagDao = false;
        if (isSign(payInput)) {
            Map<String, Object> map = mergePaymentInfo(payInput);
            flagDao = payDao.updatePaymentByPayId(map);
            //TODO 为该供应商推送"待发货"消息通知
            sendNotice(payInput);
            // 根据支付id，批量生成订单的二维码
            purchaseService.createReceivingQrcodeByPayId(payInput.getOrderId());
        }
        return flagDao;
    }

    /**
     * 支付回调接口(单订单支付)
     *
     * @param payInput 封装了回调参数,orderId为单个订单ID
     * @return Result 封装了结果
     * @throws Exception 异常
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean updatePurchasePaymentByOrderId(PayInput payInput) throws Exception {
        boolean flag = false;
        if (isSign(payInput)) {
            Map<String, Object> map = mergePaymentInfo(payInput);
            flag = payDao.updatePaymentByOrderId(map);
            // 根据订单id，成订单的二维码
            purchaseService.createReceivingQrcode(payInput.getOrderId());
            //TODO 为该供应商推送"待发货"消息通知
            String payId = payDao.findPayIdByOrderId(payInput.getOrderId());
            payInput.setOrderId(payId);
            sendNotice(payInput);
        }
        return flag;
    }

    //判断支付平台传来的串是否合法
    public boolean isSign(PayInput payInput) {
        //拼接订单ID、实付金额、支付流水号字符串
        String str = "orderId=" + payInput.getOrderId() + "&orderPrice=" + payInput.getOrderPrice() + "&orderPaymentNum=" + payInput.getOrderPaymentNum();
        //加密后的订单ID、实付金额、支付流水号字符串
        String md5Str = MD5Util.getMD5(str);
        if (payInput.getSign().equals(md5Str)) {
            return true;
        }
        return false;
    }

    //发送消息
    private void sendNotice(PayInput payInput) {
        List<Purchase> purchaseList = purchaseDao.findByPayId(payInput.getOrderId());
        List<Notification> notificationList = new ArrayList<>();
        if (null != purchaseList && purchaseList.size() > 0) {
            purchaseList.forEach(purchase -> {
                if (purchase.getPayStatus() != 0) {
                    Notification notification = new Notification(purchase.getStoreId(), 0, purchase.getOrderId(),
                            Constant.NotifiConfig.PENDING_SHIP_NOTIFI + purchase.getOrderId(), new Date(), 0);
                    notificationList.add(notification);
                }
            });
            if (null != notificationList && notificationList.size() > 0) {
                notificationDao.saveNotifications(notificationList);
            }
        }
    }

    //合并入参
    private Map<String, Object> mergePaymentInfo(PayInput payInput) throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("orderPaymentTime", new Date());//支付时间
        map.put("updatedAt", new Date());//更新时间
        map.put("orderStatus", Constant.OrderStatusConfig.PENDING_SHIP);//订单状态
        map.put("orderId", payInput.getOrderId());//订单编号
        map.put("orderPaymentNum", payInput.getOrderPaymentNum());//支付流水号
        map.put("orderPaymentMethod", payInput.getOrderPaymentMethod());//支付方式
        map.put("payStatus", 1);//支付状态  0.未支付状态  1.已支付状态
        return map;
    }

}
