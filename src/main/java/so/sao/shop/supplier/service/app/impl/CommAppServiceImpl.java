package so.sao.shop.supplier.service.app.impl;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.AccountDao;
import so.sao.shop.supplier.dao.CommImgeDao;
import so.sao.shop.supplier.dao.SupplierCommodityDao;
import so.sao.shop.supplier.dao.app.CommAppDao;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.domain.CommImge;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.output.*;
import so.sao.shop.supplier.pojo.vo.CategoryVo;
import so.sao.shop.supplier.pojo.vo.CommImgeVo;
import so.sao.shop.supplier.service.CountSoldCommService;
import so.sao.shop.supplier.service.app.CommAppService;
import so.sao.shop.supplier.util.BeanMapper;
import so.sao.shop.supplier.util.DataCompare;
import so.sao.shop.supplier.util.PageTool;

import java.math.BigDecimal;

import java.util.*;

@Service
public class CommAppServiceImpl implements CommAppService {

    @Autowired
    private CommAppDao commAppDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private CountSoldCommService countSoldCommService;
    @Autowired
    private CommImgeDao commImgeDao;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 根据查询条件查询商品详情
     * @param sku          SKU(商品ID)
     * @param supplierId     供应商ID
     * @param code69       商品条码
     * @param suppCommCode 商家商品编码
     * @param inputValue   输入值（商品名称或科属名称）
     * @param minPrice     价格（低）
     * @param maxPrice     价格（高）
     * @param pageNum      当前页号
     * @param pageSize     页面大小
     * @return PageInfo pageInfo对象
     */
    @Override
    public Result searchAllCommodities(Long supplierId, String sku, String code69, String suppCommCode, String inputValue, BigDecimal minPrice, BigDecimal maxPrice, Integer pageNum, Integer pageSize) {

        //金额校验
        String msg = DataCompare.priceCheck(minPrice,maxPrice);
        if(null != msg && msg.length()>0) {
            return Result.fail(msg);
        }
        //开始分页
        PageTool.startPage(pageNum,pageSize);
        List<CommAppSeachOutput> respList  = commAppDao.findAll(supplierId, sku, code69, suppCommCode, inputValue, minPrice, maxPrice);
        PageInfo<CommAppSeachOutput> pageInfo = new PageInfo<CommAppSeachOutput>(respList);
        return Result.success("查询成功",pageInfo);
    }

    /**
     * 根据code69查询供应商列表
     * @param code69
     * @return 供应商列表
     */
    @Override
    public Result searchSuppliers(String code69) {
        List<AccountOutput> list = commAppDao.searchSuppliersByCode69(code69);
        return Result.success("成功",list);
    }


    /**
     * 查询商品科属
     * @return Result结果集
     */
    @Override
    public Result searchCategories() {
        List<CategoryOutput> categoryOutputs = commAppDao.searchCategories();
        return Result.success("查询商品科属成功",categoryOutputs);
    }

    /**
     * 模糊查询品牌名
     * @param name
     * @return Result结果集(品牌的ID,name)
     */
    @Override
    public Result getBrandName(String name) {
        List<CommBrandOutput> brandNameList = commAppDao.findBrandName(name);
        return Result.success("查询成功",brandNameList);
    }

    /**
     * 根据供应商ID或名称查询供应商详情列表
     * @param accountId 供应商ID
     * @param providerName 供应商名称
     * @param pageNum 当前页号
     * @param pageSize 页面大小
     * @return Result Result对象（供应商详情列表）
     */
    @Override
    public Result getSuppliers(Long accountId, String providerName, Integer pageNum, Integer pageSize) {

        //开始分页
        PageTool.startPage(pageNum,pageSize);
        List<AccountOutput> accountList  = accountDao.findAccounts(accountId, providerName);
        PageInfo<AccountOutput> pageInfo = new PageInfo<AccountOutput>(accountList);
        return Result.success("查询成功",pageInfo);
    }



    /**
     * 根据供应商ID或商品名称或分类或品牌ID查询商品信息
     *
     * @param supplierId 供应商id
     * @param commName 商品名称
     * @param categoryOneId 一级分类id
     * @param categoryTwoId 二级分类id
     * @param categoryThreeId 三级分类id
     * @param brandIds 品牌集合
     * @param pageNum 当前页码
     * @param pageSize 页面大小
     * @return
     */
    @Override
    public Result getCommodities(Long supplierId, String commName, Long categoryOneId, Long categoryTwoId, Long categoryThreeId, Long[] brandIds, Integer pageNum, Integer pageSize) {

        //开始分页
        PageTool.startPage(pageNum,pageSize);
        List<CommAppOutput> commList  = commAppDao.findCommodities(supplierId,commName, categoryOneId, categoryTwoId, categoryThreeId, brandIds);
        PageInfo<CommAppOutput> pageInfo = new PageInfo<CommAppOutput>(commList);
        return Result.success("查询成功",pageInfo);
    }

    /**
     *
     * @param supplierId 供应商ID
     * @return
     */
    @Override
    public Result getMainCateGory(Long supplierId) {
        List<CategoryVo> categoryOutputs=commAppDao.findMainCateGory(supplierId);
        return Result.success("查询成功",categoryOutputs);
    }

    /**
     * 根据科属的等级参数获取所有的2或3级科属
     * @param level
     * @return
     */
    public Result getAllLevelTwoOrThreeCategories(Integer level){
        List<CategoryOutput> categoryOutputList = commAppDao.findCategories(level);;
        return Result.success("查询成功",categoryOutputList);
    }

    /**
     *根据条件 获取商品的全部品牌
     * @param categoryId
     * @return
     */
    public Result getAllBrands( Integer categoryId ){
        List<CommBrandOutput>  commBrandOutputList = commAppDao.findAllBrands( categoryId );
        return  Result.success("成功",commBrandOutputList);
    }

    /**
     * 根据动态条件(供应商ID/分类/品牌ids/排序条件)查询商品
     * @param categoryTwoId
     * @param categoryThreeId
     * @param brandIds
     * @param orderPrice
     * @param orderSalesNum
     * @param pageNum
     * @param pageSize
     * @return
     */
    public Result searchCommodities(Long categoryTwoId,Long categoryThreeId,Long[] brandIds, String  orderPrice, String orderSalesNum, Integer pageNum, Integer pageSize){
        //开始分页
        PageTool.startPage(pageNum,pageSize);
        List<CommAppOutput> commAppOutputList = commAppDao.findCommoditiesByConditionOrder(categoryTwoId, categoryThreeId,
                brandIds, orderPrice );
        String [] ArrGoodIds = new String[commAppOutputList.size()];
        try {
            if( null != commAppOutputList && commAppOutputList.size() > 0 ){
                for ( int i=0 ; i< commAppOutputList.size(); i++  ){
                    ArrGoodIds [i] = commAppOutputList.get(i).getId() + "";
                }
                List<String> salesNum = countSoldCommService.countSoldCommNum( ArrGoodIds );
                // 拿到对应商品id 的销量， 并且赋值给CommAppOutput的销量属性
                for( int i =0; i< commAppOutputList.size(); i++ ){
                    commAppOutputList.get(i).setSaleNum( Integer.valueOf( salesNum.get(i) ) );
                }
                // 判断 是否 指定 按照 销量 排序
                if (null != orderSalesNum  &&  "orderSales".equalsIgnoreCase( orderSalesNum ) ){
                    Collections.sort(commAppOutputList , new Comparator<CommAppOutput>(){
                        public int compare(CommAppOutput commOne, CommAppOutput commTwo) {
                            if( commOne.getSaleNum() < commTwo.getSaleNum() ){
                                return 1;
                            }
                            if( commOne.getSaleNum() == commTwo.getSaleNum() ){
                                return 0;
                            }
                            return -1;
                        }
                    });
                }
            }else {
                return Result.fail("暂无数据");
            }
        }catch (Exception e){
            logger.error("查询异常", e);
            return  Result.fail("查询异常",commAppOutputList);
        }
        PageInfo<CommAppOutput> pageInfo = new PageInfo<CommAppOutput>(commAppOutputList);
        return Result.success("查询成功",pageInfo);
    }

    @Override
    public Result listCommodities(Long supplierId, String commName, Integer pageNum, Integer pageSize) {
        //开始分页
        PageTool.startPage(pageNum,pageSize);
        List<CommodityOutput> commodityOutputList =commAppDao.listCommodities(supplierId,commName);
        commodityOutputList.forEach(commodityOutput->{
            List<CommImge> commImgeList = commImgeDao.find(commodityOutput.getId());
            List<CommImgeVo> commImgeVoList = new ArrayList<>();
            commImgeList.forEach(commImge->{
                CommImgeVo commImgeVo = BeanMapper.map(commImge, CommImgeVo.class);
                commImgeVoList.add(commImgeVo);
            });
            //获取销量
            List<String> countSold= null;
            try {
                countSold = countSoldCommService.countSoldCommNum(new String[]{commodityOutput.getId().toString()});
            } catch (Exception e) {
                e.printStackTrace();
            }
            //获取账户account对象
            Account account=accountDao.selectById(commodityOutput.getSupplierId());
            if (null!=account){
                commodityOutput.setProviderName(account.getProviderName());  //将获取供应商名称放入出参
                commodityOutput.setContractCity(account.getContractRegisterAddressCity());  //将获取供应商合同所在市放入出参
                commodityOutput.setSalesNumber(Integer.valueOf(countSold.get(0)));     //将获取销量放入出参
                commodityOutput.setImgeList(commImgeVoList);  //将获取图片信息放入出参
            }
        });
        PageInfo<CommAppOutput> pageInfo = new PageInfo(commodityOutputList);
        return Result.success("查询成功",pageInfo);
    }

    /**
     * 根据商品名称模糊查询商品，返回商品列表
     *
     * @param goodsName 商品名称
     * @return
     */
    @Override
    public Result getGoods(String goodsName) {
        return Result.success("查询成功", commAppDao.findGoodsByName(goodsName));
    }


}
