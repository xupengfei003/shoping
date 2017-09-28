package so.sao.shop.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.SysRegionDao;
import so.sao.shop.supplier.domain.SysRegion;
import so.sao.shop.supplier.service.SysRegionService;

import java.util.List;

/**
 * Created by acer on 2017/7/22.
 */
@Service
public class SysRegionServiceImpl implements SysRegionService {
    @Autowired
    private SysRegionDao sysRegionDao;

    /**
     * 获取地区下级列表
     *
     * @param pid 地区id
     * @return 返回下级列表
     */
    @Override
    public List<SysRegion> findByPid(Integer pid) {
        return sysRegionDao.findByPid(pid);
    }
}
