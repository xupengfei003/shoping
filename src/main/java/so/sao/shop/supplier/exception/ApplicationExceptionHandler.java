package so.sao.shop.supplier.exception;

import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
    @ExceptionHandler(RuntimeException.class)
    public void operateExp(Exception ex, HttpServletResponse response) throws IOException {
        logger.error(ex.getMessage(), ex);
        response.setCharacterEncoding("utf-8");
        response.getWriter().write(new ObjectMapper().writeValueAsString(new Result(Constant.CodeConfig.CODE_FAILURE,Constant.MessageConfig.MSG_FAILURE, ex)));
    }
}
