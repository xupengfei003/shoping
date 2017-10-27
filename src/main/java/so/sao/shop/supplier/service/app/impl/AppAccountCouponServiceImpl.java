package so.sao.shop.supplier.service.app.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.app.AppAccountCouponDao;
import so.sao.shop.supplier.dao.external.CouponDao;
import so.sao.shop.supplier.domain.AccountCoupon;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.app.AppAccountCouponService;
import so.sao.shop.supplier.util.PageTool;
import so.sao.shop.supplier.util.PageUtil;

import java.util.List;

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

    /**
     * 更新优惠券状态
     */
    @Override
    public void dailyUpdateStatus() {
        appAccountCouponDao.updateAccountCouponStatus();
    }

    /**
     * 获取我的优惠券里列表
     * @param shopId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Result getAccountCoupons(Long shopId, Integer pageNum, Integer pageSize) {
        PageTool.startPage(pageNum,pageSize);
        List<AccountCoupon> list = appAccountCouponDao.findAccountCouponsByUserId(shopId);
        PageInfo pageInfo = new PageInfo<>();
        pageInfo.setList(list);
        Result result  = Result.success(Constant.MessageConfig.MSG_SUCCESS);
        result.setData(pageInfo);
        return result;
    }

    /**
     * 领取优惠券
     * @param shopId
     * @param couponId
     * @return
     */
    @Override
    public Result addAccountCoupon(Long shopId, Long couponId) {
        AccountCoupon accountCoupon = new AccountCoupon();
        Integer i = appAccountCouponDao.addAccountCoupon(accountCoupon);
        Result result  = Result.success(Constant.MessageConfig.MSG_SUCCESS);
        return result;
    }
}
