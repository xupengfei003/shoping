package so.sao.shop.supplier.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 异常处理
 *
 * @author
 * @create 2017-07-15 21:08
 **/
@ControllerAdvice
public class ApplicationExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public @ResponseBody Result<Object> operateExp(Exception e, HttpServletResponse response) throws IOException {
        logger.error(e.getMessage(),e);

        Result<Object> result = new Result<Object>();
        result.setCode(Constant.CodeConfig.CODE_FAILURE);
        result.setMessage(e.getMessage() == null ? "NullPointerException" : e.getMessage());

        return result;
    }
}
