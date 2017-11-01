package so.sao.shop.supplier.pojo.input;/**
 * Created by wyy on 2017/10/30.
 */

import com.sun.istack.internal.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * <p>Version: supplier V1.1.0 </p>
 * <p>Title: AccountCouponInputVo</p>
 * <p>Description: </p>
 *
 * @author: yiyun.wang
 * @Date: Created in 2017/10/30 11:59
 */
public class AccountCouponInputVo {

    @NotNull
    private Long shopId;
    @NotNull
    private Long couponId;

    public Long getShopId() {
        return shopId;
    }

    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    public Long getCouponId() {
        return couponId;
    }

    public void setCouponId(Long couponId) {
        this.couponId = couponId;
    }
}
