package so.sao.shop.supplier.service.app.impl;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import so.sao.shop.supplier.dao.app.AppPurchaseDao;
import so.sao.shop.supplier.dao.app.AppPurchaseItemDao;
import so.sao.shop.supplier.pojo.output.AppPurchaseItemOutput;
import so.sao.shop.supplier.pojo.vo.AppPurchaseItemVo;
import so.sao.shop.supplier.pojo.vo.AppPurchasesVo;
import so.sao.shop.supplier.service.app.AppPurchaseItemService;
import so.sao.shop.supplier.util.BeanMapper;
import so.sao.shop.supplier.util.NumberUtil;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by acer on 2017/9/7.
 */
@Service
public class AppPurchaseItemServiceImpl implements AppPurchaseItemService {
    @Resource
    private AppPurchaseItemDao appPurchaseItemDao;
    @Resource
    private AppPurchaseDao appPurchaseDao;

    /**
     * 根据订单ID查询订单详情
     *
     * @param orderId 订单ID
     * @return AppPurchaseItemVo 订单信息
     * @throws Exception 异常
     */
    @Override
    public AppPurchaseItemOutput findOrderItemList(String orderId) throws Exception {
        /**
         * 1.如果查询待付款订单详情用合并编号查询
         * 2.如果查询其他状态订单详情则用订单编号查询
         */
        List<AppPurchasesVo> appPurchasesVoList = new ArrayList<>();
        AppPurchasesVo appPurchasesVos = new AppPurchasesVo();
        Integer orderStatus = 0;
        if(orderId.length() == 28){
            //根据合并支付ID查询所有订单
            appPurchasesVoList = appPurchaseDao.findOrderByPayId(orderId);
            appPurchasesVos = appPurchasesVoList.get(0);
            orderStatus = 1;
        }else{
            //查询订单信息
            appPurchasesVos = appPurchaseDao.findOrderByOrderId(orderId);
        }

        //获取订单ID集合
        List<String> orderIdList = new ArrayList<>();
        for(AppPurchasesVo appPurchasesVo : appPurchasesVoList){
            orderIdList.add(appPurchasesVo.getOrderId());
        }

        List<AppPurchaseItemVo> appPurchaseItemVoList = new ArrayList<>();
        //业务逻辑
        //1.如果订单状态为待付款的时候（订单状态为1），则把payID相同的订单列出；
        //2.如果订单状态为其他时，则只列出orderId对应的订单
        if(orderStatus == 1){
            //根据订单ID列表查询订单详情
            appPurchaseItemVoList = appPurchaseItemDao.findOrderItemList(orderIdList);
        }else{
            //查询详情信息
            appPurchaseItemVoList = appPurchaseItemDao.findOrderItemListByOrderId(orderId);
        }
        AppPurchaseItemOutput appPurchaseItemOutput = null;
        if(null != appPurchasesVos){
            appPurchaseItemOutput = BeanMapper.map(appPurchasesVos,AppPurchaseItemOutput.class);
            if(orderStatus == 1){
                appPurchaseItemOutput.setOrderId(appPurchasesVos.getPayId());
            }
            //输出运费
            //1.如果运费为0，则显示“包邮”
            //2.如果有运费，则输出实际金额的千分值
            if(appPurchasesVos.getOrderPostage().compareTo(new BigDecimal(0)) == 0){
                appPurchaseItemOutput.setOrderPostage("包邮");
            } else {
                appPurchaseItemOutput.setOrderPostage(NumberUtil.number2Thousand(appPurchasesVos.getOrderPostage()));
            }
        }

        if(null!=appPurchaseItemVoList && appPurchaseItemVoList.size()>0){
            BigDecimal goodsAllPrice = new BigDecimal(0);//当查询订单状态为1时，计算该订单下所有商品总价
            for(AppPurchaseItemVo appPurchaseItemVo : appPurchaseItemVoList){
                //计算总价
                BigDecimal goodsNum = new BigDecimal(appPurchaseItemVo.getGoodsNumber());
                String goodsUnit = appPurchaseItemVo.getGoodsUnitPrice().replaceAll(",","");
                goodsAllPrice = goodsAllPrice.add(goodsNum.multiply(new BigDecimal(goodsUnit)));
                //将订单信息赋值给详情
               /* appPurchaseItemVo.setStoreName(appPurchasesVos.getStoreName());
                appPurchaseItemVo.setStoreId(appPurchasesVos.getStoreId());
                appPurchaseItemVo.setUserId(appPurchasesVos.getUserId());
                appPurchaseItemVo.setUserName(appPurchasesVos.getUserName());*/
            }
            appPurchaseItemOutput.setOrderPrice(String.valueOf(goodsAllPrice));
            if(orderStatus == 1){
                appPurchaseItemOutput.setOrderId(appPurchasesVos.getPayId());
            }
            appPurchaseItemOutput.setAppPurchaseItemVos(appPurchaseItemVoList);
        }
        return appPurchaseItemOutput;
    }
}
