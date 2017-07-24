package com.sao.so.shop.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期转换工具类
 * @author gxy.
 */
public final class StringUtil {

  /**
   * 日期转字符格式化
   * @param date 日期
   * @param formate 格式化格式
   * @return
     */
  public static String fomateData(Date date, String formate){
    SimpleDateFormat sdf = new SimpleDateFormat(formate);
    return sdf.format(date);
  }

  public static String fomateData(Long time, String formate){
    Date date = new Date(time);
    SimpleDateFormat sdf = new SimpleDateFormat(formate);
    return sdf.format(date);
  }
}
