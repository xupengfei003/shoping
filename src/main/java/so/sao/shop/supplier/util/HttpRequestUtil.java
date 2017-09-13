package so.sao.shop.supplier.util;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

/**
 * 发起http请求并获取结果 
 * @author zyz
 * @date 20140522
 *
 */
public class HttpRequestUtil {

    private static RestTemplate restTemplate = new RestTemplate();

    /**
     * 获取请求接口返回数据
     * @param requestUrl
     * @return
     */
    public static String httpRequest(String requestUrl){
        String json = "";
        try {
            HttpHeaders headers = new HttpHeaders();
            HttpEntity<Object> entity = new HttpEntity<>(headers);
            ResponseEntity<String> responseEntity = restTemplate.exchange(requestUrl, HttpMethod.GET, entity, String.class);

            if(responseEntity.getBody() != null){
                return responseEntity.getBody();
            }
        }catch (Exception e){
            e.getStackTrace();
        }
        return json;
    }
}