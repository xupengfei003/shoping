package so.sao.shop.supplier.pojo.output;

import com.github.pagehelper.PageInfo;


/**
 * <p>
 * 出参类
 * </p>
 *
 * @author 透云-中软-西安项目组-zhenhai.zheng
 * @since 2017-07-20
 **/
public class RecordToPurchaseOutput<T> {

    private PageInfo<T> pageInfo;

    /**
     * 订单结算总额
     */
    private String totalOrderRevenue;

    /**
     * 订单运费总额
     */
    private String totalPostage;

    public PageInfo<T> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<T> pageInfo) {
        this.pageInfo = pageInfo;
    }

    public String getTotalOrderRevenue() {
        return totalOrderRevenue;
    }

    public void setTotalOrderRevenue(String totalOrderRevenue) {
        this.totalOrderRevenue = totalOrderRevenue;
    }

    public String getTotalPostage() {
        return totalPostage;
    }

    public void setTotalPostage(String totalPostage) {
        this.totalPostage = totalPostage;
    }
}