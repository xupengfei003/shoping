package com.sao.so.shop.web;

import com.sao.so.shop.pojo.input.CartItemInput;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;

import static org.junit.Assert.*;

/**
 * Created by wyy on 2017/7/20.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CartControllerTest {


    @Autowired
    CartController cartController;

    @Test
    public void createCartItems() throws Exception {
        CartItemInput cartItemInput = new CartItemInput();
        cartItemInput.setCommodityId(new Long(1));
        cartItemInput.setSupplierId(new Long(1));
        cartItemInput.setCommodityPrice(new BigDecimal(1));
        cartItemInput.setCount(0);
        System.out.println(" 插入的数据是 "+cartItemInput);
        boolean flag = cartController.createCartItems(cartItemInput);
        System.out.println("插入结果 "+flag);

    }

    @Test
    public void deleteBatchCartItem() throws Exception {
    }

    @Test
    public void deleteCartItem() throws Exception {
    }

    @Test
    public void updateCartItem() throws Exception {
    }

    @Test
    public void getCartItems() throws Exception {
    }


    @Autowired
    private WebApplicationContext context;
    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        //       mvc = MockMvcBuilders.standaloneSetup(new TestController()).build();
        mvc = MockMvcBuilders.webAppContextSetup(context).build();//建议使用这种
    }
    @Test
    public void test1() throws Exception {
        mvc.perform(MockMvcRequestBuilders.get("/cart")
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .param("userId", "1")
                .param("pageNum", "1")
                .param("pageSize", "5")
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.content().string(Matchers.containsString("SUCCESS")));

    }

}