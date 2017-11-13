package so.sao.shop.supplier.service.app.impl;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.AccountDao;
import so.sao.shop.supplier.dao.CommImgeDao;
import so.sao.shop.supplier.dao.FreightRulesDao;
import so.sao.shop.supplier.dao.InvoiceSettingDao;
import so.sao.shop.supplier.dao.app.CommAppDao;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.domain.CommImge;
import so.sao.shop.supplier.domain.FreightRules;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommAppInput;
import so.sao.shop.supplier.pojo.input.CommodityAppInput;
import so.sao.shop.supplier.pojo.output.*;
import so.sao.shop.supplier.pojo.vo.CategoryVo;
import so.sao.shop.supplier.pojo.vo.CommImgeVo;
import so.sao.shop.supplier.service.app.AppCommSalesService;
import so.sao.shop.supplier.service.app.CommAppService;
import so.sao.shop.supplier.util.BeanMapper;
import so.sao.shop.supplier.util.DataCompare;
import so.sao.shop.supplier.util.Ognl;
import so.sao.shop.supplier.util.PageTool;

import java.math.BigDecimal;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommAppServiceImpl implements CommAppService {

    @Autowired
    private CommAppDao commAppDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private AppCommSalesService appCommSalesService;
    @Autowired
    private CommImgeDao commImgeDao;
    @Autowired
    private FreightRulesDao freightRulesDao;
    @Autowired
    private InvoiceSettingDao invoiceSettingDao;

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
        if(null == list || list.size()<=0){
            return  Result.fail("暂无数据");
        }
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
     * @param accountId 账户ID
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
        if( null == accountList ||  accountList.size() <= 0 ){
            return  Result.fail("暂无数据");
        }
        PageInfo<AccountOutput> pageInfo = new PageInfo<AccountOutput>(accountList);
        return Result.success("查询成功",pageInfo);
    }



    /**
     * 根据供应商ID或商品名称或分类或品牌ID查询商品信息
     * @param commAppInput 入参
     * @return
     */
    @Override
    public Result getCommodities(CommAppInput commAppInput) throws Exception {

        //开始分页
        PageTool.startPage(commAppInput.getPageNum(),commAppInput.getPageSize());
        List<CommAppOutput> commList  = commAppDao.findCommodities(commAppInput);
        Map<String,String> saleMap = new HashMap<>();
        for (int i = 0; i < commList.size(); i++) {
            CommAppOutput commAppOutput = commList.get(i);
            String goodsId = commAppOutput.getId().toString();
            List<String> countSold = appCommSalesService.countSoldCommNum(new String[]{goodsId});
            commAppOutput.setSaleNum(Integer.valueOf(countSold.get(0)));
        }
        PageInfo<CommAppOutput> pageInfo = new PageInfo(commList);
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
        if( null == categoryOutputs || categoryOutputs.size() <= 0 ){
            return Result.fail("暂无数据");
        }
        return Result.success("查询成功",categoryOutputs);
    }

    /**
     * 根据科属的等级参数获取所有的2或3级科属
     * @param level
     * @return
     */
    @Override
    public Result getAllLevelTwoOrThreeCategories( Long supplierId, Integer level){
        List<CategoryOutput> categoryOutputList = commAppDao.findCategories(supplierId, level);
        if( null == categoryOutputList || categoryOutputList.size() <= 0 ){
            return Result.fail("没有数据");
        }
        return Result.success("查询成功",categoryOutputList);
    }

    /**
     *根据条件 获取所属类型下面的 商品的全部品牌
     * @param categoryId
     * @return
     */
    @Override
    public Result getAllBrands(Long supplierId, Integer categoryId ){
        List<CommBrandOutput>  commBrandOutputList = commAppDao.findAllBrands( supplierId,categoryId );
        if( null == commBrandOutputList || commBrandOutputList.size() <= 0 ){
            return Result.fail("没有数据");
        }
        return  Result.success("成功",commBrandOutputList);
    }

    /**
     * 根据动态条件(供应商ID/分类/品牌ids/排序条件)查询商品
     * @param commodityAppInput
     * @return
     */
    @Override
    public PageInfo<CommAppOutput> searchCommodities( CommodityAppInput commodityAppInput){
        //开始分页
        PageTool.startPage( commodityAppInput.getPageNum(),commodityAppInput.getPageSize() );
        if( null != commodityAppInput.getBrandIds() && commodityAppInput.getBrandIds().length == 0 ){
            commodityAppInput.setBrandIds(null);
        }
        List<CommAppOutput> commAppOutputList = commAppDao.findCommoditiesByConditionOrder( commodityAppInput );
        try {
            if( null != commAppOutputList && commAppOutputList.size() > 0 ){
                String [] ArrGoodIds = new String[commAppOutputList.size()];
                for ( int i=0 ; i< commAppOutputList.size(); i++  ){
                    ArrGoodIds [i] = commAppOutputList.get(i).getId() + "";
                }
                List<String> salesNum = appCommSalesService.countSoldCommNum( ArrGoodIds );
                // 拿到对应商品id 的销量， 并且赋值给CommAppOutput的销量属性
                for( int i =0; i< commAppOutputList.size(); i++ ){
                    commAppOutputList.get(i).setSaleNum( Integer.valueOf( salesNum.get(i) ) );
                }
                // 判断 是否 指定 按照 销量 排序
                if (null != commodityAppInput.getOrderPriceOrSalesNum()  &&  "orderSales".equalsIgnoreCase( commodityAppInput.getOrderPriceOrSalesNum() ) ){
                    Collections.sort(commAppOutputList , new Comparator<CommAppOutput>(){
                        @Override
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
                commAppOutputList = new ArrayList<>();
            }
        }catch (Exception e){
            logger.error("查询异常", e);
            commAppOutputList = new ArrayList<>();
        }
        PageInfo<CommAppOutput> pageInfo = new PageInfo<CommAppOutput>(commAppOutputList);
        return pageInfo;
    }

    /**
     * 根据供应商ID和商品名称查询供应商列表
     * @param supplierId 供应商ID
     * @param commName   商品名称
     * @param pageNum
     * @param pageSize
     * @return
     */
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
                countSold = appCommSalesService.countSoldCommNum(new String[]{commodityOutput.getId().toString()});
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
        List<Map> goods = commAppDao.findGoodsByGoodsName(goodsName);
        if( null == goods || goods.size() <= 0  ){
            return Result.fail("暂无数据");
        }
        return Result.success("查询成功", goods );
    }

	
	
	/**
     * 根据商品名称模糊查询商品，返回商品列表
     *
     * @param name 搜索名称
     * @param nameType 名称类型(0:供应商名称，1：商品名称，2:品牌名称)
     */
    @Override
    public Result getGoods(String name , int nameType) {
        /*
         *如果为0，查询供应商名称右模糊匹配的供应商名称列表，此供应商名下的商品为未删除，已上架，未失效状态
         *如果为1，查询商品名称右模糊匹配的商品名称列表，商品为未删除，已上架，未失效状态
         *如果为2，查询品牌名称右模糊匹配的品牌名称列表，此品牌下的商品为未删除，已上架，未失效状态
         *如果名称类型传其他值，默认查所有与之匹配的商品名列表，品牌对应商品列表，供应商对应商品列表，商品为未删除，已上架，未失效状态
         * */
        List<Map> names = new ArrayList<>();
        switch (nameType){
            case Constant.AppCommSearch.SEARCH_BY_SUPPLIER_NAME:
                names = commAppDao.findGoodsBySupplierName(name);
                break;
            case Constant.AppCommSearch.SEARCH_BY_GOODS_NAME:
                names = commAppDao.findGoodsByGoodsName(name);
                break;
            case Constant.AppCommSearch.SEARCH_BY_BRAND_NAME:
                names = commAppDao.findGoodsByBrandName(name);
                break;
            default:
                names = commAppDao.findGoodsByName(name);
                break;
        }
        if( null == names || names.size() <= 0  ){
            return Result.fail("暂无数据");
        }
        return Result.success("查询成功", names );
    }

    /**
     * 【12】根据供应商商品ID获取商品详情
     * @param id
     * @return
     */
    @Override
    public Result getCommodity(Long id)  {
        //根据供应商商品ID获取商品信息
        CommodityOutput commodityOutput = commAppDao.findDetail(id);
        if(null != commodityOutput){
            //根据供应商商品ID获取图片列表信息
            List<CommImge> commImgeList = commImgeDao.find(id);
            List<CommImgeVo> commImgeVoList = new ArrayList<>();
            commImgeList.forEach(commImge->{
                CommImgeVo commImgeVo = BeanMapper.map(commImge, CommImgeVo.class);
                commImgeVoList.add(commImgeVo);
            });
            List<FreightRules> freightRulesList = freightRulesDao.queryAll0(commodityOutput.getSupplierId(),null);
            commodityOutput.setFreightRulesList(freightRulesList);
            commodityOutput.setImgeList(commImgeVoList);  //将获取图片信息放入出参
            //获取销量
            List<String> countSold= null;
            try {
                countSold = appCommSalesService.countSoldCommNum(new String[]{commodityOutput.getId().toString()});
            } catch (Exception e) {
                logger.info("商品销量获取异常",e);
                e.printStackTrace();
            }

            //将获取销量放入出参
            commodityOutput.setSalesNumber(Integer.valueOf(countSold.get(0)));
            //获取账户account对象
            Account account=accountDao.selectById(commodityOutput.getSupplierId());
            if (null == account){
                return Result.success("查询成功",commodityOutput);
            }
            //  运费规则(0:通用规则 1:配送规则)
            if( 0 ==  account.getFreightRules() ){
                //  是否超出配送范围  1超出配送范围,0没有超出
                 commodityOutput.setOutOfDeliveryRange(0);
            }else {
                commodityOutput.setOutOfDeliveryRange(1);
            }
            commodityOutput.setProviderName(account.getProviderName());  //将获取供应商名称放入出参
            commodityOutput.setContractCity(account.getContractRegisterAddressCity());  //将获取供应商合同所在市放入出参

            //根据供应商信息获取开票设置
            InvoiceSettingOutput InvoiceSettingOutput = invoiceSettingDao.findBySupplierId(commodityOutput.getSupplierId());
            // 设置供应商发票开启状态
            commodityOutput.setIsOpen(InvoiceSettingOutput.getStatus());
            //设置增值税普通发票状态
            commodityOutput.setPlainInvoice(InvoiceSettingOutput.getInvoice());
            //设置增值税专用发票状态
            commodityOutput.setSpecialInvoice(InvoiceSettingOutput.getSpecialInvoice());
        }else {
            return Result.fail("暂无数据");
        }
        return Result.success("查询成功", commodityOutput);
    }

    /**
     * 根据商品名称，品牌名称，供应商名称模糊搜索商品
     * @param name
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Result getComms(String name, Integer pageNum, Integer pageSize) {
        //开始分页
        PageTool.startPage(pageNum,pageSize);
        //效验参数
        if( Ognl.isNotEmpty( name ) ){
            name = name.trim();
        }
        List<CommAppOutput> commAppOutputs = commAppDao.findComms(name);
        if( null == commAppOutputs || commAppOutputs.size()<=0 ){
            return Result.fail("暂无数据");
        }
        String [] ArrGoodIds = new String[commAppOutputs.size()];
        for ( int i=0 ; i< commAppOutputs.size(); i++  ){
            ArrGoodIds [i] = commAppOutputs.get(i).getId() + "";
        }
        try{
            List<String> salesNum = appCommSalesService.countSoldCommNum( ArrGoodIds );
            // 拿到对应商品id 的销量， 并且赋值给CommAppOutput的销量属性
            for( int i =0; i< commAppOutputs.size(); i++ ){
                commAppOutputs.get(i).setSaleNum( Integer.valueOf( salesNum.get(i) ) );
            }
        }catch (Exception e){
            logger.info("发生异常",e);
            PageInfo<CommAppOutput> pageInfo = new PageInfo(commAppOutputs);
            return Result.success("查询成功",pageInfo );
        }
        PageInfo<CommAppOutput> pageInfo = new PageInfo(commAppOutputs);
        return Result.success("查询成功",pageInfo );
    }

    /**
     * 根据分类等级查询全部商品科属信息或供应商商品科属信息
     *
     * @param supplierId 供应商ID
     * @param level 商品科属分类等级
     * @return
     * @throws Exception
     */
    @Override
    public Result getCategories(Long supplierId, Integer level) throws Exception {
        List<CategoryOutput> list = commAppDao.findSupplierCategories(supplierId,level);
        if(!Ognl.CollectionIsNotEmpty(list)){
            return Result.success("没有查询到供应商该等级的商品科属信息");
        }
        return Result.success("查询成功",list);
    }

    /**
     * 根据一级分类ID查询对应二级及三级分类信息
     *
     * @param supplierId 供应商ID
     * @param id  一级分类ID
     * @return
     * @throws Exception
     */
    @Override
    public Result getTwoAndThreeCategories(Long supplierId, Long id) throws Exception {
        /*
            1.根据供应商ID查询其可用的所有商品分类信息或所有可用的商品分类信息
            2.将查询结果根据ID分成单条,根据PID分组,根据level分组
            3.若不传id,那么查出所有的2级分类及其三级分类，若传入id,找出其对应的二级和三级分类
            4.若传入id,先验证是否为一级分类ID，再判断供应商的商品分类信息中是否有该一级分类ID
            5.查询传入的一级分类Id下对应的2级和3级分类
         */
        // 返回用map
        Map<String,Object> resultMap = new LinkedHashMap<>();
        // 1.根据供应商ID查询其可用的所有商品分类信息或所有可用的商品分类信息
        List<CategoryOutput> list = commAppDao.findBySupplierId(supplierId);
        if (!Ognl.CollectionIsNotEmpty(list)){
            return Result.success("成功",null);
        }
        // 2.将查询结果根据ID分成单条,根据PID分组,根据level分组
        Map<Long ,CategoryOutput> map = new HashMap<>(16);
        //根据ID分成单条
        for (CategoryOutput comm:list) {
            map.put(comm.getId(),comm);
        }
        //根据PID分组
        Map<Long ,List<CategoryOutput>> pMap = list.stream().collect(Collectors.groupingBy(CategoryOutput::getPid));
        //根据level分组
        Map<Integer ,List<CategoryOutput>> twoMap = list.stream().collect(Collectors.groupingBy(CategoryOutput::getLevel));
        // 3.若不传id,那么查出所有的2级分类及其三级分类，若传入id,找出其对应的二级和三级分类
        if (Ognl.isNull(id)){
            //取出2级集合
            List<CategoryOutput> twoList = twoMap.get(2);
            List childrenList = new ArrayList();
            //循环递归
            for (CategoryOutput com:twoList) {
                Map<String,Object> children= nodeTree(map,pMap,com.getId());
                childrenList.add(children);
            }
            resultMap.put("options",childrenList);
            return Result.success("成功",resultMap);
        } else {
            // 4.若传入id,先验证是否为一级分类ID，再判断供应商的商品分类信息中是否有该一级分类ID
            // 验证是否为一级分类ID
            List<CategoryOutput> oneLevelList = commAppDao.findOneLevel(id);
            if (Ognl.isNull(oneLevelList) || 0 == oneLevelList.size()){
                return Result.fail("传入的分类ID不是可用的一级ID");
            }
            // 判断供应商的商品分类信息中是否有该一级分类ID
            if (Ognl.isNull(map.get(id))){
                return Result.fail("传入的一级分类ID不是该供应商的一级分类ID");
            }
            // 5.查询传入的一级分类Id下对应的2级和3级分类
            //取出2级集合
            List<CategoryOutput> pList = pMap.get(id);
            if (Ognl.isNull(pList)){
                return Result.success("此一级分类下无二、三级分类");
            }
            List childrenList = new ArrayList();
            //循环递归
            for (CategoryOutput com:pList) {
                Map<String,Object> children= nodeTree(map,pMap,com.getId());
                childrenList.add(children);
            }
            resultMap.put("options",childrenList);
            return Result.success("成功",resultMap);
        }
    }

    /**
     * 根据id查询其子节点集
     * @param map
     * @param pMap
     * @param id
     * @return
     */
    private Map<String,Object> nodeTree(Map<Long ,CategoryOutput> map,Map<Long ,List<CategoryOutput>> pMap ,Long id ){
        Map<String,Object> resultMap = new LinkedHashMap<>();
        //根据id获取节点对象
        CategoryOutput commCategory= map.get(id);
        //封装对象
        resultMap.put("value",commCategory.getId());
        resultMap.put("label",commCategory.getName());
        //查询该id下的所有子节点
        List<CategoryOutput> list= pMap.get(id);
        if (Ognl.isNull(list)){
            return resultMap;
        }
        //子节点集合
        List childrenList = new ArrayList();
        for (CategoryOutput comm:list) {
            Map<String,Object> ccvo = nodeTree(map,pMap,comm.getId());
            childrenList.add(ccvo);
        }
        resultMap.put("children",childrenList);
        return resultMap;
    }

}
