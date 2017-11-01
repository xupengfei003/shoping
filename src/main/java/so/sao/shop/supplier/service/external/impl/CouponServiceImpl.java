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
        //条件校验
        if(!coupon.getUseStartTime().before(coupon.getUseEndTime())){
            result  = Result.fail("开始时间与结束时间不正确");
            return result;
        }
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
        List<Coupon> list = couponDao.findCouponsByName(coupon.getName());
        return list != null && list.size() > 0 ? true : false;
    }

    /**
     * 批量废弃优惠券
     * @param couponIds
     * @return
     */
    @Override
    public Result batchDiscardCouponsByIds(Long[] couponIds) {
        Integer i = couponDao.updateCouponStatusById(couponIds,4);
        Result result = Result.success(Constant.MessageConfig.MSG_SUCCESS);
        return result;
    }

    /**
     * 按照名称和状态搜索优惠券
     * @param name
     * @param couponStatus
     * @param pageNum
     * @param pageSize
     * @return
     */
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
