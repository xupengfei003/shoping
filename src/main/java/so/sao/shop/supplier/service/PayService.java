package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.input.PayInput;

/**
 * Created by acer on 2017/8/15.
 */
public interface PayService {
    boolean updatePurchasePayment(PayInput payInput) throws Exception;
}
