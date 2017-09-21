package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.CommBrandService;

@RestController
@RequestMapping("/commBrand")
@Api(description = "商品品牌管理接口")
public class CommBrandController {

    @Autowired
    private CommBrandService commBrandService;

    @ApiOperation(value="查询商品品牌", notes="【负责人：武凯江】")
    @GetMapping(value="/search")
    public Result search(){
        return commBrandService.searchBrand();
    }
}
