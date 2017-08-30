package so.sao.shop.supplier.util;


import so.sao.shop.supplier.config.CommConstant;
import so.sao.shop.supplier.pojo.vo.Page;

/**
 * Created by liugang on 2017/7/19.
 * 分页工具类PageUtil
 */
public class PageUtil {

    public static Page pageCheck(Page page)
    {
        if (null == page)
        {
            return new Page(CommConstant.PAGE_NUM_DEFAULT, CommConstant.PAGE_SIZE_DEFAULT);
        }
        int pageNum = page.getPageNum() == null ? CommConstant.PAGE_NUM_DEFAULT : page.getPageNum();
        int pageSize = page.getRows() == null ? CommConstant.PAGE_SIZE_DEFAULT : page.getRows();
        //分页参数校验、处理
        pageNum = pageNum > 0 ? pageNum : CommConstant.PAGE_NUM_DEFAULT;
        pageSize = pageSize > 0 ? pageSize : CommConstant.PAGE_SIZE_DEFAULT;
        pageNum = pageNum < 500 ? pageNum : CommConstant.PAGE_NUM_MAX_DEFAULT;
        pageSize = pageSize < 50 ? pageSize : CommConstant.PAGE_SIZE_MAX_DEFAULT;
        page.setPageNum(pageNum);
        page.setRows(pageSize);
        return page;
    }
}
