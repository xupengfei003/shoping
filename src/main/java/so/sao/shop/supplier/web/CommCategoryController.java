package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.input.CommCategoryInput;
import so.sao.shop.supplier.pojo.input.CommCategoryUpdateInput;
import so.sao.shop.supplier.pojo.output.CommCategorySelectOutput;
import so.sao.shop.supplier.service.CommCategoryService;

import javax.validation.Valid;
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


/*    @ApiOperation(value="查询商品品类", notes="根据ID返回相应的商品品类")
    @GetMapping(value="/get/{id}")
    public CommCategory get(@PathVariable Long id){
        return null;
    }*/

//    @ApiOperation(value="查询商品品类集合", notes="根据参数返回符合条件的商品品类集合")
//    @GetMapping(value="/search")
//    public PageInfo search(){
//        return null;
//    }

/*    @ApiOperation(value="查询商品品类集合树", notes="返回商品品类树")
    @GetMapping(value="/tree")
    public List<CommCategory> Tree(){
        return null;
    }*/

    @ApiOperation(value="查询商品品类集合", notes="根据参数返回符合条件的商品品类集合")
    @ApiImplicitParam(name = "pid",value = "pid",required=false,paramType = "query", dataType = "Long")
    @GetMapping(value="/searchCommCategory")
    public List<CommCategorySelectOutput> search(Long pid){
    return commCategoryService.searchCommCategory(pid);
    }

    @ApiOperation(value="保存商品品类", notes="保存新增加的商品品类")
    @PostMapping(value="/save")
    public BaseResult create(@Validated @RequestBody CommCategoryInput category){
        return commCategoryService.saveCommCategory(category);
    }
 
    @ApiOperation(value="修改商品品类", notes="修改商品类型基本信息") 
    @PutMapping(value="/update")
    public BaseResult update(@Valid @RequestBody CommCategoryUpdateInput commCategoryUpdateInput){
        return commCategoryService.updateCommCategory(commCategoryUpdateInput);
    }

    @ApiOperation(value="删除商品品类", notes="根据ID删除相应的商品品类")
    @DeleteMapping(value="/delete/{id}")
    public BaseResult delete(@PathVariable Long id){
        return commCategoryService.deleteCommCategory(id);
    }

}
