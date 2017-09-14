package so.sao.shop.supplier.util;

import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 根据URL获取输入流
     * @param urlFile
     * @return
     */
    public static InputStream getInputStream(String urlFile) {
        HttpHeaders headers = new HttpHeaders();
        InputStream inputStream;
        ResponseEntity<byte[]> response = restTemplate.exchange(
                urlFile,
                HttpMethod.GET,
                new HttpEntity<byte[]>(headers),
                byte[].class);
        byte[] result = response.getBody();
        inputStream = new ByteArrayInputStream(result);
        try {
            inputStream.close();
        } catch (IOException e) {
            e.getStackTrace();
        }
        return inputStream;
    }

}