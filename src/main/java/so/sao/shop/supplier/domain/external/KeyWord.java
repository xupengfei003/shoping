package so.sao.shop.supplier.domain.external;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * 关键字实体类
 * Created by acer on 2017/9/19.
 */
public class KeyWord {

    /**
     * 关键字表ID
     */
    private Long id;

    /**
     * 关键字类型，0-商品科属，1-商品名称，2-商品品牌
     */
    private int keyWordType;

    /**
     * 关键字名称
     */
    private String keyWordValue;
    /**
     * 操作人（登录的账号名）
     */
    private String operator;

    /**
     * 排序
     */
    private int sort;
    /**
     * 创建时间（精确到秒）
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date createAt;

    /**
     * 更新时间（精确到秒）
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="GMT+8")
    private Date updateAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getKeyWordType() {
        return keyWordType;
    }

    public void setKeyWordType(int keyWordType) {
        this.keyWordType = keyWordType;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

    public String getKeyWordValue() {
        return keyWordValue;
    }

    public void setKeyWordValue(String keyWordValue) {
        this.keyWordValue = keyWordValue;
    }

    public String getOperator() {
        return operator;
    }

    public void setOperator(String operator) {
        this.operator = operator;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }
}
