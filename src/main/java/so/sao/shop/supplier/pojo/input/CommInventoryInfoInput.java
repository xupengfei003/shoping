package so.sao.shop.supplier.pojo.input;

import javax.validation.constraints.NotNull;

/**
 * 库存输出数据结构
 * @author gxy on 2017/10/13.
 */
public class CommInventoryInfoInput {
    /**
     * 商品ID
     */
    @NotNull(message = "商品ID不能为空")
    private Long id;

    /**
     * 库存下限
     */
    @NotNull(message = "库存下限不能为空")
    private Long inventoryMinimum;

    /**
     * 库存量
     */
    @NotNull(message = "库存量不能为空")
    private Long inventory;

    /**
     * 库存增减量
     */
    @NotNull(message = "库存增减量不能为空")
    private Long inventoryIncreasement;

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

    public Long getInventoryIncreasement() {
        return inventoryIncreasement;
    }

    public void setInventoryIncreasement(Long inventoryIncreasement) {
        this.inventoryIncreasement = inventoryIncreasement;
    }
}
