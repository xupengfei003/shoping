package so.sao.shop.supplier.web.external;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommCategoryListInput;
import so.sao.shop.supplier.service.external.CommCategoryPortalService;

import javax.validation.Valid;
import java.util.Map;

/**
 *<p>Version: supplier V1.1.0 </p>
 *<p>Title: CommCategoryPortalController</p>
 *<p>Description: </p>
 *@author: hanchao
 *@Date: Created in 2017/10/27 11:46
 */
@RestController
@RequestMapping("/external/commCategory")
@Api(description = "商品分类管理接口")
public class CommCategoryPortalController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommCategoryPortalService commCategoryPortalService;

    @ApiOperation(value="批量修改商品分类名称和显示状态", notes="【责任人：韩超】")
    @PutMapping("/updateCommCategory")
    public Result updateCommCategorys(@Valid @RequestBody CommCategoryListInput commCategoryListInput) throws Exception {
        return commCategoryPortalService.updateCommCategorys(commCategoryListInput);
    }

    @ApiOperation(value = "查询所有商品分类（树形结构）", notes = "【责任人：韩超】")
    @GetMapping("/searchCommCategory")
    public Result searchAll() throws Exception {
        //查询
        Map map = commCategoryPortalService.searchCommCategorys();
        return Result.success(Constant.MessageConfig.MSG_SUCCESS,map);
    }
}

