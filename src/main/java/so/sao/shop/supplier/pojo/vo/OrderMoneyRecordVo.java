package so.sao.shop.supplier.pojo.vo;

public class OrderMoneyRecordVo {
    /**
     * 结算明细id
     */
    private String recordId;

    /**
     * 供应商名称
     */
    private String providerName;

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
     * 结算状态 0 待结算 1 已结算
     */
    private String state;

    /**
     * 银行卡号
     */
    private String bankAccount;

    /**
     * 银行流水号
     */
    private String serialNumber;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
}