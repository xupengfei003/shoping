package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotBlank;
import so.sao.shop.supplier.pojo.vo.PurchaseItemVo;

import javax.validation.constraints.Min;
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
     * 买家ID
     */
    @NotBlank
    @Min(value=0)
    private Long userId;
    /**
     * 收货人姓名
     */
    @NotBlank
    private String orderReceiverName;
    /**
     * 收货人电话
     */
    @NotBlank
    private String orderReceiverMobile;
    /**
     * 收货人地址
     */
    @NotBlank
    private String orderAddress;
    /**
     * 支付方式
     */
    @NotBlank
    @Min(value=0)
    private Integer orderPaymentMethod;
    /**
     * 订单详情信息
     */
    @NotBlank
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
}
