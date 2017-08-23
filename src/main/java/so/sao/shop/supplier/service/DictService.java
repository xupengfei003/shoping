package so.sao.shop.supplier.service;

import so.sao.shop.supplier.domain.DictItem;

import java.util.List;

/**
 * Created by xujc on 2017/7/18.
 * 字典
 */
public interface DictService {

    /**
     * 查看物流
     * @return
     */
    List<DictItem> selectExpress();
}
