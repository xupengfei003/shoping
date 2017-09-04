package so.sao.shop.supplier.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.util.StringUtils;
import so.sao.shop.supplier.pojo.ErrorResult;

import java.io.IOException;

/**
 * @author guangpu.yan
 * @create 2017-08-29
 **/
public class ErrorResultUtils {
    public static String tryGetErrorCode(String errorJson) {
        if (StringUtils.isEmpty(errorJson))
            return null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            ErrorResult result = objectMapper.readValue(errorJson, ErrorResult.class);
            return result.getCode();
        } catch (IOException e) {
            return null;
        }
    }

    public static <Detail> ErrorResult<Detail> parseErrorResult(String errorJson, Class<Detail> detailClass) {
        if (StringUtils.isEmpty(errorJson))
            return null;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            JsonNode jsonNode = objectMapper.readTree(errorJson);
            JsonNode codeNode = jsonNode.get("code");
            if (codeNode == null)
                throw new RuntimeException();
                //throw new ErrorParseException();
            JsonNode messageNode = jsonNode.get("message");
            JsonNode detailNode = jsonNode.get("detail");
            Detail detail = detailNode == null ? null : objectMapper.treeToValue(detailNode, detailClass);
            return new ErrorResult<>(
                    codeNode.asText(),
                    messageNode == null ? null : messageNode.asText(),
                    detail);
        } catch (IOException e) {
            throw new RuntimeException();
            //throw new ErrorParseException();
        }
    }
}
