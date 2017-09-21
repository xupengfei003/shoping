package so.sao.shop.supplier.pojo.input;

import java.util.Date;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class BannerInput {
	/**
	 * 上架时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd",iso=ISO.DATE)
	private Date onShelvesTime;
	/**
	 * 下架时间
	 */
	@DateTimeFormat(pattern = "yyyy-MM-dd",iso=ISO.DATE)
	private Date offShelfTime;
	/**
	 * 轮播位
	 */
	private String location;
	/**
	 * 状态（0全部、1待发布、2已发布、3已下架）
	 */
	private String status;
	/**
     * 页数
     */
    private Integer pageNum;
    /**
     * 每页长度
     */
    private Integer pageSize;
	
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
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
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
}
