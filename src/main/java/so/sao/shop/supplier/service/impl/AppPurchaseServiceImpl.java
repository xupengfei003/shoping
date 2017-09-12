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
 * Created by acer on 2017/9/6.
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
     * @param userId 用户ID
     * @param orderStatus 订单状态
     * @return List<AppPurchasesVo> 订单列表
     * @throws Exception 异常
     */
    @Override
    public PageInfo<AppPurchaseOutput> findOrderList(Integer pageNum, Integer rows, String userId, String orderStatus) throws Exception {
        PageTool.startPage(pageNum,rows);
        //查询订单信息
        List<AppPurchasesVo> orderList = appPurchaseDao.findOrderList(userId,convertStringToInt(orderStatus));
        AppPurchaseOutput appPurchaseOutput = null;//接收订单相关信息
        PageInfo pageInfo = new PageInfo(orderList);//复制分页信息
        List<String> orderIdList = new ArrayList<>();//接收订单编号
        if(null != orderList && orderList.size()>0){
            for(AppPurchasesVo appPurchasesVo : orderList){
                orderIdList.add(appPurchasesVo.getOrderId());
                appPurchaseOutput = BeanMapper.map(appPurchasesVo,AppPurchaseOutput.class);
            }
        }
        List<AppPurchaseOutput> appPurchaseOutputs = getAppPurchaseOutputs(appPurchaseOutput, orderIdList);
        pageInfo.setList(appPurchaseOutputs);
        return pageInfo;
    }

    //查询详情信息
    private List<AppPurchaseOutput> getAppPurchaseOutputs(AppPurchaseOutput appPurchaseOutput, List<String> orderIdList) throws Exception {
        List<AppPurchaseItemVo> appPurchaseItemVoList = appPurchaseItemDao.findOrderItemList(orderIdList);
        appPurchaseOutput.setAppPurchaseItemVos(appPurchaseItemVoList);
        List<AppPurchaseOutput> appPurchaseOutputs = new ArrayList<>();//接收返回list
        appPurchaseOutputs.add(appPurchaseOutput);
        return appPurchaseOutputs;
    }

    //转换数据类型
    private Integer[] convertStringToInt(String orderStatus){
        Integer[] statusArr = null;
        if(!StringUtils.isEmpty(orderStatus)){
            String[] orderStatusArr = orderStatus.split(",");
            statusArr = BeanMapper.mapArray(new Integer[orderStatusArr.length],orderStatusArr,Integer.class);
        }
        return statusArr;
    }
}
