package so.sao.shop.supplier.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConversionException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.ErrorResult;
import so.sao.shop.supplier.pojo.Result;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolationException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 异常处理
 *
 * @author guangpu.yan
 * @create 2017-07-15 21:08
 **/
@RestControllerAdvice
public class ApplicationExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * TODO 二期临时拦截
     * @param e
     * @param response
     * @return
     * @throws IOException
     */
    @ExceptionHandler(Exception.class)
    public  Result<Object> operateExp(Exception e, HttpServletResponse response) throws IOException {
        logger.error(e.getMessage(),e);
        Result<Object> result = new Result<Object>();
        result.setCode(Constant.CodeConfig.CODE_FAILURE);
        result.setMessage(e.getMessage() == null ? "服务器异常" : e.getMessage());
        return result;
    }

    /**
     * 入参校验(TODO 二期临时)
     * @param exception
     * @return
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResult<ModelValidateDetail>> handleBindException(BindException exception) {
        ModelValidateDetail detail = ModelValidateDetailBuilder.from(exception);
        Map<String, List<ValidateError>>  fieldErrors = detail.getFieldErrors();
        String message = fieldErrors.values().stream().findFirst().get().stream().findFirst().get().getMessage();
        ErrorResult<ModelValidateDetail> errorResult = new ErrorResult<>(String.valueOf(Constant.CodeConfig.CODE_FAILURE), message, null);
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    //TODO 三期需要前端配合改进入参校验不通过提示结构
    /*@ExceptionHandler(BindException.class)
    public ResponseEntity<ErrorResult<ModelValidateDetail>> handleBindException(BindException exception) {
        ModelValidateDetail detail = ModelValidateDetailBuilder.from(exception);
        ErrorResult<ModelValidateDetail> errorResult = new ErrorResult<>(String.valueOf(Constant.CodeConfig.CODE_FAILURE), "数据约束不满足", detail);
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResult<ModelValidateDetail>> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException exception) {
        ModelValidateDetail detail = ModelValidateDetailBuilder.from(exception);
        ErrorResult<ModelValidateDetail> errorResult = new ErrorResult<>(ErrorCodes.MODEL_VALIDATE, "数据约束不满足", detail);
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErrorResult<ModelValidateDetail>> handleConstraintViolationException(
            ConstraintViolationException exception) {
        ModelValidateDetail detail = ModelValidateDetailBuilder.from(exception);
        ErrorResult<ModelValidateDetail> errorResult = new ErrorResult<>(ErrorCodes.MODEL_VALIDATE, "数据约束不满足", detail);
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ModelValidateException.class)
    public ResponseEntity<ErrorResult<ModelValidateDetail>> handleModelValidateException(
            ModelValidateException exception) {
        ModelValidateDetail detail = new ModelValidateDetail(exception.getFieldErrors(), exception.getGlobalErrors());
        ErrorResult<ModelValidateDetail> errorResult = new ErrorResult<>(ErrorCodes.MODEL_VALIDATE, "数据约束不满足", detail);
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataConstraintException.class)
    public ResponseEntity<ErrorResult> handleDataConstraintException(DataConstraintException exception) {
        ErrorResult<ModelValidateDetail> errorResult = new ErrorResult<>(ErrorCodes.DATA_CONSTRAINT, exception.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResult> handleDataNotFoundException(DataNotFoundException exception) {
        ErrorResult errorResult = new ErrorResult(ErrorCodes.DATA_NOT_FOUND, exception.getMessage());
        return new ResponseEntity<>(errorResult, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ServiceInvokeException.class)
    public ResponseEntity<String> handleServiceInvokeException(ServiceInvokeException exception) {
        return new ResponseEntity<>(exception.getBody(), exception.getStatus());
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class, HttpMessageConversionException.class})
    public ResponseEntity<ErrorResult> handleMethodArgumentTypeMismatchException(Exception exception) {
        ErrorResult errorResult = new ErrorResult(ErrorCodes.INVALID_REQUEST, "非法请求");
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(errorResult, HttpStatus.BAD_REQUEST);
    }

    //TODO 三期替换掉上面的Exception拦截方法
    /*@ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResult> handleException(Exception exception) {
        String message = exception.getMessage();
        if (message == null) {
            message = "服务器异常";
        }
        ErrorResult errorResult = new ErrorResult(ErrorCodes.SERVICE_INVOKE_ERROR, message);
        logger.error(exception.getMessage(), exception);
        return new ResponseEntity<>(errorResult, HttpStatus.INTERNAL_SERVER_ERROR);
    }*/
}
