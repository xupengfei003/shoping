package so.sao.shop.supplier.service;

import com.github.pagehelper.PageInfo;
import so.sao.shop.supplier.domain.CartItem;
import so.sao.shop.supplier.pojo.input.CartItemInput;

import java.util.List;

/**
 * Created by wyy on 2017/7/18.
 */
public interface CartService {

    /**
     * 添加商品到购物车
     */
    public boolean saveCartItem(CartItemInput cartItemInput);


    /**
     * 根据ID从购物车删除商品
     */
    public boolean deleteCartItemById(Long id);

    /**
     * 根据用户ID查询购物车商品
     * @param userId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public PageInfo<CartItem> findCartItemByUserId(Long userId, int pageNum, int pageSize);

    /**
     * 根据商品ID查找信息
     * @param id
     * @return
     */
    public CartItem findOne(Long id);

    /**
     * 根据id批量删除
     * @param cartitemIds
     * @return
     */
    boolean deleteCartItemsByIds(List<Long> cartitemIds);

    /**
     * 修改购物车内商品的数量
     * @param cartitemId
     * @param commodity
     * @param number
     * @return 库存数量
     */
    Integer updateCartItem(Long cartitemId,Long commodity, Integer number);

}
