package so.sao.shop.supplier.domain;


import java.util.Date;

/**
 * <p>Version: supplier V1.1.0 </p>
 * <p>Title: CommSales</p>
 * <p>Description: 产品销量实体类</p>
 * @author: liugang
 * @Date: Created in 2017/10/30 14:37
 */
public class CommSales {
    /**
     * id
     */
    private Long id;
    /**
     * 商品ID
     */
    private Long scId;
    /**
     * 实际销量
     */
    private Integer actualSales;
    /**
     * 虚拟销量（100-150之间的随机数）
     */
    private Integer virtualSales;
    /**
     * 创建时间
     */
    private Date createAt;
    /**
     * 修改时间
     */
    private Date updateAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getScId() {
        return scId;
    }

    public void setScId(Long scId) {
        this.scId = scId;
    }

    public Integer getActualSales() {
        return actualSales;
    }

    public void setActualSales(Integer actualSales) {
        this.actualSales = actualSales;
    }

    public Integer getVirtualSales() {
        return virtualSales;
    }

    public void setVirtualSales(Integer virtualSales) {
        this.virtualSales = virtualSales;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
