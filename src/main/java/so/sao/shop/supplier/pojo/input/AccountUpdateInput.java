package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by acer on 2017/8/14.
 */
public class AccountUpdateInput {
    /**
     * 用户id
     */
    @NotNull(message = "供应商id")
    private Long accountId;
    /**
     * 状态（1：启用；0禁用）
     */
    @NotNull(message = "供应商状态")
    private int accountStatus;
    /**
     * 更新时间
     */
    private Date  updateDate;
    /**
     * 供应商电话
     */
    private String accountTel;


    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public int getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(int accountStatus) {
        this.accountStatus = accountStatus;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public String getAccountTel() {
        return accountTel;
    }

    public void setAccountTel(String accountTel) {
        this.accountTel = accountTel;
    }
}
