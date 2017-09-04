package so.sao.shop.supplier.pojo;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author
 * @create 2017-08-29
 **/
public class ErrorResult<Detail> {
    private String code;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Detail detail;

    public ErrorResult(String code, String message) {
        this(code, message, null);
    }

    @JsonCreator
    public ErrorResult(
            @JsonProperty("code") String code,
            @JsonProperty("message") String message,
            @JsonProperty("detail") Detail detail) {
        this.code = code;
        this.message = message;
        this.detail = detail;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Detail getDetail() {
        return detail;
    }
}
