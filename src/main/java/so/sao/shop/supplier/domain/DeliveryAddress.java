package so.sao.shop.supplier.domain;


import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * <p>
 * 收货地址实体
 * </p>
 *
 * @author zhangruibing
 * @since 2017-07-17
 */
@ApiModel(value = "收货地址对象", description = "deliveryAddress")
public class DeliveryAddress {

    private static final long serialVersionUID = 1L;

    /**
     * 收货地址id
     */
    @ApiModelProperty(hidden = true)
	private Long addrId;

    /**
     * 收货人
     */
	@ApiModelProperty(value = "收货人")
	private String consignee;

    /**
     * 联系电话
     */
	@ApiModelProperty(value = "联系电话")
	private String consigneePhone;

    /**
     * 省
     */
	@ApiModelProperty(value = "省")
	private String addrProvince;

    /**
     * 市
     */
	@ApiModelProperty(value = "市")
	private String addrCity;

    /**
     * 区
     */
	@ApiModelProperty(value = "区")
	private String addrArea;

    /**
     * 详细地址
     */
	@ApiModelProperty(value = "详细地址")
	private String addrDetails;

	/**
	 * 街道
	 */
	@ApiModelProperty(value = "街道")
	private String addrStreet;

    /**
     * 是否设为默认地址(0：非默认地址；1：默认地址)
     */
	@ApiModelProperty(value = "默认地址")
	private Integer addrDefault;

	/**
     * 删除状态(0：未删除；1：已删除)
     */
	@ApiModelProperty(hidden = true)
	private Integer delState;

    /**
     * 创建时间
     */
	@ApiModelProperty(hidden = true)
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
	private Date createdAt;

    /**
     * 更新时间
     */
	@ApiModelProperty(hidden = true)
	@JsonFormat(pattern="yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
	private Date updatedAt;

    /**
     * 用户id
     */
	@ApiModelProperty(hidden = true)
	private Long userId;


	public Long getAddrId() {
		return addrId;
	}

	public void setAddrId(Long addrId) {
		this.addrId = addrId;
	}

	public String getConsignee() {
		return consignee;
	}

	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}

	public String getConsigneePhone() {
		return consigneePhone;
	}

	public void setConsigneePhone(String consigneePhone) {
		this.consigneePhone = consigneePhone;
	}

	public String getAddrProvince() {
		return addrProvince;
	}

	public void setAddrProvince(String addrProvince) {
		this.addrProvince = addrProvince;
	}

	public String getAddrCity() {
		return addrCity;
	}

	public void setAddrCity(String addrCity) {
		this.addrCity = addrCity;
	}

	public String getAddrArea() {
		return addrArea;
	}

	public void setAddrArea(String addrArea) {
		this.addrArea = addrArea;
	}

	public String getAddrDetails() {
		return addrDetails;
	}

	public void setAddrDetails(String addrDetails) {
		this.addrDetails = addrDetails;
	}

	public Integer getAddrDefault() {
		return addrDefault;
	}

	public void setAddrDefault(Integer addrDefault) {
		this.addrDefault = addrDefault;
	}

	public Integer getDelState() {
		return delState;
	}

	public void setDelState(Integer delState) {
		this.delState = delState;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getAddrStreet() {
		return addrStreet;
	}

	public void setAddrStreet(String addrStreet) {
		this.addrStreet = addrStreet;
	}
	@Override
	public String toString() {
		return "DeliveryAddress{" +
			"addrId=" + addrId +
			", consignee=" + consignee +
			", consigneePhone=" + consigneePhone +
			", addrProvince=" + addrProvince +
			", addrCity=" + addrCity +
			", addrArea=" + addrArea +
  			", addrStreet=" + addrStreet +
			", addrDetails=" + addrDetails +
			", addrDefault=" + addrDefault +
			", delState=" + delState +
			", createdAt=" + createdAt +
			", updatedAt=" + updatedAt +
			", userId=" + userId +
			"}";
	}
}
