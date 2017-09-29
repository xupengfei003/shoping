package so.sao.shop.supplier.pojo.job;

public class ProListRespPagingPreviousOutput {

    /**
     * 取得筆數 (預設20,最多100)
     */
    private int limit;

    /**
     * 游標開頭
     */
    private String before;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getBefore() {
        return before;
    }

    public void setBefore(String before) {
        this.before = before;
    }
}
