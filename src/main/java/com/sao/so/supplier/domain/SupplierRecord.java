package com.sao.so.supplier.domain;

import org.springframework.format.annotation.DateTimeFormat;

import java.text.SimpleDateFormat;

/**
 * Created by acer on 2017/7/21.
 */
public class SupplierRecord {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
    /**
     * 上传日期
     */
    private String createDate;
    /**
     * 供应商名称
     */
    private String providerName;
    /**
     * 供应商责任人
     */
    private String responsible;
    /**
     * 上传方式
     */
    private String uploadMode;
    @DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
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

    public String getUploadMode() {
        return uploadMode;
    }

    public void setUploadMode(String uploadMode) {
        this.uploadMode = uploadMode;
    }
}
