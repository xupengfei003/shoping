package so.sao.shop.supplier.service.impl;

import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.AppPurchaseDao;
import so.sao.shop.supplier.dao.AppPurchaseItemDao;
import so.sao.shop.supplier.pojo.output.AppPurchaseOutput;
import so.sao.shop.supplier.pojo.vo.AppPurchaseItemVo;
import so.sao.shop.supplier.pojo.vo.AppPurchasesVo;
import so.sao.shop.supplier.service.AppPurchaseService;
import so.sao.shop.supplier.util.BeanMapper;

import javax.annotation.Resource;
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
     * @param orderStatus 订单状态
     * @return List<AppPurchasesVo> 订单列表
     * @throws Exception 异常
     */
    @Override
    public List<AppPurchaseOutput> findOrderList(Integer orderStatus) throws Exception {
        List<AppPurchasesVo> orderIdList = appPurchaseDao.findOrderList(orderStatus);
        List<AppPurchaseOutput> appPurchaseOutputs = new ArrayList<>();
        if(orderIdList.size()>0){
            for(AppPurchasesVo appPurchasesVo : orderIdList){
                List<AppPurchaseItemVo> appPurchaseItemVoList = appPurchaseItemDao.findOrderItemList(appPurchasesVo.getOrderId());
                AppPurchaseOutput appPurchaseOutput = BeanMapper.map(appPurchasesVo,AppPurchaseOutput.class);
                appPurchaseOutput.setAppPurchaseItemVos(appPurchaseItemVoList);
                appPurchaseOutputs.add(appPurchaseOutput);
            }
        }
        return appPurchaseOutputs;
    }
}
