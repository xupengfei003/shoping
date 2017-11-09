package so.sao.shop.supplier.service.app.impl;

import com.github.pagehelper.PageInfo;
import org.apache.commons.collections4.list.TreeList;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.app.AppPurchaseDao;
import so.sao.shop.supplier.dao.app.AppPurchaseItemDao;
import so.sao.shop.supplier.pojo.output.AppPurchaseOutput;
import so.sao.shop.supplier.pojo.vo.AppPurchaseItemVo;
import so.sao.shop.supplier.pojo.vo.AppPurchaseShipMethodVo;
import so.sao.shop.supplier.pojo.vo.AppPurchasesVo;
import so.sao.shop.supplier.service.app.AppPurchaseService;
import so.sao.shop.supplier.util.BeanMapper;
import so.sao.shop.supplier.util.NumberUtil;
import so.sao.shop.supplier.util.PageTool;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by bzh on 2017/9/6.
 */
@Service
public class AppPurchaseServiceImpl implements AppPurchaseService {
    @Resource
    private AppPurchaseDao appPurchaseDao;
    @Resource
    private AppPurchaseItemDao appPurchaseItemDao;

    /**
     * 根据订单状态查询订单列表
     *
     * @param userId      用户ID
     * @param orderStatus 订单状态
     * @return List<AppPurchasesVo> 订单列表
     * @throws Exception 异常
     */
    @Override
    public PageInfo<AppPurchaseOutput> findOrderList(Integer pageNum, Integer rows, String userId, String orderStatus) throws Exception {
//        List<AppPurchasesVo> orderListA = appPurchaseDao.findOrderList(userId, convertStringToInt(orderStatus));
        PageTool.startPage(pageNum, rows);
        //查询订单信息
        //业务逻辑
        //1.如果待支付订单为合并订单则合并显示；
        //2.如果为其他状态订单则单个显示
        //3.如果没有订单编号则返回null
       /* if(getOrderIds(orderStatus, orderListA).size() == 0){
            return new PageInfo<>();
        }*/
        List<AppPurchasesVo> orderList = new ArrayList<>();
        if ("1".equals(orderStatus)) {
            orderList = appPurchaseDao.findOrderList(userId, convertStringToInt(orderStatus), "1");
        } else if("".equals(orderStatus) || null == orderStatus) {
            orderList = appPurchaseDao.findOrderList(userId, convertStringToInt(orderStatus), "");
        } else {
            orderList = appPurchaseDao.findOrderList(userId, convertStringToInt(orderStatus), "2");
        }
        List<String> orderIdList = new ArrayList<>();//接收订单编号
        PageInfo pageInfo = new PageInfo(orderList);
        //如果有订单列表信息则继续后续相关操作，
        //如果没有订单列表则直接返回null，不进行后续操作。
        if (null != orderList && orderList.size() > 0) {
            orderIdList = getId(orderStatus, orderList);
        } else {
            return new PageInfo<>();
        }
        List<AppPurchaseOutput> appPurchaseOutputs = new ArrayList<>();//接收返回list
        List<AppPurchaseItemVo> appPurchaseItemVoList = getAllOrderItemList(orderIdList, orderStatus);//接收详情列表
        List<BigDecimal> totalOrderPostageList = new ArrayList<>();
        if("1".equals(orderStatus) || "".equals(orderStatus) || null == orderStatus){
            totalOrderPostageList = getOrderPostage(userId, orderStatus, appPurchaseItemVoList);
        }
        int i = 0;
        for (AppPurchasesVo appPurchasesVo : orderList) {

            List<AppPurchaseItemVo> appPurchaseItemVoListInner = new TreeList<>();
            AppPurchaseOutput appPurchaseOutput = null;
            int goodsAllNum = 0;//计算该订单下所有商品总数
            BigDecimal goodsAllPrice = new BigDecimal(0);//当查询订单状态为1时，计算该订单下所有商品总价
            //合并返回结果
            for (AppPurchaseItemVo appPurchaseItemVo : appPurchaseItemVoList) {
                //订单状态为待付款
                if (appPurchasesVo.getOrderStatus() == 1) {
                    if (appPurchaseItemVo.getPayId().equals(appPurchasesVo.getPayId())) {
                        appPurchaseItemVoListInner.add(appPurchaseItemVo);
                        //计算总数
                        goodsAllNum += appPurchaseItemVo.getGoodsNumber();
                        //计算总价
                        BigDecimal goodsNum = new BigDecimal(appPurchaseItemVo.getGoodsNumber());
                        String goodsUnit = appPurchaseItemVo.getGoodsUnitPrice().replaceAll(",", "");
                        goodsAllPrice = goodsAllPrice.add(goodsNum.multiply(new BigDecimal(goodsUnit)));
                    }
                } else {
                    //订单状态为其他
                    if (appPurchaseItemVo.getOrderId().equals(appPurchasesVo.getOrderId())) {
                        appPurchaseItemVoListInner.add(appPurchaseItemVo);
                        //计算总数
                        goodsAllNum += appPurchaseItemVo.getGoodsNumber();
                    }
                }
            }
            appPurchaseOutput = BeanMapper.map(appPurchasesVo, AppPurchaseOutput.class);
            appPurchaseOutput.setAppPurchaseItemVos(appPurchaseItemVoListInner);
            appPurchaseOutput.setOrderPrice(NumberUtil.number2Thousand(appPurchasesVo.getOrderPrice()));
            appPurchaseOutput.setCouponId(appPurchasesVo.getCouponId());
            appPurchaseOutput.setDiscount(NumberUtil.number2Thousand(appPurchasesVo.getDiscount()));
            appPurchaseOutput.setOrderTotalPrice(NumberUtil.number2Thousand(appPurchasesVo.getOrderTotalPrice()));
            appPurchaseOutput.setPayAmount(NumberUtil.number2Thousand(appPurchasesVo.getPayAmount()));
            appPurchaseOutput.setDrawbackPrice(NumberUtil.number2Thousand(appPurchasesVo.getDrawbackPrice()));
            appPurchaseOutput.setGoodsAllNum(goodsAllNum);

            //输出运费
            //1.如果运费为0，则显示“包邮”
            //2.如果有运费，则输出实际金额的千分值
            if("1".equals(orderStatus) || "".equals(orderStatus) || null == orderStatus){
                if (totalOrderPostageList.get(i).compareTo(new BigDecimal(0)) == 0) {
                    appPurchaseOutput.setOrderPostage("包邮");
                } else {
                    appPurchaseOutput.setOrderPostage(NumberUtil.number2Thousand(totalOrderPostageList.get(i)));
                }
            } else {
                if (appPurchasesVo.getOrderPostage().compareTo(new BigDecimal(0)) == 0) {
                    appPurchaseOutput.setOrderPostage("包邮");
                } else {
                    appPurchaseOutput.setOrderPostage(NumberUtil.number2Thousand(appPurchasesVo.getOrderPostage()));
                }
            }

            //当查询订单状态为1时将计算后的总价赋值输出
            if (goodsAllPrice.compareTo(new BigDecimal(0)) == 1) {
                appPurchaseOutput.setOrderPrice(NumberUtil.number2Thousand(goodsAllPrice));
            }
            //1.当订单状态为1的时候，则商户ID和商户名称为null
            //2.订单状态为其他则显示相应的值
            if (appPurchasesVo.getOrderStatus() == 1) {
                appPurchaseOutput.setStoreId(null);
                appPurchaseOutput.setStoreName(null);
                appPurchaseOutput.setOrderId(null);
            }
            i++;
            appPurchaseOutputs.add(appPurchaseOutput);

        }
        pageInfo.setList(appPurchaseOutputs);
        return pageInfo;
    }
    //获取ID（订单状态为待付款（1）获取的是payID,订单状态为其他则获取的是orderId）
    private List<String> getId(String orderStatus, List<AppPurchasesVo> orderList) {
        List<String> orderIdList = new ArrayList<>();
        if ("1".equals(orderStatus) || "".equals(orderStatus) || null == orderStatus) {
            //获取所有合并支付编号
            for (AppPurchasesVo appPurchasesVo : orderList) {
                orderIdList.add(appPurchasesVo.getPayId());
            }
            return orderIdList;
        }
        //获取所有订单编号
        for (AppPurchasesVo appPurchasesVo : orderList) {
            orderIdList.add(appPurchasesVo.getOrderId());
        }
        return orderIdList;
    }

    //获取详情信息
    private List<AppPurchaseItemVo> getAllOrderItemList(List<String> orderIdList, String orderStatus) throws Exception {
        if ("1".equals(orderStatus) || "".equals(orderStatus) || null == orderStatus) {
            return appPurchaseItemDao.findOrderItemListByPayId(orderIdList);
        }
        return appPurchaseItemDao.findOrderItemList(orderIdList);
    }

    //转换数据类型
    private Integer[] convertStringToInt(String orderStatus) {
        Integer[] statusArr = null;
        if (!StringUtils.isEmpty(orderStatus)) {
            String[] orderStatusArr = orderStatus.split(",");
            statusArr = BeanMapper.mapArray(new Integer[orderStatusArr.length], orderStatusArr, Integer.class);
        }
        return statusArr;
    }

    //计算运费
    private List<BigDecimal> getOrderPostage(String userId, String orderStatus, List<AppPurchaseItemVo> appPurchaseItemVoList) throws Exception {
        List<AppPurchasesVo> orderList = appPurchaseDao.findOrderList(userId, convertStringToInt(orderStatus), "2");
        List<AppPurchasesVo> orderListA = new ArrayList<>();
                List<BigDecimal> totalOrderPostageList = new ArrayList<>();
        List<AppPurchasesVo> orderListB = new ArrayList<>();
        if("1".equals(orderStatus)){
            orderListA = appPurchaseDao.findOrderList(userId, convertStringToInt(orderStatus), "1");
        } else{
            orderListA = appPurchaseDao.findOrderList(userId, convertStringToInt(orderStatus), "");
        }
        for (AppPurchasesVo appPurchasesVoA : orderListA) {
            BigDecimal totalOrderPostage = new BigDecimal(0);//总运费
            for (AppPurchasesVo appPurchasesVo : orderList) {
                //订单状态为其他
                if(appPurchasesVo.getOrderStatus() == 1){
                    if (appPurchasesVoA.getPayId().equals(appPurchasesVo.getPayId())) {
                        totalOrderPostage = totalOrderPostage.add(appPurchasesVo.getOrderPostage());
                    }
                } else {
                    if (appPurchasesVoA.getOrderId().equals(appPurchasesVo.getOrderId())) {
                        totalOrderPostage = totalOrderPostage.add(appPurchasesVo.getOrderPostage());
                    }
                }
            }
            totalOrderPostageList.add(totalOrderPostage);
        }
        return totalOrderPostageList;
    }
}
