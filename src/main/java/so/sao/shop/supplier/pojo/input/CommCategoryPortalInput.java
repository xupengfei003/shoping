package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 *<p>Version: supplier V1.1.0 </p>
 *<p>Title: CommCategoryPortalInput</p>
 *<p>Description: </p>
 *@author: hanchao
 *@Date: Created in 2017/10/27 16:01
 */
public class CommCategoryPortalInput {
    /**
     * 商品分类id
     */
    @NotNull(message = "商品分类id不能为空!")
    private Long id;

    /**
     * 商品分类名称
     */
    @NotBlank(message = "商品分类名称不能为空!")
    @Length(max = 50,message = "商品分类名称大小不能超过50位!")
    private String name;

    /**
     * 商品分类显示状态（0是隐藏，1是显示）
     */
    @NotNull(message = "商品分类显示状态不能为空!")
    private Integer status;

    /**
     * 更新时间
     */
    private Date updateTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}