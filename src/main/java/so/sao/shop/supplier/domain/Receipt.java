package so.sao.shop.supplier.domain;
import java.util.Date;

/**
 * 发票实体类   发票与用户关系
 * @author zhenhai.zheng
 */
public class Receipt {

    /**
     * 发票编号
     */
    private Long receiptId;

    /**
     * 发票类型
     */
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
     * 门店ID
     */
    private Long userId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Long getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(Long receiptId) {
        this.receiptId = receiptId;
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

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
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