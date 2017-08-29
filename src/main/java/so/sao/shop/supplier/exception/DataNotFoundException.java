package so.sao.shop.supplier.exception;

/**
 * @author guangpu.yan
 * @create 2017-08-29
 **/
public class DataNotFoundException extends BusinessException {
    public static final String DEFAULT_MESSAGE = "数据不存在或已被删除";

    public DataNotFoundException() {
        this(DEFAULT_MESSAGE);
    }

    public DataNotFoundException(String message) {
        super(message);
    }
}