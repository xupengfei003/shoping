package so.sao.shop.supplier.pojo.input;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.math.BigDecimal;
import java.util.Date;

/**
 * Created by Renzhiping on 2017/9/8.
 */
@Getter
@Setter
public class CommExportInput {
    private Long supplierId;

    private String commCode69;

    private String sku;

    private String code;

    private String commName;

    private Integer invalidStatus;
    private Integer status;
    private Integer auditResult;
    private Long typeId;

    private BigDecimal minPrice;

    private BigDecimal maxPrice;
    private BigDecimal minUnitPrice;

    private BigDecimal maxUnitPrice;

//    @/ApiModelProperty(value = "更新开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE)
    private Date beginUpdateAt;

//    @ApiModelProperty(value = "更新结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE)
    private Date endUpdateAt;

//    @ApiModelProperty(value = "商品条码/商品名称模糊")
    private String inputvalue;
//    @ApiModelProperty(value = "商品品牌")
    private String commBrand;

//    @ApiModelProperty(value = "页数")
    private String pageNum;

//    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;
//    @ApiModelProperty(value = "排序")
    private Integer sortStatus;

}
