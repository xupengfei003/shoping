package so.sao.shop.supplier.service.external;

import java.util.Date;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.domain.external.Banner;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.BannerInput;
import so.sao.shop.supplier.pojo.input.CommAccountBanInput;
/**
 * <p>Title: BannerService</p>
 * <p>Description: 轮播图业务层接口</p>
 * <p>Company:透云-中软-西安项目组 </p>
 * @author tengfei.zhang
 * @date 2017年9月14日
 */
public interface BannerService {
	/**
	 * 新增轮播图
	 * @param banner 轮播图对象
	 */
	Result saveBanner(Banner banner);
	
	/**
	 * 添加图片
	 * @param multipartFile 图片文件
	 * @return 添加结果
	 */
	Result uploadImage(MultipartFile multipartFile);
	
	/**
	 * 根据供应商名称查询供应商信息
	 * @param providerName 供应商名称
	 * @return 轮播图供应商列表
	 */
	Result getAccounts(CommAccountBanInput commAccountBanInput);
	
	/**
	 * 根据id更新轮播图信息
	 * @param banner 轮播图对象
	 * @return 返回更新结果
	 */
	Result updateBanner(Banner banner);
	
	/**
     * 根据商品名称商品类型查询商品信息
     * @param commodityName 商品名称
     * @param categoryOneId 商品类型一
     * @param categoryTwoId 商品类型二
     * @param categoryThreeId 商品类型三
     * @return 查询结果
     */
	Result getCommoditys(CommAccountBanInput commAccountBanInput);
	
	/**
	 * 根据id查询轮播图信息
	 * @param id
	 * @return 轮播图信息
	 */
	Result getBanner(Long id);
	
	/**
	 * 根据条件查询轮播图
	 * @param banner 参数
	 * @return 轮播图对象
	 */
	Result searchBanners(BannerInput banner);
	
	/**
	 * 根据id删除轮播图
	 * @param id
	 * @return
	 */
	Result deleteBanner(Long id);
	
	/**
	 * 批量删除轮播图
	 * @param ids
	 * @return
	 */
	Result deleteBanners(Long[] ids);
	
	/**
	 * 根据上下架时间批量更新轮播图
	 * @param onTime 上架时间
	 * @param status 更新状态
	 * @return 更新结果
	 */
	Result updateBanners(Date onTime,String status) throws Exception;
}
