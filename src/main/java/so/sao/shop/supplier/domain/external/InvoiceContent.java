package so.sao.shop.supplier.domain.external;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.validator.constraints.Length;

import java.util.Date;

/**
 * <p>Version: 运维平台 V0.9.0 </p>
 * <p>Title: InvoiceContent</p>
 * <p>Description:运维平台-发票内容实体类</p>
 *
 * @author: sha.chen
 * @Date: Created in 2017/10/30 15:00
 */

@ToString
public class InvoiceContent {

    /**
     * 发票内容id
     */
    @Getter
    @Setter
    private Long id;

    /**
     * 发票内容名称
     */
    @Getter
    @Setter
    @Length(max = 20)
    private String invoiceContentName;

    /**
     * 操作人（即登陆的账号名）
     */
    @Getter
    @Setter
    private String operator;

    /**
     * 排序
     */
    @Getter
    @Setter
    private int sort;

    /**
     * 创建时间
     */
    @Getter
    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GTM+8")
    private Date createAt;

    /**
     * 更新时间
     */
    @Getter
    @Setter
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GTM+8")
    private Date updateAt;

}
