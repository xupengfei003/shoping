package so.sao.shop.supplier.util;


import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * <p>
 * 数据比较  主要用于比较开始时间和结束时间   /  开始金额和结束金额
 * </p>
 *
 * @author 透云-中软-西安项目组-zhenhai.zheng
 * @since 2017年8月17日 09:38:46
 **/
public class DataCompare {
    /**
     * 比较开始时间和结束时间的大小
     *   注:两个时间参数为必传，且时间格式为“yyyy-MM-dd”
     * @param beginTime 开始时间
     * @param endTime 结束时间
     * @return 若为false 说明开始时间早于结束时间 或两个时间不满足必须条件（不为空且格式为“yyyy-MM-dd”）
     */
    public static boolean compareDate(String beginTime, String endTime) throws ParseException {
        if (!StringUtils.isEmpty(beginTime) && !StringUtils.isEmpty(endTime) && DateUtil.isDate(beginTime) && DateUtil.isDate(endTime)){
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date begin = sdf.parse(beginTime);
            Date end = sdf.parse(endTime);
            return begin.compareTo(end) <= 0 ? false : true;
        }
        return false;
    }

    /**
     * 比较开始金额和结束金额的大小
     * @param beginMoney 开始金额
     * @param endMoney 结束金额
     * @return 若为false  说明开始金额小于结束金额
     */
    public static boolean compareMoney(BigDecimal beginMoney,BigDecimal endMoney){
        if (null != beginMoney && null !=endMoney){
            return beginMoney.compareTo(endMoney) <= 0 ? false : true;
        }
        return false;
    }

}
