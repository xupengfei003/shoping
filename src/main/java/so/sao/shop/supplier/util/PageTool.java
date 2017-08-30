package so.sao.shop.supplier.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import so.sao.shop.supplier.config.CommConstant;

/**
 *  分页公共类
 */
public class PageTool {
    /**
     * 用法:调用dao查询方法前调用即可分页
     * @param pageNum
     * @param pageSize
     * @return
     */
    public static Page startPage(Integer pageNum, Integer pageSize){
        pageNum = (pageNum==null || pageNum<1) ? CommConstant.PAGE_NUM_DEFAULT : pageNum;
        pageSize = (pageSize==null || pageSize<1) ? CommConstant.PAGE_SIZE_DEFAULT : pageSize;
        return PageHelper.startPage(pageNum, pageSize);
    }
}
