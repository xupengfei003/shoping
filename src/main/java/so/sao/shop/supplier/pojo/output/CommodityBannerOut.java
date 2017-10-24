package so.sao.shop.supplier.pojo.output;

import so.sao.shop.supplier.config.CommConstant;

/**
 * <p>Title: CommodityBannerOut</p>
 * <p>Description:轮播图商品列表返回值 </p>
 * <p>Company:透云-中软-西安项目组 </p>
 * @author tengfei.zhang
 * @date 2017年9月18日
 */
public class CommodityBannerOut {
	/**
	 * 商品id
	 */
	private Long commodityId;
	/**
	 * 供应商名称
	 */
	private String providerName;
	/**
	 * 缩略图地址
	 */
	private String minImg;
	/**
	 * 商品条码
	 */
	private String code69;
	/**
	 * 商家编码
	 */
	private String commodityCode;
	/**
	 * 商品品牌
	 */
	private String brandName;
	/**
	 * 商品名称
	 */
	private String commodityName;
	/**
	 * 商品规格名称
	 */
	private String specName;
	/**
	 * 商品规格值
	 */
	private String ruleVal;
	/**
	 * 商品单位
	 */
	private String unitName;
	/**
	 * 库存数量
	 */
	private Double inventory;
	/**
	 * 商品状态
	 */
	private String status;
	
	public Long getCommodityId() {
		return commodityId;
	}
	public void setCommodityId(Long commodityId) {
		this.commodityId = commodityId;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public String getMinImg() {
		return minImg;
	}
	public void setMinImg(String minImg) {
		this.minImg = minImg;
	}
	public String getCode69() {
		return code69;
	}
	public void setCode69(String code69) {
		this.code69 = code69;
	}
	public String getCommodityCode() {
		return commodityCode;
	}
	public void setCommodityCode(String commodityCode) {
		this.commodityCode = commodityCode;
	}
	public String getBrandName() {
		return brandName;
	}
	public void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public String getCommodityName() {
		return commodityName;
	}
	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}
	public String getSpecName() {
		return specName;
	}
	public void setSpecName(String specName) {
		this.specName = specName;
	}
	public String getRuleVal() {
		return ruleVal;
	}
	public void setRuleVal(String ruleVal) {
		this.ruleVal = ruleVal;
	}
	public String getUnitName() {
		return unitName;
	}
	public void setUnitName(String unitName) {
		this.unitName = unitName;
	}
	public Double getInventory() {
		return inventory;
	}
	public void setInventory(Double inventory) {
		this.inventory = inventory;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = CommConstant.getStatus(Integer.parseInt(status));
	}
	
}
