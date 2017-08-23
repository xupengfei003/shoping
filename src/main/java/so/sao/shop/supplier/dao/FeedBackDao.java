package so.sao.shop.supplier.dao;

import so.sao.shop.supplier.domain.Feedback;

/**
 * @author gxy on 2017/8/14.
 */
public interface FeedBackDao {

    /**
     * 单次添加反馈建议
     * @param feedback
     * @return int
     */
    int insert(Feedback feedback);
}
