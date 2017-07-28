package so.sao.shop.supplier.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 查询条件
 * Created by acer on 2017/7/20.
 */
public class Condition {
   
    /**
     * 开始时间
     */
	@JsonFormat(pattern="yyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date beginDate;
    /**
     * 截止时间
     */
	@JsonFormat(pattern="yyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date endDate;

    /**
     * 供应商名称
     */
    private String providerName;
    /**
     * 合同截止日期
     */
    @JsonFormat(pattern="yyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date contractEndDate;
    /**
     * 合同创建日期
      */
    @JsonFormat(pattern="yyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date contractCreateDate;
    /**
     * 法人代表（合同）
     */
    private String contractResponsible;
    /**
     * 法人代表电话（合同）
     */
    private String contractResponsiblePhone;
    /**
     * 注册地区
     */
    private String contractRegisterAddress;

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

    public String getUploadMode() {
        return uploadMode;
    }

    public void setUploadMode(String uploadMode) {
        this.uploadMode = uploadMode;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
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

    public String getContractResponsible() {
        return contractResponsible;
    }

    public void setContractResponsible(String contractResponsible) {
        this.contractResponsible = contractResponsible;
    }

    public String getContractResponsiblePhone() {
        return contractResponsiblePhone;
    }

    public void setContractResponsiblePhone(String contractResponsiblePhone) {
        this.contractResponsiblePhone = contractResponsiblePhone;
    }

    public String getContractRegisterAddress() {
        return contractRegisterAddress;
    }

    public void setContractRegisterAddress(String contractRegisterAddress) {
        this.contractRegisterAddress = contractRegisterAddress;
    }


    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public Date getEndDate() {
        return endDate;
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
