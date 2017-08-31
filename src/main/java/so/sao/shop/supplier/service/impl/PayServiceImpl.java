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

    /**
     * 保存支付信息
     *
     * @param sign     支付平台传来的加密串
     * @param payInput 输入实体
     * @return int
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePurchase(String sign, PayInput payInput) throws Exception {
        boolean flagDao = false;
        Map<String, Object> map = new HashMap<>();
        if (isSign(payInput, sign)) {
            map.put("orderPaymentTime", new Date());//支付时间
            map.put("updatedAt", new Date());//更新时间
            map.put("orderStatus", Constant.OrderStatusConfig.PENDING_SHIP);//订单状态
            map.put("orderId", payInput.getOrderId());//订单编号
            map.put("orderPrice", payInput.getOrderPrice());//订单金额
            map.put("orderPaymentNum", payInput.getOrderPaymentNum());//支付流水号
            map.put("orderPaymentMethod", payInput.getOrderPaymentMethod());//支付方式
            map.put("payStatus", 1);//支付状态  0.未支付状态  1.已支付状态
            flagDao = payDao.save(map);
            //TODO 为该供应商推送"待发货"消息通知
            List<Purchase> purchaseList = purchaseDao.findByPayId(payInput.getOrderId());
            List<Notification> notificationList = new ArrayList<>();
            if(null != purchaseList && purchaseList.size() > 0){
                purchaseList.forEach(purchase -> {
                    if(purchase.getPayStatus() != 0){
                        Notification notification = new Notification(purchase.getStoreId(), 0, purchase.getOrderId(),
                                Constant.NotifiConfig.PENDING_SHIP_NOTIFI + purchase.getOrderId(), new Date(), 0);
                        notificationList.add(notification);
                    }
                });
                if(null != notificationList && notificationList.size() > 0){
                    notificationDao.saveNotifications(notificationList);
                }
            }
        }
        return flagDao;
    }

    //判断支付平台传来的串是否合法
    public boolean isSign(PayInput payInput, String sign) {
        //拼接订单ID、实付金额、支付流水号字符串
        String str = "orderId=" + payInput.getOrderId() + "&orderPrice=" + payInput.getOrderPrice() + "&orderPaymentNum=" + payInput.getOrderPaymentNum();
        //加密后的订单ID、实付金额、支付流水号字符串
        String md5Str = MD5Util.getMD5(str);
        if (sign.equals(md5Str)) {
            return true;
        }
        return false;
    }
}
