package so.sao.shop.supplier.auth;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.util.JwtTokenUtil;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

/**
 * token验证
 *
 * @author guangpu.yan
 * @create 2017-07-10 12:49
 **/
//@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Value("${jwt.header}")
    private String tokenHeader;

    @Value("${jwt.tokenHead}")
    private String tokenHead;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 根据token获取权限,user对象放到request中，供后续使用
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain chain) throws ServletException, IOException {

        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Allow-Methods","GET,POST,OPTIONS,DELETE,PUT");
        response.setHeader("Access-Control-Allow-Credentials","true");
        response.setHeader("Access-Control-Allow-Headers", "x-requested-with,Authorization,Content-Type");

        String authHeader = request.getHeader(this.tokenHeader);
        //header取不到从url中取
        authHeader = StringUtils.isBlank(authHeader)?request.getParameter("token"):authHeader;
        if (authHeader != null && authHeader.startsWith(tokenHead)) {
            final String authToken = authHeader.substring(tokenHead.length());
            //验证token 不正确时抛异常(TODO 异常处理)
            jwtTokenUtil.verifySignature(authToken);
            Map claims = jwtTokenUtil.getClaimsFromToken(authToken);
            String username = claims.get(JwtTokenUtil.CLAIM_KEY_USERNAME).toString();

            if (username != null /*&& SecurityContextHolder.getContext().getAuthentication() == null*/) {
                //redis中用户信息,包含权限信息,结构{user:{username:abc...},authentication:authentication}
                Object result = redisTemplate.opsForHash().entries(Constant.REDIS_LOGIN_KEY_PREFIX+username);
                if(result != null){
                    Map resultMap = (Map) result;
                    if (jwtTokenUtil.validateToken(authToken, (UserDetails) resultMap.get("user"))) {
                        request.setAttribute(Constant.REQUEST_USER,(UserDetails) resultMap.get("user"));
                        response.setHeader(tokenHeader,jwtTokenUtil.getTokenFromRequest(request));
                        SecurityContextHolder.getContext().setAuthentication((Authentication) resultMap.get("authentication"));
                    }else{
                        logger.debug("doFilterInternal:authToken:"+authToken+"-"+resultMap);
                    }
                }else{
                    logger.debug("doFilterInternal:result:"+result);
                }
            }else{
                logger.debug("username:"+username);
                logger.debug("getAuthentication:"+SecurityContextHolder.getContext().getAuthentication());
            }
        }else{
            logger.debug("authHeader:"+authHeader+"-url:"+request.getServletPath()+request.getPathInfo());
        }
        //if(request.getMethod().equals("OPTIONS") || islogin || new AntPathRequestMatcher("/order/export/**").matches(request)|| new AntPathRequestMatcher("/comm/exportExcel/**").matches(request) || new AntPathRequestMatcher("/account/findPassword/**").matches(request) || new AntPathRequestMatcher("/swagger-resources/**").matches(request)|| new AntPathRequestMatcher("/v2/**").matches(request) || new AntPathRequestMatcher("/webjars/**").matches(request) ||new AntPathRequestMatcher("/swagger-ui.html").matches(request) || new AntPathRequestMatcher("/account/auth/**").matches(request)){
        //    chain.doFilter(request, response);
        //}else{
       //     response.getWriter().write(new ObjectMapper().writeValueAsString(new BaseResult(-1,"needlogin")));
       // }
        chain.doFilter(request, response);
    }
}
