package so.sao.shop.supplier.service.impl;

import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.CountSoldCommDao;
import so.sao.shop.supplier.service.CountSoldCommService;

import javax.annotation.Resource;
import java.util.Random;

/**
 * Created by acer on 2017/9/8.
 */
@Service
public class CountSoldCommServiceImpl implements CountSoldCommService {
    @Resource
    private CountSoldCommDao countSoldCommDao;
    @Override
    public Integer countSoldCommNum(String goodsId) throws Exception {
        Integer countNum = countSoldCommDao.countSoldCommNum(goodsId);
        int max=100;
        int min=50;
        Random random = new Random();
        countNum = countNum * (random.nextInt(max)%(max-min+1) + min);
        if(countNum <= 0){
            countNum = random.nextInt(max)%(max-min+1) + min;
        }
        return countNum;
    }
}
