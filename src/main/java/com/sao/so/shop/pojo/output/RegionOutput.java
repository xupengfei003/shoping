package com.sao.so.shop.pojo.output;

import com.sao.so.shop.domain.Region;
import com.sao.so.shop.pojo.BaseResult;

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
