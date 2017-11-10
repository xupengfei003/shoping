package so.sao.shop.supplier.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.InvoiceSettingUpdateInput;
import so.sao.shop.supplier.service.InvoiceSettingService;

import javax.servlet.http.HttpServletRequest;

/**
 * 供应商发票设置controller
 *
 * @author zhaoyan
 * @create 2017/11/1 10:00
 */
@RestController
@RequestMapping("/invoiceSetting")
public class InvoiceSettingController {
    @Autowired
    private InvoiceSettingService invoiceSettingService;

    @PutMapping(value = "update")
    public Result update(HttpServletRequest request, @RequestBody InvoiceSettingUpdateInput invoiceSettingUpdateInput) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否非法登录
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        if (invoiceSettingUpdateInput.getStatus() == 1 && invoiceSettingUpdateInput.getInvoice() == 0 && invoiceSettingUpdateInput.getSpecialInvoice() == 0){
            return Result.fail("开启发票设置时必须至少选择一项!");
        }
        Long supplierId = user.getAccountId();
        return invoiceSettingService.updateInvoiceSetting(invoiceSettingUpdateInput,supplierId);
    }

    @GetMapping(value = "get")
    public Result search(HttpServletRequest request) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否非法登录
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        Long supplierId = user.getAccountId();
        return invoiceSettingService.searchBySupplierId(supplierId);
    }
}
