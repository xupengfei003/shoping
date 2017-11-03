package so.sao.shop.supplier.pojo.input;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * 发票与订单关系实体类入参
 * @author zhenhai.zheng
 */
public class ReceiptPurchaseInputVo {

    /**
     * 主键
     */
    private Long id;

    /**
     * 供应商Id
     */
    @NotNull(message = "供应商ID不能为空")
    private Long supplierId;

    /**
     * 订单编号
     */
    private String orderId;

    /**
     * 发票类型
     */
    @NotNull(message = "发票类型不能为空")
    private Integer receiptType;

    /**
     * 单位名称
     */
    private String company;

    /**
     * 纳税人识别号
     */
    private Long taxpayerNumber;

    /**
     * 发票内容ID
     */
    private Long receiptContentId;

    /**
     * 发票内容
     */
    private String receiptContent;

    /**
     * 注册地址
     */
    private String registerAdress;

    /**
     * 注册电话
     */
    private String registerPhone;

    /**
     * 开户银行
     */
    private String depositBank;

    /**
     * 银行账户
     */
    private String bankAmount;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId == null ? null : orderId.trim();
    }

    public Integer getReceiptType() {
        return receiptType;
    }

    public void setReceiptType(Integer receiptType) {
        this.receiptType = receiptType;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company == null ? null : company.trim();
    }

    public Long getTaxpayerNumber() {
        return taxpayerNumber;
    }

    public void setTaxpayerNumber(Long taxpayerNumber) {
        this.taxpayerNumber = taxpayerNumber;
    }

    public Long getReceiptContentId() {
        return receiptContentId;
    }

    public void setReceiptContentId(Long receiptContentId) {
        this.receiptContentId = receiptContentId;
    }

    public String getReceiptContent() {
        return receiptContent;
    }

    public void setReceiptContent(String receiptContent) {
        this.receiptContent = receiptContent == null ? null : receiptContent.trim();
    }

    public String getRegisterAdress() {
        return registerAdress;
    }

    public void setRegisterAdress(String registerAdress) {
        this.registerAdress = registerAdress == null ? null : registerAdress.trim();
    }

    public String getRegisterPhone() {
        return registerPhone;
    }

    public void setRegisterPhone(String registerPhone) {
        this.registerPhone = registerPhone == null ? null : registerPhone.trim();
    }

    public String getDepositBank() {
        return depositBank;
    }

    public void setDepositBank(String depositBank) {
        this.depositBank = depositBank == null ? null : depositBank.trim();
    }

    public String getBankAmount() {
        return bankAmount;
    }

    public void setBankAmount(String bankAmount) {
        this.bankAmount = bankAmount == null ? null : bankAmount.trim();
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}