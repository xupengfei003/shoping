package so.sao.shop.supplier.pojo.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class CommImgeUpdateVo {

    /**
     * 图片名称
     */
    @Length(max = 500,message = "图片名称长度不能大于500")
    private String name;

    /**
     * 图片存储路径
     */
    @NotBlank(message = "图片url不能为空")
    @Length(max = 500,message = "图片url长度不能大于500")
    private String url;

    /**
     * 图片格式
     */
    @Length(max = 50,message = "图片格式长度不能大于50")
    private String type;

    /**
     * 图片尺寸
     */
    @Length(max = 50,message = "图片尺寸长度不能大于50")
    private String size;

    /**
     * 图片缩略图url
     */
    @NotBlank(message = "图片缩略图url不能为空")
    @Length(max = 500,message = "图片缩略图url长度不能大于500")
    private String thumbnailUrl;

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
