package so.sao.shop.supplier.pojo.input;

/**
 * <p>Title: CommAccountBanInput</p>
 * <p>Description: 根据名称查询供应商列表、根据商品名称和商品类型查询商品列表入参</p>
 * <p>Company:透云-中软-西安项目组 </p>
 * @author tengfei.zhang
 * @date 2017年9月19日
 */
public class CommAccountBanInput {
	/**
     * 页数
     */
    private Integer pageNum;
    /**
     * 每页长度
     */
    private Integer pageSize;
    /**
     * 商品名称
     */
    private String commodityName;
    /**
     * 商品类型一
     */
    private Long categoryOneId;
    /**
     * 商品类型二
     */
    private Long categoryTwoId;
    /**
     * 商品类型三
     */
    private Long categoryThreeId; 
    /**
     * 供应商名称
     */
    private String providerName;
    
	public Integer getPageNum() {
		return pageNum;
	}
	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	public String getCommodityName() {
		return commodityName;
	}
	public void setCommodityName(String commodityName) {
		this.commodityName = commodityName;
	}
	public Long getCategoryOneId() {
		return categoryOneId;
	}
	public void setCategoryOneId(Long categoryOneId) {
		this.categoryOneId = categoryOneId;
	}
	public Long getCategoryTwoId() {
		return categoryTwoId;
	}
	public void setCategoryTwoId(Long categoryTwoId) {
		this.categoryTwoId = categoryTwoId;
	}
	public Long getCategoryThreeId() {
		return categoryThreeId;
	}
	public void setCategoryThreeId(Long categoryThreeId) {
		this.categoryThreeId = categoryThreeId;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
    
}
