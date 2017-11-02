package so.sao.shop.supplier.service.external.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.external.CommCategoryPortalDao;
import so.sao.shop.supplier.domain.CommCategory;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommCategoryListInput;
import so.sao.shop.supplier.pojo.input.CommCategoryPortalInput;
import so.sao.shop.supplier.service.external.CommCategoryPortalService;
import so.sao.shop.supplier.util.Ognl;

import java.util.*;

/**
 *<p>Version: supplier V1.1.0 </p>
 *<p>Title: CommCategoryPortalServiceImpl</p>
 *<p>Description: </p>
 *@author: hanchao
 *@Date: Created in 2017/10/27 13:53
 */
@Service
public class CommCategoryPortalServiceImpl implements CommCategoryPortalService {

    /**
     * 初始化日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private CommCategoryPortalDao commCategoryPortalDao;

    /**
     * 批量修改商品分类名称和隐藏状态
     * @param commCategoryListInput
     * @throws Exception
     * @return Result
     */
    @Override
    public Result updateCommCategorys(CommCategoryListInput commCategoryListInput) throws Exception {
            for (CommCategoryPortalInput commCategoryPortalInput : commCategoryListInput.getCommCategoryPortalInputs()) {
                commCategoryPortalInput.setUpdateTime(new Date());
        }
        commCategoryPortalDao.updateCommCategorys(commCategoryListInput.getCommCategoryPortalInputs());
        return Result.success("修改商品类别名称成功！");
    }

    /**
     * 递归查询商品分类列表
     * @return Map<String,Object>
     */
    @Override
    public Map<String,Object> searchCommCategorys() {
        //获取所有分类数据
        List<CommCategory> allList = commCategoryPortalDao.findAll();
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
     * @return Map<String,Object>
     */
    private Map<String,Object> nodeTree(Map<Long ,CommCategory> map, Map<Long ,List<CommCategory>> pMap , Long id ){
        Map<String,Object> resultMap = new LinkedHashMap<>();
        //根据id获取节点对象
        CommCategory commCategory= map.get(id);
        //封装对象
        resultMap.put("value",commCategory.getId());
        resultMap.put("label",commCategory.getName());
        resultMap.put("status",commCategory.getStatus());
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