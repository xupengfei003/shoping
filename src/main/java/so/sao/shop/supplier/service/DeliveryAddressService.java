package so.sao.shop.supplier.service;


import so.sao.shop.supplier.domain.DeliveryAddress;
import so.sao.shop.supplier.pojo.output.DeliveryAddressOutput;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author zhangruibing
 * @since 2017-07-17
 */
public interface DeliveryAddressService {
    /**
     * <p>新增收货地址</p>
     *
     * @param deliveryAddress
     * @return
     */
    DeliveryAddressOutput saveDeliveryAddress(DeliveryAddress deliveryAddress);

    /**
     * <p>编辑收货地址</p>
     *
     * @param deliveryAddress
     * @return
     */
    DeliveryAddressOutput updateDeliveryAddress(DeliveryAddress deliveryAddress);

    /**
     * <p>删除收货地址</p>
     *
     * @param addrId
     */
    DeliveryAddressOutput deleteDeliveryAddress(String addrId);

    /**
     * <p>
     * 根据用户id查询收货地址
     * </p>
     *
     * @param userId
     * @return
     */
    DeliveryAddressOutput findByUserId(String userId);

    /**
     * <p>更改默认的收货地址</p>
     *
     * @param addrId
     * @param userId
     * @return
     */
    DeliveryAddressOutput updateDeliveryAddressStatus(String addrId, String userId);
}
