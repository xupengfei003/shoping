package so.sao.shop.supplier.exception;

/**
 * @author guangpu.yan
 * @create 2017-08-29
 **/
public class ValidateError {
    private String errorCode;
    private String message;

    public ValidateError() {
    }

    public ValidateError(String errorCode, String message) {
        this.errorCode = errorCode;
        this.message = message;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}