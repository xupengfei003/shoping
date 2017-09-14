package so.sao.shop.supplier.pojo.job;

import java.util.List;

public class ProInfoRespTabsOutput {

    /**
     * 順序
     */
    private int position;

    /**
     * 頁籤內容
     */
    private List<ProInfoRespTabsContOutput> contents;

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

    public List<ProInfoRespTabsContOutput> getContents() {
        return contents;
    }

    public void setContents(List<ProInfoRespTabsContOutput> contents) {
        this.contents = contents;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
