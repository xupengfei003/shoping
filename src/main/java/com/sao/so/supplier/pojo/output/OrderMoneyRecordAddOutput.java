package com.sao.so.supplier.pojo.output;

import com.sao.so.supplier.pojo.BaseResult;

/**
 * Created by niewenchao on 2017/7/20.
 */
public class OrderMoneyRecordAddOutput extends BaseResult {

    private String recordId;

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }
}
