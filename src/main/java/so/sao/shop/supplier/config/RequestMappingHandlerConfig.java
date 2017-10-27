package so.sao.shop.supplier.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import so.sao.shop.supplier.domain.authorized.Permission;
import so.sao.shop.supplier.exception.ApplicationExceptionHandler;
import so.sao.shop.supplier.service.authorized.IPermissionService;

import javax.annotation.PostConstruct;
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
	
	@Autowired
	private IPermissionService permissionService;
	
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
		for (RequestMappingInfo info : mappings) {
			HandlerMethod method = map.get(info);
			String methodstr = method.toString();
			methodstr = methodstr.split("\\(")[0];
			methodstr = methodstr.split(" ")[2];
			int i = methodstr.lastIndexOf(".");
			methodstr = methodstr.substring(0, i);
			String urlparm = info.getPatternsCondition().toString();
			String url = urlparm.substring(1, urlparm.length() - 1);
			
			Permission permission = new Permission();
			permission.setUrl(url);
			permission.setName("test");
			permission.setType(Constant.AuthConfig.PERMISSION_TYPE_INTERFACE);
			//permissionService.startInsert(permission);
        }
    }
}
