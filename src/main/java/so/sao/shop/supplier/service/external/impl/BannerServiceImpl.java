package so.sao.shop.supplier.service.external.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import com.github.pagehelper.PageInfo;
import so.sao.shop.supplier.config.azure.AzureBlobService;
import so.sao.shop.supplier.dao.AccountDao;
import so.sao.shop.supplier.dao.SupplierCommodityDao;
import so.sao.shop.supplier.dao.external.BannerDao;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.domain.external.Banner;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.BannerInput;
import so.sao.shop.supplier.pojo.input.CommAccountBanInput;
import so.sao.shop.supplier.pojo.output.AccountBannerOut;
import so.sao.shop.supplier.pojo.output.BannerOut;
import so.sao.shop.supplier.pojo.output.CommodityBannerOut;
import so.sao.shop.supplier.pojo.vo.CommBlobUpload;
import so.sao.shop.supplier.service.external.BannerService;
import so.sao.shop.supplier.util.Ognl;
import so.sao.shop.supplier.util.PageTool;
/**
 * <p>Title: BannerServiceImpl</p>
 * <p>Description: 轮播图业务层实现</p>
 * <p>Company:透云-中软-西安项目组 </p>
 * @author tengfei.zhang
 * @date 2017年9月14日
 */
@Service
public class BannerServiceImpl implements BannerService {
	
	@Autowired
	private BannerDao bannerDao;
	
	@Autowired
    private AzureBlobService azureBlobService;
	
	@Autowired
	private AccountDao accountDao;
	
	@Autowired
	private SupplierCommodityDao supplierCommodityDao;
	
	/**
	 * 新增轮播图
	 * @param banner 轮播图对象
	 */
	@Override
	public Result saveBanner(Banner banner) {
		//判断上下架时间是否间隔一天
		if((banner.getOffShelfTime().getTime() - banner.getOnShelvesTime().getTime())/(1000*3600*24)<1) {
			return Result.fail("上下架时间必须间隔至少一天");
		}
		//根据轮播位置和上架时间确定是否有轮播图存在
		List<BannerOut> banners = bannerDao.findBanners(banner.getLocation(), banner.getOnShelvesTime());
		if(banners != null && !banners.isEmpty()) {
			return Result.fail("此轮播位在上架时间已有待上架或已上架轮播图");
		}
		//插入轮播图
		banner.setStatus(Ognl.isEmpty(banner.getStatus()) ? "1" :banner.getStatus());
		banner.setCreateAt(new Date());
		banner.setUpdateAt(new Date());
		bannerDao.saveBanner(banner);
		return Result.success("轮播图添加成功");
	}

	/**
	 * 添加图片
	 * @param multipartFile 图片文件
	 * @return 添加结果
	 */
	@Override
	public Result uploadImage(MultipartFile multipartFile) {
		//判断文件是否为空
        if (multipartFile == null) {
            return Result.fail("上传文件为空");
        }
        //获取文件名称
        String fileName = multipartFile.getOriginalFilename();
        //获取文件后缀名
        String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
        //判断文件类型 jpg/png/jpeg
        if(!("jpg".equals(prefix) || "JPG".equals(prefix)||"png".equals(prefix)||"PNG".equals(prefix)||"jpeg".equals(prefix)||"JPEG".equals(prefix))) {
            return Result.fail("上传图片必须为jpg/png/jpeg格式");
        }
        //初始化返回结果
		List<CommBlobUpload> blobUploadList = new ArrayList<CommBlobUpload>();
		Result result = azureBlobService.uploadFilesComm(new MultipartFile[] {multipartFile}, blobUploadList);
		if(blobUploadList.isEmpty()){
            return result;
        }else{
            return Result.success("文件上传成功", blobUploadList.get(0));
        }
	}
	
	/**
	 * 根据供应商名称查询供应商信息
	 * @param commAccountBanInput 供应商查询参数
	 * @return 轮播图供应商列表
	 */
	@Override
	public Result getAccounts(CommAccountBanInput commAccountBanInput) {
		PageTool.startPage(commAccountBanInput.getPageNum(),commAccountBanInput.getPageSize());
		List<AccountBannerOut> accountBannerOuts = accountDao.findByProviderName(commAccountBanInput.getProviderName());
		PageInfo<AccountBannerOut> pageInfo = new PageInfo<AccountBannerOut>(accountBannerOuts);
		if(Ognl.isNotNull(pageInfo)) {
			return  Result.success("查询供应商成功", pageInfo);
		}
		 return Result.fail("没有符合条件的供应商");
	}

	/**
	 * 根据id更新轮播图信息
	 * @param banner 轮播图对象
	 * @return 返回更新结果
	 */
	@Override
	public Result updateBanner(Banner banner) {
		//判断供应商状态
		if("2".equals(banner.getUrlType())) {
			Account account = accountDao.selectByPrimaryKey(Long.parseLong(banner.getUrlValue()));
			if(account.getAccountStatus() != 1) {
				return Result.fail("此供应商已经停用或删除，请更换供应商或更改跳转方式");
			}
		}
		//判断轮播图状态
		if("2".equals(banner.getStatus())) {
			return Result.fail("不能编辑已上架的轮播图");
		}
		//判断上下架时间间隔
		if((banner.getOffShelfTime().getTime() - banner.getOnShelvesTime().getTime())/(1000*3600*24)<1) {
			return Result.fail("上下架时间必须间隔至少一天");
		}
		//根据轮播位置和上架时间确定是否有轮播图存在
		List<BannerOut> banners = bannerDao.findBanners(banner.getLocation(),banner.getOnShelvesTime());
		//根据原上下架时间、轮播图位置确定是否更改，如果没有更改直接执行更新
		for(BannerOut bannerOut : banners) {
			if(bannerOut.getOnShelvesTime().getTime() == banner.getOnShelvesTime().getTime() && bannerOut.getOffShelfTime().getTime() == banner.getOffShelfTime().getTime() && bannerOut.getLocation().equals(banner.getLocation())) {
				banner.setStatus(Ognl.isEmpty(banner.getStatus()) ? "1" :banner.getStatus());
				banner.setUpdateAt(new Date());
				bannerDao.update(banner);
				return Result.success("更新轮播图成功");
			}
		}
		//根据轮播位置和上架时间确定是否有轮播图存在
		if(banners != null && !banners.isEmpty()) {
			return Result.fail("此轮播位在上架时间已有待上架或已上架轮播图");
		}
		//更新轮播图
		banner.setStatus(Ognl.isEmpty(banner.getStatus()) ? "1" :banner.getStatus());
		banner.setUpdateAt(new Date());
		bannerDao.update(banner);
		return Result.success("更新轮播图成功");
	}
	
	/**
     * 根据商品名称商品类型查询商品信息
     * @param commAccountBanInput 查询参数
     * @return 查询结果
     */
	@Override
	public Result getCommoditys(CommAccountBanInput commAccountBanInput) {
		PageTool.startPage(commAccountBanInput.getPageNum(),commAccountBanInput.getPageSize());
		List<CommodityBannerOut> commodityBannerOuts = supplierCommodityDao.findCommByNameAndCategory(
				commAccountBanInput.getCommodityName(), commAccountBanInput.getCategoryOneId(), commAccountBanInput.getCategoryTwoId(), commAccountBanInput.getCategoryThreeId());
		PageInfo<CommodityBannerOut> pageInfo = new PageInfo<CommodityBannerOut>(commodityBannerOuts);
		if(Ognl.isNotNull(pageInfo)){
			return Result.success("商品信息查询成功", pageInfo);
		}
		return Result.fail("没有符合条件的商品信息");
	}
	
	/**
	 * 根据id查询轮播图信息
	 * @param id
	 * @return 轮播图信息
	 */
	@Override
	public Result getBanner(Long id) {
		BannerOut banner = bannerDao.findOne(id);
		if(Ognl.isNotNull(banner)) {
			return Result.success("查询轮播图成功", banner);
		}
		return Result.fail("没有符合条件的轮播图");
	}
	
	/**
	 * 根据条件查询轮播图
	 * @param banner 参数
	 * @return 轮播图对象
	 */
	@Override
	public Result searchBanners(BannerInput banner) {
		if("0".equals(banner.getStatus())) {
			banner.setStatus(null);
		}
		if("0".equals(banner.getLocation())) {
			banner.setLocation(null);
		}
		PageTool.startPage(banner.getPageNum(),banner.getPageSize());
		List<BannerOut> banners = bannerDao.findPage(banner);
		PageInfo<BannerOut> pageInfo = new PageInfo<BannerOut>(banners);
		if(Ognl.isNotNull(pageInfo)) {
			return Result.success("查询轮播图列表成功", pageInfo);
		}
		return Result.fail("没有符合条件的轮播图");
	}

	/**
	 * 根据id删除轮播图
	 * @param id
	 * @return 删除结果
	 */
	@Override
	public Result deleteBanner(Long id) {
		BannerOut banner = bannerDao.findOne(id);
		if("2".equals(banner.getStatus())) {
			return Result.fail("只能删除待发布和已下架的轮播图");
		}
		bannerDao.deleteById(id);
		return Result.success("删除轮播图成功");
	}
	
	/**
	 * 批量删除轮播图
	 * @param ids
	 * @return 删除结果
	 */
	@Override
	public Result deleteBanners(Long[] ids) {
		for (long id : ids) {
			BannerOut banner = bannerDao.findOne(id);
			if("2".equals(banner.getStatus())) {
				return Result.fail("只能删除待发布和已下架的轮播图");
			}
		}
		bannerDao.updateByIds(ids,"4");
		return Result.success("批量删除轮播图成功");
	}
	
	/**
	 * 根据上下架时间批量更新轮播图
	 * @param onTime 上架时间
	 * @param status 更新状态
	 * @return 更新结果
	 * @throws Exception 
	 */
	@Override
	public Result updateBanners(Date onTime, String status) throws Exception {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		//根据上下架时间和轮播图状态查询轮播图
		List<BannerOut> BannerOut = bannerDao.findByUpAndDownTime(sdf.parse(sdf.format(onTime)), status);
		if(BannerOut != null && !BannerOut.isEmpty()) {
			Long[] ids = new Long[10];
			for(int i = 0; i < BannerOut.size(); i++) {
				ids[i] = BannerOut.get(i).getId();
			}
			bannerDao.updateByIds(ids, String.valueOf((Integer.parseInt(status)+1)));
			return Result.success("批量更新轮播图成功");
		}else {
			return Result.fail("当天没有要更新状态的轮播图");
		}
	}

}
