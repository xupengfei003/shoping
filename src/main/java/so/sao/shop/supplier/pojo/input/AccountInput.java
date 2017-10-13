package so.sao.shop.supplier.pojo.input;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.util.Date;

/**
 * 查询条件
 * Created by acer on 2017/7/20.
 */
public class AccountInput {
    /**
     * 创建时间开始时间
     */
	@DateTimeFormat(pattern = "yyyy-MM-dd",iso=ISO.DATE)
    private Date createBeginDate;
    /**
     * 创建时间截止时间
     */
	@DateTimeFormat(pattern = "yyyy-MM-dd",iso=ISO.DATE)
    private Date createEndDate;
    /**
     * 供应商名称/法人代表模糊查询
     */
    private String vague;
    /**
     * 供应商名称
     */
    private String providerName;
    /**
     * 合同截止日期
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso=ISO.DATE)
    private Date contractEndDate;
    /**
     * 合同创建日期
      */
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso=ISO.DATE)
    private Date contractCreateDate;
    /**
     * 法人代表
     */
    private String responsible;
    /**
     * 法人代表电话
     */
    private String responsiblePhone;
    /**
     * 注册地址（省）
     */
    private String registAddressProvince;
    /**
     * 注册地址（市）
     */
    private String registAddressCity;
    /**
     * 注册地址（区）
     */
    private String registAddressDistrict;
    /**
     * 页数
     */
    private Integer pageNum;
    /**
     * 每页长度
     */
    private Integer pageSize;
    /**
     * 供应商上传方式
     */
    private String uploadMode;

    /**
     * 供应商更新时间开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso=ISO.DATE)
    private Date updateBeginDate;
    /**
     * 供应商更新时间截止时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso=ISO.DATE)
    private Date updateEndDate;
    /**
     * 供应商资质审核状态（0.未发起审核 1、待审核 2、审核通过 3、审核未通过）
     */
    private String qualificationStatus;
    /**
     * 供应商状态
     */
    private String accountStatus;

    public String getUploadMode() {
        return uploadMode;
    }

    public void setUploadMode(String uploadMode) {
        this.uploadMode = uploadMode;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName!=null?providerName.replaceAll(" ","").trim():null;
    }

    public String getResponsible() {
        return responsible;
    }

    public void setResponsible(String responsible) {
        this.responsible = responsible!=null?responsible.replaceAll(" ","").trim():null;
    }

    public String getResponsiblePhone() {
        return responsiblePhone;
    }

    public void setResponsiblePhone(String responsiblePhone) {
        this.responsiblePhone = responsiblePhone!=null?responsiblePhone.replaceAll(" ","").trim():null;
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


    public Date getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public Date getContractCreateDate() {
        return contractCreateDate;
    }

    public void setContractCreateDate(Date contractCreateDate) {
        this.contractCreateDate = contractCreateDate;
    }

    public String getVague() {
        return vague;
    }

    public void setVague(String vague) {
        this.vague = vague;
    }

    public Date getCreateBeginDate() {
        return createBeginDate;
    }

    public void setCreateBeginDate(Date createBeginDate) {
        this.createBeginDate = createBeginDate;
    }

    public Date getCreateEndDate() {
        return createEndDate;
    }

    public void setCreateEndDate(Date createEndDate) {
        this.createEndDate = createEndDate;
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

    public Date getUpdateBeginDate() {
        return updateBeginDate;
    }

    public void setUpdateBeginDate(Date updateBeginDate) {
        this.updateBeginDate = updateBeginDate;
    }

    public Date getUpdateEndDate() {
        return updateEndDate;
    }

    public void setUpdateEndDate(Date updateEndDate) {
        this.updateEndDate = updateEndDate;
    }

    public String getQualificationStatus() {
        return qualificationStatus;
    }

    public void setQualificationStatus(String qualificationStatus) {
        this.qualificationStatus = qualificationStatus;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
