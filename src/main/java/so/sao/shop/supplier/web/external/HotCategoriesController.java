package so.sao.shop.supplier.web.external;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.domain.external.HotCategories;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommSearchInput;
import so.sao.shop.supplier.service.external.HotCategoriesService;
import so.sao.shop.supplier.service.external.HotCommoditiesService;
import so.sao.shop.supplier.util.CheckUtil;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 运维平台热门分类配置Controller
 * Created by LiuGang on 2017/9/18.
 */
@RestController
@RequestMapping("/external/hotCategories")
@Api(description = "运维平台热门分类配置接口【责任人：刘刚】")
public class HotCategoriesController {

    @Autowired
    private HotCategoriesService hotCategoriesService;

    /**
     * 查询热门分类列表
     * @param pageNum  当前页号
     * @param pageSize 页面大小/记录条数
     * @return Result
     */
    @ApiOperation(value="查询热门分类列表", notes="查询热门分类列表")
    @GetMapping(value="/search")
    public Result search(@RequestParam(required = false) Integer pageNum,@RequestParam(required = false) Integer pageSize){

        return hotCategoriesService.searchHotCategories(pageNum, pageSize);
    }

    /**
     * 更换icon
     * @param file    图片文件
     * @return
     */
    @ApiOperation(value="更换icon", notes="更换icon")
    @PutMapping(value="/updateIcon")
    public Result updateIcon(MultipartFile file){

        return hotCategoriesService.updateIcon(file);
    }

    /**
     *  添加/编辑热门分类
     * @param hotCategories 热门分类集合
     * @return
     */
    @ApiOperation(value="添加/编辑热门分类", notes="添加/编辑热门分类")
    @PostMapping(value = "/create/bulk")
    public Result save(@RequestBody List<HotCategories> hotCategories)
    {
        return hotCategoriesService.saveHotCategories(hotCategories);
    }
}
