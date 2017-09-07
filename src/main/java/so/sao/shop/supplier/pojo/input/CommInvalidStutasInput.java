package so.sao.shop.supplier.pojo.input;


import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

public class CommInvalidStutasInput {

    /**
     * 供应商id
     */
    private Long supplierId ;

    /**
     * 供应商状态
     */
    private Integer invalidStatus;

    /**
     * 修改时间
     */
    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date updatedAt;

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Integer getInvalidStatus() {
        return invalidStatus;
    }

    public void setInvalidStatus(Integer invalidStatus) {
        this.invalidStatus = invalidStatus;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }
}
