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
     * �����ϼ�id��ѯ�¼�
     * @param pid �ϼ�id
     * @return �¼��б�
     */
    @Override
    public List<SysRegion> findByPid(Integer pid) {
        return sysRegionDao.findByPid(pid);
    }
}
