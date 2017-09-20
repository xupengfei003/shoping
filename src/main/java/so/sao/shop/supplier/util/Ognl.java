package so.sao.shop.supplier.util;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

/**
 * 用户mybatis动态SQL验证判断字符串用
 *
 * @author wh
 */
public class Ognl {
    /**
     * 判断是否为空与为null
     *
     * @param o 需要判断的对象
     * @return 如果为空或null返回true
     */
    public static boolean isEmpty(Object o) {
        if (null == o) {
            return true;
        }
        if (o instanceof String) {
            return StringUtils.isEmpty(o.toString().trim());
        }
        return false;
    }

    /**
     * 判断是否为null
     *
     * @param o 需要判断的对象
     * @return 如果为空或null返回true
     */
    public static boolean isNull(Object o) {
        return null==o;
    }

    /**
     * 判断是否不为null
     *
     * @param o 需要判断的对象
     * @return 如果为空或null返回true
     */
    public static boolean isNotNull(Object o) {
        return null!=o;
    }

    /**
     * 判断是否不为空与为null
     *
     * @param o 需要判断的对象
     * @return 如果为空或null返回false
     */
    public static boolean isNotEmpty(Object o) {
        return !isEmpty(o);
    }

    /**
     * 判断第一个字符串是否包含第二个字符串
     *
     * @param str  需要判断的对象
     * @param str1 判断包含的字符串
     * @return 如果为空或不包含返回false
     */
    public static boolean isIndexOf(String str, String str1) {
        if (null != str) {
            return str.indexOf(str) > -1;
        }
        return false;
    }

    /**
     * 判断第一个字符串是否不包含第二个字符串
     *
     * @param str  需要判断的对象
     * @param str1 判断包含的字符串
     * @return 如果为空或不包含返回true
     */
    public static boolean isNotIndexOf(String str, String str1) {
        return !isIndexOf(str, str1);
    }

    /**
     * 判断集合是否不为null与isEmpty
     * @param coll
     * @return
     */
    public static boolean CollectionIsNotEmpty(final Collection<?> coll){
        return CollectionUtils.isNotEmpty(coll);
    }


    /**
     * 判断参数是不是排序要求
     * @param orderPrice
     * @return
     */
    public  static boolean isDESC(String orderPrice){
        if ( null != orderPrice &&  "DESC" .equalsIgnoreCase(orderPrice)){
            return  true;
        }
        return false;
    }


    /**
     * 判断参数是不是排序要求
     * @param orderPrice
     * @return
     */
    public  static boolean isASC(String orderPrice){
        if (  null != orderPrice && "ASC" .equalsIgnoreCase(orderPrice)){
            return  true;
        }
        return false;
    }
    /**
     * 判断参数是不是排序要求
     * @param orderPrice
     * @return
     */
    public  static boolean isNotASCandDESC(String orderPrice){
        if (  !("ASC" .equalsIgnoreCase(orderPrice)) && !("DESC" .equalsIgnoreCase(orderPrice)) ){
            return  true;
        }
        return false;
    }


}

