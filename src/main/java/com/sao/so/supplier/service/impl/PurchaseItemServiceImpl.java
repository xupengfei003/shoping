package com.sao.so.supplier.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sao.so.supplier.config.Constant;
import com.sao.so.supplier.dao.PurchaseItemDao;
import com.sao.so.supplier.domain.PurchaseItem;
import com.sao.so.supplier.pojo.output.RecordToPurchaseOutput;
import com.sao.so.supplier.pojo.vo.AccountPurchaseItemVo;
import com.sao.so.supplier.service.PurchaseItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by niewenchao on 2017/7/19.
 */
@Service
public class PurchaseItemServiceImpl implements PurchaseItemService {

    @Autowired
    PurchaseItemDao purchaseItemDao;

    /**
     * 根据订单编号查询所有相关的订单明细记录（分页）
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @param orderId 订单编号
     * @return 相关记录的集合
     */
    @Override
    public RecordToPurchaseOutput searchPurchaseItems(Integer pageNum, Integer pageSize, Long orderId) {
        /**
         * 1.判断参数是否为空，是否合法
         *   ①.判断当前页码和每页显示条数是否为空，为空赋予默认值，否则继续执行
         *   ②.判断订单编号orderId 是否存在，存在继续执行，否则结束业务，返回错误代码
         * 2.访问持久化层，获取数据集合
         *   ①.使用分页插件设置分页
         *   ②.访问持久化层方法获取数据
         * 3.将获取到的数据集合中的泛型转化成vo层的类对象
         *   ①.将获取到的数据集合使用循环方法依次放入另一个集合中
         * 4.将转化后的数据集合封装到PageInfo对象中
         *   ①.封装PageInfo时要封装：当前页码，每页显示条数，数据集合
         * 5.将PageInfo对象封装到output类中返回
         */
        RecordToPurchaseOutput output = new RecordToPurchaseOutput();//出参对象

        //1.1判断当前页码和每页显示条数是否为空
        if (null==pageNum){
            pageNum = 1;
        }
        if (null==pageSize){
            pageSize = 10;
        }

        //分页
        PageHelper.startPage(pageNum,pageSize);

        //访问持久化层获取数据
        List<PurchaseItem> purchaseItemList = purchaseItemDao.findPage(orderId);

        if(null != purchaseItemList && !purchaseItemList.isEmpty()){

            PageInfo<PurchaseItem> pageInfo = new PageInfo<>(purchaseItemList);

            //3.将获得的list集合中的对象全部转化成vo层的对象
            List<AccountPurchaseItemVo> purchaseItemVos = this.inversion(purchaseItemList);

            PageInfo<AccountPurchaseItemVo> pageInfoVo = new PageInfo<>();
            pageInfoVo.setPageNum(pageNum);
            pageInfoVo.setPageSize(pageSize);
            pageInfoVo.setTotal(pageInfo.getTotal());
            pageInfoVo.setPages(pageInfo.getPages());
            pageInfoVo.setSize(pageInfo.getSize());
            pageInfoVo.setList(purchaseItemVos);

            //4.将转化后的数据集合封装到PageInfo对象中,封装成功信息和code
            output.setCode(Constant.CodeConfig.CODE_SUCCESS);
            output.setMessage(Constant.MessageConfig.MSG_SUCCESS);
            output.setPageInfo(pageInfoVo);
        } else {
            output.setCode(Constant.CodeConfig.CODE_NOT_FOUND_RESULT);
            output.setMessage(Constant.MessageConfig.MSG_NOT_FOUND_RESULT);
            output.setPageInfo(null);
        }
        return output;
    }


    /**
     * 转化集合中的泛型
     * @param purchaseItemList 转化前的集合
     * @return 转化后的集合
     */
    private List<AccountPurchaseItemVo> inversion(List<PurchaseItem> purchaseItemList) {
        List<AccountPurchaseItemVo> purchaseItemVos = new ArrayList<>();//存储转化后泛型的集合
        AccountPurchaseItemVo purchaseItemVo = null;//要转化的泛型对象
        if (null != purchaseItemList && !purchaseItemList.isEmpty()) {
            for (PurchaseItem purchaseItem:purchaseItemList) {
                //循环转化数据
                purchaseItemVo = new AccountPurchaseItemVo();
                purchaseItemVo.setGoodsId(purchaseItem.getGoodsId());//商品编号
                purchaseItemVo.setGoodsName(purchaseItem.getGoodsName());//商品名称
                purchaseItemVo.setGoodsNumber(purchaseItem.getGoodsNumber());//商品数量
                purchaseItemVo.setGoodsUnitPrice(purchaseItem.getGoodsUnitPrice());//商品单价
                purchaseItemVos.add(purchaseItemVo);//将转化后的数据添加到集合中
            }
        }
        return purchaseItemVos;
    }

}
