package so.sao.shop.supplier.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * Created by acer on 2017/7/21.
 */
public class SupplierRecord {
	
    /**
     * 上传日期
     */
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
    private Date createDate;
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
    
    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateDate(Date createDate) {
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
