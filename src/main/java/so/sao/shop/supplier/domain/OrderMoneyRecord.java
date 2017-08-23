package so.sao.shop.supplier.domain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OrderMoneyRecord {
    /**
     * 申请提现记录id
     */
    private String recordId;

    /**
     * 申请人
     */
    private Long userId;

    /**
     * 开户行
     */
    private String bankName;

    /**
     * 开户支行
     */
    private String bankNameBranch;

    /**
     * 银行卡号
     */
    private String bankAccount;

    /**
     * 待结算金额
     */
    private BigDecimal totalMoney;

    /**
     * 结算状态 0 待结算 1 已结算
     */
    private String state;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updatedAt;

    /**
     * 银行流水号
     */
    private String serialNumber;

    /**
     * 供应商名称
     */
    private String providerName;

    /**
     * 已结算金额
     */
    private BigDecimal settledAmount;

    /**
     * 开户人姓名
     */
    private String bankUserName;

    /**
     * 结账时间
     */
    private Date checkoutAt;

    /**
     * 结算时间
     */
    private Date settledAt;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }

    public String getBankNameBranch() {
        return bankNameBranch;
    }

    public void setBankNameBranch(String bankNameBranch) {
        this.bankNameBranch = bankNameBranch == null ? null : bankNameBranch.trim();
    }

    public String getBankAccount() {
        return bankAccount;
    }

    public void setBankAccount(String bankAccount) {
        this.bankAccount = bankAccount == null ? null : bankAccount.trim();
    }

    public BigDecimal getTotalMoney() {
        return totalMoney;
    }

    public void setTotalMoney(BigDecimal totalMoney) {
        this.totalMoney = totalMoney;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state == null ? null : state.trim();
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber == null ? null : serialNumber.trim();
    }

    public String getProviderName() {
        return providerName;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public BigDecimal getSettledAmount() {
        return settledAmount;
    }

    public void setSettledAmount(BigDecimal settledAmount) {
        this.settledAmount = settledAmount;
    }

    public String getBankUserName() {
        return bankUserName;
    }

    public void setBankUserName(String bankUserName) {
        this.bankUserName = bankUserName;
    }

    public Date getCheckoutAt() {
        return checkoutAt;
    }

    public void setCheckoutAt(Date checkoutAt) {
        this.checkoutAt = checkoutAt;
    }

    public Date getSettledAt() {
        return settledAt;
    }

    public void setSettledAt(Date settledAt) {
        this.settledAt = settledAt;
    }

    public static Object[] converData(OrderMoneyRecord orderMoneyRecord){
        Object[] data = new Object[7];

        if (null != orderMoneyRecord) {
            data[0] = orderMoneyRecord.getProviderName();//供应商名称
            if(orderMoneyRecord.getCheckoutAt() != null){//结账时间
               data[1] = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss").format(orderMoneyRecord.getCheckoutAt());
            }
            data[2] = orderMoneyRecord.getTotalMoney();//待结算金额（¥）
            data[3] = orderMoneyRecord.getSettledAmount();//已结算金额（¥）
            data[4] = orderMoneyRecord.getSettledAt();//结算时间
            switch (orderMoneyRecord.getState()){
                case "0":
                    data[5] = "未结算";//结算状态
                    break;
                case "1":
                    data[5] = "已结算";//结算状态
                    break;

            }
            data[6] = orderMoneyRecord.getBankAccount();//供应商账户
        }


        return data;
    }
}