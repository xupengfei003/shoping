package so.sao.shop.supplier.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class AccountUser {
    /**
     * 用户id
     */
    private Long userId;

    /**
     * 登录名称
     */
    private String loginName;

    /**
     * 登录密码
     */
    private String loginPassword;

    /**
     * 用户邮箱
     */
    private String userEmail;

    /**
     * 激活时间
     */
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
    private Date activeTime;

    /**
     * 登录时间
     */
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
    private Date loginTime;

    /**
     * 上次登录时间
     */
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
    private Date lastLoginTime;

    /**
     * 用户状态(0 删除，1启用，2停用)
     */
    private String userStatus;

    /**
     * 供应商名称
     */
    private String providerName;

    /**
     * 责任人
     */
    private String responsible;

    /**
     * 供应商联系电话
     */
    private String phoneNum;

    /**
     * 注册地址
     */
    private String registAddress;

    /**
     * 服务类型
     */
    private String serviceType;

    /**
     * 行业类型
     */
    private String businessType;

    /**
     * 营业执照
     */
    private String license;

    /**
     * 购买方名称
     */
    private String buyerName;

    /**
     * 纳税人识别号
     */
    private String taxpayerNum;

    /**
     * 银行账号
     */
    private String bankAccount;

    /**
     * 开户行名称
     */
    private String bankName;

    /**
     * 支行名称
     */
    private String bankNameBranch;

    /**
     * 公司地址
     */
    private String companyAddress;

    /**
     * 公司联系电话
     */
    private String companyPhone;

    /**
     * 供应商介绍
     */
    private String providerDesc;

    /**
     * 余额
     */
    private BigDecimal balance;

    /**
     * 历史总收入
     */
    private BigDecimal income;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
    private Date createdAt;

    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
    private Date updatedAt;

    /**
     * 备注
     */
    private String remark;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName == null ? null : loginName.trim();
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword == null ? null : loginPassword.trim();
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail == null ? null : userEmail.trim();
    }

    public Date getActiveTime() {
        return activeTime;
    }

    public void setActiveTime(Date activeTime) {
        this.activeTime = activeTime;
    }

    public Date getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Date loginTime) {
        this.loginTime = loginTime;
    }

    public Date getLastLoginTime() {
        return lastLoginTime;
    }

    public void setLastLoginTime(Date lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus == null ? null : userStatus.trim();
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

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum == null ? null : phoneNum.trim();
    }

    public String getRegistAddress() {
        return registAddress;
    }

    public void setRegistAddress(String registAddress) {
        this.registAddress = registAddress == null ? null : registAddress.trim();
    }

    public String getServiceType() {
        return serviceType;
    }

    public void setServiceType(String serviceType) {
        this.serviceType = serviceType == null ? null : serviceType.trim();
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType == null ? null : businessType.trim();
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license == null ? null : license.trim();
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName == null ? null : buyerName.trim();
    }

    public String getTaxpayerNum() {
        return taxpayerNum;
    }

    public void setTaxpayerNum(String taxpayerNum) {
        this.taxpayerNum = taxpayerNum == null ? null : taxpayerNum.trim();
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankNameBranch() {
        return bankNameBranch;
    }

    public void setBankNameBranch(String bankNameBranch) {
        this.bankNameBranch = bankNameBranch == null ? null : bankNameBranch.trim();
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress == null ? null : companyAddress.trim();
    }

    public String getCompanyPhone() {
        return companyPhone;
    }

    public void setCompanyPhone(String companyPhone) {
        this.companyPhone = companyPhone == null ? null : companyPhone.trim();
    }

    public String getProviderDesc() {
        return providerDesc;
    }

    public void setProviderDesc(String providerDesc) {
        this.providerDesc = providerDesc == null ? null : providerDesc.trim();
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}