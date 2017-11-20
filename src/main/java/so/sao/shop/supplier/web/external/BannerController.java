package so.sao.shop.supplier.web.external;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.domain.external.Banner;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.BannerInput;
import so.sao.shop.supplier.pojo.input.CommAccountBanInput;
import so.sao.shop.supplier.service.external.BannerService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * <p>Title: BannerController</p>
 * <p>Description: 轮播图接口</p>
 * <p>Company:透云-中软-西安项目组 </p>
 * @author tengfei.zhang
 * @date 2017年9月14日
 */
@RestController
@RequestMapping("/external/banner")
public class BannerController {
	
	@Autowired
	BannerService bannerService;
	
	/**
	 * 添加图片
	 * @param request
	 * @return 上传结果
	 * @throws Exception 
	 */
    @PostMapping("/uploadimage")
	public Result uploadImage(HttpServletRequest request,@RequestBody Map<String,Object> fileObj) throws Exception {
//		String fileName = (String) fileObj.get("fileName");
//		String fileBytes =  (String) fileObj.get("fileBytes");
//		byte[] filebyte = Base64.getDecoder().decode(fileBytes);
//        MultipartFile multipartFile = new  MockMultipartFile(fileName,fileName, "image/jpeg", filebyte);
//        Result result = bannerService.uploadImage(multipartFile);
		return null;
	}
	
	/**
	 * 添加轮播图
	 * @param request
	 * @param banner 轮播图对象
	 * @return
	 */
	@PostMapping("/create")
	public Result createBanner(HttpServletRequest request, @Valid @RequestBody Banner banner) throws Exception {
		return bannerService.saveBanner(banner);
	}
	
	/**
	 * 根据供应商名称查询供应商
	 * @param request
	 * @param commAccountBanInput 供应商查询参数
	 * @return
	 */
	@GetMapping("/findaccouts")
	public Result getAccounts(HttpServletRequest request,CommAccountBanInput commAccountBanInput) {
		return bannerService.getAccounts(commAccountBanInput);
	}
	
	/**
	 * 根据id更新轮播图信息
	 * @param request
	 * @param banner 轮播图对象
	 * @return 更新结果
	 */
	@PutMapping("/update")
	public Result update(HttpServletRequest request,@Valid @RequestBody Banner banner) throws Exception{
		return bannerService.updateBanner(banner);
	}
	
	/**
	 * 根据商品名称商品类型查询商品信息
	 * @param request
	 * @param commAccountBanInput 商品查询参数
     * @return 查询结果
	 */
	@GetMapping("/findcommodity")
	public Result getCommoditys(HttpServletRequest request,CommAccountBanInput commAccountBanInput) {
		return bannerService.getCommoditys(commAccountBanInput);
	}
	
	/**
	 * 根据id查询轮播图
	 * @param request
	 * @param id
	 * @return 查询结果
	 */
	@GetMapping("/get")
	public Result get(HttpServletRequest request,@RequestParam Long id) {
		return bannerService.getBanner(id);
	}
	
	/**
	 * 查询轮播图列表
	 * @param request
	 * @param banner 查询条件
	 * @return 查询结果
	 */
	@GetMapping("/search")
	public Result search(HttpServletRequest request,BannerInput banner) {
		return bannerService.searchBanners(banner);
	}
	
	/**
	 * 根据id删除轮播图
	 * @param id
	 * @return
	 */
	@DeleteMapping("/delete")
	public Result delete(HttpServletRequest request,@RequestParam Long id) {
		return bannerService.deleteBanner(id);
	}
	
	/**
	 * 批量删除轮播图
	 * @param ids
	 * @return
	 */
	@DeleteMapping("/deletebanners")
	public Result deleteBanners(HttpServletRequest request,@RequestParam String ids) {
		String[] idss = ids.split(",");
		Long[] bannerIds = new Long[idss.length];
		for(int i=0;i<idss.length;i++) {
			bannerIds[i] = Long.parseLong(idss[i]);
		}
		return bannerService.deleteBanners(bannerIds);
	}
}
