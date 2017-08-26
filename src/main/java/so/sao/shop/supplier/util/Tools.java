package so.sao.shop.supplier.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by acer on 2017/7/25.
 */
public class Tools {

    public static boolean isNumeric(String str){
        Pattern pattern = Pattern.compile("[0-9]*");
        Matcher isNum = pattern.matcher(str);
        if( !isNum.matches() ){
            return false;
        }
        return true;
    }

    /**
     * 拼接 order by 后面的字符串
     * 将传进的排序规则字符串按照要排序的表字段转化成sql语句中的排序字符串
     *  注：传进的排序规则字符串中 排序字段为数字，该数字将作为数组下标进行转化
     *                          排序规则(升序降序)：升序：0，降序：1
     *      若返回null表示该排序规则字符串有异常（不符合排序规则）
     *      建议先判断排序规则字符串是否为空，不为空调用该方法
     * @param orderByStr 排序字符串格式：fields下标,[升序0或降序1],...
     *                   ag:(0,1,1,1,2,0)
     * @param fields 要排序的字段([id,name,age])
     * @return sql语句中的排序字符串   ag:(id DESC,name DESC,age ASC)
     */
    public static String concatOrderByStr(String orderByStr, String[] fields) {// 0,0,1,0
        if (Ognl.isEmpty(orderByStr)) {
            return null;
        }

        Pattern p=Pattern.compile("[\\d+,]+\\d");
        Matcher matcher = p.matcher(orderByStr);
        if (!matcher.matches()) {
            return null;
        }

        String[] temp = orderByStr.split(",");
        int len = temp.length;
        if (temp.length % 2 != 0) {
            len = len - 1;
        }

        String[] orders = {"ASC", "DESC"};
        StringBuilder sb = new StringBuilder();
        try {
            for (int i = 0; i < len; i++) {
                String index = temp[i];
                if (i % 2 == 0) { // 偶数
                    sb.append(fields[Integer.parseInt(index)]);
                    sb.append(" ");
                } else {
                    sb.append(orders[Integer.parseInt(index)]);
                    sb.append(",");
                }
            }
        } catch (Exception e) {
            return null;
        }
        if (sb.length() > 0) {
            return  sb.substring(0, sb.length() - 1);
        }

        return null;
    }
}


