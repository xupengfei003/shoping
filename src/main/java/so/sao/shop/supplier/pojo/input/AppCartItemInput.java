package so.sao.shop.supplier.pojo.input;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

public class AppCartItemInput {


    /**
     * 购物车ID
     */
    @NotNull(message = "购物车记录ID不能为空")
    private Long cartitemId;

    /**
     * 更新购物车中商品的数量
     */
    @NotNull(message = "更新数量不能为空")
    @Pattern(regexp = "^[1-9][0-9]*$", message = "更新数量有误")
    private Integer number;

    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    public Long getCartitemId() {
        return cartitemId;
    }

    public void setCartitemId(Long cartitemId) {
        this.cartitemId = cartitemId;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}