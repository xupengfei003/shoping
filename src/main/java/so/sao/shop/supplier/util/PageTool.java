package so.sao.shop.supplier.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.apache.poi.ss.formula.functions.T;
import so.sao.shop.supplier.config.CommConstant;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * 用法：对于查询结果直接分页
     * @param o
     * @param pageNum
     * @param pageSize
     * @return
     */
    public static List<Object> getListByPageNum(List<Object> o, Integer pageNum , Integer pageSize ){
        //得到全部列表
        if(pageNum == null){
            return o;
        }
        //得到区间列表
        List<Object> returnPageList = new ArrayList<>();
        int startRow ; // 开始条数
        int endRow = pageNum * pageSize ; //结束条数
        for( startRow = (pageNum-1) * pageSize +1 ; startRow <= endRow ; startRow++){
            returnPageList.add(o.get(startRow));
        }
        return returnPageList;
    }
}
