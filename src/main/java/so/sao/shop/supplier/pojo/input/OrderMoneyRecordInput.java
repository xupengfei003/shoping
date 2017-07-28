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
    private Date startTime;

    /**
     * 查询结束时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date endTime;

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

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
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
