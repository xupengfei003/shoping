package so.sao.shop.supplier.pojo.input;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by acer on 2017/9/19.
 * 查询商品状态入参实体类
 */
@Getter
@Setter
public class CommodityAuditInput {
    /**
     * 商品状态
     */
    private String status;
    /**
     * 审核结果
     */
    private String auditResult;
    /**
     * 页数
     */
    private int pageNum;
    /**
     *页面显示条数
     */
    private int pageSize;
    /**
     *商品名称/商品条码／供应商名称
     */
    private String inputvalue;
    /**
     *排序字段
     */
    private Integer sortStatus;

}
