package so.sao.shop.supplier.domain;

import java.util.Date;

/**
 * 二维码
 *
 * @author hengle.yang
 * @since 2017/8/11 9:55
 */
public class Qrcode {

    /**
     * 二维码ID
     */
    private String qrcodeId;

    /**
     * 外键ID
     */
    private String foreignKey;

    /**
     * 二维码路径
     */
    private String url;

    /**
     * 二维码内容
     */
    private String content;

    /**
     * 二维码状态
     */
    private Integer status;

    /**
     * 生成时间
     */
    private Date createdAt;

    /**
     * 修改时间
     */
    private Date updatedAt;

    /**
     * 失效时间
     */
    private Date disabledAt;

    /**
     * 二维码宽度
     */
    private Double width;

    /**
     * 二维码高度
     */
    private Double height;

    public String getQrcodeId() {
        return qrcodeId;
    }

    public void setQrcodeId(String qrcodeId) {
        this.qrcodeId = qrcodeId;
    }

    public String getForeignKey() {
        return foreignKey;
    }

    public void setForeignKey(String foreignKey) {
        this.foreignKey = foreignKey;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getDisabledAt() {
        return disabledAt;
    }

    public void setDisabledAt(Date disabledAt) {
        this.disabledAt = disabledAt;
    }

    public Double getWidth() {
        return width;
    }

    public void setWidth(Double width) {
        this.width = width;
    }

    public Double getHeight() {
        return height;
    }

    public void setHeight(Double height) {
        this.height = height;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Qrcode qrcode = (Qrcode) o;

        if (qrcodeId != null ? !qrcodeId.equals(qrcode.qrcodeId) : qrcode.qrcodeId != null) return false;
        if (foreignKey != null ? !foreignKey.equals(qrcode.foreignKey) : qrcode.foreignKey != null) return false;
        if (url != null ? !url.equals(qrcode.url) : qrcode.url != null) return false;
        if (content != null ? !content.equals(qrcode.content) : qrcode.content != null) return false;
        if (status != null ? !status.equals(qrcode.status) : qrcode.status != null) return false;
        if (createdAt != null ? !createdAt.equals(qrcode.createdAt) : qrcode.createdAt != null) return false;
        if (updatedAt != null ? !updatedAt.equals(qrcode.updatedAt) : qrcode.updatedAt != null) return false;
        if (disabledAt != null ? !disabledAt.equals(qrcode.disabledAt) : qrcode.disabledAt != null) return false;
        if (width != null ? !width.equals(qrcode.width) : qrcode.width != null) return false;
        return height != null ? height.equals(qrcode.height) : qrcode.height == null;
    }

    @Override
    public int hashCode() {
        int result = qrcodeId != null ? qrcodeId.hashCode() : 0;
        result = 31 * result + (foreignKey != null ? foreignKey.hashCode() : 0);
        result = 31 * result + (url != null ? url.hashCode() : 0);
        result = 31 * result + (content != null ? content.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (createdAt != null ? createdAt.hashCode() : 0);
        result = 31 * result + (updatedAt != null ? updatedAt.hashCode() : 0);
        result = 31 * result + (disabledAt != null ? disabledAt.hashCode() : 0);
        result = 31 * result + (width != null ? width.hashCode() : 0);
        result = 31 * result + (height != null ? height.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Qrcode{" +
                "qrcodeId='" + qrcodeId + '\'' +
                ", foreignKey='" + foreignKey + '\'' +
                ", url='" + url + '\'' +
                ", content='" + content + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", disabledAt=" + disabledAt +
                ", width=" + width +
                ", height=" + height +
                '}';
    }
}
