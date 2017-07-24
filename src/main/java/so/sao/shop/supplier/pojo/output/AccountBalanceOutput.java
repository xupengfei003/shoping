package so.sao.shop.supplier.pojo.output;


import so.sao.shop.supplier.pojo.BaseResult;

import java.math.BigDecimal;

/**
 * Created by fangzhou on 2017/7/20.
 */
public class AccountBalanceOutput extends BaseResult {
    private BigDecimal blance;

    public BigDecimal getBlance() {
        return blance;
    }

    public void setBlance(BigDecimal blance) {
        this.blance = blance;
    }
}
