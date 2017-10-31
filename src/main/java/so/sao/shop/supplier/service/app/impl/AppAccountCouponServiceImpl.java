package so.sao.shop.supplier.service.app.impl;

import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.app.AppAccountCouponDao;
import so.sao.shop.supplier.dao.external.CouponDao;
import so.sao.shop.supplier.domain.AccountCoupon;
import so.sao.shop.supplier.domain.external.Coupon;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.output.CouponOutputVo;
import so.sao.shop.supplier.service.app.AppAccountCouponService;
import so.sao.shop.supplier.util.PageTool;

import java.math.BigDecimal;
import java.util.Date;
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
     * @param usableValue
     *@param pageNum
     * @param pageSize   @return
     */
    @Override
    public Result getAccountCoupons(@Param("shopId") Long shopId, BigDecimal usableValue, Integer pageNum, Integer pageSize) {
        PageTool.startPage(pageNum,pageSize);
        List<AccountCoupon> list = appAccountCouponDao.findAccountCouponsByUserId(shopId,usableValue);
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
    @Transactional(rollbackFor = Exception.class )
    @Override
    public Result addAccountCoupon(@Param("shopId") Long shopId, @Param("couponId")Long couponId) {
        AccountCoupon accountCoupon = new AccountCoupon();
        accountCoupon.setAccountId(shopId);
        accountCoupon.setCouponId(couponId);
        accountCoupon.setCreateAt(new Date());
        accountCoupon.setStatus(0);
        accountCoupon.setGetTime(new Date());
        Integer i = appAccountCouponDao.findAccountCoupon(shopId,couponId);
        if (i != null && i.intValue() > 0){
            Result result  = Result.fail("请勿重复领取！");
            return result;
        }
        //优惠券中心数量减一
        i = couponDao.updateCouponNum(couponId,1);
        //用户优惠券记录加一
        i = appAccountCouponDao.insertAccountCoupon(accountCoupon);
        Result result  = Result.success(Constant.MessageConfig.MSG_SUCCESS);
        return result;
    }

    /**
     * 领券中心
     * @param shopId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Result getCouponCenter(Long shopId, Integer pageNum, Integer pageSize) {
        PageTool.startPage(pageNum,pageSize);
        List<CouponOutputVo> list = couponDao.findCouponsByShopId(shopId);
        PageInfo pageInfo = new PageInfo<>();
        pageInfo.setList(list);
        Result result  = Result.success(Constant.MessageConfig.MSG_SUCCESS);
        result.setData(pageInfo);
        return result;
    }

    /**
     * 使用优惠券
     * @param accountCouponId
     * @return
     */
    @Override
    public Integer useAccountCoupon(Long accountCouponId){
        appAccountCouponDao.updateAccountCouponStatusById(accountCouponId);
        return null;
    }
}
