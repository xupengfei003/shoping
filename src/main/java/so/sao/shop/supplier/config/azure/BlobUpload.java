package so.sao.shop.supplier.config.azure;

/**
 * Created by liyanyong on 2017/3/29.
 */
public class BlobUpload {

    /**
     * 文件存储在azure上的名称
     */
    private String fileName;

    /**
     * 不包含域名的相对路径
     */
    private String fileUrl;
    
    /**
     * 合同原文件名
     */
    private String originalFileName;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public BlobUpload(String fileName, String fileUrl) {
        this.fileName = fileName;
        this.fileUrl = fileUrl;
    }

	public String getOriginalFileName() {
		return originalFileName;
	}

	public void setOriginalFileName(String originalFileName) {
		this.originalFileName = originalFileName;
	}
}
