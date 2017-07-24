package com.sao.so.shop.pojo.output;

import com.sao.so.shop.pojo.BaseResult;

import java.math.BigDecimal;

/**
 * Created by jujiangkun on 2017/7/20.
 */
public class SumIncome extends BaseResult{
    private BigDecimal income;

    public BigDecimal getIncome() {
        return income;
    }

    public void setIncome(BigDecimal income) {
        this.income = income;
    }

    @Override
    public String toString() {
        return "SumIncome{" +
                "income=" + income +
                '}';
    }
}
