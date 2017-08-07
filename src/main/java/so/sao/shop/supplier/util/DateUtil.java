package so.sao.shop.supplier.util;

import java.text.SimpleDateFormat;
import java.util.Date;

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
    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyy-MM-dd HH:mm:ss
     */
    public static String getStringDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        return dateString;
    }
}
