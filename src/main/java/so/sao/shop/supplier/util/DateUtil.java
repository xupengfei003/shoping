package so.sao.shop.supplier.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by bzh on 2017/7/29.
 */
public class DateUtil {
    /**
     * 判断时间格式
     * @param datetime
     * @return
     */
    public static boolean isDate(String datetime){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        boolean dateFlag = true;
        try {
            sdf.parse(datetime);
        } catch (Exception e) {
            dateFlag = false;
        }
        return dateFlag;
    }
}
