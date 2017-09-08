package so.sao.shop.supplier.service.impl;

import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.AppPurchaseDao;
import so.sao.shop.supplier.dao.AppPurchaseItemDao;
import so.sao.shop.supplier.dao.PurchaseDao;
import so.sao.shop.supplier.pojo.output.AppPurchaseItemOutput;
import so.sao.shop.supplier.pojo.vo.AppPurchaseItemVo;
import so.sao.shop.supplier.pojo.vo.AppPurchasesVo;
import so.sao.shop.supplier.service.AppPurchaseItemService;
import so.sao.shop.supplier.util.BeanMapper;

import javax.annotation.Resource;
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
     * @return List<AppPurchaseItemVo> 订单列表
     * @throws Exception 异常
     */
    @Override
    public AppPurchaseItemOutput findOrderItemList(String orderId) throws Exception {
        //查询详情信息
        List<AppPurchaseItemVo> appPurchaseItemVoList = appPurchaseItemDao.findOrderItemList(orderId);
        //查询订单信息
        List<AppPurchasesVo> appPurchasesVos = appPurchaseDao.findOrderListByOrderId(orderId);
        AppPurchaseItemOutput appPurchaseItemOutput = null;
        if(null != appPurchasesVos && appPurchasesVos.size()>0){
            appPurchaseItemOutput = BeanMapper.map(appPurchasesVos.get(0),AppPurchaseItemOutput.class);
        }
        if(null!=appPurchaseItemVoList && appPurchaseItemVoList.size()>0){
            appPurchaseItemOutput.setAppPurchaseItemVos(appPurchaseItemVoList);
        }
        return appPurchaseItemOutput;
    }
}
