package so.sao.shop.supplier.pojo.input;

import javax.validation.constraints.NotNull;

/**
 * @author gxy on 2017/8/14.
 */
public class FeedbackInput {
    /**
     * 供应商ID
     */
    private Long accountId;
    /**
     * 建议
     */
    @NotNull(message = "供应商建议不能为空")
    private String suggest;

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }
}
