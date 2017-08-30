package so.sao.shop.supplier.pojo;

import so.sao.shop.supplier.config.Constant;

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

    /**
     * 失败时返回结构
     * @param msg
     * @return
     */
    public static Result fail(String msg){
        return new Result(Constant.CodeConfig.CODE_FAILURE, msg, null);
    }

    /**
     * 失败时返回结构
     * @param msg
     * @return
     */
    public static Result fail(String msg, Object data){
        return new Result(Constant.CodeConfig.CODE_FAILURE, msg, data);
    }

    /**
     * 成功时返回结构
     * @param msg
     * @return
     */
    public static Result success(String msg){
        return new Result(Constant.CodeConfig.CODE_SUCCESS, msg, null);
    }

    /**
     * 成功时返回结构
     * @param msg
     * @param data
     * @return
     */
    public static Result success(String msg, Object data){
        return new Result(Constant.CodeConfig.CODE_SUCCESS, msg, data);
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
