package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotEmpty;
import so.sao.shop.supplier.pojo.vo.PurchaseItemVo;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <p>
 * 订单入参
 * </p>
 *
 * @author 透云-中软-西安项目组-wh
 * @since 2017-07-19
 */
public class PurchaseInput {
    /**
     * 门店ID
     */
    @NotNull(message="门店ID不能为空")
    private Long userId;

    /**
     * 门店名称
     */
    @NotEmpty(message="门店名称不能为空")
    private String userName;

    /**
     * 收货人姓名
     */
    @NotEmpty(message="收货人姓名不能为空")
    private String orderReceiverName;
    /**
     * 收货人电话
     */
    @NotEmpty(message="收货人电话不能为空")
    private String orderReceiverMobile;
    /**
     * 收货人地址
     */
    @NotEmpty(message="收货人地址不能为空")
    private String orderAddress;
    /**
     * 支付方式
     */
    @NotNull(message="支付方式不能为空")
    private Integer orderPaymentMethod;
    /**
     * 订单详情信息
     */
    @NotNull(message="商品列表属性不能为空")
    @Valid
    private List<PurchaseItemVo> listPurchaseItem;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getOrderReceiverName() {
        return orderReceiverName;
    }

    public void setOrderReceiverName(String orderReceiverName) {
        this.orderReceiverName = orderReceiverName;
    }

    public String getOrderReceiverMobile() {
        return orderReceiverMobile;
    }

    public void setOrderReceiverMobile(String orderReceiverMobile) {
        this.orderReceiverMobile = orderReceiverMobile;
    }

    public String getOrderAddress() {
        return orderAddress;
    }

    public void setOrderAddress(String orderAddress) {
        this.orderAddress = orderAddress;
    }

    public Integer getOrderPaymentMethod() {
        return orderPaymentMethod;
    }

    public void setOrderPaymentMethod(Integer orderPaymentMethod) {
        this.orderPaymentMethod = orderPaymentMethod;
    }

    public List<PurchaseItemVo> getListPurchaseItem() { return listPurchaseItem; }

    public void setListPurchaseItem(List<PurchaseItemVo> listPurchaseItem) { this.listPurchaseItem = listPurchaseItem; }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

}
