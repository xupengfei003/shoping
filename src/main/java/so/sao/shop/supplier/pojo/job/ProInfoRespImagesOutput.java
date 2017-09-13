package so.sao.shop.supplier.pojo.job;

public class ProInfoRespImagesOutput {

    /**
     * 順序
     */
    private int position;

    /**
     * 是否為主圖
     */
    private boolean primary;

    /**
     * 圖片連結
     */
    private String url;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public boolean isPrimary() {
        return primary;
    }

    public void setPrimary(boolean primary) {
        this.primary = primary;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
