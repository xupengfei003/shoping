package so.sao.shop.supplier.exception;

/**
 * @author guangpu.yan
 * @create 2017-08-29
 **/
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }
}
