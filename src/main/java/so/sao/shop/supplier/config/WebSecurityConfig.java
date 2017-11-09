package so.sao.shop.supplier.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import so.sao.shop.supplier.auth.JwtAuthenticationTokenFilter;
import so.sao.shop.supplier.auth.SecurityInterceptor;
import so.sao.shop.supplier.auth.SecurityUserDetailsService;
import so.sao.shop.supplier.exception.ApplicationExceptionHandler;

/**
 * 权限配置类
 *
 * @author
 * @create 2017-07-08 21:38
 **/
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    public ApplicationExceptionHandler applicationExceptionHandler(){
        return new ApplicationExceptionHandler();
    }
    /**
     * 注册SecurityInterceptor bean
     *
     * @return
     */
    @Bean
    SecurityInterceptor securityInterceptor() {
        return new SecurityInterceptor();
    }

    /**
     * 注册UserDetailsService bean
     *
     * @return
     */
    @Bean
    UserDetailsService securityUserService() {
        return new SecurityUserDetailsService();
    }

    /**
     * 加密方式
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    /**
     * 验证
     * @return
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(securityUserService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    /**
     * 注册JwtAuthenticationTokenFilter bean
     *
     * @return
     * @throws Exception
     */
    @Bean
    public JwtAuthenticationTokenFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationTokenFilter();
    }

    /**
     * 设置securityUserService并注册到配置
     *
     * @param auth
     * @throws Exception
     */
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(securityUserService());
        auth.authenticationProvider(authenticationProvider());
    }

    /**
     * 拦截路径、JwtAuthenticationTokenFilter添加到配置
     *
     * @param httpSecurity
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                //.addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)//添加JwtAuthenticationTokenFilter
                //.addFilterBefore(securityInterceptor(), FilterSecurityInterceptor.class)
                .csrf().disable()//使用的是JWT，我们这里不需要csrf
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)//基于token，所以不需要session
                .and().authorizeRequests()
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/*.html",
                        "/favicon.ico",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js"
                ).permitAll()//允许对于网站静态资源的无授权访问
                .antMatchers("/account/auth/**").permitAll();//允许匿名访问获取token
                //.anyRequest().authenticated();
                /*.and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error")
                .permitAll() //登录页面用户任意访问
                .and()
                .logout().permitAll();*/
        //注销行为任意访问
        //httpSecurity.addFilterBefore(myFilterSecurityInterceptor, FilterSecurityInterceptor.class);
        // 添加JWT filter
    }
}
