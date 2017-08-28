package so.sao.shop.supplier.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.stereotype.Component;
import so.sao.shop.supplier.domain.User;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * token帮助类
 *
 * @author guangpu.yan
 * @create 2017-07-14 14:27
 **/
@Component
public class JwtTokenUtil implements Serializable {
    protected final Log logger = LogFactory.getLog(getClass());
    private static final long serialVersionUID = 8113483942194008184L;
    public static final String CLAIM_KEY_USERNAME = "sub";
    public static final String CLAIM_KEY_CREATED = "created";
    public static final String CLAIM_KEY_EXPIRATION = "exp";

    @Value("${jwt.privateKey}")
    private String privateKey;
    @Value("${jwt.publicKey}")
    private String publicKey;
    @Value("${jwt.tokenHead}")
    private String tokenHead;
    @Value("${jwt.header}")
    private String tokenHeader;
    @Value("${jwt.expiration}")
    private int expiration;



    /**
     * 根据userDetails生成token
     *
     * @param userDetails
     * @return
     * @throws IOException
     */
    public String generateToken(UserDetails userDetails) throws IOException {
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
        claims.put(CLAIM_KEY_CREATED, new Date());
        return generateToken(claims);
    }

    /**
     * 生成token
     *
     * @param claims
     * @return
     * @throws IOException
     */
    String generateToken(Map<String, Object> claims) throws IOException {
        claims.put(CLAIM_KEY_EXPIRATION, new Date(System.currentTimeMillis() + expiration * 1000));
        String content = new ObjectMapper().writeValueAsString(claims);
        return tokenHead + JwtHelper.encode(content, new RsaSigner(privateKey), Collections.singletonMap("alg", "RS256")).getEncoded();
    }

    /**
     * 刷新token
     *
     * @param token
     * @return
     * @throws IOException
     */
    public String refreshToken(String token) throws IOException {
        Map claims = getClaimsFromToken(token);
        String refreshedToken;
        try {
            claims.put(CLAIM_KEY_CREATED, new Date());
            refreshedToken = generateToken(claims);
        } catch (Exception e) {
            refreshedToken = null;
        }
        return refreshedToken;
    }

    /**
     * 从token获取claims
     * @param token
     * @return
     * @throws IOException
     */
    public Map getClaimsFromToken(String token) throws IOException {
        return new ObjectMapper().readValue(JwtHelper.decode(token).getClaims(), Map.class);
    }


    /**
     * 判断token是否过期
     * @param token
     * @return
     * @throws IOException
     */
    private Boolean isTokenExpired(String token) throws IOException {
        Date expiration = new Date((Long)getClaimsFromToken(token).get(CLAIM_KEY_EXPIRATION));
        return expiration.before(new Date());
    }

    /**
     * token创建时间和密码最后一次修改时间对比
     * @param created
     * @param lastPasswordReset
     * @return
     */
    private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
        return created.before(lastPasswordReset);
    }

    /**
     * token创建时间和登出时间对比
     * @param created
     * @param logoutTime
     * @return
     */
    private Boolean isLogout(Date created, Date logoutTime) {
        return created.before(logoutTime);
    }


    /**
     * 验证token是否失效
     * @param token
     * @param userDetails
     * @return
     * @throws IOException
     */
    public Boolean validateToken(String token, UserDetails userDetails) throws IOException {
        User user = (User)userDetails;
        Map claims = getClaimsFromToken(token);
        String username = claims.get(CLAIM_KEY_USERNAME).toString();
        Date created = new Date(Long.valueOf(claims.get(CLAIM_KEY_CREATED).toString()));
        logger.debug("validateToken:"+username+"-"+user);
        return (
                username.equals(user.getUsername())
                        && !isTokenExpired(token) && (user.getLastPasswordResetDate()==null || !isCreatedBeforeLastPasswordReset(created, user.getLastPasswordResetDate())) && (user.getLogoutTime()==null || !isLogout(created,user.getLogoutTime())));
    }

    /**
     * 从request中获取claims
     * @param request
     * @return
     * @throws IOException
     */
    public Map getClaimsFromRequest(HttpServletRequest request) throws IOException {
        String authHeader = request.getHeader(tokenHeader);
        authHeader = StringUtils.isBlank(authHeader)?request.getParameter("token"):authHeader;
        String authToken = authHeader.substring(tokenHead.length());
        return new ObjectMapper().readValue(JwtHelper.decode(authToken).getClaims(), Map.class);
    }

    /**
     * 从request中获取token
     * @param request
     * @return
     * @throws IOException
     */
    public String getTokenFromRequest(HttpServletRequest request) throws IOException {
        String authHeader = request.getHeader(tokenHeader);
        authHeader = StringUtils.isBlank(authHeader)?request.getParameter("token"):authHeader;
        String authToken = authHeader.substring(tokenHead.length());
        return authToken;
    }

    /**
     * 验证token正确性
     * @param token
     */
    public void verifySignature(String token){
        JwtHelper.decode(token).verifySignature(new RsaVerifier(publicKey));
    }

    /**
     * 判断是否可以刷新token(token是否过期/token创建时间在修改密码之前/是否登出)
     *
     * @param token
     * @param lastPasswordReset
     * @return
     * @throws IOException
     */
    public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset, Date logoutTime) throws IOException  {
        Map claims = getClaimsFromToken(token);
        Date created = new Date(Long.valueOf(claims.get(CLAIM_KEY_CREATED).toString()));
        return !isTokenExpired(token) && (lastPasswordReset==null || !isCreatedBeforeLastPasswordReset(created, lastPasswordReset))
                && (logoutTime==null || !isLogout(created, logoutTime));
    }

}
