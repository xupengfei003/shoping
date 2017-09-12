package so.sao.shop.supplier.service;

import com.github.pagehelper.PageInfo;
import so.sao.shop.supplier.domain.AppCartItem;
import so.sao.shop.supplier.pojo.input.AppCartItemInput;
import so.sao.shop.supplier.pojo.output.AppCartItemOut;

import java.util.List;

/**
 * Created by wyy on 2017/7/18.
 */
public interface AppCartService {

    /**
     * 添加商品到购物车
     */
    public boolean saveCartItem(AppCartItemInput appCartItemInput);


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
    public PageInfo<AppCartItem> findCartItemByUserId(Long userId, int pageNum, int pageSize);

    /**
     * 根据商品ID查找信息
     * @param id
     * @return
     */
    public AppCartItem findOne(Long id);

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
     * @return
     */
    AppCartItem updateCartItem(Long cartitemId,Long commodity, Integer number);

    /**
     * 查询该用户下的所有信息
     * @param id
     * @return
     */
    List<AppCartItemOut> findCartItemsByUserId(Long userId);
}
