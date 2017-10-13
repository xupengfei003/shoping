package so.sao.shop.supplier.pojo.output;

/**
 * 库存输出数据结构
 * @author gxy on 2017/10/13.
 */
public class CommInventoryInfoOutput {
    /**
     * 商品ID
     */
    private Long id;

    /**
     * 库存下限
     */
    private Long inventoryMinimum;

    /**
     * 库存量
     */
    private Long inventory;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getInventoryMinimum() {
        return inventoryMinimum;
    }

    public void setInventoryMinimum(Long inventoryMinimum) {
        this.inventoryMinimum = inventoryMinimum;
    }

    public Long getInventory() {
        return inventory;
    }

    public void setInventory(Long inventory) {
        this.inventory = inventory;
    }
}
