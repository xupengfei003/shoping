package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Max;
import javax.validation.constraints.Size;

/**
 * @author gxy on 2017/8/15.
 */
public class NotificationInput {
    /**
     * 消息内容
     */
    @NotBlank(message = "消息内容不能为空")
    @Size(max = 100, min = 1, message = "消息内容字数不能超过100")
    private String notifiDetail;

    /**
     * 消息富文本
     */
    @NotBlank(message = "消息富文本不能为空")
    @Size(max = 255, min = 1, message = "消息富文本字数不能超过255")
    private String comment;

    public String getNotifiDetail() {
        return notifiDetail;
    }

    public void setNotifiDetail(String notifiDetail) {
        this.notifiDetail = notifiDetail;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
