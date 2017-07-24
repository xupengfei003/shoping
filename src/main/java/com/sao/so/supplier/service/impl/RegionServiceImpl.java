package com.sao.so.supplier.service.impl;


import com.sao.so.supplier.config.Constant;
import com.sao.so.supplier.dao.RegionMapper;
import com.sao.so.supplier.domain.Region;
import com.sao.so.supplier.pojo.output.RegionOutput;
import com.sao.so.supplier.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 行政区字典表 服务实现类
 * </p>
 *
 * @author zhangruibing
 * @since 2017-07-17
 */
@Service
public class RegionServiceImpl implements RegionService {

    @Autowired
    private RegionMapper districtDicDao;

    @Override
    public RegionOutput getListForRegion(Integer parentId, Integer level) {
        RegionOutput output = new RegionOutput();
        try{
            List<Region> list = districtDicDao.findListForRegion(parentId, level);
            if(list != null && list.size() > 0) {
                output.setCode(Constant.CodeConfig.CODE_SUCCESS);
                output.setMessage(Constant.MessageConfig.MSG_SUCCESS);
                output.setRegionList(list);
            } else {
                output.setCode(Constant.CodeConfig.CODE_NOT_FOUND_RESULT);
                output.setMessage(Constant.MessageConfig.MSG_NOT_FOUND_RESULT);
                output.setRegionList(null);
            }
        } catch (Exception e) {
            output.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
            output.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
            output.setRegionList(null);
        }

        return output;
    }
}
