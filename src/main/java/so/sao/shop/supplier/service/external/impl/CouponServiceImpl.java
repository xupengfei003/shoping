package so.sao.shop.supplier.service.external.impl;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.external.CouponDao;
import so.sao.shop.supplier.domain.external.Coupon;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.external.CouponService;
import so.sao.shop.supplier.util.PageTool;

import java.util.Date;
import java.util.List;

/**
 * <p>Version: shop-portal V0.9.0 </p>
 * <p>Title: CouponServiceImpl</p>
 * <p>Description: 优惠券业务实现类</p>
 * @author: liugang
 * @Date: Created in 2017/10/23 16:55
 */
@Service
public class CouponServiceImpl implements CouponService{

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CouponDao couponDao;

    /**
     *
     */
    @Override
    public void dailyUpdateStatus() {

    }

    @Override
    public Result addCoupon(Coupon coupon) {
        Result result = null;
        if(checkCouponName(coupon)){
            result  = Result.fail("优惠券名称重复");
            return result;
        }
        //默认是全部类别
        coupon.setCategoryId(0L);
        //默认未满减
        coupon.setDiscountWay(2);
        //日期
        coupon.setCreateAt(new Date());
        //领取数量初始化
        coupon.setSendNum(0);
        //使用数量初始化
        coupon.setUseNum(0);
        //已生效
        coupon.setStatus(0);
        Integer i = couponDao.insertCoupon(coupon);
        logger.debug(" 影响的行数 " + i);
        result  = Result.success(Constant.MessageConfig.MSG_SUCCESS);
        return result;
    }

    /**
     * 验证优惠券名称时候重复
     * @param coupon
     * @return
     */
    private boolean checkCouponName(Coupon coupon) {
        Integer i = couponDao.findCouponsByName(coupon.getName());
        return i != null && i > 0 ? true : false;
    }

    @Override
    public Result batchDiscardCouponsByIds(Long[] couponIds) {
        Integer i = couponDao.updateCouponStatusById(couponIds);
        Result result = Result.success(Constant.MessageConfig.MSG_SUCCESS);
        return result;
    }

    @Override
    public Result searchCoupons(String name, Integer[] couponStatus, Integer pageNum, Integer pageSize) {
        PageTool.startPage(pageNum,pageSize);
        List<Coupon> couponList = couponDao.findCoupons(name,couponStatus);
        PageInfo pageInfo = new PageInfo();
        pageInfo.setList(couponList);
        Result result  = Result.success(Constant.MessageConfig.MSG_SUCCESS);
        result.setData(pageInfo);
        return result;
    }
}
