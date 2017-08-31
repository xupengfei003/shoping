package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
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
    public BaseResult update(@Valid @RequestBody CommCategoryUpdateInput commCategoryUpdateInput , BindingResult bindingResult){
        //校验商品品类修改对象的
        if (bindingResult.hasErrors()) {
            BaseResult result = new BaseResult();
            List<ObjectError> list = bindingResult.getAllErrors();
            for (ObjectError error : list) {
                result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                result.setMessage(error.getDefaultMessage());
            }
            return result;
        }
        return commCategoryService.updateCommCategory(commCategoryUpdateInput);
    }

    @ApiOperation(value="删除商品科属", notes="根据ID删除相应的商品科属【责任人：陈沙】")
    @DeleteMapping(value="/delete/{id}")
    public Result delete(@PathVariable Long id){
        return commCategoryService.deleteCommCategory(id);
    }

    @ApiOperation(value="查询商品一级类型集合", notes="根据参数返回符合条件的商品品类集合")
    @GetMapping(value="/searchCategory")
    public List<CommCategorySelectOutput> searchType0(){
        return commCategoryService.searchCommCategory(0L);
    }

}
