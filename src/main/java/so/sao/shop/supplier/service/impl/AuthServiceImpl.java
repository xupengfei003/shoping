package so.sao.shop.supplier.service.impl;

import com.aliyun.mns.model.TopicMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.config.sms.SmsService;
import so.sao.shop.supplier.dao.UserDao;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.AuthService;
import so.sao.shop.supplier.util.Constant;
import so.sao.shop.supplier.util.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;

/**
 * 权限service
 *
 * @author guangpu.yan
 * @create 2017-07-10 16:31
 **/
@Service
public class AuthServiceImpl implements AuthService {

    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private UserDao userDao;
    private JwtTokenUtil jwtTokenUtil;
    private SmsService smsService;


    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            UserDao userDao,JwtTokenUtil jwtTokenUtil,SmsService smsService) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userDao = userDao;
        this.jwtTokenUtil=jwtTokenUtil;
        this.smsService=smsService;
    }

    /**
     * 登陆方法，返回token
     * 后续请求需带上token样例:请求header Authorization:Bearer eyJhbGciOiJSUzI1NiIsInR5cCI6IkpXVCJ9.eyJwYXNzd29yZCI6ImFkbWluIiwidXNlcm5hbWUiOiJhZG1pbiIsImF1dGhvcml0aWVzIjpbeyJhdXRob3JpdHkiOiJST0xFX0FETUlOIn0seyJhdXRob3JpdHkiOiJST0xFX0hPTUUifSx7ImF1dGhvcml0eSI6InEifV0sImFjY291bnROb25FeHBpcmVkIjp0cnVlLCJhY2NvdW50Tm9uTG9ja2VkIjp0cnVlLCJjcmVkZW50aWFsc05vbkV4cGlyZWQiOnRydWUsImVuYWJsZWQiOnRydWV9.Uq9iKbjQz99ZQIPlBh4GVslh6IwsAG1UFBEM0-SbTzLDfhRGHA7AonBJiYiP2oHJH3wDP-YQRaTl3j9oDIgPRO6D3Qofu2XxZl2wO3lRE-sL4FJZNsmEWKooFeCiUNey-BXgk7-PvuF3QBCpym-jdmKlgtZDZ3sjWQkOFg9UmLI
     * @param username
     * @param password
     * @return
     * @throws IOException
     */
    public Result login(String username, String password) throws IOException {
        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
        final Authentication authentication = authenticationManager.authenticate(upToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetails userDetails = null;
        try {
            userDetails = userDetailsService.loadUserByUsername(username);
        }catch (UsernameNotFoundException e){
            return new Result(0,"当前号码无效!","");
        }
        return new Result<String>(1, "", jwtTokenUtil.generateToken(userDetails));
    }

    /**
     * 登出
     * @param userId
     * @return
     */
    public Result logout(Long userId){
        userDao.updateLogoutTime(userId, System.currentTimeMillis());
        return new Result(1,"登出成功",null);
    }

    /**
     * 刷新token
     * @param request
     * @return
     * @throws IOException
     */
    public String refresh(HttpServletRequest request) throws IOException {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        final String token = jwtTokenUtil.getTokenFromRequest(request);
        if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate(), user.getLogoutTime())){
            return jwtTokenUtil.refreshToken(token);
        }
        return null;
    }

    /**
     * 忘记密码
     * @param phone
     * @return
     * @throws IOException
     */
    @Override
    public BaseResult getPassword(String phone, Long userId) throws IOException {
        User u = userDao.findByUsername(phone);
        //判断当前登录人和接收密码手机是否一直
        if(u!=null&& StringUtils.isNotBlank(u.getUsername())&&u.getId()==userId){
            String password = smsService.getVerCode();
            //密码加密保存,忘记密码只能手机验证发送新密码
            userDao.updatePassword(u.getId(),new BCryptPasswordEncoder().encode(password), new Date().getTime());
            smsService.sendSms(Collections.singletonList(phone), password);
            return new BaseResult(1, "发送成功");
        }else{
            return new BaseResult(0, "当前号码无效!");
        }
    }

    /**
     * 发送验证码
     * @param phone
     * @return
     * @throws IOException
     */
    @Override
    public BaseResult sendCode(String phone) throws IOException {
        String code = smsService.getVerCode();
        userDao.saveSmsCode(phone, code);
        TopicMessage topicMessage = smsService.sendSms(Collections.singletonList(phone), code);
        if(topicMessage != null) {
            return new BaseResult(1,"发送成功");
        } else {
            return new BaseResult(0,"发送失败");
        }
    }

    /**
     * 验证码校验
     * @param userId
     * @param code
     * @return
     */
    @Override
    public BaseResult verifySmsCode(Long userId, String code){
        String SmsCode = userDao.findSmsCode(userId);
        if(SmsCode!=null&&SmsCode.equals(code)){
            return new BaseResult(1, "验证通过");
        }else{
            return new BaseResult(0, "验证不通过");
        }
    }

    /**
     * 修改密码
     * @param userId
     * @param encodedPassword
     * @return
     */
    @Override
    public BaseResult updatePassword(Long userId,  String encodedPassword) {

        User user = userDao.findOne(userId);
        if(user!=null&& StringUtils.isNotBlank(user.getPassword())){
            userDao.updatePassword(user.getId(),new BCryptPasswordEncoder().encode(encodedPassword), new Date().getTime());
            return new BaseResult(1,"密码修改成功");
        }
        return new BaseResult(0,"当前号码无效!");
    }
}