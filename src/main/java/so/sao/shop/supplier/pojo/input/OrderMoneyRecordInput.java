package so.sao.shop.supplier.pojo.input;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.*;

import javax.validation.constraints.Pattern;
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
     * 结算状态 0 待结算 1 已结算
     */
    @Pattern(regexp="^[0-1]*$", message="结算状态错误")
    @NotNull(message = "状态不能为空")
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

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
