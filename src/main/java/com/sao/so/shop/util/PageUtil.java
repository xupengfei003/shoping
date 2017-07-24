package com.sao.so.shop.util;


import com.sao.so.shop.pojo.vo.Page;

/**
 * Created by liugang on 2017/7/19.
 * 分页工具类PageUtil
 */
public class PageUtil {

    public static Page pageCheck(Page page)
    {
        if (null == page)
        {
            return new Page(Constant.PAGE_NUM_DEFAULT, Constant.PAGE_SIZE_DEFAULT);
        }
        int pageNum = page.getPageNum();
        int pageSize = page.getRows();
        //分页参数校验、处理
        pageNum = pageNum > 0 ? pageNum : Constant.PAGE_NUM_DEFAULT;
        pageSize = pageSize > 0 ? pageSize : Constant.PAGE_SIZE_DEFAULT;
        pageNum = pageNum < 500 ? pageNum : Constant.PAGE_NUM_MAX_DEFAULT;
        pageSize = pageSize < 50 ? pageSize : Constant.PAGE_SIZE_MAX_DEFAULT;
        page.setPageNum(pageNum);
        page.setRows(pageSize);
        return page;
    }
}
