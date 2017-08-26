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
     * ƴ�� order by ������ַ���
     * ����������������ַ�������Ҫ����ı��ֶ�ת����sql����е������ַ���
     *  ע����������������ַ����� �����ֶ�Ϊ���֣������ֽ���Ϊ�����±����ת��
     *                          �������(������)������0������1
     *      ������null��ʾ����������ַ������쳣���������������
     *      �������ж���������ַ����Ƿ�Ϊ�գ���Ϊ�յ��ø÷���
     * @param orderByStr �����ַ�����ʽ��fields�±�,[����0����1],...
     *                   ag:(0,1,1,1,2,0)
     * @param fields Ҫ������ֶ�([id,name,age])
     * @return sql����е������ַ���   ag:(id DESC,name DESC,age ASC)
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
                if (i % 2 == 0) { // ż��
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


