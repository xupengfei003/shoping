package so.sao.shop.supplier.pojo.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import so.sao.shop.supplier.pojo.output.CommodityAuditinputOutput;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by acer on 2017/9/19.
 */
public class CommodityAuditinputVo {
        /**
         * ID
         */
        private Long id;
        /**
         * 商品ID
         */
        private Long scId;
        /**
         * 供应商ID
         */
        private Long supplierId;
        /**
         * 更新时间
         */
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
        private Date updatedAt;
        /**
         * 创建时间
         */
        @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
        private Date createdAt;
        /**
         * 商品状态
         */
        private int status;
        /**
         * 审核结果
         */
        private int auditResult;
        /**
         * 商品数据集合
         */
        private CommodityAuditinputOutput commodityAuditinputOutput;

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

    public Long getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Long supplierId) {
        this.supplierId = supplierId;
    }

    public Date getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getAuditResult() {
        return auditResult;
    }

    public void setAuditResult(int auditResult) {
        this.auditResult = auditResult;
    }

    public CommodityAuditinputOutput getCommodityAuditinputOutput() {
        return commodityAuditinputOutput;
    }

    public void setCommodityAuditinputOutput(CommodityAuditinputOutput commodityAuditinputOutput) {
        this.commodityAuditinputOutput = commodityAuditinputOutput;
    }
}
