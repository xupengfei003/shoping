package com.sao.so.supplier.pojo.output;

import com.sao.so.supplier.domain.DeliveryAddress;
import com.sao.so.supplier.pojo.BaseResult;

import java.util.List;

/**
 * <p>收货地址出参</p>
 * Created by zhangruibing on 2017/7/20.
 */

public class DeliveryAddressOutput extends BaseResult {
    /**
     * <p>查询所有收货地址</p>
     */
    private List<DeliveryAddress> deliveryAddressList;

    public List<DeliveryAddress> getDeliveryAddressList() {
        return deliveryAddressList;
    }

    public void setDeliveryAddressList(List<DeliveryAddress> deliveryAddressList) {
        this.deliveryAddressList = deliveryAddressList;
    }
}
