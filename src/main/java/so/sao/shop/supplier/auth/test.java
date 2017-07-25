package so.sao.shop.supplier.auth;/**
 * Created by lenovo on 2017/7/25.
 */

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * d
 *
 * @author
 * @create 2017-07-25 19:51
 **/
public class test {
    public static void main(String[]s){
        System.out.println(new BCryptPasswordEncoder().encode("123"));
        System.out.println(new BCryptPasswordEncoder().encode("456"));
        System.out.println(new BCryptPasswordEncoder().encode("789"));
    }
}
