package so.sao.shop.supplier.pojo.input;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

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
    private Long beginDate;

    /**
     * 截至时间,用于比较
     */
    private Long endDate;

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

    public Long getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Long beginDate) {
        this.beginDate = beginDate;

    }

    public Long getEndDate() {
        return endDate;
    }

    public void setEndDate(Long endDate) {
        this.endDate = endDate;
    }

    public void setEndMoney(BigDecimal endMoney) {
        this.endMoney = endMoney;
    }

    @Override
    public String toString() {
        return "AccountPurchaseInput{" +
                ", beginMoney=" + beginMoney +
                ", endMoney=" + endMoney +
                ", endMoney=" + endMoney +
                '}';
    }
}
