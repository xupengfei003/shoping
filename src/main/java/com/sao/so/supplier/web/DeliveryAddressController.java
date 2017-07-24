package com.sao.so.supplier.web;

import com.sao.so.supplier.domain.DeliveryAddress;
import com.sao.so.supplier.pojo.output.DeliveryAddressOutput;
import com.sao.so.supplier.service.DeliveryAddressService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 
 * 前端控制器
 * 
 *
 * @author zhangruibing
 * @since 2017-07-17
 */
@Api(value = "DeliveryAddressController", tags = "收货地址")
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
    @ApiOperation(value = "新增收货地址", notes = "新增收货地址")
    @ApiImplicitParam(value = "用户id", name = "userId", required = true, paramType = "form")
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
    @ApiOperation(value = "编辑收货地址", notes = "编辑收货地址")
    @ApiImplicitParam(name = "addrId", value = "收货地址id", required = true, dataType = "long", paramType = "path")
    @PutMapping(value = "/deliveryAddresses/{addrId}")
    public DeliveryAddressOutput update(DeliveryAddress deliveryAddress) {
        return deliveryAddressService.updateDeliveryAddress(deliveryAddress);
    }

    /**
     * 删除收货地址
     *
     * @param addrId 收货地址id
     */
    @ApiOperation(value = "删除收货地址", notes = "删除收货地址")
    @ApiImplicitParam(value = "收货地址id", name = "addrId", required = true, paramType = "path")
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
    @ApiOperation(value = "查询用户的所有收货地址", notes = "查询用户的所有收货地址")
    @ApiImplicitParam(value = "用户id", name = "userId", required = true, paramType = "path")
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
    @ApiOperation(value = "设置默认收货地址", notes = "设置默认收货地址")
    @ApiImplicitParams({
            @ApiImplicitParam(value = "用户id", name = "userId", required = true, paramType = "query"),
            @ApiImplicitParam(value = "收货地址id", name = "addrId", required = true, paramType = "path")
    })
    @PutMapping(value = "/deliveryAddressses/{addrId}/addrDefault")
    public DeliveryAddressOutput updateStatus(@PathVariable String addrId, String userId) {
        return deliveryAddressService.updateDeliveryAddressStatus(addrId, userId);
    }
}