package so.sao.shop.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.CommBrandDao;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.CommBrandService;

@Service
public class CommBrandServiceImpl implements CommBrandService{

    @Autowired
    private CommBrandDao commBrandDao;

    /**
     * 查询商品品牌
     * @return
     */
    @Override
    public Result searchBrand() {

        return Result.success("查询商品品牌成功", commBrandDao.search());
    }
}
