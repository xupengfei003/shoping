package com.sao.so.shop.config;

import com.sao.so.shop.exception.ApplicationExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * 扫描请求路径入库配置
 *
 * @author
 * @create 2017-07-11 9:26
 **/
@Configuration
public class RequestMappingHandlerConfig {
    @Bean
    public ApplicationExceptionHandler applicationExceptionHandler(){
        return new ApplicationExceptionHandler();
    }
    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        RequestMappingHandlerMapping mapping = new RequestMappingHandlerMapping();
        return mapping;
    }

    /**
     * 扫描请求路径入库
     */
    @PostConstruct
    public void detectHandlerMethods() {
        final RequestMappingHandlerMapping requestMappingHandlerMapping = requestMappingHandlerMapping();
        Map<RequestMappingInfo, HandlerMethod> map = requestMappingHandlerMapping.getHandlerMethods();
        Set<RequestMappingInfo> mappings = map.keySet();
        System.out.println(mappings);
        Map<String, String> reversedMap = new HashMap<String, String>();
        /* for(RequestMappingInfo info : mappings) {
               HandlerMethod method = map.get(info);
               String methodstr = method.toString();
               methodstr = methodstr.split("\\(")[0];
               methodstr = methodstr.split(" ")[2];
               int i=methodstr.lastIndexOf(".");
               methodstr = methodstr.substring(0,i);
               String urlparm = info.getPatternsCondition().toString();
               String url = urlparm.substring(1, urlparm.length()-1);
               List<SysResource> list = sresourceService.findByResourceString(url);
               if(list==null || list.size()<=0){
                   int num = (int)(Math.random()*100+1);
                   String rand = String.valueOf(num);
                   String resourceId = "res"+System.currentTimeMillis()+rand;
                   SysResource sysresource = new SysResource();
                   sysresource.setResourceString(url);
                   sysresource.setRemark("0");
                   sysresource.setResourceId(resourceId);
                   sysresource.setMethodPath(methodstr);
                   sresourceService.save(sysresource);
                   System.out.println ("===>"+url);
               }
        }*/
    }
}
