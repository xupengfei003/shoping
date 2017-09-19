package so.sao.shop.supplier.pojo.vo;

import java.math.BigDecimal;

/**
 * Created by fangzhou on 2017/8/18.
 */
public class AccountOrderMoneyRecordVO {
    /**
     * 结算明细id
     */
    private String recordId;

    /**
     * 结账时间
     */
    private String checkoutAt;

    /**
     * 待结算金额
     */
    private String totalMoney;

    /**
     * 已结算金额
     */
    private String settledAmount;

    /**
     * 结算时间
     */
    private String settledAt;

    /**
     * 银行卡号
     */
    private String bankAccount;

    /**
     * 开户人姓名
     */
    private String bankUserName;

    /**
     * 开户行
     */
    private String bankName;

    /**
     * 开户支行
     */
    private String bankNameBranch;

    /**
     * 银行流水号
     */
    private String serialNumber;

    /**
     * 运费总金额
     */
    private String postageTotalAmount;

    /**
     * 订单总金额
     */
    private String orderTotalAmount;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getCheckoutAt() {
        return checkoutAt;
    }

    public void setCheckoutAt(String checkoutAt) {
        this.checkoutAt = checkoutAt;
    }

    public String getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(String totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(String settledAmount) {
        this.settledAmount = settledAmount;
    }

    public String getSettledAt() {
        return settledAt;
    }

    public void setSettledAt(String settledAt) {
        this.settledAt = settledAt;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getBankUserName() {
        return bankUserName;
    }

    public void setBankUserName(String bankUserName) {
        this.bankUserName = bankUserName;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getBankNameBranch() {
        return bankNameBranch;
    }

    public void setBankNameBranch(String bankNameBranch) {
        this.bankNameBranch = bankNameBranch;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getPostageTotalAmount() {
        return postageTotalAmount;
    }

    public void setPostageTotalAmount(String postageTotalAmount) {
        this.postageTotalAmount = postageTotalAmount;
    }

    public String getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(String orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }
}
