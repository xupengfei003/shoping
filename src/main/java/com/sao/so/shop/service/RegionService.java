package com.sao.so.shop.service;




import com.sao.so.shop.domain.Region;
import com.sao.so.shop.pojo.output.RegionOutput;

import java.util.List;

/**
 * <p>
 * 行政区字典表 服务类
 * </p>
 *
 * @author zhangruibing
 * @since 2017-07-17
 */
public interface RegionService {
	RegionOutput getListForRegion(Integer parentId, Integer level);
}
