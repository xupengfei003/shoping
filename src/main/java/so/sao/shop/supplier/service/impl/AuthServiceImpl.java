package so.sao.shop.supplier.service.impl;

import com.aliyun.mns.model.TopicMessage;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.config.sms.SmsService;
import so.sao.shop.supplier.dao.AccountDao;
import so.sao.shop.supplier.dao.UserDao;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.AuthService;
import so.sao.shop.supplier.util.JwtTokenUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 权限service
 *
 * @author guangpu.yan
 * @create 2017-07-10 16:31
 **/
@Service
public class AuthServiceImpl implements AuthService {

    private Logger logger = LoggerFactory.getLogger(getClass());
    private AuthenticationManager authenticationManager;
    private UserDetailsService userDetailsService;
    private UserDao userDao;
    private JwtTokenUtil jwtTokenUtil;
    private SmsService smsService;
    private RedisTemplate redisTemplate;
    @Autowired
    private AccountDao accountDao;

    /**
     * 验证码
     */
    @Value("${shop.aliyun.sms.sms-template-code1}")
    String smsTemplateCode1;

    /**
     * 找回密码
     */
    @Value("${shop.aliyun.sms.sms-template-code3}")
    String smsTemplateCode3;

    @Autowired
    public AuthServiceImpl(
            AuthenticationManager authenticationManager,
            UserDetailsService userDetailsService,
            UserDao userDao, JwtTokenUtil jwtTokenUtil, SmsService smsService, RedisTemplate redisTemplate) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userDao = userDao;
        this.jwtTokenUtil = jwtTokenUtil;
        this.smsService = smsService;
        this.redisTemplate = redisTemplate;
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
        User userDetails = null;
        try {
            userDetails = (User)userDetailsService.loadUserByUsername(username);
        }catch (UsernameNotFoundException e){
            return Result.fail("当前号码无效!");
        }
        try{
            UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
            final Authentication authentication = authenticationManager.authenticate(upToken);
            //TODO 后续增加权限功能时,权限、角色变更（新增权限到人，新增角色并给人，角色变更权限，权限变更）反查到人更新authentication
            redisTemplate.opsForHash().put(Constant.REDIS_LOGIN_KEY_PREFIX+userDetails.getUsername(),"authentication", authentication);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (Exception e){
            logger.debug("登陆异常:"+e.getMessage());
            return Result.fail("用户名或密码错误!");
        }
        //如果登录用户是员工，则根据该员工对应的供应商状态设置它的登录状态: 正常:1 / 禁用:2
        if (null == userDetails.getUserStatus()){
            Account account = accountDao.selectByPrimaryKey(userDetails.getAccountId());
            userDetails.setUserStatus(account.getAccountStatus().toString());
        }
        if (userDetails.getUserStatus().equals("0")){
            userDetails.setUserStatus("1");
        }

        //登陆后放入缓存,后续从redis取,登出时del
        redisTemplate.opsForHash().put(Constant.REDIS_LOGIN_KEY_PREFIX+userDetails.getUsername(),"user", userDetails);
        Map result = new HashMap();
        result.put("token", jwtTokenUtil.generateToken(userDetails));
        result.put("user",userDetails);

        return Result.success("", result);
    }

    /**
     * 登出
     * @param user
     * @return
     */
    public Result logout(User user){
        userDao.updateLogoutTime(user.getId(), new Date());
        //登出时del
        redisTemplate.opsForHash().delete(Constant.REDIS_LOGIN_KEY_PREFIX+user.getUsername(),"user", "authentication");
        return Result.success("登出成功");
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
    public Result getPassword(String phone) throws IOException {
        User u = userDao.findByUsername(phone);
        //判断当前登录人和接收密码手机是否一直
        if(u!=null&& StringUtils.isNotBlank(u.getUsername())){
            String password = smsService.getVerCode();
            //密码加密保存,忘记密码只能手机验证发送新密码
            userDao.updatePassword(u.getId(),new BCryptPasswordEncoder().encode(password), new Date());
            smsService.sendSms(Collections.singletonList(phone), Collections.singletonList("password"), Collections.singletonList(password), smsTemplateCode3);
            return Result.success("发送成功");
        }else{
            return Result.fail("当前号码无效!");
        }
    }

    /**
     * 发送验证码
     * @param phone
     * @return
     * @throws IOException
     */
    @Override
    public Result sendCode(String phone) throws IOException {
        String code = smsService.getVerCode();
        userDao.saveSmsCode(phone, code);
        TopicMessage topicMessage = smsService.sendSms(Collections.singletonList(phone),Collections.singletonList("code"), Collections.singletonList(code), smsTemplateCode1);
        if(topicMessage != null) {
            return Result.success("发送成功");
        } else {
            return Result.fail("发送失败");
        }
    }
    /**
     * 验证码校验
     * @param user
     * @param code
     * @return
     */
    @Override
    public Result verifySmsCode(User user, String code){
        String SmsCode = userDao.findSmsCode(user.getId());
        if(SmsCode!=null&&SmsCode.equals(code)){
            userDao.saveSmsCode(user.getUsername(),"");
            return Result.success("验证通过");
        }else{
            return Result.fail("验证不通过");
        }
    }

    /**
     * 修改密码
     * @param userId
     * @param encodedPassword
     * @return
     */
    @Override
    public Result updatePassword(Long userId,  String encodedPassword) throws IOException{

        User user = userDao.findOne(userId);
        if(user!=null&& StringUtils.isNotBlank(user.getPassword())){
            if(new BCryptPasswordEncoder().matches(encodedPassword,user.getPassword())){
                return Result.fail("新密码不能和旧密码相同");
            }
            userDao.updatePassword(user.getId(),new BCryptPasswordEncoder().encode(encodedPassword), new Date());
            UserDetails userDetails = null;
            try {
                userDetails = userDetailsService.loadUserByUsername(user.getUsername());
            }catch (UsernameNotFoundException e){
                logger.error("当前号码无效",e.getMessage());
                return Result.fail("当前号码无效");
            }
            redisTemplate.opsForHash().put(Constant.REDIS_LOGIN_KEY_PREFIX+userDetails.getUsername(),"user", userDetails);
            Map result = new HashMap();
            result.put("token", jwtTokenUtil.generateToken(userDetails));
            result.put("user",userDetails);
            return Result.success("密码修改成功", result);
        }
        return Result.fail("当前号码无效");
    }
}