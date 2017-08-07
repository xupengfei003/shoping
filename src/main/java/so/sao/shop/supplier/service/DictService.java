package so.sao.shop.supplier.service;

import com.github.pagehelper.PageInfo;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.domain.Condition;
import so.sao.shop.supplier.domain.DictItem;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.output.AccountBalanceOutput;

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
