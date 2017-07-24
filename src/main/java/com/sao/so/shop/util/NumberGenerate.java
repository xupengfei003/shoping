package com.sao.so.shop.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
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

    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f",
            "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s",
            "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5",
            "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G", "H", "I",
            "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V",
            "W", "X", "Y", "Z"};

    public static String generateUuid() {
        SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
        StringBuffer shortBuffer = new StringBuffer(sf.format(new Date()));
        String uuid = UUID.randomUUID().toString().replace("-", "");
        for (int i = 0; i < 8; i++) {
            String str = uuid.substring(i * 4, i * 4 + 4);
            int x = Integer.parseInt(str, 16);
            shortBuffer.append(chars[x % 62]);
        }
        return shortBuffer.toString();
    }
}
