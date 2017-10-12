package so.sao.shop.supplier.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
     * 日期格式转化
     * @return返回字符串格式 yyyy-MM-dd
     */
    public static String getStringDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String dateString = formatter.format(date);
        return dateString;
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

    /**
     * 获取本周第一天
     */
    public static Date getTimesWeekmorning() {
        Calendar cal = Calendar.getInstance();
        cal.set(cal.get(Calendar.YEAR), cal.get(Calendar.MONDAY), cal.get(Calendar.DAY_OF_MONTH), 0, 0, 0);
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        return  cal.getTime();
    }

    /**
     * 获取本周最后一天
     */
    public  static Date getTimesWeeknight() {
        Calendar cal = Calendar.getInstance();
        cal.setTime(getTimesWeekmorning());
        cal.add(Calendar.DAY_OF_WEEK, 6);
        return cal.getTime();
    }

    /**
     * 获取指定时间所在月的第一天
     */
    public static Date firstDayOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    /**
     * 获取指定时间所在月的最后一天
     * @param date
     * @return
     */
    public static Date lastDayOfMonth(Date date){
        Calendar ca = Calendar.getInstance();
        ca.set(Calendar.DAY_OF_MONTH, ca.getActualMaximum(Calendar.DAY_OF_MONTH));
        return ca.getTime();
    }

    /**
     * 获取指定时间--n个月之前的时间
     */
    public static Date subtractMonths(Date date, int n) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.MONTH, -(n-1));
        return firstDayOfMonth(calendar.getTime());
    }

    /**
     * 获取当前年的第一天时间
     */
    public static Date getFirstDayOfYear(){
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        calendar.clear();
        calendar.set(Calendar.YEAR, currentYear);
        return calendar.getTime();
    }

}
