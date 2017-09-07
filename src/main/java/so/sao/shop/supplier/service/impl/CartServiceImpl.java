package so.sao.shop.supplier.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.dao.CartItemDao;
import so.sao.shop.supplier.dao.SupplierCommodityDao;
import so.sao.shop.supplier.domain.CartItem;
import so.sao.shop.supplier.domain.SupplierCommodity;
import so.sao.shop.supplier.pojo.input.CartItemInput;
import so.sao.shop.supplier.service.CartService;

import java.util.Date;
import java.util.List;

/**
 * Created by wyy on 2017/7/19.
 */
@Transactional
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartItemDao cartItemDao;

    @Autowired
    private SupplierCommodityDao supplierCommodityDao;


    public static final Integer MORE_REMAINING = -1;//大于库存
    public static final Integer EQUALS_REMAINING = 0;//等于库存


    @Override
    public boolean saveCartItem(CartItemInput cartItemInput) {

        if(cartItemInput == null){
            return false;
        }
        CartItem cartItem = new CartItem();
        cartItem = convertToCartItem(cartItemInput);
        cartItem.setCreatedAt(new Date());
        //查询是否存在相同的商品
        List<CartItem> existsCartItemLit = cartItemDao.findExistsCartItem(cartItem.getUserId(),cartItem.getSupplierId(),cartItem.getCommodityId());
        if(existsCartItemLit!=null&&existsCartItemLit.size()>0){//如果存在，则更新商品数量
            CartItem existsCartItem = existsCartItemLit.get(0);
            CartItem updateCartItem = new CartItem();
            updateCartItem.setId(existsCartItem.getId());
            updateCartItem.setCount(existsCartItem.getCount()+cartItemInput.getCount());
            updateCartItem.setUpdatedAt(new Date());
            return cartItemDao.updateByPrimaryKeySelective(updateCartItem)>0?true:false;
        }else{
            return cartItemDao.insertSelective(cartItem)>0?true:false;
        }
    }

    @Override
    public boolean deleteCartItemById(Long id) {
        return cartItemDao.deleteByPrimaryKey(id)>0?true:false;
    }

    @Override
    public PageInfo<CartItem> findCartItemByUserId(Long userId, int pageNum, int pageSize) {
        PageHelper.startPage(pageNum,pageSize);
        List<CartItem> list = cartItemDao.findCartItemByUserId(userId);
        return new PageInfo<CartItem>(list);
    }

    @Override
    public CartItem findOne(Long id) {
        return cartItemDao.findCartItemById(id);
    }


    @Override
    public boolean deleteCartItemsByIds(List<Long> cartitemIds) {
        return cartItemDao.deleteBatchByPrimaryKey(cartitemIds)>0?true:false;
    }

    @Override
    public Integer updateCartItem(Long cartitemId,Long commodityId,Integer number) {

        //判断库存余量
        SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(commodityId);
        Double inventory = supplierCommodity.getInventory();

        if(number == null || number.intValue() < 1){
            return inventory.intValue();
        }

        if(inventory.doubleValue() > 0){//有库存
            Integer remaining = inventory.intValue();
            if(number > remaining){//购物车更新数大于库存数
                return MORE_REMAINING;
            }
            //购物车数量小于或等于库存数量，更新购物车数量
            CartItem cartItem = new CartItem();
            cartItem.setId(cartitemId);
            cartItem.setCount(number);
            cartItem.setUpdatedAt(new Date());
            cartItemDao.updateByPrimaryKeySelective(cartItem);
            return remaining;
        }else{
            return MORE_REMAINING;
        }
    }



    /**
     * 转换对象
     * @param cartItemInput
     * @return
     */
    private CartItem convertToCartItem(CartItemInput cartItemInput) {
        if(cartItemInput==null){
            return null;
        }
        CartItem cartItem = new CartItem();

        cartItem.setUserId(cartItemInput.getUserId());
        cartItem.setSupplierId(cartItemInput.getSupplierId());
        cartItem.setSupplierName(cartItemInput.getSupplierName());

        cartItem.setCommodityId(cartItemInput.getCommodityId());
        cartItem.setCommodityPrice(cartItemInput.getCommodityPrice());
        cartItem.setCommodityName(cartItemInput.getCommodityName());
        cartItem.setCommodityPic(cartItemInput.getCommodityPic());
        cartItem.setCommodityProperties(cartItemInput.getCommodityProperties());
        cartItem.setCount(cartItemInput.getCount());
        return cartItem;
    }
}
