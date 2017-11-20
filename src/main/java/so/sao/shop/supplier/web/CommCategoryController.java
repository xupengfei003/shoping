package so.sao.shop.supplier.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.output.CategoryOutput;
import so.sao.shop.supplier.service.CommCategoryService;

import java.util.List;
import java.util.Map;

/**
 * 商品品类Controller
 * Created by QuJunLong on 2017/7/17.
 */
@RestController
@RequestMapping("/commType")
public class CommCategoryController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommCategoryService commCategoryService;

    @GetMapping(value="/searchCommCategory")
    public List<CategoryOutput> search(Long pid){
        return commCategoryService.searchCommCategory(pid);
    }

    @GetMapping(value = "/searchAllCommCategory")
    public Result searchAll() throws Exception {
        //查询
        Map map = commCategoryService.searchCommCategorys();
        return Result.success(Constant.MessageConfig.MSG_SUCCESS,map);
    }
}
