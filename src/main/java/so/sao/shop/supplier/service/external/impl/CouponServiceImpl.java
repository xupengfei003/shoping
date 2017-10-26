package so.sao.shop.supplier.service.external.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.external.CouponDao;
import so.sao.shop.supplier.service.external.CouponService;

/**
 * <p>Version: shop-portal V0.9.0 </p>
 * <p>Title: CouponServiceImpl</p>
 * <p>Description: 优惠券业务实现类</p>
 * @author: liugang
 * @Date: Created in 2017/10/23 16:55
 */
@Service
public class CouponServiceImpl implements CouponService{
    @Autowired
    private CouponDao couponDao;
}
