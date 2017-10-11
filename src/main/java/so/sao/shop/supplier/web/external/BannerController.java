package so.sao.shop.supplier.web.external;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import so.sao.shop.supplier.domain.external.Banner;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.BannerInput;
import so.sao.shop.supplier.pojo.input.CommAccountBanInput;
import so.sao.shop.supplier.service.external.BannerService;

/**
 * <p>Title: BannerController</p>
 * <p>Description: 轮播图接口</p>
 * <p>Company:透云-中软-西安项目组 </p>
 * @author tengfei.zhang
 * @date 2017年9月14日
 */
@RestController
@RequestMapping("/external/banner")
@Api(description = "轮播图-所有接口")
public class BannerController {
	
	@Autowired
	BannerService bannerService;
	
	/**
	 * 添加图片
	 * @param request
	 * @return 上传结果
	 * @throws Exception 
	 */
	@ApiOperation(value = "添加图片",notes = "添加图片【负责人：张腾飞】")
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
	@ApiOperation(value = "添加轮播图",notes = "添加轮播图【负责人：张腾飞】")
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
	@ApiOperation(value = "根据供应商名称查询供应商",notes = "根据供应商名称查询供应商【负责人：张腾飞】")
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
	@ApiOperation(value = "根据id更新轮播图信息",notes = "根据id更新轮播图信息【负责人：张腾飞】")
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
	@ApiOperation(value = "根据商品名称商品类型查询商品信息",notes = "根据商品名称商品类型查询商品信息【负责人：张腾飞】")
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
	@ApiOperation(value = "根据id查询轮播图",notes = "根据id查询轮播图【负责人：张腾飞】")
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
	@ApiOperation(value = "查询轮播图列表",notes = "查询轮播图列表【负责人：张腾飞】")
	@GetMapping("/search")
	public Result search(HttpServletRequest request,BannerInput banner) {
		return bannerService.searchBanners(banner);
	}
	
	/**
	 * 根据id删除轮播图
	 * @param id
	 * @return
	 */
	@ApiOperation(value = "根据id删除轮播图",notes = "根据id删除轮播图【负责人：张腾飞】")
	@DeleteMapping("/delete")
	public Result delete(HttpServletRequest request,@RequestParam Long id) {
		return bannerService.deleteBanner(id);
	}
	
	/**
	 * 批量删除轮播图
	 * @param ids
	 * @return
	 */
	@ApiOperation(value = "批量删除轮播图",notes = "批量删除轮播图【负责人：张腾飞】")
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
