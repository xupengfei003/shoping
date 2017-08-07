package so.sao.shop.supplier.pojo.input;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by niewenchao on 2017/7/24.
 * 提现申请入参封装类
 */
public class OrderMoneyRecordInput {

    /**
     * 查询起始时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private String startTime;

    /**
     * 查询结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private String endTime;

    /**
     * 起始时间，用于后台比较
     */
    private Date startDate;

    /**
     * 结束时间，用于后台比较
     */
    private Date endDate;

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

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
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
