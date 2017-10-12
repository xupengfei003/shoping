package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.Valid;

/**
 * Created by acer on 2017/10/11.
 */
public class LogisticInfoUpdateInput {
    /**
     * 订单ID
     */
    @NotEmpty(message = "订单编号不能为空")
    private String orderId;
    /**
     * 配送方式
     */
    @NotBlank(message = "配送方式不能为空")
    private Integer orderShipMethod;
    /**
     * 物流公司/配送人姓名
     */
    @NotEmpty(message = "物流公司或配送人姓名不能为空")
    private String name;
    /**
     * 物流单号/配送人电话
     */
    @NotEmpty(message = "物流单号或配送人电话不能为空")
    private String number;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public Integer getOrderShipMethod() {
        return orderShipMethod;
    }

    public void setOrderShipMethod(Integer orderShipMethod) {
        this.orderShipMethod = orderShipMethod;
    }
}
