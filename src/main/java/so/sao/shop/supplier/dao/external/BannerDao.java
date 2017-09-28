package so.sao.shop.supplier.dao.external;

import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.external.Banner;
import so.sao.shop.supplier.pojo.input.BannerInput;
import so.sao.shop.supplier.pojo.output.BannerOut;

/**
 * <p>Title: BannerDao</p>
 * <p>Description: 轮播图增删改查 </p>
 * <p>Company:透云-中软-西安项目组 </p>
 * @author tengfei.zhang
 * @date 2017年9月14日
 */
public interface BannerDao {
	/**
	 * 新增轮播图
	 * @param banner 轮播图对象
	 */
	void saveBanner(Banner banner);
	
	/**
	 * 根据上架时间查询轮播图是否存在
	 * @param onShelvesTime
	 * @return
	 */
	List<BannerOut> findBanners(@Param("location") String location,@Param("onShelvesTime") Date onShelvesTime);
	
	/**
	 * 更新轮播图
	 * @param banner 轮播图对象
	 */
	void update(Banner banner);
	
	/**
	 * 根据id查询轮播图
	 * @param id
	 * @return 返回轮播图
	 */
	BannerOut findOne(Long id);
	
	/**
	 * 根据条件查询轮播图对象
	 * @param banner 参数
	 * @return 轮播图对象
	 */
	List<BannerOut> findPage(BannerInput banner);
	
	/**
	 * 根据id删除轮播图
	 * @param id
	 */
	void deleteById(Long id);
	
	/**
	 * 批量更新轮播图
	 * @param ids
	 */
	void updateByIds(@Param("ids") Long[] ids,@Param("status") String status);
	/**
	 * 根据上下架时间查询轮播图
	 * @param onTime 上架时间
	 * @param status 轮播图状态
	 * @return 轮播图列表
	 */
	List<BannerOut> findByUpAndDownTime(@Param("onTime") Date onTime,@Param("status") String status);
}
