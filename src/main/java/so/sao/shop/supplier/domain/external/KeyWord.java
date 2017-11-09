package so.sao.shop.supplier.domain.external;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * 关键字实体类
 * Created by acer on 2017/9/19.
 */
public class KeyWord {

    /**
     * 关键字表ID
     */

    @Getter
    @Setter
    private Long id;

    /**
     * 关键字类型，0-供应商名称，1-商品名称，2-商品品牌
     */
    @Getter
    @Setter
    private int keyWordType;

    /**
     * 关键字名称
     */

    @Getter
    @Setter
    private String keyWordValue;
    /**
     * 操作人（登录的账号名）
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
     * 创建时间（精确到秒）
     */

    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Getter
    @Setter
    private Date createAt;

    /**
     * 更新时间（精确到秒）
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    @Getter
    @Setter
    private Date updateAt;

}
