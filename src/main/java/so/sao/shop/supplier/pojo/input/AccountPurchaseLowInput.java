package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

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
     * 查询条件(订单编号/流水号)
     */
    private String condition;

    /**
     * 排序的字段 0 付款时间
     */
    @Pattern(regexp = "^[0]$", message = "排序字段不存在")
    @NotEmpty(message = "排序字段码不能为空")
    private String sortName;

    /**
     * 排序的次序 0 正序; 1 逆序
     */
    @Pattern(regexp = "^[0-1]$", message = "排序次序码错误")
    @NotEmpty(message = "排序次序码不能为空")
    private String sortType;

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

    public String getSortName() {
        return sortName;
    }

    public void setSortName(String sortName) {
        this.sortName = sortName;
    }

    public String getSortType() {
        return sortType;
    }

    public void setSortType(String sortType) {
        this.sortType = sortType;
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
