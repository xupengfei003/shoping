package so.sao.shop.supplier.service.app.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import so.sao.shop.supplier.dao.app.AppPurchaseDao;
import so.sao.shop.supplier.dao.app.AppPurchaseItemDao;
import so.sao.shop.supplier.pojo.output.AppPurchaseOutput;
import so.sao.shop.supplier.pojo.vo.AppPurchaseItemVo;
import so.sao.shop.supplier.pojo.vo.AppPurchasesVo;
import so.sao.shop.supplier.service.app.AppPurchaseService;
import so.sao.shop.supplier.util.BeanMapper;
import so.sao.shop.supplier.util.NumberUtil;
import so.sao.shop.supplier.util.PageTool;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

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
        PageTool.startPage(pageNum, rows);
        //查询订单信息
        List<AppPurchasesVo> orderList = appPurchaseDao.findOrderList(userId, convertStringToInt(orderStatus));
        List<String> orderIdList = new ArrayList<>();//接收订单编号
        PageInfo pageInfo = new PageInfo(orderList);//复制分页信息
        //如果有订单列表信息则继续后续相关操作，
        //如果没有订单列表则直接返回null，不进行后续操作。
        if (null != orderList && orderList.size() > 0) {
            orderIdList = getId(orderStatus, orderList);
        } else {
            return pageInfo;
        }
        List<AppPurchaseOutput> appPurchaseOutputs = new ArrayList<>();//接收返回list
        List<AppPurchaseItemVo> appPurchaseItemVoList = getAllOrderItemList(orderIdList,orderStatus);//接收详情列表
        for (AppPurchasesVo appPurchasesVo : orderList) {

            List<AppPurchaseItemVo> appPurchaseItemVoListInner = new ArrayList<>();
            AppPurchaseOutput appPurchaseOutput;
            int goodsAllNum = 0;//计算该订单下所有商品总数
            BigDecimal goodsAllPrice = new BigDecimal(0);//当查询订单状态为1时，计算该订单下所有商品总价
            //合并返回结果
            for (AppPurchaseItemVo appPurchaseItemVo : appPurchaseItemVoList) {
                //订单状态为待付款
                if("1".equals(orderStatus)){
                    if (appPurchaseItemVo.getPayId().equals(appPurchasesVo.getPayId())) {
                        appPurchaseItemVoListInner.add(appPurchaseItemVo);
                        //计算总数
                        goodsAllNum += appPurchaseItemVo.getGoodsNumber();
                        //计算总价
                        BigDecimal goodsNum = new BigDecimal(appPurchaseItemVo.getGoodsNumber());
                        String goodsUnit = appPurchaseItemVo.getGoodsUnitPrice().replaceAll(",","");
                        goodsAllPrice = goodsAllPrice.add(goodsNum.multiply(new BigDecimal(goodsUnit)));
                    }
                    continue;
                }
                //订单状态为其他
                if (appPurchaseItemVo.getOrderId().equals(appPurchasesVo.getOrderId())) {
                    appPurchaseItemVoListInner.add(appPurchaseItemVo);
                    //计算总数
                    goodsAllNum += appPurchaseItemVo.getGoodsNumber();
                }
            }
            appPurchaseOutput = BeanMapper.map(appPurchasesVo,AppPurchaseOutput.class);
            appPurchaseOutput.setAppPurchaseItemVos(appPurchaseItemVoListInner);
            appPurchaseOutput.setOrderPrice(NumberUtil.number2Thousand(appPurchasesVo.getOrderPrice()));
            appPurchaseOutput.setGoodsAllNum(goodsAllNum);
            //输出运费
            //1.如果运费为0，则显示“包邮”
            //2.如果有运费，则输出实际金额的千分值
            if(appPurchasesVo.getOrderPostage().intValue() == 0){
                appPurchaseOutput.setOrderPostage("包邮");
            } else {
                appPurchaseOutput.setOrderPostage(NumberUtil.number2Thousand(appPurchasesVo.getOrderPostage()));
            }
            //当查询订单状态为1时将计算后的总价赋值输出
            if(goodsAllPrice.intValue() > 0){
                appPurchaseOutput.setOrderPrice(NumberUtil.number2Thousand(goodsAllPrice));
            }
            appPurchaseOutputs.add(appPurchaseOutput);
        }
        pageInfo.setList(appPurchaseOutputs);
        return pageInfo;
    }

    //获取ID（订单状态为待付款（1）获取的是payID,订单状态为其他则获取的是orderId）
    private List<String> getId(String orderStatus, List<AppPurchasesVo> orderList) {
        List<String> orderIdList = new ArrayList<>();
        if("1".equals(orderStatus)){
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
    private List<AppPurchaseItemVo> getAllOrderItemList(List<String> orderIdList,String orderStatus) throws Exception {
        if("1".equals(orderStatus)){
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
}
