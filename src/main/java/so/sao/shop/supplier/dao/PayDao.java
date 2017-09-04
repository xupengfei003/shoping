package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Param;

import java.util.Map;

/**
 * Created by acer on 2017/8/15.
 */
public interface PayDao {
    /**
     * 保存支付信息
     * @param payInput
     * @return int
     * @throws Exception
     */
    public abstract boolean save(@Param("payInput") Map<String ,Object> payInput) throws Exception;

}
