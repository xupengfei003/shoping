package so.sao.shop.supplier.util;

import java.text.ParseException;
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
            sdf.setLenient(false);
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

    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyyMMddHHmmssSSS
     */
    public static String getStringDateTime() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        String dateString = formatter.format(currentTime);
        return dateString;
    }

    /**
     * 获取现在时间
     *
     * @return返回字符串格式 yyyyMMddHHmmssSSS
     */
    public static Date stringToDate(String value) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            if(value == null || "".equals(value)){
                date = new Date();
            }else{
                date = sdf.parse(value.trim());
            }
        } catch (ParseException e) {
            e.printStackTrace();
            date = new Date();
        }
        return date;
    }

    /**
     *截取指定的时间串
     * @param originStr 源串
     * @param index 字符下标
     * @return  String
     */
    public static String subStringByIndex(String originStr , int index){
        if(originStr.length()<index){
            return "";
        }
        return originStr.substring(0 , index);
    }
}
