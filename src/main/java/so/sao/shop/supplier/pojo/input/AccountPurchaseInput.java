package so.sao.shop.supplier.pojo.input;

import com.fasterxml.jackson.annotation.JsonFormat;

import javax.validation.constraints.Min;
import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 账户余额明细查询input类
 * </p>
 *
 * @author 透云-中软-西安项目组-zhenhai.zheng
 * @since 2017年7月22日
 **/
public class AccountPurchaseInput {

    /**
     * 开始时间,用于比较
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date beginDate;

    /**
     * 截至时间,用于比较
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date endDate;
    /**
     * 开始时间  用于判断时间格式
     */
    private String beginTime;
    /**
     * 结束时间  用于判断时间格式
     */
    private String endTime;
    /**
     * 起始金额
     */
    @Min(0)
    private BigDecimal beginMoney;

    /**
     * 截至金额
     */
    @Min(0)
    private BigDecimal endMoney;

    public BigDecimal getBeginMoney() {
        return beginMoney;
    }

    public void setBeginMoney(BigDecimal beginMoney) {
        this.beginMoney = beginMoney;
    }

    public BigDecimal getEndMoney() {
        return endMoney;
    }

    public Date getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Date beginDate) {
        this.beginDate = beginDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setEndMoney(BigDecimal endMoney) {
        this.endMoney = endMoney;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "AccountPurchaseInput{" +
                "beginDate=" + beginDate +
                ", endDate=" + endDate +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", beginMoney=" + beginMoney +
                ", endMoney=" + endMoney +
                '}';
    }
}
