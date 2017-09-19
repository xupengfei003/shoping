package so.sao.shop.supplier.domain;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import so.sao.shop.supplier.util.NumberUtil;
import so.sao.shop.supplier.util.StringUtil;

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

    /**
     * 运费总金额
     */
    private BigDecimal postageTotalAmount;

    /**
     * 订单结算总金额
     */
    private BigDecimal orderTotalAmount;

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

    public BigDecimal getPostageTotalAmount() {
        return postageTotalAmount;
    }

    public void setPostageTotalAmount(BigDecimal postageTotalAmount) {
        this.postageTotalAmount = postageTotalAmount;
    }

    public BigDecimal getOrderTotalAmount() {
        return orderTotalAmount;
    }

    public void setOrderTotalAmount(BigDecimal orderTotalAmount) {
        this.orderTotalAmount = orderTotalAmount;
    }

    public static Object[] converData(OrderMoneyRecord orderMoneyRecord){
        Object[] data = new Object[10];

        if (null != orderMoneyRecord) {
            data[0] = orderMoneyRecord.getProviderName();//供应商名称
            if(orderMoneyRecord.getCheckoutAt() != null){//结账时间
               data[1] = StringUtil.fomateData(orderMoneyRecord.getCheckoutAt(), "yyyy-MM-dd");
            }
            data[2] = NumberUtil.number2Thousand(orderMoneyRecord.getTotalMoney());//待结算金额（¥）
            data[3] = NumberUtil.number2Thousand(orderMoneyRecord.getSettledAmount());//已结算金额（¥）
            if(orderMoneyRecord.getSettledAt() != null) {//结算时间
                data[4] = StringUtil.fomateData(orderMoneyRecord.getSettledAt(), "yyyy-MM-dd HH:mm");
            }
            switch (orderMoneyRecord.getState()){
                case "0":
                    data[5] = "未结算";//结算状态
                    break;
                case "1":
                    data[5] = "已结算";//结算状态
                    break;

            }
            data[6] = orderMoneyRecord.getBankAccount();//供应商账户
            data[7] = orderMoneyRecord.getSerialNumber();//银行流水号
            data[8] = NumberUtil.number2Thousand(orderMoneyRecord.getPostageTotalAmount());//运费总金额
            data[9] = NumberUtil.number2Thousand(orderMoneyRecord.getOrderTotalAmount());//订单总金额
        }

        return data;
    }
}