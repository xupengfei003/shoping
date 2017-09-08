package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Pattern;

/**
 * Created by fangzhou on 2017/9/7.
 */
public class OrderMoneyRecordRankInput extends OrderMoneyRecordInput {
    /**
     * 排序的字段 0 结账时间; 1 待结算金额; 2 结算时间; 3 结算金额
     */
    @Pattern(regexp = "^[0-3]$", message = "排序字段不存在")
    @NotEmpty(message = "排序字段码不能为空")
    private String sortName;

    /**
     * 排序的次序 0 正序; 1 逆序
     */
    @Pattern(regexp = "^[0-1]$", message = "排序次序码错误")
    @NotEmpty(message = "排序次序码不能为空")
    private String sortType;

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
}
