package so.sao.shop.supplier.dao;

import so.sao.shop.supplier.domain.RecordPurchase;

import java.util.List;

/**
 * Created by niewenchao on 2017/8/11.
 */
public interface RecordPurchaseDao {

    /**
     * 批量保存提现申请记录与订单之间的关系
     * @param list RecordPurchase的list
     * @return
     */
    int saveRecordPurchases(List<RecordPurchase> list) throws Exception;

}
