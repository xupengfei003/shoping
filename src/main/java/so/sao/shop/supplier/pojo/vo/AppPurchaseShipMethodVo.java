package so.sao.shop.supplier.pojo.vo;

/**
 * Created by acer on 2017/9/28.
 */
public class AppPurchaseShipMethodVo {
    /**
     * 配送方式（1.自配送，2.物流公司）
     */
    private Integer orderShipMethod;
    /**
     * 配送人姓名
     */
    private String distributorName;
    /**
     * 配送人电话）
     */
    private String distributorMobile;
    /**
     * 物流公司名称
     */
    private String logisticsCompany;
    /**
     * 物流公司物流单号
     */
    private String orderShipmentNumber;

    public Integer getOrderShipMethod() {
        return orderShipMethod;
    }

    public void setOrderShipMethod(Integer orderShipMethod) {
        this.orderShipMethod = orderShipMethod;
    }

    public String getDistributorName() {
        return distributorName;
    }

    public void setDistributorName(String distributorName) {
        this.distributorName = distributorName;
    }

    public String getDistributorMobile() {
        return distributorMobile;
    }

    public void setDistributorMobile(String distributorMobile) {
        this.distributorMobile = distributorMobile;
    }

    public String getLogisticsCompany() {
        return logisticsCompany;
    }

    public void setLogisticsCompany(String logisticsCompany) {
        this.logisticsCompany = logisticsCompany;
    }

    public String getOrderShipmentNumber() {
        return orderShipmentNumber;
    }

    public void setOrderShipmentNumber(String orderShipmentNumber) {
        this.orderShipmentNumber = orderShipmentNumber;
    }
}
