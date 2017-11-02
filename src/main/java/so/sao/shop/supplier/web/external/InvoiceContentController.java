package so.sao.shop.supplier.web.external;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.domain.external.InvoiceContent;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.external.InvoiceContentService;

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
@Api(description = "运维-发票内容模块【责任人：陈沙】")
public class InvoiceContentController {

    @Autowired
    private InvoiceContentService invoiceContentService;

    @ApiOperation(value = "添加发票内容",notes = "添加发票内容")
    @PostMapping(value = "/create")
    public Result create(@RequestBody InvoiceContent invoiceContent){

        return invoiceContentService.saveInvocieContent(invoiceContent);
    }

    @ApiOperation(value = "编辑发票内容",notes = "编辑发票内容")
    @PutMapping(value = "/update")
    public Result update(@RequestBody InvoiceContent invoiceContent){
        return invoiceContentService.updateInvoiceContent(invoiceContent);
    }

    @ApiOperation(value = "删除发票内容", notes = "删除发票内容")
    @DeleteMapping(value = "/delete")
    public Result delete(@RequestParam Long id){
        return invoiceContentService.delete(id);
    }

    @ApiOperation(value = "查询发票内容列表",notes = "查询发票内容列表")
    @GetMapping(value="/searchAll")
    public Result searchAll(@RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize){
        return invoiceContentService.searchInvoiceContents(pageNum,pageSize);
    }
}
