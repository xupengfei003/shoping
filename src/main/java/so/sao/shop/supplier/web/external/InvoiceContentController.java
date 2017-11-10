package so.sao.shop.supplier.web.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.domain.external.InvoiceContent;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.external.InvoiceContentService;

import javax.validation.Valid;

/**
 * <p>Version: 运维平台 V0.9.0 </p>
 * <p>Title: InvoiceContent</p>
 * <p>Description:运维平台-发票内容controller</p>
 *
 * @author: sha.chen
 * @Date: Created in 2017/10/30 15:00
 */
@RestController
@RequestMapping("/external/invoiceContent")
public class InvoiceContentController {

    @Autowired
    private InvoiceContentService invoiceContentService;

    @PostMapping(value = "/create")
    public Result create(@RequestBody @Valid InvoiceContent invoiceContent){

        return invoiceContentService.saveInvocieContent(invoiceContent);
    }

    @PutMapping(value = "/update")
    public Result update(@RequestBody @Valid InvoiceContent invoiceContent){
        return invoiceContentService.updateInvoiceContent(invoiceContent);
    }

    @DeleteMapping(value = "/delete")
    public Result delete(@RequestParam Long id){
        return invoiceContentService.delete(id);
    }

    @GetMapping(value="/searchAll")
    public Result searchAll(@RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize){
        return invoiceContentService.searchInvoiceContents(pageNum,pageSize);
    }
}
