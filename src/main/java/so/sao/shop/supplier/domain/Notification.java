package so.sao.shop.supplier.domain;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

/**
 * @author gxy on 2017/8/14.
 */
public class Notification {

    /**
     * 消息通知ID
     */
    private Integer notifiId;

    /**
     * 账户ID
     */
    private Long accountId;

    /**
     * 消息类型 0订单，1系统，2合同消息
     */
    private Integer notifiType;

    /**
     * 订单ID
     */
    private String orderId;

    /**
     * 消息内容
     */
    private String notifiDetail;

    /**
     * 消息富文本
     */
    private String comment;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createdAt;

    /**
     * 操作标记作-为管理员删除系统消息时使用
     */
    private String sigin;
    /**
     * 消息状态 0未读,1已读
     */
    private Integer notifiStatus;

    /**
     * notifiDetail(逗号前的内容)
     */
    private String title;

    /**
     * /**
     * notifiDetail(逗号后的内容)
     */
    private String content;

    public Notification() {
    }

    public Notification(Long accountId, Integer notifiType, String orderId, String notifiDetail, Date createdAt, Integer notifiStatus) {
        this.notifiId = notifiId;
        this.accountId = accountId;
        this.notifiType = notifiType;
        this.orderId = orderId;
        this.notifiDetail = notifiDetail;
        this.createdAt = createdAt;
        this.notifiStatus = notifiStatus;
    }

    public Integer getNotifiId() {
        return notifiId;
    }

    public void setNotifiId(Integer notifiId) {
        this.notifiId = notifiId;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public Integer getNotifiType() {
        return notifiType;
    }

    public void setNotifiType(Integer notifiType) {
        this.notifiType = notifiType;
    }

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

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getSigin() {
        return sigin;
    }

    public void setSigin(String sigin) {
        this.sigin = sigin;
    }

    public Integer getNotifiStatus() {
        return notifiStatus;
    }

    public void setNotifiStatus(Integer notifiStatus) {
        this.notifiStatus = notifiStatus;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
