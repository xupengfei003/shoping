package com.sao.so.shop.pojo.input;

import org.hibernate.validator.constraints.NotBlank;

/**
 * 接收的-商品品类实体类
 * Created by XuPengFei on 2017/7/20.
 */
public class CommCategoryInput {
    /**
     * ID 自动增加，
     */
    //private Long id;
    /**
     *  PID
     */
    private Long pid;
    /**
     *  类型名称
     */
    @NotBlank
    private String name;
    /**
     *  备注
     */
    private String remark;
    /**
     *  类型的排序
     */
    private int sort;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
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
