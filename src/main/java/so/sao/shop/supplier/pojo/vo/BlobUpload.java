package so.sao.shop.supplier.pojo.vo;

/**
 * Created by acer on 2017/7/25.
 * 定义上传成功后，返回的结果类，包含文件名称，上传到azure storage后文件的链接，缩略图链接
 */
public class BlobUpload {
    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件的链接
     */
    private String fileUrl;
    /**
     * 缩略图链接
     */
    private String thumbnailUrl;
    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
    public String getFileUrl() {
        return fileUrl;
    }
    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }
    public String getThumbnailUrl() {
        return thumbnailUrl;
    }
    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }
}
