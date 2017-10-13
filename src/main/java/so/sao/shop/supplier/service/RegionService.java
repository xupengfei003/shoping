package so.sao.shop.supplier.service;




import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.output.RegionOutput;

import java.util.Map;

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

    Map<String, Object> getAllRegion();
}
