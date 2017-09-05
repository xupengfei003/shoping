package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.pojo.output.CommCategorySelectOutput;
import so.sao.shop.supplier.service.CommCategoryService;

import java.util.List;

/**
 * 商品品类Controller
 * Created by QuJunLong on 2017/7/17.
 */
@RestController
@RequestMapping("/commType")
@Api(description = "商品类型管理接口")
public class CommCategoryController {

    @Autowired
    private CommCategoryService commCategoryService;

    @ApiOperation(value="查询商品品类集合", notes="【责任人：张瑞兵】")
    @ApiImplicitParam(name = "pid",value = "pid",required=false,paramType = "query", dataType = "Long")
    @GetMapping(value="/searchCommCategory")
    public List<CommCategorySelectOutput> search(Long pid){
        return commCategoryService.searchCommCategory(pid);
    }

}
