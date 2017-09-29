package so.sao.shop.supplier.pojo.job;

import java.math.BigDecimal;

public class ProListOutput {

    /**
     * 查詢結果success成功 fail 失敗
     */
    private String status;

    /**
     * 回傳資料
     */
    private ProListRespOutput response;

    /**
     * 錯誤訊息
     */
    private String message;

    /**
     * 錯誤代碼
     */
    private String code;

    /**
     * 响应时间
     */
    private BigDecimal server_time_consumed;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ProListRespOutput getResponse() {
        return response;
    }

    public void setResponse(ProListRespOutput response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public BigDecimal getServer_time_consumed() {
        return server_time_consumed;
    }

    public void setServer_time_consumed(BigDecimal server_time_consumed) {
        this.server_time_consumed = server_time_consumed;
    }
}
