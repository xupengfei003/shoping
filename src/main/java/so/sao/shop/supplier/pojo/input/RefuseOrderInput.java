package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Created by acer on 2017/8/17.
 */
public class RefuseOrderInput {
    /**
     * 订单编号
     */
    @NotEmpty(message = "订单编号不能为空")
    private String orderId;
    /**
     * 拒收类型
     */
    private String refuseType;
    /**
     * 拒收理由
     */
    @NotEmpty(message = "拒收理由不能为空")
    private String refuseReason;
    /**
     * 拒收图片
     */
    private List<String> refuseImg;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getRefuseType() {
        return refuseType;
    }

    public void setRefuseType(String refuseType) {
        this.refuseType = refuseType;
    }

    public String getRefuseReason() {
        return refuseReason;
    }

    public void setRefuseReason(String refuseReason) {
        this.refuseReason = refuseReason;
    }

    public List<String> getRefuseImg() {
        return refuseImg;
    }

    public void setRefuseImg(List<String> refuseImg) {
        this.refuseImg = refuseImg;
    }
}
