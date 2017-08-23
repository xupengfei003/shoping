package so.sao.shop.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.FeedBackDao;
import so.sao.shop.supplier.domain.Feedback;
import so.sao.shop.supplier.service.FeedbackService;

import java.util.Date;

/**
 * @author gxy on 2017/8/14.
 */
@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedBackDao feedBackDao;
    /**
     * 提交反馈信息
     * @param accountId 供应商ID
     * @param suggest 建议
     * @throws Exception Exception
     */
    @Override
    public void createFeedback(Long accountId, String suggest) throws Exception {
        Feedback feedback = new Feedback();
        feedback.setAccountId(accountId);
        feedback.setCreatedAt(new Date());
        feedback.setSuggest(suggest);
        feedBackDao.insert(feedback);
    }
}
