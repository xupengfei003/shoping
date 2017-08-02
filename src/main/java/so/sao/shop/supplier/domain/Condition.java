package so.sao.shop.supplier.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 查询条件
 * Created by acer on 2017/7/20.
 */
public class Condition {
   
    /**
     * 开始时间
     */
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private String beginDate;
    /**
     * 截止时间
     */
	@JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private String endDate;

    /**
     * 供应商名称
     */
    private String providerName;
    /**
     * 合同截止日期
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private String contractEndDate;
    /**
     * 合同创建日期
      */
    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private String contractCreateDate;
    /**
     * 法人代表（合同）
     */
    private String contractResponsible;
    /**
     * 法人代表电话（合同）
     */
    private String contractResponsiblePhone;
    /**
     * 合同注册地址（省）
     */
    private String contractRegisterAddressProvince;
    /**
     * 合同注册地址（市）
     */
    private String contractRegisterAddressCity;
    /**
     * 合同注册地址（区）
     */
    private String contractRegisterAddressDistrict;

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


    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName!=null?providerName.replaceAll(" ","").trim():null;
    }


    public String getContractResponsible() {
        return contractResponsible;
    }

    public void setContractResponsible(String contractResponsible) {
        this.contractResponsible = contractResponsible!=null?contractResponsible.replaceAll(" ","").trim():null;
    }

    public String getContractResponsiblePhone() {
        return contractResponsiblePhone;
    }

    public void setContractResponsiblePhone(String contractResponsiblePhone) {
        this.contractResponsiblePhone = contractResponsiblePhone;
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

    public String getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(String beginDate) {
        this.beginDate = beginDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(String contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getContractCreateDate() {
        return contractCreateDate;
    }

    public void setContractCreateDate(String contractCreateDate) {
        this.contractCreateDate = contractCreateDate;
    }
}
