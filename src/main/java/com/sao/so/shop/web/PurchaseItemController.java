package com.sao.so.shop.web;

import com.sao.so.shop.pojo.output.RecordToPurchaseOutput;
import com.sao.so.shop.service.PurchaseItemService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 订单明细表对应的controller类
 * </p>
 *
 * @author 透云-中软-西安项目组-niewenchao
 * @since 2017年7月19日
 **/
@RestController
@RequestMapping(value = "/orderItem")
@Api(description = "订单明细接口")
public class PurchaseItemController {
    @Autowired
    private PurchaseItemService purchaseItemService;

    /**
     * 根据订单编号分页显示订单明细
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @param orderId ID
     * @return 显示数据
     */
    @ApiOperation(value="分页显示订单明细列表", notes="  根据订单编号分页显示订单明细")
    @GetMapping(value = "/purchase/{orderId}/PurchaseItems")
    public RecordToPurchaseOutput search(Integer pageNum, Integer pageSize, @PathVariable("orderId") Long orderId){
        return purchaseItemService.searchPurchaseItems(pageNum,pageSize,orderId);
    }

}
