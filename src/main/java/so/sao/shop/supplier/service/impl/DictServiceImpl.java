package so.sao.shop.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.DictItemMapper;
import so.sao.shop.supplier.domain.DictItem;
import so.sao.shop.supplier.service.DictService;
import java.util.*;

/**
 * 字典
 * @author guangpu.yan
 * @create 2017-07-10 16:31
 **/
@Service
public class DictServiceImpl implements DictService {

    @Autowired
    private DictItemMapper dictItemMapper;

    @Override
    public List<DictItem> selectExpress() {
        return dictItemMapper.selectExpress();
    }
}