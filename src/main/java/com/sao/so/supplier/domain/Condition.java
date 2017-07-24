package com.sao.so.supplier.domain;

import java.util.Date;

/**
 * 查询条件
 * Created by acer on 2017/7/20.
 */
public class Condition {
    
     /**
     * 开始时间(做类型转换)
     */
    private Date beginTime;
    
	 /**
     * 截止时间(做类型转换)
     */
    private Date endTime;
   
    /**
     * 开始时间
     */
    private Long beginDate;
    /**
     * 截止时间
     */
    private Long endDate;

    /**
     * 合同截止日期(做类型转换)
     */
    private Date contractEndTime;
    /**
     * 合同创建日期(做类型转换)
     */
    private Date contractCreateTime;


    /**
     * 供应商名称
     */
    private String providerName;
    /**
     * 合同截止日期
     */
    private Long contractEndDate;
    /**
     * 合同创建日期
      */
    private Long contractCreateDate;
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

    public Long getBeginDate() {
        return beginDate;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public Long getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Long contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public Long getContractCreateDate() {
        return contractCreateDate;
    }

    public void setContractCreateDate(Long contractCreateDate) {
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

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public void setBeginDate(Long beginDate) {
        this.beginDate = beginDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public Long getEndDate() {
        return endDate;
    }

     public Date getContractEndTime() {
        return contractEndTime;
    }

    public void setContractEndTime(Date contractEndTime) {
        this.contractEndTime = contractEndTime;
    }

    public Date getContractCreateTime() {
        return contractCreateTime;
    }

    public void setContractCreateTime(Date contractCreateTime) {
        this.contractCreateTime = contractCreateTime;
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
