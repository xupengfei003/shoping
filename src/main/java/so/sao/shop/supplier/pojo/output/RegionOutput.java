package so.sao.shop.supplier.pojo.output;

import so.sao.shop.supplier.domain.Region;
import so.sao.shop.supplier.pojo.BaseResult;

import java.util.List;

/**
 * <p>
 *     行政区域出参
 * </p>
 */
public class RegionOutput extends BaseResult {

    /**
     * 行政区域列表
     */
    private List<Region> regionList;

    public List<Region> getRegionList() {
        return regionList;
    }

    public void setRegionList(List<Region> regionList) {
        this.regionList = regionList;
    }
}
