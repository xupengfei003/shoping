package com.sao.so.shop.service.impl;


import com.sao.so.shop.config.Constant;
import com.sao.so.shop.dao.DeliveryAddressDao;
import com.sao.so.shop.domain.DeliveryAddress;
import com.sao.so.shop.pojo.output.DeliveryAddressOutput;
import com.sao.so.shop.service.DeliveryAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 服务实现类
 * @author zhangruibing
 * @since 2017-07-17
 */
@Service
public class DeliveryAddressServiceImpl implements DeliveryAddressService {

    @Autowired
    private DeliveryAddressDao deliveryAddressDao;

    /**
     *
     * 新增收货地址
     * @param deliveryAddress
     */
    @Override
    public DeliveryAddressOutput saveDeliveryAddress(DeliveryAddress deliveryAddress) {
        int count = 0;
        try {
            Date date = new Date();
            deliveryAddress.setCreatedAt(date.getTime());//设置创建时间
            count = deliveryAddressDao.save(deliveryAddress);
        } catch (Exception e) {
            return this.outputException();
        }
        return this.output(count, null);
    }

    /**
     * 编辑收货地址
     * @param deliveryAddress
     * @return
     */
    @Override
    public DeliveryAddressOutput updateDeliveryAddress(DeliveryAddress deliveryAddress) {
        int count = 0;
        try {
            Date date = new Date();
            deliveryAddress.setUpdatedAt(date.getTime());
            count = deliveryAddressDao.update(deliveryAddress);
        } catch (Exception e) {
            return this.outputException();
        }
        return this.output(count, null);
    }

    /**
     * 删除收货地址
     * @param addrId
     */
    @Override
    public DeliveryAddressOutput deleteDeliveryAddress(String addrId) {
        int count = 0;
        try {
            count = deliveryAddressDao.deleteByAddrId(Long.parseLong(addrId));
        } catch (Exception e) {
            return this.outputException();
        }
        return this.output(count, null);
    }

    /**
     * 根据用户id查询收货地址
     * @param userId
     * @return
     */
    @Override
    public DeliveryAddressOutput findByUserId(String userId) {
        int count = 0;
        List<DeliveryAddress> list = null;
        try {
            list = deliveryAddressDao.findByUserId(Long.parseLong(userId));
            if (list != null && list.size() > 0) {
                count = 1;
            }
        } catch (Exception e) {
            return this.outputException();
        }
        return this.output(count, list);
    }

    /**
     * 更改默认的收货地址
     * @param addrId
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public DeliveryAddressOutput updateDeliveryAddressStatus(String addrId, String userId) {
        int count = 0;
        try {
            //step 1 状态归零
            deliveryAddressDao.updateDeliveryAddressStatus(Long.parseLong(userId));
            //step 2 设为默认
            count = deliveryAddressDao.updateDeliveryAddressStatusByid(Long.parseLong(addrId));
        } catch (Exception e) {
            return this.outputException();
        }
        return this.output(count, null);
    }

    /**
     *
     * 结果状态
     * @param count
     * @param list
     * @return
     */
    public DeliveryAddressOutput output(int count, List list) {
        DeliveryAddressOutput output = new DeliveryAddressOutput();
        output.setDeliveryAddressList(list);
        if (count > 0) {
            output.setCode(Constant.CodeConfig.CODE_SUCCESS);
            output.setMessage(Constant.MessageConfig.MSG_SUCCESS);
        } else {
            output.setCode(Constant.CodeConfig.CODE_NOT_FOUND_RESULT);
            output.setMessage(Constant.MessageConfig.MSG_NOT_FOUND_RESULT);
        }
        return output;
    }

    /**
     * 异常状态
     * @return
     */
    public DeliveryAddressOutput outputException() {
        DeliveryAddressOutput output = new DeliveryAddressOutput();
        output.setDeliveryAddressList(null);
        output.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
        output.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
        return output;
    }
}
