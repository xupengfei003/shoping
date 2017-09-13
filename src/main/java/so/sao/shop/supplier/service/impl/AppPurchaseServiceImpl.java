package so.sao.shop.supplier.service.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import so.sao.shop.supplier.dao.AppPurchaseDao;
import so.sao.shop.supplier.dao.AppPurchaseItemDao;
import so.sao.shop.supplier.pojo.output.AppPurchaseOutput;
import so.sao.shop.supplier.pojo.vo.AppPurchaseItemVo;
import so.sao.shop.supplier.pojo.vo.AppPurchasesVo;
import so.sao.shop.supplier.service.AppPurchaseService;
import so.sao.shop.supplier.util.BeanMapper;
import so.sao.shop.supplier.util.PageTool;

import javax.annotation.Resource;
import java.math.BigInteger;
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
        if (null != orderList && orderList.size() > 0) {
            //获取所有订单编号
            for (AppPurchasesVo appPurchasesVo : orderList) {
                orderIdList.add(appPurchasesVo.getOrderId());
            }
        }
        PageInfo pageInfo = new PageInfo(orderList);//复制分页信息
        List<AppPurchaseOutput> appPurchaseOutputs = new ArrayList<>();//接收返回list
        List<AppPurchaseItemVo> appPurchaseItemVoList = getAllOrderItemList(orderIdList);//接收详情列表
        for (AppPurchasesVo appPurchasesVo : orderList) {
            List<AppPurchaseItemVo> appPurchaseItemVoListInner = new ArrayList<>();
            AppPurchaseOutput appPurchaseOutput;
            int goodsAllNum = 0;//计算该订单下所有商品总数
            //合并返回结果
            for (AppPurchaseItemVo appPurchaseItemVo : appPurchaseItemVoList) {
                if (appPurchaseItemVo.getOrderId().equals(appPurchasesVo.getOrderId())) {
                    appPurchaseItemVoListInner.add(appPurchaseItemVo);
                    goodsAllNum += appPurchaseItemVo.getGoodsNumber();
                }
            }
            appPurchaseOutput = BeanMapper.map(appPurchasesVo,AppPurchaseOutput.class);
            appPurchaseOutput.setAppPurchaseItemVos(appPurchaseItemVoListInner);
            appPurchaseOutput.setGoodsAllNum(goodsAllNum);
            appPurchaseOutputs.add(appPurchaseOutput);

        }
        pageInfo.setList(appPurchaseOutputs);
        return pageInfo;
    }

    //获取详情信息
    private List<AppPurchaseItemVo> getAllOrderItemList(List<String> orderIdList) throws Exception {
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
