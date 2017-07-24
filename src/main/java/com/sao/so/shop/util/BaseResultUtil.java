package com.sao.so.shop.util;

import com.sao.so.shop.config.Constant;
import com.sao.so.shop.pojo.BaseResult;

/**
 * Created by liugang on 2017/7/22.
 */
public class BaseResultUtil {
    public static BaseResult transTo(Boolean flag, String successMessage, String failMessage)
    {
        BaseResult result = new BaseResult();
        if (flag)
        {
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage(successMessage);
        }else {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage(failMessage);
        }
        return result;
    }
}
