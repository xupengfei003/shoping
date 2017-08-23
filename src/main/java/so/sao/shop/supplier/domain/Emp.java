package so.sao.shop.supplier.domain;

import java.util.Date;

import javax.validation.constraints.Pattern;

import org.hibernate.validator.constraints.NotEmpty;

import io.swagger.annotations.ApiModelProperty;
/**
 * <p>Title: Emp</p>
 * <p>Description: 员工对象</p>
 * <p>Company:透云-中软-西安项目组 </p>
 * @author clown
 * @date 2017年8月11日
 */
public class Emp {
	/**
	 * 员工id
	 */
	@ApiModelProperty(hidden = true)
	private Long id;
	/**
	 * 用户id
	 */
	@ApiModelProperty(hidden = true)
	private Long userId;
	/**
	 * 供应商id
	 */
	@ApiModelProperty(hidden = true)
	private Long accountId;
	/**
	 * 员工姓名
	 */
	@NotEmpty(message = "请输入员工姓名")
	@ApiModelProperty(value = "员工姓名")
	private String empName;
	/**
	 * 员工性别
	 */
	@NotEmpty(message = "请选择员工性别")
	@ApiModelProperty(value = "员工性别")
	private String empSex;
	/**
	 * 员工电话
	 */
	@ApiModelProperty(value = "员工电话")
	@NotEmpty(message = "请输入正确的手机号码")
	@Pattern(regexp = "^1[34578]\\d{9}$", message = "请输入正确的手机号码")
	private String empTel;
	/**
	 * 创建时间
	 */
	@ApiModelProperty(hidden = true)
	private Date createAt;
	/**
	 * 更新时间
	 */
	@ApiModelProperty(hidden = true)
	private Date updateAt;
	/**
	 * 员工状态
	 */
	@ApiModelProperty(hidden = true)
	private String empStatus;
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long userId) {
		this.userId = userId;
	}
	public Long getAccountId() {
		return accountId;
	}
	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	public String getEmpSex() {
		return empSex;
	}
	public void setEmpSex(String empSex) {
		this.empSex = empSex;
	}
	public String getEmpTel() {
		return empTel;
	}
	public void setEmpTel(String empTel) {
		this.empTel = empTel;
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
	public String getEmpStatus() {
		return empStatus;
	}
	public void setEmpStatus(String empStatus) {
		this.empStatus = empStatus;
	}
	
	
}
