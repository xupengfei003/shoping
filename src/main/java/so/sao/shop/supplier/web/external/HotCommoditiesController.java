package so.sao.shop.supplier.web.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.AddHotCommInput;
import so.sao.shop.supplier.pojo.input.HotCommodityInput;
import so.sao.shop.supplier.pojo.input.HotCommoditySaveInput;
import so.sao.shop.supplier.service.external.HotCommoditiesService;

import java.util.List;

@RestController
@RequestMapping("/external/hotCommodities")
public class HotCommoditiesController {

    @Autowired
    private HotCommoditiesService hotCommoditiesService;

    @GetMapping(value="/searchAll")
    public Result searchHotCommodity( HotCommodityInput hotCommodityInput) {
        return hotCommoditiesService.searchHotCommodities(hotCommodityInput);
    }

    @GetMapping(value="/searchCommodity")
    public Result searchCommodity( AddHotCommInput addHotCommInput) {
        return hotCommoditiesService.searchCommodities(addHotCommInput);
    }

    @PostMapping(value="/saveAll")
    public Result saveAll( @RequestBody List<HotCommoditySaveInput> hotCommoditySaveInputs) {
        return hotCommoditiesService.saveAllHotCommodity(hotCommoditySaveInputs);
    }


}
