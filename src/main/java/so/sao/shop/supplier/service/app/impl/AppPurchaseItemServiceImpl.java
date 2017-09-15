package so.sao.shop.supplier.service.app.impl;

import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.app.AppPurchaseDao;
import so.sao.shop.supplier.dao.app.AppPurchaseItemDao;
import so.sao.shop.supplier.pojo.output.AppPurchaseItemOutput;
import so.sao.shop.supplier.pojo.vo.AppPurchaseItemVo;
import so.sao.shop.supplier.pojo.vo.AppPurchasesVo;
import so.sao.shop.supplier.service.app.AppPurchaseItemService;
import so.sao.shop.supplier.util.BeanMapper;

import javax.annotation.Resource;
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
        List<AppPurchaseItemVo> appPurchaseItemVoList = appPurchaseItemDao.findOrderItemListByOrderId(orderId);
        //查询订单信息
        AppPurchasesVo appPurchasesVos = appPurchaseDao.findOrderByOrderId(orderId);
        AppPurchaseItemOutput appPurchaseItemOutput = null;
        if(null != appPurchasesVos){
            appPurchaseItemOutput = BeanMapper.map(appPurchasesVos,AppPurchaseItemOutput.class);
        }
        if(null!=appPurchaseItemVoList && appPurchaseItemVoList.size()>0){
            appPurchaseItemOutput.setAppPurchaseItemVos(appPurchaseItemVoList);
        }
        return appPurchaseItemOutput;
    }
}
