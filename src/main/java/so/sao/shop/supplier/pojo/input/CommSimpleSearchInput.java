package so.sao.shop.supplier.pojo.input;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
@Getter
@Setter
public class CommSimpleSearchInput {

    /**
     * 供应商id
     */

    private Long supplierId;
    /**
     *审核结果：0代表未通过，1代表通过
     */
    private Integer auditResult;
    /**
     * 商品状态（2:上架、3:下架、4:上架待审核、5:下架待审核、6: 编辑待审核）
     */
    private Integer status;

    /**
     * 商品条码/商品名称模糊
     */
    private String inputvalue;

    /**
     * 创建开始时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE)
    private Date beginCreateAt;

    /**
     * 创建结束时间
     */
    @DateTimeFormat(pattern = "yyyy-MM-dd",iso= DateTimeFormat.ISO.DATE)
    private Date endCreateAt;

    /**
     * 页数
     */
    private Integer pageNum;

    /**
     * 每页条数
     */
    private Integer pageSize;
    /**
     *排序字段
     */
    private Integer sortStatus;

    @Override
    public String toString() {
        return "CommSimpleSearchInput{" +
                "supplierId=" + supplierId +
                ", auditResult=" + auditResult +
                ", status=" + status +
                ", inputvalue='" + inputvalue + '\'' +
                ", beginCreateAt=" + beginCreateAt +
                ", endCreateAt=" + endCreateAt +
                ", pageNum=" + pageNum +
                ", pageSize=" + pageSize +
                ", sortStatus=" + sortStatus +
                '}';
    }
}
