package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import java.math.BigDecimal;

/**
 * <p>
 * 账户余额明细查询input类 (普通查询条件类)
 * </p>
 *
 * @author 透云-中软-西安项目组-zhenhai.zheng
 * @since 2017年7月22日
 **/
public class AccountPurchaseLowInput {

    /**
     * 开始时间（创建时间）  用于判断时间格式
     */
    private String createBeginTime;

    /**
     * 结束时间（创建时间）  用于判断时间格式
     */
    private String createEndTime;

    /**
     * 查询条件(收货人名称/订单编号/流水号)
     */
    private String condition;

    public String getCreateBeginTime() {
        return createBeginTime;
    }

    public void setCreateBeginTime(String createBeginTime) {
        this.createBeginTime = createBeginTime;
    }

    public String getCreateEndTime() {
        return createEndTime;
    }

    public void setCreateEndTime(String createEndTime) {
        this.createEndTime = createEndTime;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    @Override
    public String toString() {
        return "AccountPurchaseLowInput{" +
                "createBeginTime='" + createBeginTime + '\'' +
                ", createEndTime='" + createEndTime + '\'' +
                ", condition='" + condition + '\'' +
                '}';
    }


}
