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
        //查询详情信息
//        List<AppPurchaseItemVo> appPurchaseItemVoList = appPurchaseItemDao.findOrderItemListByOrderId(orderId);
        //查询订单信息
        AppPurchasesVo appPurchasesVos = appPurchaseDao.findOrderByOrderId(orderId);
        //根据合并支付ID查询所有订单
        List<AppPurchasesVo> appPurchasesVoList = appPurchaseDao.findOrderByPayId(appPurchasesVos.getPayId());
        //获取订单ID集合
        List<String> orderIdList = new ArrayList<>();
        for(AppPurchasesVo appPurchasesVo : appPurchasesVoList){
            orderIdList.add(appPurchasesVo.getOrderId());
        }
        //根据订单ID列表查询订单详情
        List<AppPurchaseItemVo> appPurchaseItemVoList = appPurchaseItemDao.findOrderItemList(orderIdList);

        AppPurchaseItemOutput appPurchaseItemOutput = null;
        if(null != appPurchasesVos){
            appPurchaseItemOutput = BeanMapper.map(appPurchasesVos,AppPurchaseItemOutput.class);
            if(appPurchasesVos.getOrderStatus() == 1){
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
                appPurchaseItemVo.setStoreName(appPurchasesVos.getStoreName());
                appPurchaseItemVo.setStoreId(appPurchasesVos.getStoreId());
                appPurchaseItemVo.setUserId(appPurchasesVos.getUserId());
                appPurchaseItemVo.setUserName(appPurchasesVos.getUserName());
            }
            appPurchaseItemOutput.setOrderPrice(String.valueOf(goodsAllPrice));
            appPurchaseItemOutput.setAppPurchaseItemVos(appPurchaseItemVoList);
        }
        return appPurchaseItemOutput;
    }
}
