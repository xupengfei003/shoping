package so.sao.shop.supplier.config.aspect;

import com.google.gson.Gson;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tengfei.zhang on 2017/10/13.
 */
@Aspect
@Component
public class LogAspect {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    private String requestPath = null ; // 请求地址
    private String userName = null ; // 用户名
    private Map<?,?> inputParamMap = null ; // 传入参数
    private Map<String, Object> outputParamMap = null; // 存放输出结果
    private long startTimeMillis = 0; // 开始时间
    private long endTimeMillis = 0; // 结束时间

    /**
     *
     * @Title：doBeforeInServiceLayer
     * @Description: 方法调用前触发
     *  记录开始时间
     * @author tengfei.zhang
     * @date 2017年10月13日 下午3:30:53
     * @param joinPoint
     */
    @Before(value = "(@annotation(org.springframework.web.bind.annotation.GetMapping)) || " +
            "(@annotation(org.springframework.web.bind.annotation.PostMapping)) || " +
            "(@annotation(org.springframework.web.bind.annotation.PutMapping)) || " +
            "(@annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public void doBeforeInServiceLayer(JoinPoint joinPoint) {
        startTimeMillis = System.currentTimeMillis(); // 记录方法开始执行的时间
        this.startPrintOptLog();
    }

    /**
     *
     * @Title：doAfterInServiceLayer
     * @Description: 方法调用后触发
     *  记录结束时间
     * @author tengfei.zhang
     * @date 2017年10月13日 下午4:46:21
     * @param joinPoint
     */
    @After(value = "(@annotation(org.springframework.web.bind.annotation.GetMapping)) || " +
            "(@annotation(org.springframework.web.bind.annotation.PostMapping)) || " +
            "(@annotation(org.springframework.web.bind.annotation.PutMapping)) || " +
            "(@annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public void doAfterInServiceLayer(JoinPoint joinPoint) {
        endTimeMillis = System.currentTimeMillis(); // 记录方法执行完成的时间
        this.endPrintOptLog();
    }

    /**
     *
     * @Title：doAround
     * @Description: 环绕触发
     * @author tengfei.zhang
     * @date 2014年11月3日 下午1:58:45
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around(value = "(@annotation(org.springframework.web.bind.annotation.GetMapping)) || " +
            "(@annotation(org.springframework.web.bind.annotation.PostMapping)) || " +
            "(@annotation(org.springframework.web.bind.annotation.PutMapping)) || " +
            "(@annotation(org.springframework.web.bind.annotation.DeleteMapping))")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        /**
         * 1.获取request信息
         * 2.根据request获取token
         * 3.从token中取出登录用户信息
         */
        RequestAttributes ra = RequestContextHolder.getRequestAttributes();
        ServletRequestAttributes sra = (ServletRequestAttributes)ra;
        HttpServletRequest request = sra.getRequest();
        // 从token中获取用户信息
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user != null && !"".equals(user)){
            userName = user.getUsername();
        }else{
            userName = "用户未登录" ;
        }
        // 获取输入参数
        inputParamMap = request.getParameterMap();
        // 获取请求地址
        requestPath = request.getRequestURI();

        // 执行完方法的返回值：调用proceed()方法，就会触发切入点方法执行
        outputParamMap = new HashMap<String, Object>();
        Object result = pjp.proceed();// result的值就是被拦截方法的返回值
        outputParamMap.put("result", result);

        return result;
    }

    /**
     *
     * @Title：printOptLog
     * @Description: 输出日志
     * @author tengfei.zhang
     * @date 2017年10月13日 下午4:47:09
     */
    private void startPrintOptLog() {
        Gson gson = new Gson(); // 需要用到google的gson解析包
        String startTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(startTimeMillis);
        logger.info("\n user："+ userName +"  url："+ requestPath +"; param："+ gson.toJson(inputParamMap)+ ";startTime："+ startTime);
    }
    /**
     *
     * @Title：printOptLog
     * @Description: 输出日志
     * @author tengfei.zhang
     * @date 2017年10月13日 下午4:47:09
     */
    private void endPrintOptLog() {
        Gson gson = new Gson(); // 需要用到google的gson解析包
        String endTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(endTimeMillis);
        logger.info("\n user："+ userName +"  result："+gson.toJson(outputParamMap)+ "endTime："+endTime);
    }
}
