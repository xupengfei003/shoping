package so.sao.shop.supplier.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.domain.DeliveryAddress;
import so.sao.shop.supplier.pojo.output.DeliveryAddressOutput;
import so.sao.shop.supplier.service.DeliveryAddressService;

/**
 * 
 * 前端控制器
 * 
 *
 * @author zhangruibing
 * @since 2017-07-17
 */
@RestController
public class DeliveryAddressController {

    @Autowired
    private DeliveryAddressService deliveryAddressService;

    /**
     * 新增收货地址
     *
     * @param deliveryAddress
     * @return
     */
    @PostMapping("/deliveryAddresses")
    public DeliveryAddressOutput create(DeliveryAddress deliveryAddress) {
        return deliveryAddressService.saveDeliveryAddress(deliveryAddress);
    }

    /**
     * 编辑收货地址
     *
     * @param deliveryAddress
     * @return
     */
    @PutMapping(value = "/deliveryAddresses/{addrId}")
    public DeliveryAddressOutput update(DeliveryAddress deliveryAddress) {
        return deliveryAddressService.updateDeliveryAddress(deliveryAddress);
    }

    /**
     * 删除收货地址
     *
     * @param addrId 收货地址id
     */
    @DeleteMapping(value = "/deliveryAddresses/{addrId}")
    public DeliveryAddressOutput delete(@PathVariable String addrId) {
        return deliveryAddressService.deleteDeliveryAddress(addrId);
    }

    /**
     * 根据用户id查询收货地址列表
     *
     * @param userId 用户Id
     * @return
     */
    @GetMapping(value = "/user/{userId}/deliveryAddresses")
    public DeliveryAddressOutput findByUserId(@PathVariable String userId) {
        return deliveryAddressService.findByUserId(userId);
    }

    /**
     * 更改默认的收货地址
     *
     * @param addrId 收货地址id
     * @param userId 用户Id
     * @return
     */
    @PutMapping(value = "/deliveryAddressses/{addrId}/addrDefault")
    public DeliveryAddressOutput updateStatus(@PathVariable String addrId, String userId) {
        return deliveryAddressService.updateDeliveryAddressStatus(addrId, userId);
    }
}