package so.sao.shop.supplier.pojo.job;

import java.util.List;

public class ProListRespOutput {

    /**
     * 商品列表
     */
    private List<ProListRespDataOutput> data;

    /**
     * 分頁資訊
     */
    private ProListRespPagingOutput paging;

    public List<ProListRespDataOutput> getData() {
        return data;
    }

    public void setData(List<ProListRespDataOutput> data) {
        this.data = data;
    }

    public ProListRespPagingOutput getPaging() {
        return paging;
    }

    public void setPaging(ProListRespPagingOutput paging) {
        this.paging = paging;
    }
}
