package so.sao.shop.supplier.service.app;

import java.util.Map;


/**
 * Created by wyy on 2017/7/18.
 */
public interface AppCartService {

    /**
     * 根据购物车记录ID从购物车删除商品
     * @param id
     * @return
     * @throws Exception
     */
    boolean deleteCartItemById(Long id,Long userId) throws Exception;

    /**
     * 修改购物车内商品的数量
     * @param cartitemId
     * @param number
     * @param userId
     * @return
     * @throws Exception
     */
    Map<String,Object> updateCartItem(Long cartitemId, Integer number, Long userId) throws Exception;


    /**
     * 添加商品到购物车
     * @param commodityId
     * @param number
     * @param userId
     * @return
     * @throws Exception
     */
    Map<String,Object> saveCartItem(Long commodityId, Integer number, Long userId) throws Exception;

    /**
     * 查询该用户下的所有购物车记录信息
     * @param userId
     * @return
     * @throws Exception
     */
    Map<String, Object> findCartItemsByUserId(Long userId) throws Exception;
}
