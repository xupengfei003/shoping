package so.sao.shop.supplier.domain;

public class CommImgeTmp {

    /**
     * ID
     */
    private Long id;
    /**
     * 供应商与商品关系审核表ID
     */
    private Long scaId;
    /**
     * 图片名称
     */
    private String name;
    /**
     * 图片存储路径
     */
    private String url;
    /**
     * 图片格式
     */
    private String type;
    /**
     * 图片尺寸
     */
    private String size;
    /**
     * 缩略图url
     */
    private String thumbnailUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScaId() {
        return scaId;
    }

    public void setScaId(Long scaId) {
        this.scaId = scaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
