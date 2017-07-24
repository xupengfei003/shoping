package com.sao.so.shop.pojo.output;

import com.github.pagehelper.PageInfo;
import com.sao.so.shop.pojo.BaseResult;
import com.sao.so.shop.pojo.vo.OrderMoneyRecordVo;


/**
 * Created by fangzhou on 2017/7/20.
 */
public class OrderMoneyRecordOutput extends BaseResult {

    private PageInfo<OrderMoneyRecordVo> pageInfo;

    public PageInfo<OrderMoneyRecordVo> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<OrderMoneyRecordVo> pageInfo) {
        this.pageInfo = pageInfo;
    }

}
