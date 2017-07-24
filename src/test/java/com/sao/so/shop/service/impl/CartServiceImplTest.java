package com.sao.so.shop.service.impl;


import com.github.pagehelper.PageInfo;
import com.sao.so.shop.domain.CartItem;
import com.sao.so.shop.pojo.input.CartItemInput;
import com.sao.so.shop.service.CartService;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

/**
 * Created by wyy on 2017/7/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CartServiceImplTest {

    @Autowired
    CartService cartService;




    @Test
    public void saveCartItem() throws Exception {
        CartItemInput cartItemInput = new CartItemInput();
        cartItemInput.setUserId(new Long(1));
        cartItemInput.setCommodityId(new Long(1));
        cartItemInput.setSupplierId(new Long(1));
        cartItemInput.setCommodityPrice(new BigDecimal(0.00));
        cartItemInput.setCount(0);
        System.out.println(" 插入的数据是 "+cartItemInput);
        boolean flag = cartService.saveCartItem(cartItemInput);
        System.out.println("插入结果 "+flag);
    }

    @Test
    public void findCartItemByUserId() throws Exception {
        Long userid = new Long(1);
        Integer pageNum = 1;
        Integer pageSize = 2;
        System.out.println("查询的 userid : "+userid);
        System.out.println("查询的 pageNum : "+pageNum);
        System.out.println("查询的 pageSize : "+pageSize);
        PageInfo<CartItem> pageInfo = cartService.findCartItemByUserId(userid,pageNum,pageSize);
        ObjectMapper objectMapper = new ObjectMapper();
        String str = objectMapper.writeValueAsString(pageInfo);
        System.out.println("查询的结果  : "+str);
    }




    @Test
    public void updateCartItem() throws Exception {
        Long id = new Long(1);
        PageInfo<CartItem> pageInfo = cartService.findCartItemByUserId(new Long(1),1,5);
        List<CartItem> cartItemList =  pageInfo.getList();
        CartItem cartItem = cartItemList.get(0);
        System.out.println("更新前："+cartItem.toString());
        id = cartItem.getId();

        CartItemInput cartItemInput =  new CartItemInput();
        cartItemInput.setId(id);
        Random random = new Random();
        int count = random.nextInt();
        count = Math.abs(count);
        System.out.println("更新的数量为："+count);
        cartItemInput.setCount(count);

        boolean  flag = cartService.updateCartItem(id,count);
        cartItem = cartService.findOne(cartItem.getId());
        System.out.println("更新结果："+flag);
        System.out.println("跟新后:"+cartItem.toString());
    }

    @Test
    public void deleteCartItemById() throws Exception {
        Long id = new Long(1);
        System.out.println("删除使用的 cartitemid ： "+id);
        boolean flag = cartService.deleteCartItemById(id);
        System.out.println("删除结果"+flag);
        CartItem cartItem = cartService.findOne(id);
        System.out.println("反查后结果 ："+cartItem);
    }




}