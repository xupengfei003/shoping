package so.sao.shop.supplier.pojo.job;

public class ProInfoRespTabsContOutput {

    /**
     * 順序
     */
    private int position;

    /**
     * 順序
     */
    private ProInfoRespTabsContDataOutput data;

    /**
     * 內容類型 (TEXT: 文字, IMAGE: 圖片)
     */
    private String type;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ProInfoRespTabsContDataOutput getData() {
        return data;
    }

    public void setData(ProInfoRespTabsContDataOutput data) {
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
