package so.sao.shop.supplier.pojo.vo;

/**
 * Created by acer on 2017/10/9.
 */
public class RefuseImg {
    /**
     * 图片路径
     */
    private String url;
    /**
     * 图片缩略图
     */
    private String minImgUrl;
    /**
     * 图片大小
     */
    private String size;
    /**
     * 图片类型
     */
    private String type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getMinImgUrl() {
        return minImgUrl;
    }

    public void setMinImgUrl(String minImgUrl) {
        this.minImgUrl = minImgUrl;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
