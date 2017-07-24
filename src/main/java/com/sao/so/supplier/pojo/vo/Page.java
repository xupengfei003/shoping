package com.sao.so.supplier.pojo.vo;

/**
 * Created by acer on 2017/7/17.
 */
public class Page {
    /**
     * 页面显示的页数
     */
    private Integer pageNum;
    /**
     * 每页显示的行数
     */
    private Integer pageSize;

    public Page() {
    }

    public Page(Integer pageNum, Integer pageSize) {
        this.pageNum = pageNum;
        this.pageSize = pageSize;
    }

    public Integer getPageNum() {
        return pageNum;
    }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getRows() {
        return pageSize;
    }

    public void setRows(Integer rows) {
        this.pageSize = rows;
    }
}
