package so.sao.shop.supplier.domain;

import org.hibernate.validator.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class Account {
	/**
	 * 账号id
	 */
	private Long accountId;

	/**
	 * 用户id
	 */
	private Long userId;
	/**
	 * 供应商名称
	 */
	@NotEmpty(message = "供应商名称不能为空")
	private String providerName;
	/**
	 * 供应商法人代表
	 */
	@NotEmpty(message = "供应商法人代表不能为空")
	private String responsible;
	/**
	 * 供应商法人代表电话
	 */
	@NotEmpty(message = "供应商法人代表电话不能为空")
	@Pattern(regexp = "^1[34578]\\d{9}$", message = "请输入正确的手机号码")
	private String responsiblePhone;
	/**
	 * 供应商营业执照号
	 */
	@NotEmpty(message = "供应商营业执照号不能为空")
	private String license;
	/**
	 * 供应商营业执照开始时间
	 */
	@NotNull(message = "供应商营业执照开始时间不能为空")
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date licenseTimeCreate;
	/**
	 * 供应商营业执照截至时间
	 */
	@NotNull(message = "供应商营业执照截至时间不能为空")
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date licenseTimeEnd;
	/**
	 * 供应商行业类型
	 */
	@NotEmpty(message = "供应商行业类型不能为空")
	private String businessType;
	/**
	 * 供应商注册地址（省，市，区）
	 */
	private String registAddress;
	 /**
     * 供应商注册地址（省）
     */
    @NotEmpty(message="供应商注册地址不能为空")
    private String registAddressProvince;
    /**
     * 供应商注册地址（市）
     */
    @NotEmpty(message="供应商注册地址不能为空")
    private String registAddressCity;
    /**
     * 供应商注册地址（区）
     */
    @NotEmpty(message="供应商注册地址不能为空")
    private String registAddressDistrict;
	/**
	 * 供应商注册详细地址（街道，门牌号）
	 */
	@NotEmpty(message = "供应商注册详细地址不能为空")
	private String registerAddressDetail;
	/**
	 * 折扣信息
	 */
	private String discount;
	/**
	 * 开户行名称
	 */
	@NotEmpty(message = "开户行名称不能为空")
	private String bankName;
	/**
	 * 银行账号
	 */
	@NotEmpty(message = "银行账号不能为空")
	private String bankNum;
	/**
	 * 开户人姓名
	 */
	@NotEmpty(message = "开户人姓名不能为空")
	private String bankUserName;
	/**
	 * 合同开始日期
	 */
	@NotNull(message = "合同开始日期不能为空")
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date contractCreateDate;
	/**
	 * 合同截至日期
	 */
	@NotNull(message = "合同截至日期不能为空")
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date contractEndDate;
	/**
	 * 汇款结算方式 1:自然月 2:固定时间
	 */
	@NotEmpty(message = "汇款结算方式不能为空")
	private String remittanceType;
	/**
	 * remittance_type=1时表示每月哪天,remittance_type=2时表示天数
	 */
	private String remittanced;
	/**
	 * 用户状态
	 */
	private Integer accountStatus;
	/**
	 * 用户创建日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date createDate;
	/**
	 * 用户修改日期
	 */
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date updateDate;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 余额
	 */
	private BigDecimal balance;
	/**
	 * 历史总收入
	 */
	private BigDecimal income;
	/**
	 * 合同法人代表
	 */
	@NotEmpty(message = "合同法人代表不能为空")
	private String contractResponsible;
	/**
	 * 合同法人代表电话
	 */
	@NotEmpty(message = "合同法人代表电话不能为空")
	@Pattern(regexp = "^1[34578]\\d{9}$", message = "请输入正确的手机号码")
	private String contractResponsiblePhone;
	/**
	 * 合同营业执照号
	 */
	@NotEmpty(message = "合同营业执照号不能为空")
	private String contractLicense;
	/**
	 * 合同营业执照开始日期
	 */
	@NotNull(message = "合同营业执照开始日期不能为空")
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date contractLicenseCreate;
	/**
	 * 合同营业执照截至日期
	 */
	@NotNull(message = "合同营业执照截至日期不能为空")
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
	private Date contractLicenseEnd;
	/**
	 * 合同注册地址（省，市，区）
	 */
	private String contractRegisterAddress;
	/**
     * 合同注册地址（省）
     */
    @NotEmpty(message="合同注册地址不能为空")
    private String contractRegisterAddressProvince;
    /**
     * 合同注册地址（市）
     */
    @NotEmpty(message="合同注册地址不能为空")
    private String contractRegisterAddressCity;
    /**
     * 合同注册地址（区）
     */
    @NotEmpty(message="合同注册地址不能为空")
    private String contractRegisterAddressDistrict;
	/**
	 * 合同注册详细地址（街道，门牌号）
	 */
	@NotEmpty(message = "合同注册详细地址不能为空")
	private String contractRegisterAddressDetail;
	/**
	 * 上传方式 ，1单次，2批量
	 */
	private String uploadMode;

	/**
	 * 短信验证码(一期暂时)
	 */

	private String smsCode;

	/**
	 * 供应商地址和供应商合同地址
	 */
	private List areaList;
	
	/**
	 * 合同云端名称
	 */
	@NotEmpty(message = "合同云端名称")
	private String contractOriginalName;
	/**
	 * 合同云端地址
	 */
	@NotEmpty(message = "合同云端地址")
	private String contractOriginal;

	/**
	 * 上次结算时间
	 */
	private Date lastSettlementDate;

	public Long getAccountId() {
		return accountId;
	}

	public void setAccountId(Long accountId) {
		this.accountId = accountId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName == null ? null : providerName.trim();
	}

	public String getResponsible() {
		return responsible;
	}

	public void setResponsible(String responsible) {
		this.responsible = responsible == null ? null : responsible.trim();
	}

	public String getResponsiblePhone() {
		return responsiblePhone;
	}

	public void setResponsiblePhone(String responsiblePhone) {
		this.responsiblePhone = responsiblePhone == null ? null : responsiblePhone.trim();
	}

	public String getLicense() {
		return license;
	}

	public void setLicense(String license) {
		this.license = license == null ? null : license.trim();
	}

	public Date getLicenseTimeCreate() {
		return licenseTimeCreate;
	}

	public void setLicenseTimeCreate(Date licenseTimeCreate) {
		this.licenseTimeCreate = licenseTimeCreate;
	}

	public Date getLicenseTimeEnd() {
		return licenseTimeEnd;
	}

	public void setLicenseTimeEnd(Date licenseTimeEnd) {
		this.licenseTimeEnd = licenseTimeEnd;
	}

	public Date getContractLicenseCreate() {
		return contractLicenseCreate;
	}

	public void setContractLicenseCreate(Date contractLicenseCreate) {
		this.contractLicenseCreate = contractLicenseCreate;
	}

	public Date getContractLicenseEnd() {
		return contractLicenseEnd;
	}

	public void setContractLicenseEnd(Date contractLicenseEnd) {
		this.contractLicenseEnd = contractLicenseEnd;
	}

	public String getBusinessType() {
		return businessType;
	}

	public void setBusinessType(String businessType) {
		this.businessType = businessType == null ? null : businessType.trim();
	}

	public String getRegistAddress() {
		return this.registAddressProvince+"-"+this.registAddressCity+"-"+this.registAddressDistrict;
	}

	public void setRegistAddress(String registAddress) {
		this.registAddress = this.registAddressProvince+"-"+this.registAddressCity+"-"+this.registAddressDistrict;
	}

	public String getRegisterAddressDetail() {
		return registerAddressDetail;
	}

	public void setRegisterAddressDetail(String registerAddressDetail) {
		this.registerAddressDetail = registerAddressDetail == null ? null : registerAddressDetail.trim();
	}

	public String getDiscount() {
		return discount;
	}

	public void setDiscount(String discount) {
		this.discount = discount == null ? null : discount.trim();
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName == null ? null : bankName.trim();
	}

	public String getBankNum() {
		return bankNum;
	}

	public void setBankNum(String bankNum) {
		this.bankNum = bankNum == null ? null : bankNum.trim();
	}

	public String getBankUserName() {
		return bankUserName;
	}

	public void setBankUserName(String bankUserName) {
		this.bankUserName = bankUserName == null ? null : bankUserName.trim();
	}

	public Date getContractCreateDate() {
		return contractCreateDate;
	}

	public void setContractCreateDate(Date contractCreateDate) {
		this.contractCreateDate = contractCreateDate;
	}

	public Date getContractEndDate() {
		return contractEndDate;
	}

	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	public String getRemittanceType() {
		return remittanceType;
	}

	public void setRemittanceType(String remittanceType) {
		this.remittanceType = remittanceType == null ? null : remittanceType.trim();
	}

	public String getRemittanced() {
		return remittanced;
	}

	public void setRemittanced(String remittanced) {
		this.remittanced = remittanced == null ? null : remittanced.trim();
	}

	public Integer getAccountStatus() {
		return accountStatus;
	}

	public void setAccountStatus(Integer accountStatus) {
		this.accountStatus = accountStatus;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark == null ? null : remark.trim();
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public BigDecimal getIncome() {
		return income;
	}

	public void setIncome(BigDecimal income) {
		this.income = income;
	}

	public String getContractResponsible() {
		return contractResponsible;
	}

	public void setContractResponsible(String contractResponsible) {
		this.contractResponsible = contractResponsible == null ? null : contractResponsible.trim();
	}

	public String getContractResponsiblePhone() {
		return contractResponsiblePhone;
	}

	public void setContractResponsiblePhone(String contractResponsiblePhone) {
		this.contractResponsiblePhone = contractResponsiblePhone == null ? null : contractResponsiblePhone.trim();
	}

	public String getContractLicense() {
		return contractLicense;
	}

	public void setContractLicense(String contractLicense) {
		this.contractLicense = contractLicense == null ? null : contractLicense.trim();
	}

	public String getContractRegisterAddress() {
		return this.contractRegisterAddressProvince+"-"+this.contractRegisterAddressCity+"-"+this.contractRegisterAddressDistrict;
	}

	public void setContractRegisterAddress(String contractRegisterAddress) {
		this.contractRegisterAddress = this.contractRegisterAddressProvince+"-"+this.contractRegisterAddressCity+"-"+this.contractRegisterAddressDistrict;
	}

	public String getContractRegisterAddressDetail() {
		return contractRegisterAddressDetail;
	}

	public void setContractRegisterAddressDetail(String contractRegisterAddressDetail) {
		this.contractRegisterAddressDetail = contractRegisterAddressDetail == null ? null
				: contractRegisterAddressDetail.trim();
	}

	public String getUploadMode() {
		return uploadMode;
	}

	public void setUploadMode(String uploadMode) {
		this.uploadMode = uploadMode == null ? null : uploadMode.trim();
	}

	public String getSmsCode() {
		return smsCode;
	}

	public void setSmsCode(String smsCode) {
		this.smsCode = smsCode == null ? null : smsCode.trim();
	}

	public List getAreaList() {
		return areaList;
	}

	public void setAreaList(List areaList) {
		this.areaList = areaList;
	}
	
	 public String getRegistAddressProvince() {
        return registAddressProvince;
    }

    public void setRegistAddressProvince(String registAddressProvince) {
        this.registAddressProvince = registAddressProvince;
    }

    public String getRegistAddressCity() {
        return registAddressCity;
    }

    public void setRegistAddressCity(String registAddressCity) {
        this.registAddressCity = registAddressCity;
    }

    public String getRegistAddressDistrict() {
        return registAddressDistrict;
    }

    public void setRegistAddressDistrict(String registAddressDistrict) {
        this.registAddressDistrict = registAddressDistrict;
    }

    public String getContractRegisterAddressProvince() {
        return contractRegisterAddressProvince;
    }

    public void setContractRegisterAddressProvince(String contractRegisterAddressProvince) {
        this.contractRegisterAddressProvince = contractRegisterAddressProvince;
    }

    public String getContractRegisterAddressCity() {
        return contractRegisterAddressCity;
    }

    public void setContractRegisterAddressCity(String contractRegisterAddressCity) {
        this.contractRegisterAddressCity = contractRegisterAddressCity;
    }

    public String getContractRegisterAddressDistrict() {
        return contractRegisterAddressDistrict;
    }

    public void setContractRegisterAddressDistrict(String contractRegisterAddressDistrict) {
        this.contractRegisterAddressDistrict = contractRegisterAddressDistrict;
    }

	public String getContractOriginal() {
		return contractOriginal;
	}

	public void setContractOriginal(String contractOriginal) {
		this.contractOriginal = contractOriginal;
	}

	public String getContractOriginalName() {
		return contractOriginalName;
	}

	public void setContractOriginalName(String contractOriginalName) {
		this.contractOriginalName = contractOriginalName;
	}

	public Date getLastSettlementDate() {
		return lastSettlementDate;
	}

	public void setLastSettlementDate(Date lastSettlementDate) {
		this.lastSettlementDate = lastSettlementDate;
	}
    
}