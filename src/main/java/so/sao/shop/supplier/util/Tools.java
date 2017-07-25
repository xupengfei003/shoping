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




}


