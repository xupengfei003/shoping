package so.sao.shop.supplier.service.external.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.dao.external.HotCommoditiesDao;
import so.sao.shop.supplier.domain.external.HotCommodities;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.AddHotCommInput;
import so.sao.shop.supplier.pojo.input.HotCommodityInput;
import so.sao.shop.supplier.pojo.input.HotCommoditySaveInput;
import so.sao.shop.supplier.pojo.output.AddHotCommOutput;
import so.sao.shop.supplier.service.app.AppCommSalesService;
import so.sao.shop.supplier.service.external.HotCommoditiesService;
import so.sao.shop.supplier.util.BeanMapper;
import so.sao.shop.supplier.util.PageTool;

import java.util.*;

@Service
public class HotCommoditiesServiceImpl implements HotCommoditiesService{

    @Autowired
    private HotCommoditiesDao hotCommoditiesDao;
    @Autowired
    private AppCommSalesService appCommSalesService;

    /**
     * 查询所有热门商品列表
     *
     * @param hotCommodityInput
     * @return
     */
    @Override
    public Result searchHotCommodities(HotCommodityInput hotCommodityInput) {

        PageTool.startPage(hotCommodityInput.getPageNum(), hotCommodityInput.getPageSize());
        List<HotCommodities> hotCommoditiesList =  hotCommoditiesDao.findAll( hotCommodityInput.getInputvalue(),
                    hotCommodityInput.getCategoryOneId(), hotCommodityInput.getCategoryTwoId(), hotCommodityInput.getCategoryThreeId());
        if(hotCommoditiesList == null || hotCommoditiesList.size()==0)
            return Result.fail("暂无热销商品！");
        //统计商品销量
        String [] ArrGoodIds = new String[hotCommoditiesList.size()];
        for ( int i=0 ; i< hotCommoditiesList.size(); i++  ){
            ArrGoodIds [i] = hotCommoditiesList.get(i).getScId().toString();
        }

        List<String> salesVolumes = null;
        try {
            salesVolumes = appCommSalesService.countSoldCommNum( ArrGoodIds );
        } catch (Exception e) {
            return Result.fail("销量统计异常！",e);
        }

        for( int i =0; i< hotCommoditiesList.size(); i++ ){
            hotCommoditiesList.get(i).setSalesVolume( Integer.valueOf( salesVolumes.get(i) ) );
        }
        PageInfo<HotCommodities> pageInfo = new PageInfo<HotCommodities>(hotCommoditiesList);
        return Result.success("查询热门商品列表成功！", pageInfo);
    }

    /**
     * 按照条件查询所有商品列表
     *
     * @param addHotCommInput
     * @return
     */
    @Override
    public Result searchCommodities(AddHotCommInput addHotCommInput) {

        PageTool.startPage(addHotCommInput.getPageNum(), addHotCommInput.getPageSize());
        List<AddHotCommOutput> addHotCommVos =  hotCommoditiesDao.find( addHotCommInput.getInputvalue(),
                addHotCommInput.getCategoryOneId(), addHotCommInput.getCategoryTwoId(), addHotCommInput.getCategoryThreeId());
        if(addHotCommVos == null || addHotCommVos.size()==0)
            return Result.fail("暂无商品！");
        //统计商品销量
        String [] ArrGoodIds = new String[addHotCommVos.size()];
        for ( int i=0 ; i< addHotCommVos.size(); i++  ){
            ArrGoodIds [i] = addHotCommVos.get(i).getScId().toString();
        }

        List<String> salesVolumes = null;
        try {
            salesVolumes = appCommSalesService.countSoldCommNum( ArrGoodIds );
        } catch (Exception e) {
            return Result.fail("销量统计异常！",e);
        }

        for( int i =0; i< addHotCommVos.size(); i++ ){
            addHotCommVos.get(i).setSalesVolume( Integer.valueOf( salesVolumes.get(i) ) );
        }
        PageInfo<AddHotCommOutput> pageInfo = new PageInfo<AddHotCommOutput>(addHotCommVos);
        return Result.success("查询商品列表成功！", pageInfo);
    }

    /**
     * 保存所有热门商品
     *
     * @param hotCommoditySaveInputs
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveAllHotCommodity(List<HotCommoditySaveInput> hotCommoditySaveInputs) {
        if(hotCommoditySaveInputs == null ){
            return Result.fail("热门商品列表不能为空！");
        }
        //验证热门商品列表不能重复
        Set<String> scIds = new TreeSet<>();
        hotCommoditySaveInputs.forEach(hotCommoditySaveInput->{
            String scId = hotCommoditySaveInput.getScId();
            scIds.add(scId);
        });
        if(scIds.size()<hotCommoditySaveInputs.size()){
            return Result.fail("热门商品列表不能重复！");
        }
        if(hotCommoditySaveInputs.size()>30){
            return Result.fail("添加失败！添加的数量大于允许添加的数量");
        }
        //开始保存
        List<HotCommodities> hotCommoditiesList = new ArrayList<>();
        hotCommoditySaveInputs.forEach(hotCommoditySaveInput->{
            HotCommodities hotCommodities = BeanMapper.map(hotCommoditySaveInput, HotCommodities.class);
            hotCommoditiesList.add(hotCommodities);
        });
        for(int i = 0 ; i < hotCommoditiesList.size() ; i++){
            hotCommoditiesList.get(i).setSort(i+1);
            hotCommoditiesList.get(i).setUpdatedAt(new Date());
            hotCommoditiesList.get(i).setCreatedAt(new Date());
        }
        //先清空
        hotCommoditiesDao.truncateHotCommodity();
        hotCommoditiesDao.saveAll( hotCommoditiesList );
        return Result.success("保存成功！", hotCommoditiesList);
    }
}
