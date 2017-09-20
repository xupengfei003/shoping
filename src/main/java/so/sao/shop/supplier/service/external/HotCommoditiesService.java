package so.sao.shop.supplier.service.external;

import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.AddHotCommInput;
import so.sao.shop.supplier.pojo.input.HotCommodityInput;
import so.sao.shop.supplier.pojo.input.HotCommoditySaveInput;

import java.util.List;

public interface HotCommoditiesService {

    /**
     * 查询所有热门商品列表
     * @param hotCommodityInput
     * @return
     */
    Result searchHotCommodities(HotCommodityInput hotCommodityInput);

    /**
     * 按照条件查询所有商品列表
     * @param addHotCommInput
     * @return
     */
    Result searchCommodities(AddHotCommInput addHotCommInput);

    /**
     * 保存所有热门商品
     * @param hotCommoditySaveInput
     * @return
     */
    Result saveAllHotCommodity(List<HotCommoditySaveInput> hotCommoditySaveInput);
}
