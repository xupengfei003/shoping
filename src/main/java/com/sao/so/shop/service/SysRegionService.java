package com.sao.so.shop.service;

import com.sao.so.shop.domain.SysRegion;

import java.util.List;

/**
 * Created by acer on 2017/7/22.
 */
public interface SysRegionService {
    /**
     * 根据上级id查询下级
     * @param pid 上级id
     * @return 下级列表
     */
    List<SysRegion> findByPid(Integer pid);
}
