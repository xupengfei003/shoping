package so.sao.shop.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.CommCategoryDao;
import so.sao.shop.supplier.domain.CommCategory;
import so.sao.shop.supplier.pojo.output.CategoryOutput;
import so.sao.shop.supplier.service.CommCategoryService;
import so.sao.shop.supplier.util.BeanMapper;
import so.sao.shop.supplier.util.Ognl;

import java.util.*;

/**
 * 商品品类维护Service
 * Created by QuJunLong on 2017/7/18.
 */
@Service
public class CommCategoryServiceImpl implements CommCategoryService {

    @Autowired
    private CommCategoryDao commCategoryDao;

    /**
     * 查询商品品类列表；
     * @param pid
     * @return CommCategorySelectOutput
     */
    @Override
    public List<CategoryOutput> searchCommCategory(Long pid) {
        List<CategoryOutput> list = new ArrayList<>();
        //将CommCategory遍历添加到一个新数组里面，新数组存放需要的对象属性值
        List<CommCategory> tempList = commCategoryDao.find(pid);
        tempList.forEach( commCategory ->{
            CategoryOutput commCategorySelectOutput = BeanMapper.map(commCategory , CategoryOutput.class);
            list.add(commCategorySelectOutput);
        });
        return list;
    }

    /**
     * 递归查询商品类型列表
     * @return
     */
    @Override
    public Map<String,Object> searchCommCategorys() {
        //获取所有分级数据
        List<CommCategory> allList = commCategoryDao.findAll();
        //根据ID分成单条,根据PID分组
        Map<Long ,CommCategory> map = new HashMap<>();
        Map<Long ,List<CommCategory>> pMap = new HashMap<>();
        for (CommCategory comm:allList) {
            //根据ID分成单条
            map.put(comm.getId(),comm);
            //根据PID分组
            if (Ognl.isNull(pMap.get(comm.getPid()))){
                List<CommCategory> list = new ArrayList<>();
                list.add(comm);
                pMap.put(comm.getPid(),list);
            }else {
                List<CommCategory> list = pMap.get(comm.getPid());
                list.add(comm);
                pMap.put(comm.getPid(),list);
            }
        }
        //取出pid为0的id
        List<CommCategory> topList = pMap.get(0L);
        //组装返回数据
        Map<String,Object> resultMap = new LinkedHashMap<>();
        List childrenList = new ArrayList();
        for (CommCategory com:topList) {
            Map<String,Object> children= nodeTree(map,pMap,com.getId());
            childrenList.add(children);
        }
        resultMap.put("options",childrenList);
        return resultMap;
    }

    /**
     * 根据id查询其子节点集
     * @param map
     * @param pMap
     * @param id
     * @return
     */
    private Map<String,Object> nodeTree(Map<Long ,CommCategory> map,Map<Long ,List<CommCategory>> pMap ,Long id ){
        Map<String,Object> resultMap = new LinkedHashMap<>();
        //根据id获取节点对象
        CommCategory commCategory= map.get(id);
        //封装对象
        resultMap.put("value",commCategory.getId());
        resultMap.put("label",commCategory.getName());
        //查询该id下的所有子节点
        List<CommCategory> list= pMap.get(id);
        if (Ognl.isNull(list)){
            return resultMap;
        }
        //子节点集合
        List childrenList = new ArrayList();
        for (CommCategory comm:list) {
            Map<String,Object> ccvo = nodeTree(map,pMap,comm.getId());
            childrenList.add(ccvo);
        }
        resultMap.put("children",childrenList);
        return resultMap;
    }
}
