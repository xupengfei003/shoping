package so.sao.shop.supplier.pojo;

/**
 * 返回对象
 *
 * @author
 * @create 2017-07-15 22:28
 **/
public class Result<T>{
    private Integer code;
    private String msg;
    /**
     * 返回数据
     */
    private T data;

    public Result(){}

    public Result(Integer code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
