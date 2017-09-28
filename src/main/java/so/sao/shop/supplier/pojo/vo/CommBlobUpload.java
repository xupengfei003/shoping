package so.sao.shop.supplier.pojo.vo;

/**
 * Created by acer on 2017/7/25.
 * 定义上传成功后，返回的结果类，包含文件名称，上传到azure storage后文件的链接，缩略图链接
 */
public class CommBlobUpload {
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 图片链接
     */
    private String Url;
    /**
     * 缩略图链接
     */
    private String minImgUrl;
    /**
     * 上传图片类型
     */
    private String type;
    /**
     * 上传图片尺寸
     */
    private String size;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }

    public String getMinImgUrl() {
        return minImgUrl;
    }

    public void setMinImgUrl(String minImgUrl) {
        this.minImgUrl = minImgUrl;
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
}
