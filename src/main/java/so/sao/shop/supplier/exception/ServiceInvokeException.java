package so.sao.shop.supplier.exception;

import org.springframework.http.HttpStatus;
import so.sao.shop.supplier.pojo.ErrorResult;
import so.sao.shop.supplier.util.ErrorResultUtils;

/**
 * @author guangpu.yan
 * @create 2017-08-29
 **/
public class ServiceInvokeException extends RuntimeException {
    private static final String DEFAULT_ERROR_MESSAGE = "服务调用失败";

    private HttpStatus status;
    private String body;

    public ServiceInvokeException(HttpStatus status, String body) {
        this(status, DEFAULT_ERROR_MESSAGE, body);
    }

    public ServiceInvokeException(HttpStatus status, String message, String body) {
        super(message);
        this.status = status;
        this.body = body;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getBody() {
        return body;
    }

    public <Detail> ErrorResult<Detail> asErrorResult(Class<Detail> errorDetailClass) {
        return ErrorResultUtils.parseErrorResult(body, errorDetailClass);
    }
}