package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * 权限service
 *
 * @author guangpu.yan
 * @create 2017-07-10 16:31
 **/
public interface AuthService {

    /**
     * 登陆方法，返回token
     * 后续请求需带上token样例:请求header Authorization:Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXNzd29yZCI6ImFkbWluIiwidXNlcm5hbWUiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJST0xFX0FETUlOIn0seyJhdXRob3JpdHkiOiJST0xFX0hPTUUifSx7ImF1dGhvcml0eSI6InEifV0sImFjY291bnROb25FeHBpcmVkIjp0cnVlLCJhY2NvdW50Tm9uTG9ja2VkIjp0cnVlLCJjcmVkZW50aWFsc05vbkV4cGlyZWQiOnRydWUsImVuYWJsZWQiOnRydWV9.Uq9iKbjQz99ZQIPlBh4GVslh6IwsAG1UFBEM0-SbTzLDfhRGHA7AonBJiYiP2oHJH3wDP-YQRaTl3j9oDIgPRO6D3Qofu2XxZl2wO3lRE-sL4FJZNsmEWKooFeCiUNey-BXgk7-PvuF3QBCpym-jdmKlgtZDZ3sjWQkOFg9UmLI
     * @param username
     * @param password
     * @return
     * @throws IOException
     */
    public Result login(String username, String password) throws IOException ;

    /**
     * 登出
     * @param userId
     * @return
     */
    public Result logout(Long userId) ;

    /**
     * 刷新token
     * @param request
     * @return
     * @throws IOException
     */
    public String refresh(HttpServletRequest request) throws IOException;

    /**
     * 根据手机号获取密码
     * @param tel
     * @return
     * @throws IOException
     */
    public BaseResult getPassword(String tel) throws IOException;


    /**
     * 发送验证码
     * @param tel
     * @return
     */
    public BaseResult sendCode(String tel) throws IOException;

    /**
     * 验证码校验
     * @param userId
     * @param code
     * @return
     */
    public BaseResult verifySmsCode(Long userId, String code);

    /**
     * 密码修改
     * @param userId
     * @param encodedPassword
     * @return
     */
    public BaseResult updatePassword(Long userId, String encodedPassword);

}