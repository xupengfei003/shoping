package so.sao.shop.supplier.service.app.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.app.AppAccountCouponDao;
import so.sao.shop.supplier.dao.external.CouponDao;
import so.sao.shop.supplier.service.app.AppAccountCouponService;

/**
 * <p>Version: shop-business V2.5.0 </p>
 * <p>Title: AppAccountCouponServiceImpl</p>
 * <p>Description: APP 用户优惠券业务实现类</p>
 * @author: liugang
 * @Date: Created in 2017/10/23 16:54
 */
@Service
public class AppAccountCouponServiceImpl implements AppAccountCouponService {
    @Autowired
    private AppAccountCouponDao appAccountCouponDao;
    @Autowired
    private CouponDao couponDao;
}
