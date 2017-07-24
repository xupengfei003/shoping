package so.sao.shop.supplier.pojo.input;

import java.math.BigDecimal;

/**
 * Created by niewenchao on 2017/7/24.
 * 提现申请入参封装类
 */
public class OrderMoneyRecordInput {

    /**
     * 查询起始时间
     */
    private Long startTime;

    /**
     * 查询结束时间
     */
    private Long endTime;

    /**
     * 提现金额起始值
     */
    private BigDecimal startMoney;

    /**
     * 提现金额起始值
     */
    private BigDecimal endMoney;

    /**
     * 状态 0 提现申请中 1 已通过 2 已完成
     */
    private String state;

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public BigDecimal getStartMoney() {
        return startMoney;
    }

    public void setStartMoney(BigDecimal startMoney) {
        this.startMoney = startMoney;
    }

    public BigDecimal getEndMoney() {
        return endMoney;
    }

    public void setEndMoney(BigDecimal endMoney) {
        this.endMoney = endMoney;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
