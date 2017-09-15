package so.sao.shop.supplier.pojo.output;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wyy on 2017/9/12.
 */
public class AppCartItemOut {

    /**
     * 供应商ID
     */
    private Long supplierId;
    /**
     * 供应商名称
     */
    private String supplierName;

    List<AppCartItemOutSub> appCartItems = new ArrayList<>();
    /**
     * 选中的店铺商品
     */
    private String[] list;

    /**
     *当前店铺商品是否全选
     */
    private String isSelectShop;

    public String[] getList() {
        return list;
    }

    public void setList(String[] list) {
        this.list = list;
    }

    public String getIsSelectShop() {
        return isSelectShop;
    }

    public void setIsSelectShop(String isSelectShop) {
        this.isSelectShop = isSelectShop;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public String getSupplierName() {
        return supplierName;
    }

    public void setSupplierName(String supplierName) {
        this.supplierName = supplierName;
    }

    public List<AppCartItemOutSub> getAppCartItems() {
        return appCartItems;
    }

    public void setAppCartItems(List<AppCartItemOutSub> appCartItems) {
        this.appCartItems = appCartItems;
    }


    @Override
    public String toString() {
        return "AppCartItemOut{" +
                "supplierId=" + supplierId +
                ", supplierName='" + supplierName + '\'' +
                ", appCartItems=" + appCartItems +
                '}';
    }


}
