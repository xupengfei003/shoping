package so.sao.shop.supplier.pojo.job;

public class ProListRespPagingOutput {

    /**
     * 前一頁
     */
    private ProListRespPagingPreviousOutput previous;

    /**
     * 後一頁
     */
    private ProListRespPagingNextOutput next;

    public ProListRespPagingPreviousOutput getPrevious() {
        return previous;
    }

    public void setPrevious(ProListRespPagingPreviousOutput previous) {
        this.previous = previous;
    }

    public ProListRespPagingNextOutput getNext() {
        return next;
    }

    public void setNext(ProListRespPagingNextOutput next) {
        this.next = next;
    }
}
