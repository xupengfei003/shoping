package so.sao.shop.supplier.util;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import so.sao.shop.supplier.config.CommConstant;

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
     * list 分页
     * @param dataList
     * @param fromPageNo 页码
     * @param limit 每页条数
     * @return
     */
    public static List<?> getListByPage(List<?> dataList,Integer fromPageNo,Integer toPageNo,Integer limit){
        if(DataCompare.formatInteger(fromPageNo) > 0 && DataCompare.formatInteger(limit) > 0){
            int totalcount = dataList.size();
            int startInt = totalcount > (fromPageNo-1)*limit ? (fromPageNo-1)*limit : 99999999;
            int endInt = totalcount > toPageNo*limit ? toPageNo*limit : totalcount;
            if(startInt != 99999999 && dataList != null && dataList.size() > 0){
                return dataList.subList(startInt,endInt);
            }
        }
        return dataList;
    }
}
