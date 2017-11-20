package so.sao.shop.supplier.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.PurchaseItemService;

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
public class PurchaseItemController {
    @Autowired
    private PurchaseItemService purchaseItemService;

    /**
     * 根据订单编号分页显示订单明细
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示条数
     * @param orderId  订单编号
     * @return result 显示数据
     */
    @GetMapping(value = "/purchase/{orderId}/PurchaseItems")
    public Result search(Integer pageNum, Integer pageSize, @PathVariable("orderId") String orderId) {

        return purchaseItemService.searchPurchaseItems(pageNum, pageSize, orderId);
    }
}
