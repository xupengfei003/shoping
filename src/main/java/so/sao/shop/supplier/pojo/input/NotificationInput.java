package so.sao.shop.supplier.pojo.input;

import javax.validation.constraints.NotNull;

/**
 * @author gxy on 2017/8/15.
 */
public class NotificationInput {
    /**
     * 消息内容
     */
    @NotNull(message = "消息内容不能为空")
    private String notifiDetail;

    /**
     * 消息富文本
     */
    @NotNull(message = "消息富文本不能为空")
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
