package com.sao.so.supplier.pojo.input;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

/**
 * 商品类型入参
 * 用于商品类型修改功能使用
 * Created by QuJunLong on 2017/7/17.
 */
public class CommCategoryUpdateInput {
    /**
     * ID
     */
    private Long id;
    /**
     *  类型名称
     */
    @NotBlank(message = "类型名称不能为空")
    @Length(max = 10,message = "商品类型名称长度不能超过10")
    private String name;
    /**
     *  备注
     */
    @NotBlank(message = "商品类型备注信息不能为空")
    @Length(max = 20,message = "商品类型备注信息长度不能超过20")
    private String remark;
    /**
     *  类型顺序
     */
    private int sort;

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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }
}
