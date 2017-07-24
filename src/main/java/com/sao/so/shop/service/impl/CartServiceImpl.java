package com.sao.so.shop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sao.so.shop.dao.CartItemDao;
import com.sao.so.shop.domain.CartItem;
import com.sao.so.shop.pojo.input.CartItemInput;
import com.sao.so.shop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by wyy on 2017/7/19.
 */
@Transactional
@Service
public class CartServiceImpl implements CartService {

    @Autowired
    CartItemDao cartItemDao;

    @Override
    public boolean saveCartItem(CartItemInput cartItemInput) {

        if(cartItemInput == null){
            return false;
        }
        CartItem cartItem = new CartItem();
        cartItem = convertToCartItem(cartItemInput);
        //查询是否存在相同的商品
        CartItem existsCartItem = cartItemDao.findExistsCartItem(cartItem.getUserId(),cartItem.getSupplierId(),cartItem.getCommodityId());
        if(existsCartItem!=null){//如果存在，则更新商品数量
            CartItem updateCartItem = new CartItem();
            updateCartItem.setId(existsCartItem.getId());
            updateCartItem.setCount(existsCartItem.getCount()+cartItemInput.getCount());
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
    public boolean updateCartItem(Long cartitemId, Integer number) {
        if(number==null||number.intValue()<1){
            return false;
        }
        CartItem cartItem = new CartItem();
        cartItem.setId(cartitemId);
        cartItem.setCount(number);
        cartItem.setUpdatedAt(System.currentTimeMillis());
        return cartItemDao.updateByPrimaryKeySelective(cartItem)>0?true:false;
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
        cartItem.setCreatedAt(System.currentTimeMillis());cartItem.setCreatedAt(System.currentTimeMillis());

        return cartItem;
    }
}
