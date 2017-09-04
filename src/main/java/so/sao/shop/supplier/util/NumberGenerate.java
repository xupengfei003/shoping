package so.sao.shop.supplier.util;

import org.springframework.boot.SpringApplication;
import so.sao.shop.supplier.Application;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * <p>
 * 生成8位UUID码
 * 经过测试3000万记录不重复。
 * </p>
 *
 * @author 透云-中软-西安项目组-wh
 * @since 2017-07-19
 */
public class NumberGenerate {

    public static String[] chars = new String[]{"00", "01", "02", "03", "04", "05",
            "06", "07", "08", "09", "10", "11", "12", "13", "14", "15", "16", "17", "18",
            "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31",
            "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44",
            "45", "46", "47", "48", "49", "50", "51", "52", "53", "54", "55", "56", "57",
            "58", "59", "60", "61", "62", "63", "64", "65", "66", "67", "68", "69", "70",
            "71", "72", "73", "74", "75", "76", "77", "78", "79", "80", "81", "82", "83",
            "84", "85", "86", "87", "88", "89", "90", "91", "92", "93", "94", "95", "96",
            "97", "98", "99"};

    public static String[] chars_en = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "g", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "G", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    /**
     * 通过UUID进行截取每4位一个单位，转换10进制数字，与100求余得出当前选中参数
     * @return
     */
    public static String generateUuid() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        StringBuffer shortBuffer = new StringBuffer(sf.format(new Date()));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 100]);
        }
        return shortBuffer.toString();
    }

    /**
     * 通过UUID进行截取每4位一个单位，转换10进制数字，与100求余得出当前选中参数
     * @return
     */
    public static String generateId() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMddHHmmss");
        StringBuffer shortBuffer = new StringBuffer(sf.format(new Date()));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars_en[x % 62]);
        }
        return shortBuffer.toString();
    }
}
