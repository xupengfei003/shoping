package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Pattern;
import java.util.Date;

/**
 * Created by niewenchao on 2017/7/24.
 * 提现申请入参封装类
 */
public class OrderMoneyRecordInput {

    /**
     * 查询起始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startTime;

    /**
     * 查询结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endTime;

    /**
     * 结算状态 0 待结算 1 已结算
     */
    @Pattern(regexp = "^[0-1]$", message = "结算状态错误")
    @NotEmpty(message = "状态不能为空")
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
