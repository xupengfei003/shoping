package com.sao.so.shop.pojo.output;

import com.github.pagehelper.PageInfo;
import com.sao.so.shop.pojo.BaseResult;


/**
 * <p>
 * 出参类
 * </p>
 *
 * @author 透云-中软-西安项目组-zhenhai.zheng
 * @since 2017-07-20
 **/
public class RecordToPurchaseOutput<T> extends BaseResult {

    private PageInfo<T> pageInfo;

    public PageInfo<T> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<T> pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "RecordToPurchaseOutput{" +
                "pageInfo=" + pageInfo +
                ", code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}