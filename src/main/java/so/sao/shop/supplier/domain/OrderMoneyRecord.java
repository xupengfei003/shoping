package so.sao.shop.supplier.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

public class OrderMoneyRecord {
    /**
     * 申请提现记录id
     */
    private Long recordId;

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
     * 提现金额
     */
    private BigDecimal totalMoney;

    /**
     * 状态 0 提现申请中 1 已通过 2 已完成
     */
    private String state;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
    private Date createdAt;

    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy/MM/dd HH:mm:ss", timezone="GMT+8")
    private Date updatedAt;

    /**
     * 银行流水号
     */
    private String serialNumber;

    /**
     * 申请提现金额所对应的订单id,多个以逗号分隔
     */
    private String orderId;

    public Long getRecordId() {
        return recordId;
    }

    public void setRecordId(Long recordId) {
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

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }
}