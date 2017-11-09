package so.sao.shop.supplier.pojo.input;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;
@Getter
@Setter
public class CommSearchInput {

    /**
     * 供应商id
     */
    private Long supplierId;

    /**
     * 商品条码
     */
    private String commCode69;

    /**
     * 商家编码
     */
    private String code;


    /**
     * 商品商家品牌
     */
    private String commBrand;

    /**
     * 商品名称
     */
    private String commName;

    /**
     * 商品状态
     */
    private Integer status;

    /**
     *	一级类型ID
     */
    private Long typeOneId;
    /**
     *  二级类型ID
     */
    private Long typeTwoId;
    /**
     *三级类型ID
     */
    private Long typeThreeId;
    /**
     * 供货售价[低]
     */
    private BigDecimal minPrice;
    /**
     * 供货售价[高]
     */
    private BigDecimal maxPrice;
    /**
     * 透云进货价[低]
     */
    private BigDecimal minUnitPrice;
    /**
     * 透云进货价[高]
     */
    private BigDecimal maxUnitPrice;
    /**
     * 更新时间[前]
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss",iso= DateTimeFormat.ISO.DATE)
    private Date beginUpdateAt;
    /**
     * 更新时间[后]
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss",iso= DateTimeFormat.ISO.DATE)
    private Date endUpdateAt;
    /**
     * 当前页号
     */
    private Integer pageNum;
    /**
     * 页面大小
     */
    private Integer pageSize;
    /**
     * 审核结果：0代表未通过，1代表通过
     */
    private Integer auditResult;
    /**
     * 供应商名称
     */
    private String supplierName;


    /**
     * 用来区分管理员的字段
     */
    private String role;
    /**
     * 排序字段
     */
    private Integer sortStatus;

    @Override
    public String toString() {
        return "CommSearchInput{" +
                "supplierId=" + supplierId +
                ", commCode69='" + commCode69 + '\'' +
                ", code='" + code + '\'' +
                ", commBrand='" + commBrand + '\'' +
                ", commName='" + commName + '\'' +
                ", status=" + status +
                ", typeOneId=" + typeOneId +
                ", typeTwoId=" + typeTwoId +
                ", typeThreeId=" + typeThreeId +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", minUnitPrice=" + minUnitPrice +
                ", maxUnitPrice=" + maxUnitPrice +
                ", beginUpdateAt=" + beginUpdateAt +
                ", endUpdateAt=" + endUpdateAt +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", auditResult=" + auditResult +
                ", sortStatus=" + sortStatus +
                ", supplierName='" + supplierName + '\'' +
                ", role='" + role + '\'' +
                '}';
    }
}
