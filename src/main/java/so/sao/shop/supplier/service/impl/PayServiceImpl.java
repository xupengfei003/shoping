package so.sao.shop.supplier.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.PayDao;
import so.sao.shop.supplier.pojo.input.PayInput;
import so.sao.shop.supplier.service.PayService;
import so.sao.shop.supplier.util.MD5Util;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by acer on 2017/8/15.
 */
@Service
public class PayServiceImpl implements PayService {
    @Resource
    private PayDao payDao;

    /**
     * 保存支付信息
     *
     * @param sign 支付平台传来的加密串
     * @param payInput 输入实体
     * @return int
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean savePurchase(String sign, PayInput payInput) throws Exception {
        boolean flagDao = false;
        Map<String,Object> map = new HashMap<>();
        if (isSign(payInput,sign)){
            map.put("orderPaymentTime",new Date());//支付时间
            map.put("updatedAt",new Date());//更新时间
            map.put("orderStatus",Constant.OrderStatusConfig.PENDING_SHIP);//订单状态
            map.put("orderId",payInput.getOrderId());//订单编号
            map.put("orderPrice",payInput.getOrderPrice());//订单金额
            map.put("orderPaymentNum",payInput.getOrderPaymentNum());//支付流水号
            map.put("orderPaymentMethod",payInput.getOrderPaymentMethod());//支付方式
            flagDao = payDao.save(map);
        }
        return flagDao;
    }

    //判断支付平台传来的串是否合法
    public boolean isSign(PayInput payInput,String sign){
        //拼接订单ID、实付金额、支付流水号字符串
        String str = "orderId=" + payInput.getOrderId() + "&orderPrice=" + payInput.getOrderPrice() + "&orderPaymentNum=" + payInput.getOrderPaymentNum();
        //加密后的订单ID、实付金额、支付流水号字符串
        String md5Str = MD5Util.getMD5(str);

        if(sign.equals(md5Str)){
            return true;
        }
        return false;
    }
}
