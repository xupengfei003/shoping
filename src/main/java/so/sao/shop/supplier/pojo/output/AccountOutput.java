package so.sao.shop.supplier.pojo.output;


import java.util.Date;

/**
 * Created by acer on 2017/9/8.
 */
public class AccountOutput {

    /**
     * 供应商id
     */
    private Long accountId;

    /**
     * 供应商名称
     */
    private String providerName;
    /**
     * 供应商法人代表
     */
    private String responsible;
    /**
     * 供应商法人代表电话
     */
    private String responsiblePhone;

    /**
     * 合同截至日期
     */
    private Date contractEndDate;

    /**
     * 供应商注册地址（省）编码
     */
    private String registAddressProvince;
    /**
     * 供应商注册地址（省）名称
     */
    private String registAddressProvinceName;
    /**
     * 供应商注册地址（市）编码
     */
    private String registAddressCity;
    /**
     * 供应商注册地址（市）名称
     */
    private String registAddressCityName;
    /**
     * 供应商注册地址（区）编码
     */
    private String registAddressDistrict;
    /**
     * 供应商注册地址（区）名称
     */
    private String registAddressDistrictName;

    /**
     * 合同注册地址（省）编码
     */
    private String contractRegisterAddressProvince;
    /**
     * 合同注册地址（省）名称
     */
    private String contractRegisterAddressProvinceName;
    /**
     * 合同注册地址（市）编码
     */
    private String contractRegisterAddressCity;
    /**
     * 合同注册地址（市）名称
     */
    private String contractRegisterAddressCityName;
    /**
     * 合同注册地址（区）编码
     */
    private String contractRegisterAddressDistrict;
    /**
     * 合同注册地址（区）名称
     */
    private String contractRegisterAddressDistrictName;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
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

    public String getResponsiblePhone() {
        return responsiblePhone;
    }

    public void setResponsiblePhone(String responsiblePhone) {
        this.responsiblePhone = responsiblePhone;
    }

    public String getRegistAddressProvince() {
        return registAddressProvince;
    }

    public void setRegistAddressProvince(String registAddressProvince) {
        this.registAddressProvince = registAddressProvince;
    }

    public String getRegistAddressProvinceName() {
        return registAddressProvinceName;
    }

    public void setRegistAddressProvinceName(String registAddressProvinceName) {
        this.registAddressProvinceName = registAddressProvinceName;
    }

    public String getRegistAddressCity() {
        return registAddressCity;
    }

    public void setRegistAddressCity(String registAddressCity) {
        this.registAddressCity = registAddressCity;
    }

    public String getRegistAddressCityName() {
        return registAddressCityName;
    }

    public void setRegistAddressCityName(String registAddressCityName) {
        this.registAddressCityName = registAddressCityName;
    }

    public String getRegistAddressDistrict() {
        return registAddressDistrict;
    }

    public void setRegistAddressDistrict(String registAddressDistrict) {
        this.registAddressDistrict = registAddressDistrict;
    }

    public String getRegistAddressDistrictName() {
        return registAddressDistrictName;
    }

    public void setRegistAddressDistrictName(String registAddressDistrictName) {
        this.registAddressDistrictName = registAddressDistrictName;
    }

    public Date getContractEndDate() {
        return contractEndDate;
    }

    public void setContractEndDate(Date contractEndDate) {
        this.contractEndDate = contractEndDate;
    }

    public String getContractRegisterAddressProvince() {
        return contractRegisterAddressProvince;
    }

    public void setContractRegisterAddressProvince(String contractRegisterAddressProvince) {
        this.contractRegisterAddressProvince = contractRegisterAddressProvince;
    }

    public String getContractRegisterAddressProvinceName() {
        return contractRegisterAddressProvinceName;
    }

    public void setContractRegisterAddressProvinceName(String contractRegisterAddressProvinceName) {
        this.contractRegisterAddressProvinceName = contractRegisterAddressProvinceName;
    }

    public String getContractRegisterAddressCity() {
        return contractRegisterAddressCity;
    }

    public void setContractRegisterAddressCity(String contractRegisterAddressCity) {
        this.contractRegisterAddressCity = contractRegisterAddressCity;
    }

    public String getContractRegisterAddressCityName() {
        return contractRegisterAddressCityName;
    }

    public void setContractRegisterAddressCityName(String contractRegisterAddressCityName) {
        this.contractRegisterAddressCityName = contractRegisterAddressCityName;
    }

    public String getContractRegisterAddressDistrict() {
        return contractRegisterAddressDistrict;
    }

    public void setContractRegisterAddressDistrict(String contractRegisterAddressDistrict) {
        this.contractRegisterAddressDistrict = contractRegisterAddressDistrict;
    }

    public String getContractRegisterAddressDistrictName() {
        return contractRegisterAddressDistrictName;
    }

    public void setContractRegisterAddressDistrictName(String contractRegisterAddressDistrictName) {
        this.contractRegisterAddressDistrictName = contractRegisterAddressDistrictName;
    }
}
