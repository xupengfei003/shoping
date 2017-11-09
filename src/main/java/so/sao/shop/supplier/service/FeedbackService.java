package so.sao.shop.supplier.service;

/**
 * @author gxy on 2017/8/14.
 */
public interface FeedbackService {
    /**
     * 提交反馈信息
     *
     * @param accountId 供应商ID
     * @param suggest   建议
     * @throws Exception Exception
     */
    void createFeedback(Long accountId, String suggest) throws Exception;

}
