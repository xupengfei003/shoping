
package so.sao.shop.supplier.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

/**
 * 商品箱规单位
 * @author chensha
 */
public class CommCarton {
    /**
     * ID
     */
    private Long id;
    /**
     * 箱规单位名称
     */
    @NotEmpty(message = "箱规单位名称不能为空")
    private String name;
    /**
     * 供应商ID （管理员supplierId=0)
     */
    @NotEmpty(message = "供应商ID不能为空")
    private Long supplierId;
    /**
     *创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date createdAt;
    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date updatedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
