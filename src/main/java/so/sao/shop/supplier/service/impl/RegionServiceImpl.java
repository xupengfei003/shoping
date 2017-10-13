package so.sao.shop.supplier.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.RegionMapper;
import so.sao.shop.supplier.domain.Region;
import so.sao.shop.supplier.pojo.output.RegionOutput;
import so.sao.shop.supplier.service.RegionService;

import java.util.*;

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

    /**
     * 获取所有省市区数据
     * @return
     */
    @Override
    public Map<String, Object> getAllRegion() {
        //返回的map
        Map<String, Object> map = new HashMap<>();

        //从数据中一次查出所有的省市区数据List
        List<Region> allList = districtDicDao.getAllRegion();

        //构造根节点，即省的数据List
        List<Region> tmpProvinceList = new ArrayList<>();

        //构造非省的数据List
        List<Region> notParentList = new ArrayList<>();

        if (null == allList || allList.isEmpty()) {
            return map;
        }

        for (Region region : allList) {
            int level = region.getLevel();
            if (level == 0) {
                //获取根节点，即把省的数据添加到tmpShengList
                tmpProvinceList.add(region);
            } else {
                //把非省的数据添加到notParentList
                notParentList.add(region);
            }
        }
        map.put("options", getChildrenRegion(tmpProvinceList, notParentList));
        return map;
    }

    /**
     * 根据省查出子节点数据
     * @param provinceList 省的数据List
     * @param notParentList 非省的数据List
     * @return
     */
    public List<Map> getChildrenRegion(List<Region> provinceList, List<Region> notParentList) {
        List<Map> returnList = new ArrayList<>();
        for (Region region : provinceList) {
            Map map = new LinkedHashMap();
            map.put("value", region.getSrId());
            map.put("label",  region.getName());
            map.put("children", getChild(map, notParentList));
            returnList.add(map);
        }
        return returnList;
    }

    /**
     * 递归查询省->市->区(县)数据
     * @param map 键为value、label的map
     * @param notParentList 非省的数据List
     * @return
     */
    public List<Map> getChild(Map map, List<Region> notParentList) {
        Integer tmpSrId = (Integer) map.get("value");
        List<Map> childList = new ArrayList<>();
        for (int i = 0; i < notParentList.size(); i++) {
            Region notParentRegion = notParentList.get(i);
            Integer tmpParentId = notParentRegion.getParentId();
            if (tmpSrId.equals(tmpParentId)) {
                Map currentMap = new LinkedHashMap();
                currentMap.put("value", notParentRegion.getSrId());
                currentMap.put("label", notParentRegion.getName());
                notParentList.remove(notParentRegion);
                i--;
                List<Map> tmpChildList = getChild(currentMap, notParentList);
                //判断是否为叶子节点，如果不是的话，继续递归查询
                if (tmpChildList != null && tmpChildList.size() > 0) {
                    currentMap.put("children", tmpChildList);
                }
                childList.add(currentMap);
            }
        }
        return childList;
    }

}
