package so.sao.shop.supplier.pojo.input;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

/**
 * Created by fangzhou on 2017/9/22.
 */
public class AppCartItemSaveInput {

    /**
     * 加入购物车的商品ID
     */
    @NotNull(message = "加入购物车的商品ID不能为空")
    private Long commodityId;

    /**
     * 加入购物车中商品的数量
     */
    @NotNull(message = "加入商品数量不能为空")
    @Pattern(regexp = "^[1-9][0-9]*$", message = "加入商品数量有误")
    private String number;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    public Long getCommodityId() {
        return commodityId;
    }

    public void setCommodityId(Long commodityId) {
        this.commodityId = commodityId;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
