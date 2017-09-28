package so.sao.shop.supplier.pojo.job;

public class ProListRespPagingNextOutput {

    /**
     * 取得筆數 (預設20,最多100)
     */
    private int limit;

    /**
     * 游標結尾
     */
    private String after;

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }

    public String getAfter() {
        return after;
    }

    public void setAfter(String after) {
        this.after = after;
    }
}
