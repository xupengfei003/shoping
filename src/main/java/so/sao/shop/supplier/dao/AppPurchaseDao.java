package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.pojo.vo.AppPurchasesVo;

import java.math.BigInteger;
import java.util.List;

/**
 * Created by acer on 2017/9/6.
 */
public interface AppPurchaseDao {
    /**
     * 根据订单状态查询订单列表
     *
     * @param orderStatus 订单状态
     * @return List<AppPurchaseItemVo> 订单列表
     * @throws Exception 异常
     */
    List<AppPurchasesVo> findOrderList(@Param("userId") BigInteger userId, @Param("orderStatus") Integer orderStatus) throws Exception;
}
