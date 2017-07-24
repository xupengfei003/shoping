package com.sao.so.supplier.service.impl;

import com.sao.so.supplier.dao.SysRegionDao;
import com.sao.so.supplier.domain.SysRegion;
import com.sao.so.supplier.service.SysRegionService;
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
     * �����ϼ�id��ѯ�¼�
     * @param pid �ϼ�id
     * @return �¼��б�
     */
    @Override
    public List<SysRegion> findByPid(Integer pid) {
        return sysRegionDao.findByPid(pid);
    }
}
