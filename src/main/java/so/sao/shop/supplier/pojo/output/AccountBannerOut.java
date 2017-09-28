package so.sao.shop.supplier.pojo.output;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * <p>Title: AccountBannerOut</p>
 * <p>Description:轮播图供应商查询 </p>
 * <p>Company:透云-中软-西安项目组 </p>
 * @author tengfei.zhang
 * @date 2017年9月17日
 */
public class AccountBannerOut {
	/**
	 * 供应商id
	 */
	private Long accountId;
	/**
	 * 供应商名称
	 */
	private String providerName;
	/**
	 * 合同法人代表
	 */
	private String responsible;
	/**
	 * 合同创建日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date createDate;
	/**
	 * 合同截止日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date endDate;
	/**
	 * 合同法人代表电话
	 */
	private String responsiblePhone;
	/**
	 * 合同注册地址（省）
	 */
	private String province;
	/**
	 * 合同注册地址（市）
	 */
	private String city;
	/**
	 * 合同注册地址（区）
	 */
	private String district;
	
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public String getProviderName() {
		return providerName;
	}
	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
	public String getResponsible() {
		return responsible;
	}
	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getEndDate() {
		return endDate;
	}
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
	public String getResponsiblePhone() {
		return responsiblePhone;
	}
	public void setResponsiblePhone(String responsiblePhone) {
		this.responsiblePhone = responsiblePhone;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		this.district = district;
	}
}
