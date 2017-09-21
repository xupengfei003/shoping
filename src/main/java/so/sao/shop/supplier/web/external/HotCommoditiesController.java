package so.sao.shop.supplier.web.external;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.AddHotCommInput;
import so.sao.shop.supplier.pojo.input.HotCommodityInput;
import so.sao.shop.supplier.pojo.input.HotCommoditySaveInput;
import so.sao.shop.supplier.service.external.HotCommoditiesService;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/comm/hotCommodities")
@Api(description = "运维-热门商品接口")
public class HotCommoditiesController {

    @Autowired
    private HotCommoditiesService hotCommoditiesService;

    @ApiOperation(value="查询热门商品列表", notes="负责人：张瑞兵")
    @GetMapping(value="/searchAll")
    public Result searchHotCommodity( HotCommodityInput hotCommodityInput) {
        return hotCommoditiesService.searchHotCommodities(hotCommodityInput);
    }

    @ApiOperation(value="按条件查询所有的商品列表", notes="负责人：张瑞兵")
    @GetMapping(value="/searchCommodity")
    public Result searchCommodity( AddHotCommInput addHotCommInput) {
        return hotCommoditiesService.searchCommodities(addHotCommInput);
    }

    @ApiOperation(value="保存所有热门商品", notes="负责人：张瑞兵")
    @PostMapping(value="/saveAll")
    public Result saveAll( @RequestBody List<HotCommoditySaveInput> hotCommoditySaveInputs) {
        return hotCommoditiesService.saveAllHotCommodity(hotCommoditySaveInputs);
    }


}
