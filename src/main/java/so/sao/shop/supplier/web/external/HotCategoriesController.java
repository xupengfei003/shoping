package so.sao.shop.supplier.web.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.domain.external.HotCategories;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.external.HotCategoriesService;

import java.util.List;

/**
 * 运维平台热门分类配置Controller
 * Created by LiuGang on 2017/9/18.
 */
@RestController
@RequestMapping("/external/hotCategories")
public class HotCategoriesController {

    @Autowired
    private HotCategoriesService hotCategoriesService;

    /**
     * 查询热门分类列表
     * @param pageNum  当前页号
     * @param pageSize 页面大小/记录条数
     * @return Result
     */
    @GetMapping(value="/search")
    public Result search(@RequestParam(required = false) Integer pageNum, @RequestParam(required = false) Integer pageSize){

        return hotCategoriesService.searchHotCategories(pageNum, pageSize);
    }

    /**
     * 更换icon
     * @param file 图片文件
     * @return
     */
    @PostMapping(value="/updateIcon")
    public Result updateIcon(MultipartFile file){

        return hotCategoriesService.updateIcon(file);
    }

    /**
     *  添加/编辑热门分类
     * @param hotCategories 热门分类集合
     * @return
     */
    @PostMapping(value = "/create/bulk")
    public Result save(@RequestBody List<HotCategories> hotCategories)
    {
        return hotCategoriesService.saveHotCategories(hotCategories);
    }
}
