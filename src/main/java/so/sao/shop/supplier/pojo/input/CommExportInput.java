package so.sao.shop.supplier.pojo.input;
import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(value = "供应商id", required = true)
    private Long supplierId;

    @ApiModelProperty(value = "商品条码")
    private String commCode69;

    @ApiModelProperty(value = "商品sku")
    private String sku;

    @ApiModelProperty(value = "商品商家编码")
    private String code;

    @ApiModelProperty(value = "商品名称")
    private String commName;

    @ApiModelProperty(value = "商品状态")
    private Integer invalidStatus;
    @ApiModelProperty(value = "商品状态")
    private Integer status;
    @ApiModelProperty(value = "审核结果")
    private Integer auditResult;
    @ApiModelProperty(value = "商品科属id")
    private Long typeId;

    @ApiModelProperty(value = "app订货价最小价格")
    private BigDecimal minPrice;

    @ApiModelProperty(value = "app订货价最大价格")
    private BigDecimal maxPrice;
    @ApiModelProperty(value = "透云进货价最小价格")
    private BigDecimal minUnitPrice;

    @ApiModelProperty(value = "透云进货价最大价格")
    private BigDecimal maxUnitPrice;

    @ApiModelProperty(value = "更新开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE)
    private Date beginUpdateAt;

    @ApiModelProperty(value = "更新结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE)
    private Date endUpdateAt;

    @ApiModelProperty(value = "商品条码/商品名称模糊")
    private String inputvalue;
    @ApiModelProperty(value = "商品品牌")
    private String commBrand;

    @ApiModelProperty(value = "页数")
    private String pageNum;

    @ApiModelProperty(value = "每页显示条数")
    private Integer pageSize;
    @ApiModelProperty(value = "排序")
    private Integer sortStatus;

}
