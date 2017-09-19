package so.sao.shop.supplier.service.impl;

import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.CountSoldCommDao;
import so.sao.shop.supplier.pojo.vo.CountSoldNumVo;
import so.sao.shop.supplier.service.CountSoldCommService;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by acer on 2017/9/8.
 */
@Service
public class CountSoldCommServiceImpl implements CountSoldCommService {
    @Resource
    private CountSoldCommDao countSoldCommDao;
    /**
     * 根据商品ID统计已售商品数量
     *
     * @param goodsIds 商品ID列表
     * @return Integer 统计数
     */
    @Override
    public List<String> countSoldCommNum(String[] goodsIds) throws Exception {
        List<String> result = new ArrayList();
        List<CountSoldNumVo> countNums = countSoldCommDao.countSoldCommNum(goodsIds);
        Random random = new Random();
        int resultNum = 0;
        for (String goodId : goodsIds){
            if(countNums.size() == 0){
                resultNum = random.nextInt(51) + 50;
            } else {
                resultNum = getResultNum(countNums, random, resultNum, goodId);
            }
            result.add(String.valueOf(resultNum));
        }
        return result;
    }
    //获取销售量
    private int getResultNum(List<CountSoldNumVo> countNums, Random random, int resultNum, String goodId) {
        for (CountSoldNumVo countSold : countNums){
            int rNumber = random.nextInt(51) + 50;
            resultNum = rNumber;
            if (String.valueOf(countSold.getGoodsId()).equals(goodId)){
                resultNum = rNumber * countSold.getGoodsCountNum();
                return resultNum;
            }
        }
        return resultNum;
    }
}
