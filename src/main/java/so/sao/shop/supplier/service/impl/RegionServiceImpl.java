package so.sao.shop.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.RegionMapper;
import so.sao.shop.supplier.domain.Region;
import so.sao.shop.supplier.pojo.output.RegionOutput;
import so.sao.shop.supplier.service.RegionService;
import so.sao.shop.supplier.util.Ognl;

import java.util.*;
import java.util.stream.Collectors;

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

        //从数据中一次查出所有的省市区数据List
        List<Region> allList = districtDicDao.getAllRegion();

        //根据srId分成单条,根据parentId分组
        Map<Integer, Region> map = new HashMap<>();
        Map<Integer, List<Region>> pMap = allList.stream().collect(Collectors.groupingBy(Region::getParentId));
        for (Region region : allList) {
            //根据srId分成单条
            map.put(region.getSrId(), region);
        }

        //取出parentId为0的List<Region>
        List<Region> topList = pMap.get(0);

        //组装返回数据
        Map<String, Object> resultMap = new LinkedHashMap<>();
        List childrenList = new ArrayList();
        for (Region region : topList) {
            Map<String, Object> children = nodeTree(map, pMap, region.getSrId());
            childrenList.add(children);
        }
        resultMap.put("options", childrenList);
        return resultMap;
    }

    /**
     * 递归查询省->市->区(县)数据
     * @param map 省市区所有数据 key:srId，value:Region
     * @param pMap parentId对应的子节点列表的map  key:parentId，value:List<Region> </>
     * @param srId
     * @return
     */
    private Map<String,Object> nodeTree(Map<Integer, Region> map, Map<Integer, List<Region>> pMap , Integer srId ) {
        Map<String,Object> resultMap = new LinkedHashMap<>();
        //根据srId获取该对象
        Region region = map.get(srId);
        resultMap.put("value", region.getSrId());
        resultMap.put("label", region.getName());

        //查询该srId下的所有子节点
        List<Region> list = pMap.get(srId);

        //判断该节点下是否还有子节点，如果没有，则返回；否则，继续递归查询；
        if (Ognl.isNull(list)){
            return resultMap;
        }
        //子节点List
        List childrenList = new ArrayList();
        for (Region tmpRegion:list) {
            Map<String,Object> vo = nodeTree(map, pMap, tmpRegion.getSrId());
            childrenList.add(vo);
        }
        resultMap.put("children",childrenList);
        return resultMap;
    }

}
