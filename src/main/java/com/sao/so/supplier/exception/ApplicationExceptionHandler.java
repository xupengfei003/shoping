package com.sao.so.supplier.exception;

import com.sao.so.supplier.pojo.BaseResult;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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
        response.getWriter().write(new ObjectMapper().writeValueAsString(new BaseResult(0,"系统异常")));
    }

}
