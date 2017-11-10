package so.sao.shop.supplier.service.app.impl;

import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.app.AppCommSalesDao;
import so.sao.shop.supplier.domain.CommSales;
import so.sao.shop.supplier.pojo.input.AppCommSalesInput;
import so.sao.shop.supplier.pojo.vo.CountSoldNumVo;
import so.sao.shop.supplier.service.app.AppCommSalesService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by acer on 2017/9/8.
 */
@Service
public class AppCommSalesServiceImpl implements AppCommSalesService {
    @Resource
    private AppCommSalesDao appCommSalesDao;
    /**
     * 根据商品ID统计已售商品数量
     *
     * @param goodsIds 商品ID列表
     * @return Integer 统计数
     */
    @Override
    public List<String> countSoldCommNum(String[] goodsIds) throws Exception {
        List<String> result = new ArrayList();
        List<CommSales> countNums = appCommSalesDao.countSoldCommNum(goodsIds);
        for (String goodId : goodsIds) {
            for (CommSales commSales : countNums) {
                if (String.valueOf(commSales.getScId()).equals(goodId)) {
                    result.add(String.valueOf(commSales.getVirtualSales()));//返回虚拟销量
                }
            }
        }
        return result;
    }

    /**
     * 更新商品销量
     * @param commSalesInputs 商品销量更新入参
     * @return
     */
    @Override
    public boolean updateSalesNum(List<AppCommSalesInput> commSalesInputs) {
        return appCommSalesDao.updateSalesNum(commSalesInputs);
    }

    /**
     * 添加商品销量
     *
     * @param goodsIds 商品ID
     * @return
     */
    @Override
    public boolean saveCommSales(String goodsIds) {
        String[] goodsIdArr = goodsIds.split(",");
        return appCommSalesDao.saveCommSales(goodsIdArr);
    }
}

