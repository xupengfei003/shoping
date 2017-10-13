package so.sao.shop.supplier.pojo.output;

/**
 * 供应商首页-供应商商品部分统计出参对象
 * @author zhaoyan
 * @create 2017/10/13 9:14
 */
public class CountCommDetailOutput {
    /**
     * 库存预警数量
     */
    private int inventoryStatusNum;
    /**
     * 已上架数量
     */
    private int onShelvesNum;
    /**
     * 已下架数量
     */
    private int offShelvesNum;
    /**
     * 上架待审核数量
     */
    private int onShelvesAuditNum;
    /**
     * 下架待审核数量
     */
    private int offShelvesAuditNum;
    /**
     * sku数量
     */
    private int skuNum;

    public int getInventoryStatusNum() {
        return inventoryStatusNum;
    }

    public void setInventoryStatusNum(int inventoryStatusNum) {
        this.inventoryStatusNum = inventoryStatusNum;
    }

    public int getOnShelvesNum() {
        return onShelvesNum;
    }

    public void setOnShelvesNum(int onShelvesNum) {
        this.onShelvesNum = onShelvesNum;
    }

    public int getOffShelvesNum() {
        return offShelvesNum;
    }

    public void setOffShelvesNum(int offShelvesNum) {
        this.offShelvesNum = offShelvesNum;
    }

    public int getOnShelvesAuditNum() {
        return onShelvesAuditNum;
    }

    public void setOnShelvesAuditNum(int onShelvesAuditNum) {
        this.onShelvesAuditNum = onShelvesAuditNum;
    }

    public int getOffShelvesAuditNum() {
        return offShelvesAuditNum;
    }

    public void setOffShelvesAuditNum(int offShelvesAuditNum) {
        this.offShelvesAuditNum = offShelvesAuditNum;
    }

    public int getSkuNum() {
        return skuNum;
    }

    public void setSkuNum(int skuNum) {
        this.skuNum = skuNum;
    }

    public CountCommDetailOutput() {
    }
}
