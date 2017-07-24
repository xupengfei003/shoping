package com.sao.so.shop.service.impl;

import com.sao.so.shop.dao.SysRegionDao;
import com.sao.so.shop.domain.SysRegion;
import com.sao.so.shop.service.SysRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by acer on 2017/7/22.
 */
@Service
public class SysRegionServiceImpl implements SysRegionService {
    @Autowired
    private SysRegionDao sysRegionDao;

    /**
     * 根据上级id查询下级
     * @param pid 上级id
     * @return 下级列表
     */
    @Override
    public List<SysRegion> findByPid(Integer pid) {
        return sysRegionDao.findByPid(pid);
    }
}
