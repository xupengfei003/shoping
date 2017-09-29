package so.sao.shop.supplier.pojo.output;

import so.sao.shop.supplier.pojo.vo.AppCartItemVo;

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

    /**
     * 购物车记录Vo的集合
     */
    List<AppCartItemVo> appCartItems = new ArrayList<>();

    /**
     * 选中的店铺商品
     */
    private String[] list;

    /**
     *当前店铺商品是否全选
     */
    private Boolean isSelectShop;

    public String[] getList() {
        return list;
    }

    public void setList(String[] list) {
        this.list = list;
    }

    public Boolean getIsSelectShop() {
        return isSelectShop;
    }

    public void setIsSelectShop(Boolean isSelectShop) {
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

    public List<AppCartItemVo> getAppCartItems() {
        return appCartItems;
    }

    public void setAppCartItems(List<AppCartItemVo> appCartItems) {
        this.appCartItems = appCartItems;
    }
}
