package so.sao.shop.supplier.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.CommBrandService;

@RestController
@RequestMapping("/commBrand")
public class CommBrandController {

    @Autowired
    private CommBrandService commBrandService;

    @GetMapping(value="/search")
    public Result search(){
        return commBrandService.searchBrand();
    }
}
