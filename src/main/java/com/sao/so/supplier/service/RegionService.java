package com.sao.so.supplier.service;




import com.sao.so.supplier.pojo.output.RegionOutput;

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
