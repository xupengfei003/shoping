package so.sao.shop.supplier.domain.external;

import java.util.Date;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
/**
 * <p>Title: Banner</p>
 * <p>Description: 轮播图</p>
 * <p>Company:透云-中软-西安项目组 </p>
 * @author tengfei.zhang
 * @date 2017年9月14日
 */
public class Banner {
	/**
	 * 轮播图id
	 */
	private Long id;
	/**
	 * 图片名称
	 */
	@NotBlank(message = "图片名称不能为空")
	@Length(max = 255,message = "图片名称长度不能大于255")
	private String fileName;
	/**
	 * 缩略图链接
	 */
	@NotBlank(message = "缩略图链接不能为空")
	@Length(max = 255,message = "缩略图链接长度不能大于255")
	private String minImgUrl;
	/**
	 * 图片链接
	 */
	@NotBlank(message = "图片链接不能为空")
	@Length(max = 255,message = "图片链接长度不能大于255")
	private String url;
	/**
	 * 轮播位
	 */
	@NotBlank(message = "轮播位置不能为空")
	@Length(max = 2,message = "轮播位置只能传1-10")
	private String location;
	/**
	 * 上架时间
	 */
	@NotNull(message = "上架时间不能为空")
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date onShelvesTime;
	/**
	 * 下架时间
	 */
	@NotNull(message = "下架时间不能为空")
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date offShelfTime;
	/**
	 * 状态（0全部、1待发布、2已发布、3已下架）
	 */
	private String status;
	/**
	 * url类型：0：链接：1：商品id：2：供应商id
	 */
	@NotBlank(message = "跳转链接类型不能为空")
	private String urlType;	
	/**
	 * 外部链接
	 */
	@NotBlank(message = "跳转链接不能为空")
	private String urlValue;
	/**
	 * 操作人
	 */
	private String operator;
	/**
	 * 创建时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date createAt;
	/**
	 * 更新时间
	 */
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
	private Date updateAt;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getMinImgUrl() {
		return minImgUrl;
	}
	public void setMinImgUrl(String minImgUrl) {
		this.minImgUrl = minImgUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public Date getOnShelvesTime() {
		return onShelvesTime;
	}
	public void setOnShelvesTime(Date onShelvesTime) {
		this.onShelvesTime = onShelvesTime;
	}
	public Date getOffShelfTime() {
		return offShelfTime;
	}
	public void setOffShelfTime(Date offShelfTime) {
		this.offShelfTime = offShelfTime;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUrlType() {
		return urlType;
	}
	public void setUrlType(String urlType) {
		this.urlType = urlType;
	}
	public String getUrlValue() {
		return urlValue;
	}
	public void setUrlValue(String urlValue) {
		this.urlValue = urlValue;
	}
	public String getOperator() {
		return operator;
	}
	public void setOperator(String operator) {
		this.operator = operator;
	}
	public Date getCreateAt() {
		return createAt;
	}
	public void setCreateAt(Date createAt) {
		this.createAt = createAt;
	}
	public Date getUpdateAt() {
		return updateAt;
	}
	public void setUpdateAt(Date updateAt) {
		this.updateAt = updateAt;
	}
}
