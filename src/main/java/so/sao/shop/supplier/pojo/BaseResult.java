package so.sao.shop.supplier.pojo;

/**
 * <p>
 *  父类返回结果
 * </p>
 *
 * @author 透云-中软-西安项目组-wh
 * @since 2017-07-19
 */
public class BaseResult {
    public Integer code;//状态码
    public String message;//文字说明

    public BaseResult(){}

    public BaseResult(Integer code, String message){
        this.code = code;
        this.message = message;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
