package so.sao.shop.supplier.pojo;

/**
 * 返回对象
 *
 * @author
 * @create 2017-07-15 22:28
 **/
public class Result<T>{
    private Integer code;
    private String message;
    /**
     * 返回数据
     */
    private T data;

    public Result(){}

    public Result(Integer code, String message, T data){
        this.code = code;
        this.message = message;
        this.data = data;
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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
