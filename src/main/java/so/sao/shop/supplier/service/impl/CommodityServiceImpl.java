package so.sao.shop.supplier.service.impl;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile; 
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import so.sao.shop.supplier.config.CommConstant;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.config.azure.AzureBlobService;
import so.sao.shop.supplier.dao.*;
import so.sao.shop.supplier.domain.*;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.*;
import so.sao.shop.supplier.pojo.output.*;
import so.sao.shop.supplier.pojo.vo.*;
import so.sao.shop.supplier.service.CommodityService;
import so.sao.shop.supplier.service.app.AppCommSalesService;
import so.sao.shop.supplier.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * 商品管理Service
 * Created by QuJunLong on 2017/7/18.
 */
@Service
public class CommodityServiceImpl implements CommodityService {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @Autowired
    private CommCategoryDao commCategoryDao;
    @Autowired
    private CommodityDao commodityDao;
    @Autowired
    private CommImgeDao commImgeDao;
    @Autowired
    private TyCommImagDao tyCommImagDao;
    @Autowired
    private CommBrandDao commBrandDao;
    @Autowired
    private SupplierCommodityDao supplierCommodityDao;
    @Autowired
    private AccountDao accountDao;
    @Autowired
    private CommTagDao commTagDao;

    @Autowired
    private CommUnitDao commUnitDao;

    @Autowired
    private CommMeasureSpecDao commMeasureSpecDao;

    @Autowired
    private CommCartonDao commCartonDao;

    @Autowired
    private AzureBlobService azureBlobService;

    @Autowired
    private AppCommSalesService appCommSalesService;

    @Autowired
    private SupplierCommodityTmpDao supplierCommodityTmpDao;

    @Autowired
    private SupplierCommodityAuditDao supplierCommodityAuditDao;

    @Autowired
    private CommImgeTmpDao commImgeTmpDao;

    @Autowired
    private FreightRulesDao freightRulesDao;

    @Autowired
    private DistributionScopeDao distributionScopeDao;

    @Autowired
    private QualificationDao qualificationDao;

    @Value("${excel.tempaltepath}")
    private String urlFile;

    /**
     * 新增商品
     * @param commodityInput 商品信息对象
     * @param supplierId 供应商Id
     * @return Result结果集
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveCommodity(@Valid CommodityInput commodityInput,Long supplierId){
        //获取当前供应商
        Account account = accountDao.selectById(supplierId);
        if(null == account){
            return Result.fail("供应商不存在！");
        }
        //验证请求体
        Result result = validateCommodityInput(commodityInput);
        if(result.getCode() == 0) return result;
        //验证品牌是否存在，不存在则新增
        CommBrand brand = commBrandDao.findById(commodityInput.getBrandId());
        if (null == brand){
            List<CommBrand> commBrands = commBrandDao.searchByName(commodityInput.getBrandName());
            if(commBrands.size() > 0){
                brand = commBrands.get(0);
            }else{
                brand = new CommBrand();
                brand.setCreatedAt(new Date());
                brand.setCreatedBy(supplierId);
                brand.setUpdatedAt(new Date());
                brand.setUpdatedBy(supplierId);
                brand.setName(commodityInput.getBrandName());
                commBrandDao.save(brand);
            }
        }
        //商品规格id集合
        String scIds = "";
        for (SupplierCommodityVo commodityVo : commodityInput.getCommodityList()) {
            if(null != commodityVo.getTagId()){
                //验证商品标签是否存在
                int commTagNum = commTagDao.countById(commodityVo.getTagId());
                if(commTagNum == 0){
                    return Result.fail("商品标签不存在！商品条码：" + commodityVo.getCode69());
                }
            }
            //验证库存单位是否存在
            int commUnitNum = commUnitDao.countById(commodityVo.getUnitId());
            if(commUnitNum == 0){
                return Result.fail("库存单位不存在！商品条码：" + commodityVo.getCode69());
            }
            //验证计量规格是否存在
            int commMeasureSpecNum = commMeasureSpecDao.countById(commodityVo.getMeasureSpecId());
            if(commMeasureSpecNum == 0){
                return Result.fail("计量规格不存在！商品条码：" + commodityVo.getCode69());
            }
            //验证箱规单位是否存在
            int commCartonNum = commCartonDao.countById(commodityVo.getCartonId());
            if(commCartonNum == 0){
                return Result.fail("箱规单位不存在！商品条码：" + commodityVo.getCode69());
            }
            String code69 = commodityVo.getCode69();
            //验证商品是否存在,不存在则新增商品
            Commodity commodity = commodityDao.findCommInfoByCode69(code69);
            if(null == commodity) {
                commodity = BeanMapper.map(commodityInput, Commodity.class);
                commodity.setBrandId(brand.getId());
                commodity.setCode69(code69);
                commodity.setCreatedAt(new Date());
                commodity.setCreatedBy(supplierId);
                commodity.setUpdatedAt(new Date());
                commodity.setUpdatedBy(supplierId);
                commodityDao.save(commodity);
                //保存公共库图片
                List<TyCommImge> tyCommImges = new ArrayList<>();
                commodityVo.getImgeList().forEach(imgeVo->{
                    TyCommImge tyCommImge = BeanMapper.map(imgeVo, TyCommImge.class);
                    tyCommImge.setCode69(code69);
                    tyCommImge.setCreatedAt(new Date());
                    tyCommImge.setUpdatedAt(new Date());
                    tyCommImges.add(tyCommImge);
                });
                tyCommImagDao.batchSave(tyCommImges);
            }else{
                if(null == commodity.getMarketTime() || null == commodity.getBrandId() || StringUtil.isNull(commodity.getCompanyName()) || StringUtil.isNull(commodity.getOriginPlace())){
                    commodity.setBrandId(brand.getId());
                    commodity.setOriginPlace(commodityInput.getOriginPlace());
                    commodity.setCompanyName(commodityInput.getCompanyName());
                    commodity.setUpdatedAt(new Date());
                    commodity.setUpdatedBy(supplierId);
                    commodityDao.updateComm(commodity);
                }
            }
            //校验商品条码是否重复
            int scNum = supplierCommodityDao.countByCode69(code69,supplierId);
            if(scNum > 0){
                return Result.fail("商品已存在！");
            }
            //新增商品规格
            SupplierCommodity sc = BeanMapper.map(commodityVo, SupplierCommodity.class);
            sc.setRemark(commodityInput.getRemark());
            sc.setStatus(CommConstant.COMM_ST_OFF_SHELVES);
            sc.setSupplierId(supplierId);
            sc.setCreatedBy(supplierId);
            sc.setUpdatedBy(supplierId);
            sc.setCreatedAt(new Date());
            sc.setUpdatedAt(new Date());
            sc.setInventory(CommConstant.INVENTORY_DEFAULT_VALUE);
            sc.setInventoryMinimum(CommConstant.INVENTORY_MINIMUM_DEFAULT_VALUE);
            sc.setInventoryStatus(CommConstant.INVENTORY_WARNING);
            //若供应商被禁用，新增的商品是失效状态
            if(account.getAccountStatus() == CommConstant.ACCOUNT_INVALID_STATUS){
                sc.setInvalidStatus(CommConstant.COMM_INVALID_STATUS);
            }else if(account.getAccountStatus() == CommConstant.ACCOUNT_ACTIVE_STATUS){
                sc.setInvalidStatus(CommConstant.COMM_ACTIVE_STATUS);
            }
            supplierCommodityDao.save(sc);
            Long scId = sc.getId();
            if(scIds.equals("")){
                scIds += scId.toString();
            }else{
                scIds += "," + scId.toString();
            }
            //保存图片
            List<CommImge> commImges = new ArrayList<>();
            commodityVo.getImgeList().forEach(imgeVo->{
                CommImge imge = BeanMapper.map(imgeVo, CommImge.class);
                imge.setScId(scId);
                commImges.add(imge);
            });
            commImgeDao.batchSave(commImges);
        }
        appCommSalesService.saveCommSales(scIds);
        return Result.success("添加商品成功");
    }

    /**
     * 验证 commodityInput.getCommodityList
     * @param commodityInput
     * @return Result
     */
    public Result validateCommodityInput(CommodityInput commodityInput) {
        //用于存放商品69码
        Set<String> code69Sets = new TreeSet<>();
        //拼装69码集合
        commodityInput.getCommodityList().forEach(commodityVo->{
            String code69 = commodityVo.getCode69();
            code69Sets.add(code69);
        });

        //校验插入数据是否有重复69码
        if (code69Sets.size() < commodityInput.getCommodityList().size()) {
            return Result.fail("存在重复的商品条码，请修改后重新添加！");
        }
        Long categoryOneId = commodityInput.getCategoryOneId();
        Long categoryTwoId = commodityInput.getCategoryTwoId();
        Long categoryThreeId = commodityInput.getCategoryThreeId();
        //获取三级分类list
        List<CommCategory> commCategoryList = commCategoryDao.findByIds(categoryOneId, categoryTwoId, categoryThreeId);
        if(commCategoryList.size() < CommConstant.CATEGORY_LEVEL_NUMBER){
            return Result.fail("商品科属有分类不存在！");
        }
        CommCategory commCategoryOne = null;
        CommCategory commCategoryTwo = null;
        CommCategory commCategoryThree = null;
        for(CommCategory commCategory : commCategoryList){
            if(commCategory.getId().equals(categoryOneId)){
                commCategoryOne = commCategory;
            } else if(commCategory.getId().equals(categoryTwoId)){
                commCategoryTwo = commCategory;
            } else if(commCategory.getId().equals(categoryThreeId)){
                commCategoryThree = commCategory;
            }
        }
        //验证三级分类之间的关联是否正确
        if(!verifyAssociation(commCategoryOne, commCategoryTwo, commCategoryThree)){
            return Result.fail("商品科属之间的关联不正确！");
        }
        return Result.success("校验通过");
    }

    /**
     * 验证商品分类关联关系
     * @param commCategoryOne
     * @param commCategoryTwo
     * @param commCategoryThree
     * @return
     */
    private boolean verifyAssociation(CommCategory commCategoryOne, CommCategory commCategoryTwo, CommCategory commCategoryThree){
        if(!(commCategoryOne.getPid().equals(CommConstant.CATEGORY_ONE_PID) && commCategoryOne.getId().equals(commCategoryTwo.getPid()) && commCategoryTwo.getId().equals(commCategoryThree.getPid()))){
            return false;
        }
        return true;
    }
    /**
     * 根据code69查询商品信息
     * @param code69
     * @return 商品信息
     */
    @Override
    public Result findCommodity(String code69){
        CommodityInfoOutput commodityInfoOutput = commodityDao.findByCode69(code69);
        if(null == commodityInfoOutput){
            return Result.fail("商品不存在");
        } else {
            List<TyCommImge> imgeList = tyCommImagDao.findByCode69(code69);    //获取图片集合
            commodityInfoOutput.setImgeList(imgeList);
            return Result.success("成功！",commodityInfoOutput);
        }
    }

    /**
     * 修改商品
     * @param commodityUpdateInput 商品信息对象
     * @param supplierId
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateCommodity(CommodityUpdateInput commodityUpdateInput, Long supplierId){
        for (SupplierCommodityUpdateVo commodityVo : commodityUpdateInput.getCommodityList()) {
            long id =  commodityVo.getId();
            //验证商品是否存在
            int supplierCommodityNum = supplierCommodityDao.countById(id);
            if (supplierCommodityNum == 0) {
                return Result.fail("商品不存在！");
            }
            //验证商品是否在审核状态
            int pendingNum = supplierCommodityAuditDao.countByScidAndAuditResult(id);
            if(pendingNum > 0){
                return Result.fail("待审核状态下的商品,禁止操作！");
            }
            //商品状态
            int status = supplierCommodityDao.findStatus(id);
            //如果商品状态是下架，可以直接修改
            if(status == CommConstant.COMM_ST_OFF_SHELVES){
                if(null != commodityVo.getTagId()){
                    //验证商品标签是否存在
                    int commTagNum = commTagDao.countById(commodityVo.getTagId());
                    if(commTagNum == 0){
                        return Result.fail("商品标签不存在！");
                    }
                }
                //验证包装单位是否存在
                int commUnitNum = commUnitDao.countById(commodityVo.getUnitId());
                if (commUnitNum == 0) {
                    return Result.fail("库存单位不存在！");
                }
                //验证计量规格是否存在
                int commMeasureSpecNum = commMeasureSpecDao.countById(commodityVo.getMeasureSpecId());
                if (commMeasureSpecNum == 0) {
                    return Result.fail("计量规格不存在！");
                }
                //验证箱规单位是否存在
                int commCartonNum = commCartonDao.countById(commodityVo.getCartonId());
                if(commCartonNum == 0){
                    return Result.fail("箱规单位不存在！");
                }
                //修改商品规格
                SupplierCommodity sc = BeanMapper.map(commodityVo, SupplierCommodity.class);
                sc.setRemark(commodityUpdateInput.getRemark());
                sc.setUpdatedBy(supplierId);
                sc.setUpdatedAt(new Date());
                sc.setId(commodityVo.getId());
                supplierCommodityDao.update(sc);
                //清空原有大图数据信息
                List<Long> ids = commImgeDao.findIdsByScId(sc.getId());
                if (ids != null && ids.size() > 0) {
                    commImgeDao.deleteByIds(ids);
                }
                //保存图片
                List<CommImge> commImges = new ArrayList<>();
                commodityVo.getImgeList().forEach(imgeVo->{
                    CommImge imge = BeanMapper.map(imgeVo, CommImge.class);
                    imge.setScId(sc.getId());
                    commImges.add(imge);
                });
                commImgeDao.batchSave(commImges);
            }else{
                supplierCommodityAuditDao.updateAuditFlagByScId(commodityVo.getId(), CommConstant.AUDIT_RECORD);
                //添加审核表数据
                SupplierCommodityAudit sca = new SupplierCommodityAudit();
                sca.setScId(commodityVo.getId());
                sca.setSupplierId(supplierId);
                sca.setCreatedAt(new Date());
                sca.setUpdatedAt(new Date());
                sca.setStatus(CommConstant.COMM_EDIT_AUDIT);
                sca.setAuditResult(CommConstant.UN_AUDIT);
                sca.setAuditFlag(CommConstant.CURRENT_AUDIT);
                supplierCommodityAuditDao.save(sca);
                //修改后数据保存
                SupplierCommodityTmp sct = BeanMapper.map(commodityVo, SupplierCommodityTmp.class);
                SupplierCommodity sc = supplierCommodityDao.findOne(commodityVo.getId());
                sct.setCode69(sc.getCode69());
                sct.setStatus(sc.getStatus());
                sct.setDeleted(sc.getDeleted());
                sct.setCreatedAt(sc.getCreatedAt());
                sct.setUpdatedAt(sc.getUpdatedAt());
                sct.setInvalidStatus(sc.getInvalidStatus());
                sct.setRemark(commodityUpdateInput.getRemark());
                sct.setId(commodityVo.getId());
                sct.setSupplierId(supplierId);
                sct.setUpdatedBy(supplierId);
                sct.setCreatedBy(supplierId);
                sct.setScaId(sca.getId());
                sct.setInventory(sc.getInventory());
                sct.setInventoryStatus(sc.getInventoryStatus());
                sct.setInventoryMinimum(sc.getInventoryMinimum());
                supplierCommodityTmpDao.save(sct);
                //修改后图片数据保存
                List<CommImgeTmp> commImgeTmps = new ArrayList<>();
                commodityVo.getImgeList().forEach(imgeVo->{
                    CommImgeTmp imge = BeanMapper.map(imgeVo, CommImgeTmp.class);
                    imge.setScaId(sca.getId());
                    commImgeTmps.add(imge);
                });
                commImgeTmpDao.batchSave(commImgeTmps);
                return Result.success("已上架商品编辑提交时，需要管理员审核，审核通过后会自动上架，稍后注意查询商品列表审核结果。");
            }
        }
        return Result.success("更新商品信息成功");
    }
    /**
     * 根据供应商商品ID获取商品·
     * @param id
     * @return
     */
    @Override
    public Result getCommodity(Long id) {
        //根据商品 id 判断该商品有没有审核记录
        int auditReult = supplierCommodityAuditDao.countByScidAndAuditResult(id);
        //存放查询到的商品详细信息
        CommodityOutput commodityOutput = null;
        List<CommImge> commImgeList;
        //商品有审核状态
        if(auditReult > 0){
            //根据商品的id查询出商品的状态
            int status = supplierCommodityDao.findSupplierCommStatus(id);
            //如果是商品的状态是 6 (编辑待审核),则根据供应商商品ID获取编辑后的商品信息
            if (status > 0) {
                commodityOutput = supplierCommodityTmpDao.findDetailTmp(id);
                if (Ognl.isNull(commodityOutput.getCartonId())){
                    commodityOutput.setCartonName(null);
                }else {
                    // 查询箱规名称
                    CommCarton commCarton = commCartonDao.findOne(commodityOutput.getCartonId());
                    commodityOutput.setCartonName(commCarton.getName());
                }
                commodityOutput.setStatus(supplierCommodityDao.findAuditStatus(id));
                if (null != commodityOutput) {
                    //根据供应商商品ID获取图片列表信息
                     commImgeList = commImgeTmpDao.findImgTmp(commodityOutput.getScaId());
                    List<CommImgeVo> commImgeVoList = new ArrayList<>();
                    commImgeList.forEach(commImge -> {
                        CommImgeVo commImgeVo = BeanMapper.map(commImge, CommImgeVo.class);
                        commImgeVoList.add(commImgeVo);
                    });
                    commodityOutput.setImgeList(commImgeVoList);
                }
            }else {
                //商品没状态不为 6(编辑待审核),则根据供应商商品ID获取商品信息
                commodityOutput = supplierCommodityDao.findDetail(id);
                if (Ognl.isNull(commodityOutput)){
                    return Result.success("商品不存在", commodityOutput);
                }
                if (Ognl.isNull(commodityOutput.getCartonId())){
                    commodityOutput.setCartonName(null);
                }else {
                    // 查询箱规名称
                    CommCarton commCarton = commCartonDao.findOne(commodityOutput.getCartonId());
                    commodityOutput.setCartonName(commCarton.getName());
                }
                commodityOutput.setStatus(supplierCommodityDao.findAuditStatus(id));
                readImgData(id,commodityOutput); //获取图片信息
            }
        } else {
            //商品没有审核记录,则根据供应商商品ID获取商品信息
            commodityOutput = supplierCommodityDao.findDetail(id);
            if (Ognl.isNull(commodityOutput)){
                return Result.success("商品不存在", commodityOutput);
            }
            if (Ognl.isNull(commodityOutput.getCartonId())){
                commodityOutput.setCartonName(null);
            }else {
                // 查询箱规名称
                CommCarton commCarton = commCartonDao.findOne(commodityOutput.getCartonId());
                commodityOutput.setCartonName(commCarton.getName());
            }
            readImgData(id,commodityOutput); // 获取图片信息
        }
        return Result.success("查询成功", commodityOutput);
    }

    @Override
    public Account findAccountByUserId(Long userId){
        return accountDao.findByUserId(userId);
    }
    /**
     * 根据查询条件查询商品详情(简单条件查询)
     *
     * @param commSimpleSearchInput 简单查询请求
     * @param request
     * @return
     */
    @Override
    public Result simpleSearchCommodities(CommSimpleSearchInput commSimpleSearchInput, HttpServletRequest request) {

        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //开始分页

        Integer status = commSimpleSearchInput.getStatus();
        String inputvalue = commSimpleSearchInput.getInputvalue();
        Integer auditResult = commSimpleSearchInput.getAuditResult();
        Integer sortStatus = commSimpleSearchInput.getSortStatus();
        String role = getRole(user);

        Long supplierId;
        if("admin".equals(role)){//管理员
            supplierId = commSimpleSearchInput.getSupplierId();
        }else{//供应商
            /**
             * 手机端查询的时候没有对应的supplierId，这里要从数据库查询。
             */
            supplierId = accountDao.findSupplierIdByUserId(user.getId());
            //能走到下面这个条件应该是出现了脏数据
            if(supplierId == null){
                logger.debug("检查数据库的user表，accountid表，emp表，可能出现了脏数据！");
                return Result.fail(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
            }

        }


        PageTool.startPage(commSimpleSearchInput.getPageNum(), commSimpleSearchInput.getPageSize());
        List<SuppCommSearchVo> respList = supplierCommodityDao.findSimple(status,inputvalue,auditResult,role,supplierId,sortStatus);

        respList.forEach(suppCommSearchVo->{
            suppCommSearchVo.setStatus(CommConstant.getStatus(suppCommSearchVo.getStatusNum()));
            //转换金额为千分位
            suppCommSearchVo.setUnitPrice(NumberUtil.number2ThousandFormat(suppCommSearchVo.getUnitPrice()));
            suppCommSearchVo.setPrice(NumberUtil.number2ThousandFormat(suppCommSearchVo.getPrice()));
        });
        PageInfo<SuppCommSearchVo> pageInfo = new PageInfo<SuppCommSearchVo>(respList);
        return Result.success("查询完成", pageInfo);
    }

    /**
     * 获取角色
     * @param user
     * @return
     */
    private String getRole(User user) {
        String role = null;
        if(Constant.ADMIN_STATUS.equals(user.getIsAdmin())){
            role = "admin";
        }
        return role;
    }

    /**
     * 高级搜索
     * @param commSearchInput 高级搜索查询请求
     * @param request
     * @return
     */
    @Override
    public Result searchCommodities(CommSearchInput commSearchInput, HttpServletRequest request) {
        /**
         * 入参校验
         * 1.价格不能为负数";
         * 2.最小金额不能大于最大金额
         */
        String priceMessage = DataCompare.priceCheck(commSearchInput.getMinPrice(), commSearchInput.getMaxPrice());
        if(!"".equals(priceMessage)){
            return Result.fail(priceMessage);
        }
        //起始时间不能大于终止时间
        String createAtMessage = DataCompare.createAtCheck(commSearchInput.getBeginUpdateAt(),commSearchInput.getEndUpdateAt());
        if(!"".equals(createAtMessage)){
            return Result.fail(createAtMessage);
        }

        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        commSearchInput.setRole(getRole(user));
        if(!"admin".equals(commSearchInput.getRole())){
             Long supplierId = accountDao.findSupplierIdByUserId(user.getId());
            //能走到下面这个条件应该是出现了脏数据
            if(supplierId == null){
                logger.debug("检查数据库的user表，accountid表，emp表，可能出现了脏数据！");
                return Result.fail(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
            }
            commSearchInput.setSupplierId(supplierId);
        }


        //开始分页
        PageTool.startPage(commSearchInput.getPageNum(), commSearchInput.getPageSize());
        List<SuppCommSearchVo> respList = supplierCommodityDao.find(commSearchInput);
        respList.forEach(suppCommSearchVo->{
            suppCommSearchVo.setStatus(CommConstant.getStatus(suppCommSearchVo.getStatusNum()));
            //转换金额为千分位
            suppCommSearchVo.setPrice(NumberUtil.number2ThousandFormat(suppCommSearchVo.getPrice()));
            suppCommSearchVo.setUnitPrice(NumberUtil.number2ThousandFormat(suppCommSearchVo.getUnitPrice()));
        });

        PageInfo<SuppCommSearchVo> pageInfo = new PageInfo<SuppCommSearchVo>(respList);
        return Result.success("查询成功！", pageInfo);
    }

    /**
     * 删除商品
     * @param id 供应商商品ID
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteCommodity(Long id) {
        //根据商品与供应商关系ID获取供应商商品对象
        SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(id);
        Map<String,Long> map = new HashMap<>();
        if(null != supplierCommodity){
            map.put("supplierId", supplierCommodity.getSupplierId());
            //验证商品是否在审核状态
            int pendingNum = supplierCommodityAuditDao.countByScidAndAuditResult(id);
            //商品需下架才可删除
            if(supplierCommodity.getStatus() != CommConstant.COMM_ST_OFF_SHELVES || pendingNum > 0){
                return Result.fail("商品需下架才可删除！", map);
            }
            //删除商品,deleted更新为1
            supplierCommodityDao.deleteById(id, true, new Date());
        }
        return Result.success("删除商品成功！", map);
    }

    /**
     * 批量删除商品
     * @param ids 供应商商品ID
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteCommodities(Long[] ids) {
        Map<String,Long> map = new HashMap<>();
        //可删除商品ID集合
        TreeSet<Long> idList = new TreeSet<>();
        //不可删除商品商品编码集合
        for(long id : ids){
            //根据商品与供应商关系ID获取供应商商品对象
            SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(id);
            if(null != supplierCommodity){
                map.put("supplierId", supplierCommodity.getSupplierId());
                //验证商品是否在审核状态
                int pendingNum = supplierCommodityAuditDao.countByScidAndAuditResult(id);
                //商品需下架才可删除
                if(supplierCommodity.getStatus() != CommConstant.COMM_ST_OFF_SHELVES || pendingNum > 0){
                    return Result.fail("存在已上架或审核中的商品，请重新选择！");
                }else{
                    idList.add(id);
                }
            }
        }
        if(idList.size() > 0){
            List<SupplierCommodity> supplierCommodityList = new ArrayList<>();
            final SupplierCommodity[] supplierCommodity = new SupplierCommodity[1];
            idList.forEach(id->{
                supplierCommodity[0] = new SupplierCommodity();
                supplierCommodity[0].setId(id);
                supplierCommodity[0].setUpdatedAt(new Date());
                supplierCommodity[0].setDeleted(1);
                supplierCommodityList.add(supplierCommodity[0]);
            });
            supplierCommodityDao.deleteByIds(supplierCommodityList);
        }
        return Result.success("删除商品成功！", map);
    }

    /**
     *判断配送范围和运费规则是否完整
     * @param supplierId 供应商ID
     * @return boolean
     */
    private boolean checkFreightRules(Long supplierId){
        //判断该供应商是否存在配送范围数据
        List<DistributionScope> list = distributionScopeDao.queryAll(supplierId);
        if (list == null || list.size() == 0){
            return false;
        }
        //判断供应商通用运费规则
        List<FreightRules> CommList = freightRulesDao.queryAll(supplierId,0);
        if (CommList != null && CommList.size() > 0){
            return true;
        }
        //判断供应商按配送范围地区运费规则
        List<FreightRules> AreaList = freightRulesDao.queryAll(supplierId,1);
        if (AreaList == null || AreaList.size() == 0){
            return false;
        }
        for (FreightRules freightRules:AreaList) {
            if (freightRules.getWhetherShipping() == null) {
                return false;
            }
        }
        return true;
    }

    /**
     * 商品上架
     * @param id,isAdmin
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result onShelves(Long id) {
        int count = supplierCommodityDao.countById(id);
        //判断有无该商品记录
        if(count == 0){
            return Result.fail("该商品无记录！");
        }
        SupplierCommodity supplierCommodity=supplierCommodityDao.findOne(id);
        Long supplierId = supplierCommodity.getSupplierId();
        Result result = checkSupplier(supplierId);
        //校验供应商资质与配送运费
        if( null != result){
            return result;
        }
        //判断该商品是否处于审核中
        int num = supplierCommodityAuditDao.countByScidAndAuditResult(id);
        if (num>0){
            return Result.fail("该商品已提交管理员审核，暂不能进行任何操作！");
        }
        //判断是否重复执行上架操作
        if(supplierCommodity.getStatus()==CommConstant.COMM_ST_ON_SHELVES){
            return Result.fail("不能进行重复上架操作！");
        }
        //插入审核记录前，将以前的审核记录flag变为0
        supplierCommodityAuditDao.updateAuditFlagByScId(id, CommConstant.AUDIT_RECORD);
        SupplierCommodityAudit supplierCommodityAudit = makeSupplierCommodityAudit(supplierCommodity, CommConstant.COMM_ST_ON_SHELVES_AUDIT);
        //执行上架申请操作
        supplierCommodityAuditDao.save(supplierCommodityAudit);
        return Result.success("上架商品需要管理员审核，审核通过后会自动上架，稍后查询商品列表审核结果！");
    }

    /**
     * 商品下架
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result offShelves(Long id) {
        SupplierCommodity one = supplierCommodityDao.findOne(id);
        if (null == one) {
            return Result.fail("该商品无记录！");
        }
        //判断该供应商商品是否已处于待审核状态
        int num = supplierCommodityAuditDao.countByScidAndAuditResult(id);
        if (num > 0) {
            return Result.fail("该商品已提交管理员审核，暂不能进行任何操作！");
        }
        //不能重复下架
        if (CommConstant.COMM_ST_OFF_SHELVES == one.getStatus()){
            return Result.fail("不能进行重复下架操作！");
        }
        //更新scId对应的历史记录
        supplierCommodityAuditDao.updateAuditFlagByScId(id, CommConstant.AUDIT_RECORD);
        //添加供应商商品待审核记录，并等待管理员审核
        SupplierCommodityAudit supplierCommodityAudit = makeSupplierCommodityAudit(one, CommConstant.COMM_ST_OFF_SHELVES_AUDIT);
        supplierCommodityAuditDao.save(supplierCommodityAudit);
        return Result.success("已上架商品下架提交时，需要管理员审核，审核通过后会自动下架，稍后注意查询商品列表审核结果！");
    }

    /**
     * 管理员批量上架商品--管理员批量上架
     * @param ids 供应商商品id数组
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result onShelvesBatchByAdmin(Long[] ids) {
        //根据id数组查询，过滤已删除的商品
        List<SupplierCommodity> supplierCommodityList = supplierCommodityDao.findSupplierCommodityByIds(ids);
        if (null == supplierCommodityList || supplierCommodityList.size() == 0) {
            return Result.fail("该商品无记录！");
        }
        //判断该供应商商品数组中是否已处于待审核状态
        int num = supplierCommodityAuditDao.countByScidArrayAndAuditResult(ids);
        if (num > 0) {
            return Result.fail("所选商品中包含待审核商品，请重新选择！");
        }
        //存储supplierId
        Set<Long> suSet = new TreeSet<>();
        for (SupplierCommodity sc:supplierCommodityList) {
            suSet.add(sc.getSupplierId());
        }
        //存储符合验证条件的supplierId
        Set<Long> passSet = new TreeSet<>();
        //存储不符合验证条件的supplierId
        Set<Long> noPassSet = new TreeSet<>();
        //管理员登录时校验所有供应商的资质、配送与运费
        for (Long supplierId : suSet) {
            //判断供应商资质审核
            if (!Objects.equals(Constant.QUALIFICATION_VERIFY_PASS, qualificationDao.getAccountQualificationStatus(supplierId))) {
                noPassSet.add(supplierId);
                continue;
            }
            //判断供应商是否已完善配送范围和运费规则
            boolean freightRulesFlag = checkFreightRules(supplierId);
            if (!freightRulesFlag) {
                noPassSet.add(supplierId);
                continue;
            }
            //添加满足条件的supplerId
            passSet.add(supplierId);
        }
        //所有都不满足条件情况
        if (passSet.size() == 0){
            return Result.fail("所选供应商存在还没通过资质审核、配送范围或运费规则，无法上架商品！");
        }
        //需要操作的供应商商品审核记录
        List<SupplierCommodityAudit> list = new ArrayList<>();
        for (SupplierCommodity supplierCommodity:supplierCommodityList) {
            if(passSet.contains(supplierCommodity.getSupplierId())){
                //过滤重复上架
                if (CommConstant.COMM_ST_ON_SHELVES == supplierCommodity.getStatus()) {
                    continue;
                }
                SupplierCommodityAudit supplierCommodityAudit = makeSupplierCommodityAudit(supplierCommodity, CommConstant.COMM_ST_ON_SHELVES_AUDIT);
                list.add(supplierCommodityAudit);
            }
        }
        if(list.size() <= 0){
            return Result.fail("不能进行重复上架操作！");
        }
        //更新scId对应的历史记录
        supplierCommodityAuditDao.updateAuditFlagByList(list, CommConstant.AUDIT_RECORD);
        //添加新记录
        supplierCommodityAuditDao.saveBatch(list);
        if(passSet.size() > 0 && noPassSet.size() > 0){
            return Result.success("部分商品上架成功，上架商品需要管理员审核，审核通过后会自动上架，稍后注意查询商品列表审核结果！");
        }
        return Result.success("上架商品需要管理员审核，审核通过后会自动上架，稍后注意查询商品列表审核结果！");
    }

    /**
     * 供应商批量上架商品--供应商批量上架
     * @param ids 供应商商品id数组
     * @param supplierId 供应商id
     * @return
     */
    @Override
    public Result onShelvesBatchBySupplier(Long[] ids, Long supplierId) {
        //根据id数组查询，过滤已删除的商品
        List<SupplierCommodity> supplierCommodityList = supplierCommodityDao.findSupplierCommodityByIds(ids);
        if (null == supplierCommodityList || supplierCommodityList.size() == 0) {
            return Result.fail("该商品无记录！");
        }
        //校验供应商资质审核、配送范围与配送运费
        Result result = checkSupplier(supplierId);
        if( null != result){
            return result;
        }
        //判断该供应商商品数组中是否已处于待审核状态
        int num = supplierCommodityAuditDao.countByScidArrayAndAuditResult(ids);
        if (num > 0) {
            return Result.fail("所选商品中包含待审核商品，请重新选择！");
        }
        //需要操作的供应商商品审核记录
        List<SupplierCommodityAudit> list = new ArrayList<>();
        for (SupplierCommodity supplierCommodity:supplierCommodityList) {
            //过滤重复上架
            if (CommConstant.COMM_ST_ON_SHELVES == supplierCommodity.getStatus()) {
                continue;
            }
            SupplierCommodityAudit supplierCommodityAudit = makeSupplierCommodityAudit(supplierCommodity, CommConstant.COMM_ST_ON_SHELVES_AUDIT);
            list.add(supplierCommodityAudit);
        }
        if(list.size() <= 0){
            return Result.fail("不能进行重复上架操作！");
        }
        //更新scId对应的历史记录
        supplierCommodityAuditDao.updateAuditFlagByList(list, CommConstant.AUDIT_RECORD);
        //添加新记录
        supplierCommodityAuditDao.saveBatch(list);
        return Result.success("上架商品需要管理员审核，审核通过后会自动上架，稍后注意查询商品列表审核结果！");
    }

    /**
     * 商品批量下架
     * @param ids 供应商商品id数组
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result offShelvesBatch(Long[] ids) {
        //根据id数组查询，过滤已删除的商品
        List<SupplierCommodity> supplierCommodityList = supplierCommodityDao.findSupplierCommodityByIds(ids);
        if (null == supplierCommodityList || supplierCommodityList.size() == 0) {
            return Result.fail("该商品无记录！");
        }
        //判断该供应商商品数组中是否已处于待审核状态
        int num = supplierCommodityAuditDao.countByScidArrayAndAuditResult(ids);
        if (num > 0) {
            return Result.fail("所选商品中包含待审核商品，请重新选择！");
        }
        //存放状态需要转为待审核的商品
        List<SupplierCommodityAudit> auditList = new ArrayList<>();
        for (SupplierCommodity supplierCommodity:supplierCommodityList) {
            //过滤重复上架
            if (CommConstant.COMM_ST_OFF_SHELVES == supplierCommodity.getStatus()) {
                continue;
            }
            //需要下架审核的商品存入auditList中
            SupplierCommodityAudit supplierCommodityAudit = makeSupplierCommodityAudit(supplierCommodity, CommConstant.COMM_ST_OFF_SHELVES_AUDIT);
            auditList.add(supplierCommodityAudit);
        }
        if (auditList.size() <= 0){
            return Result.fail("不能进行重复下架操作！");
        }
        //更新scId对应的历史记录
        supplierCommodityAuditDao.updateAuditFlagByList(auditList, CommConstant.AUDIT_RECORD);
        //添加新记录
        supplierCommodityAuditDao.saveBatch(auditList);
        return Result.success("已上架商品下架提交时，需要管理员审核，审核通过后会自动下架，稍后注意查询商品列表审核结果！");
    }

    /**
     * 组装供应商商品审核记录对象
     * @param supplierCommodity 供应商商品对象
     * @param status 供应商商品需审核的状态
     * @return 供应商商品审核记录对象
     */
    private SupplierCommodityAudit makeSupplierCommodityAudit(SupplierCommodity supplierCommodity, int status){
        SupplierCommodityAudit supplierCommodityAudit = new SupplierCommodityAudit();
        supplierCommodityAudit.setScId(supplierCommodity.getId());
        supplierCommodityAudit.setSupplierId(supplierCommodity.getSupplierId());
        supplierCommodityAudit.setCreatedAt(new Date());
        supplierCommodityAudit.setUpdatedAt(new Date());
        supplierCommodityAudit.setStatus(status);
        supplierCommodityAudit.setAuditResult(CommConstant.UN_AUDIT);
        supplierCommodityAudit.setAuditFlag(CommConstant.CURRENT_AUDIT);
        return supplierCommodityAudit;
    }

    /**
     * 校验供应商的资质审核与配送范围、运费规则
     * @param supplierId 供应商ID
     * @return result
     */
    private Result checkSupplier(Long supplierId){
        //判断供应商资质审核
        if (!Objects.equals(Constant.QUALIFICATION_VERIFY_PASS, qualificationDao.getAccountQualificationStatus(supplierId))){
            return Result.fail("抱歉，该供应商还没通过资质信息审核，无法上架商品！",supplierId);
        }
        //判断配送范围和运费规则是否完整
        Boolean freightRulesFlag = checkFreightRules(supplierId);
        if(!freightRulesFlag){
            return Result.fail("抱歉，该供应商未配置配送范围与运费规则，无法上架商品！");
        }
        return null;
    }

    /**
     *  批量导入商品
     * @param multipartFile 文件对象
     * @param request
     * @param supplierId
     * @return
     * @throws Exception
     */
    @Override
    public Result importExcel(MultipartFile multipartFile, HttpServletRequest request, Long supplierId) throws Exception {
        Map<String ,List> outmap = new HashMap();// 封装errorList 和 rightList 数据
        List<CommodityImportOutput> commodityImportOutputList = new ArrayList<CommodityImportOutput>();
        String tempPath = request.getSession().getServletContext().getRealPath("") + "/file/";// 文件上传到的文件夹
        String filename = transferTo(request, tempPath);//文件移动至tempPath路径下
        if(!(filename.endsWith(".zip")||filename.endsWith(".ZIP"))){
            return Result.fail("只支持.zip压缩包！", outmap);
        }
        //接下来开始解压缩文件
        String newFileName = DateUtil.getStringDateTime();
        Map excelmap = new HashMap();
        if(!"".equals(filename.trim())){
            newFileName= deCompressFile(tempPath, filename, newFileName, excelmap);//解压缩文件
        }
        String excelpath = tempPath + newFileName +"/"+excelmap.get("excel").toString();
        //使用工具类 获取Excel 内容
        Map<String, Object> maps = ExcelReader.readExcelContent(excelpath,1);
        //Excel中正确记录信息
        Map<Integer,Map<String, String>> mapRight = ( Map<Integer,Map<String, String>>)maps.get("mapright");
        //Excel中错误行号
        List<Map<String, Object>> errorRowList =  (List<Map<String, Object>>) maps.get("maperror");
        List<CommodityBatchInput> commodityBatchInputs = new ArrayList<CommodityBatchInput>();
        // Excel 内容转 CommodityInput对象
        if(mapRight != null){
            //封装数据
            commodityBatchInputs = checkCellData(mapRight, errorRowList,tempPath + newFileName, supplierId );
        }
        List<CommodityImageVo> commodityImageVoList =new ArrayList<CommodityImageVo>();
        //入庫
        for (CommodityBatchInput commodityBatchInput : commodityBatchInputs) {
            int rowNum = commodityBatchInput.getRowNum();
            if(errorRowList.contains(rowNum)){//过滤错误数据记录
                continue;
            }
            Result baseResult =saveBatchCommodity(commodityBatchInput, supplierId);
            String code69 = commodityBatchInput.getCommodityList().get(0).getCode69();
            if(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS != baseResult.getCode()){
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message",baseResult.getMessage());
                errorRowList.add(errorMap);
            }else {
                commodityImageVoList.add( (CommodityImageVo)baseResult.getData());
                CommodityImportOutput commodityImportOutput = new CommodityImportOutput();
                commodityImportOutput.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
                commodityImportOutput.setMessage("商品条码:" + code69+"成功导入！");
                commodityImportOutput.setCode69(code69);
                commodityImportOutput.setBrand(commodityBatchInput.getBrandName());
                commodityImportOutput.setSjcode(commodityBatchInput.getCommodityList().get(0).getCode());
                commodityImportOutput.setRowNum(rowNum);
                commodityImportOutput.setTagName(commodityBatchInput.getTagName());
                commodityImportOutput.setUnit(commodityBatchInput.getCommodityList().get(0).getUnitName());
                commodityImportOutput.setMeasureSpecName(commodityBatchInput.getCommodityList().get(0).getMeasureSpecName());
                commodityImportOutput.setCompanyName(commodityBatchInput.getCompanyName());
                commodityImportOutput.setOriginPlace(commodityBatchInput.getOriginPlace());
                commodityImportOutput.setInventory(0L);
                commodityImportOutput.setName(commodityBatchInput.getName());
                commodityImportOutput.setRuleVal(commodityBatchInput.getCommodityList().get(0).getRuleVal());
                commodityImportOutput.setMinOrderQuantity(commodityBatchInput.getCommodityList().get(0).getMinOrderQuantity());
                commodityImportOutputList.add(commodityImportOutput);
            }
        }
        //多线程上传图片，更新相关图片表
        final String filePath = tempPath + newFileName;
        uploadImages(filePath,commodityImageVoList,supplierId);
        outmap.put("rightlist",commodityImportOutputList);
        outmap.put("errolist",errorRowList);
        return Result.success("完成导入！", outmap);
    }
    private void  uploadImages(String  filePath, List<CommodityImageVo> commodityImageVoList,Long supplierId){
        Map<Integer, List<CommodityImageVo>> reMap = convertMap(commodityImageVoList);
        // 创建一个可重用固定线程数的线程池
        ExecutorService pool = Executors.newFixedThreadPool(reMap.size() + 1);
        reMap.forEach((k, v)-> {
            ImagesUploadThread thread = new ImagesUploadThread( v ,filePath,supplierId,  azureBlobService, commImgeDao, tyCommImagDao,supplierCommodityDao, commodityDao);
            // 将线程放入池中进行执行
            pool.execute(thread);
        });
        monitorThreadTime(filePath, pool);
    }

    /**
     * 数据转换
     * @param commodityImageVoList
     * @return
     */
    private Map<Integer, List<CommodityImageVo>> convertMap( List<CommodityImageVo> commodityImageVoList){
        Map<String, List<CommodityImageVo>> cvMap = new HashMap<>();
        String[] imgStr;
        for (CommodityImageVo cv : commodityImageVoList) {
            imgStr = cv.getImage().replaceAll("，", ",").split(",");

            for(String imgName : imgStr) {//图片名称拆解
                if (cvMap.get(imgName) == null) {
                    cvMap.put(imgName, new ArrayList<>());
                }
                cvMap.get(imgName).add(cv);
            }
        }
        List<CommodityImageVo> list = new ArrayList<>();
        Map<Integer, List<CommodityImageVo>> reMap = new HashMap<>();
        Map<Long, CommodityImageVo> cMap = new HashMap<>();
        int num=0;
        for (Map.Entry<String,List<CommodityImageVo>> itmap  : cvMap.entrySet()) {
            for (CommodityImageVo cv:itmap.getValue()){
                cMap.put(cv.getScId(),cv);
            }
        }
        for (Map.Entry<Long,CommodityImageVo> lmap  : cMap.entrySet()) {
            list.add(lmap.getValue());

            if(list.size() >= CommConstant.THREAD_NUM){
                reMap.put(num, list);
                num++;
                list = new ArrayList<>();
            }
        }

        if(list.size() > 0){
            reMap.put(num, list);
        }
        return reMap;
    }

    /**
     * 监控线程执行完毕守护线程
     *@param filePath
     * @param pool:执行结果表单，将用来计算超时
     * */
    private void monitorThreadTime(String filePath, ExecutorService pool){
        //执行守护线程
        new Thread(new Runnable() {
            @Override
            public void run() {
                pool.shutdown();
                while(true){
                    if(pool.isTerminated()){
                        logger.info("所有的子线程都结束了！");
                        FileUtil.deleteDirectory(filePath);
                        break;
                    }

                }

            }
        }).start();
    }


    /**
     * 校验单元格数据
     * @param mapRight
     * @param errorRowList
     * @param filePath
     * @param supplierId
     */
    private List<CommodityBatchInput>  checkCellData(Map<Integer,Map<String, String>> mapRight,  List<Map<String, Object>>  errorRowList ,String filePath, long supplierId){
        List<CommodityBatchInput> commodityBatchInputs = new ArrayList<CommodityBatchInput>();

        //  获取所有标签
        List<CommTag> commAllTagList = commTagDao.find(supplierId);
        Map<String,Long> mapTag = new HashMap<String,Long>();
        for ( CommTag  tags: commAllTagList ) {
            mapTag.put(tags.getName(),tags.getId());
        }
        List<CommMeasureSpec> commMeasureSpeclist = commMeasureSpecDao.find(supplierId);
        Map<String,Long> commMeasureSpecMap = new HashMap<String,Long>();
        for ( CommMeasureSpec  commMeasureSpec: commMeasureSpeclist ) {
            commMeasureSpecMap.put(commMeasureSpec.getName(),commMeasureSpec.getId());
        }
        List<CommUnit> commUnitList = commUnitDao.search(supplierId);
        Map<String,Long> commUnitMap = new HashMap<String,Long>();
        for ( CommUnit  commUnit: commUnitList ) {
            commUnitMap.put(commUnit.getName(),commUnit.getId());
        }
        List<CommCarton> commCartonList = commCartonDao.find(supplierId);
        Map<String,Long> commCartonMap = new HashMap<String,Long>();
        for ( CommCarton  commCarton: commCartonList ) {
            commCartonMap.put(commCarton.getName(),commCarton.getId());
        }
        for (Map.Entry<Integer,Map<String, String>> itmap : mapRight.entrySet()) {
            int rowNum = itmap.getKey();
            Map<String, String> map = itmap.getValue();
            CommodityBatchInput commodityBatchInput = new CommodityBatchInput();
            commodityBatchInput.setRowNum(rowNum);
            List<SupplierCommodityVo> commodityList = new ArrayList<SupplierCommodityVo>();
            SupplierCommodityVo supplierCommodityVo = new SupplierCommodityVo();
            long pid = 0;
            String code69 = map.get("*商品条码");
            String code=map.get("商品商家编码");
            String name = map.get("*商品名称");
            String img = map.get("*商品图片（1-10张）");
            String brand = map.get("*商品品牌");
            String commCategoryOne = map.get("*商品分类一级");
            String commCategoryTwo = map.get("*商品分类二级");
            String commCategoryThree = map.get("*商品分类三级");
            String originPlace = map.get("*商品产地");
            String unitName = map.get("*库存单位");
            String measureSpecName = map.get("*规格单位");
            String measureSpec = map.get("*规格");
            measureSpec= measureSpec.replaceAll("／","/");
            String measureSpecVal ="";
            if(measureSpec.contains(measureSpecName)){
                measureSpecVal =measureSpec.substring(0,measureSpec.indexOf(measureSpecName.trim()));
            }else {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","规格单位不一致");
                errorRowList.add(errorMap);
                continue;
            }
            String carton = map.get("*箱规");
            carton= carton.replaceAll("／","/");
            String cartonName=carton.substring(carton.indexOf("/")+1,carton.length());
            String cartonVal="";
            if(carton.contains(unitName)){
                cartonVal=carton.substring(0,carton.indexOf(unitName.trim()));
            }else {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","库存单位不一致");
                errorRowList.add(errorMap);
                continue;
            }
            if(!unitName.equals(measureSpec.substring(measureSpec.indexOf("/")+1,measureSpec.length()))){
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","库存单位不一致");
                errorRowList.add(errorMap);
                continue;
            }
            String unitPrice = map.get("*透云进货价");
            String price = map.get("*app订货价");
            String productionDate=map.get("*生产日期");
            String guaranteePeriod=map.get("*有效期(天)");
            String  guaranteePeriodUnit="天";
            String  minOrderQuantity = map.get("*最小起订量");
            String companyName= map.get("*企业名称");
            String  suppliercode = map.get("供应商id");
            String tag = map.get("商品标签");
            String remark = map.get("*商品说明");

            String regexnum ="^[0-9]*$";
            String regex ="^[A-Za-z0-9]*$";

            if(code69.matches(regexnum)&&code69.length()<20){
                //通过code69,supplierId,deleted=0判断商品是否存在
                SupplierCommodity suppliercommodity = supplierCommodityDao.findSupplierCommodityInfo(code69, supplierId);
                Commodity commodity = commodityDao.findCommInfoByCode69(code69);
                if (null != suppliercommodity) {
                    Map<String, Object> errorMap =new HashMap<String, Object>();
                    errorMap.put("rowNum",rowNum);
                    errorMap.put("message","商品条码已存在");
                    errorRowList.add(errorMap);
                    continue;
                }else  {

                    if(null == commodity){
                        if((!"".equals(originPlace))&&(!"".equals(companyName))){
                            supplierCommodityVo.setCode69(code69);
                        }else {
                            Map<String, Object> errorMap =new HashMap<String, Object>();
                            errorMap.put("rowNum",rowNum);
                            errorMap.put("message","商品产地或者企业名称为空");
                            errorRowList.add(errorMap);
                            continue;
                        }
                    }else {
                        supplierCommodityVo.setCode69(code69);
                    }

                }
            } else {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","商品条码不能为空并且不能大于20位的数字");
                errorRowList.add(errorMap);
                continue;
            }
            if(brand.length()<128){
                commodityBatchInput.setBrandName(brand);
            }else {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","商品品牌长度不能超过128个字符");
                errorRowList.add(errorMap);
                continue;
            }

            if(name.length()<128){
                commodityBatchInput.setName(name);
            }else {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","商品名称长度不能超过128个字符");
                errorRowList.add(errorMap);
                continue;
            }
            if(code.matches(regex)&&code.length()<20){
                supplierCommodityVo.setCode(code);
            }else {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","商品商家编码长度不能大于20位字母或数字");
                errorRowList.add(errorMap);
                continue;
            }
            if(!"".equals(tag)){
                if ( null != mapTag.get(tag) ) {
                    commodityBatchInput.setTagId(mapTag.get(tag));
                    commodityBatchInput.setTagName(tag);
                }else {
                    Map<String, Object> errorMap =new HashMap<String, Object>();
                    errorMap.put("rowNum",rowNum);
                    errorMap.put("message","此商品标签不存在");
                    errorRowList.add(errorMap);
                    continue;
                }
            }

            if(originPlace.length()<128){
                commodityBatchInput.setOriginPlace(originPlace);
            }else {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","商品产地长度不能超过128个字符");
                errorRowList.add(errorMap);
                continue;
            }
            if(companyName.length()<128){
                commodityBatchInput.setCompanyName(companyName);
            }else {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","企业名称长度不能超过128个字符");
                errorRowList.add(errorMap);
                continue;
            }
            CommCategory commCategoryone = commCategoryDao.findCommCategoryByNameAndPid(commCategoryOne, pid);
            if (null != commCategoryone) {
                pid = commCategoryone.getId();
                commodityBatchInput.setCategoryOneId(commCategoryone.getId());
            } else {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","商品分类一级不存在");
                errorRowList.add(errorMap);
                continue;
            }
            CommCategory commCategorytwo = commCategoryDao.findCommCategoryByNameAndPid(commCategoryTwo, pid);
            if (null != commCategorytwo) {
                pid = commCategorytwo.getId();
                commodityBatchInput.setCategoryTwoId(commCategorytwo.getId());
            } else {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","商品分类二级不存在");
                errorRowList.add(errorMap);
                continue;
            }

            CommCategory commCategorythree = commCategoryDao.findCommCategoryByNameAndPid(commCategoryThree, pid);
            if (null != commCategorythree) {
                commodityBatchInput.setCategoryThreeId(commCategorythree.getId());
            } else {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","商品分类三级不存在");
                errorRowList.add(errorMap);
                continue;
            }
            commodityBatchInput.setRemark(remark);

            if ( null != commMeasureSpecMap.get(measureSpecName) ) {
                supplierCommodityVo.setMeasureSpecName(measureSpecName);
                supplierCommodityVo.setMeasureSpecId( commMeasureSpecMap.get(measureSpecName) );
            } else {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","规格单位不存在");
                errorRowList.add(errorMap);
                continue;
            }


            if(measureSpecVal.matches(regexnum)&&measureSpecVal.length()<=4&&Integer.parseInt(measureSpecVal)>0&&Integer.parseInt(measureSpecVal)<=9999){

            } else {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","规格值必须是1到9999的正整数");
                errorRowList.add(errorMap);
                continue;
            }
            supplierCommodityVo.setMeasureSpecVal(Long.parseLong(measureSpecVal));
            if(cartonVal.matches(regexnum)&&cartonVal.length()<=4&&Integer.parseInt(cartonVal)>0&&Integer.parseInt(cartonVal)<=9999){

            } else {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","箱规值必须是1到9999的正整数");
                errorRowList.add(errorMap);
                continue;
            }
            if ( null != commCartonMap.get(cartonName) ) {
                supplierCommodityVo.setCartonName(cartonName);
                supplierCommodityVo.setCartonId( commCartonMap.get(cartonName) );
                supplierCommodityVo.setCartonVal(Long.parseLong(cartonVal));
            } else {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","库存单位不存在");
                errorRowList.add(errorMap);
                continue;
            }
            String ruleVal = "("+measureSpecVal+measureSpecName+"*"+cartonVal+unitName+")"+"/"+cartonName;
            supplierCommodityVo.setRuleVal(ruleVal);
            img=img.replaceAll("，", ",");
          String []  imgStr  =  img.split(",");
        if(imgStr.length>0&&imgStr.length<11){
            commodityBatchInput.setImage(img);
        } else {
            Map<String, Object> errorMap =new HashMap<String, Object>();
            errorMap.put("rowNum",rowNum);
            errorMap.put("message","图片应为1-10张");
            errorRowList.add(errorMap);
            continue;
        }
            String regexprice ="-?[0-9]+.*[0-9]*";
        if(!unitPrice.matches(regexprice)){
            Map<String, Object> errorMap =new HashMap<String, Object>();
            errorMap.put("rowNum",rowNum);
            errorMap.put("message","供货价不能是非数字，必须是1-99999999的数");
            errorRowList.add(errorMap);
            continue;
        }
            if(!price.matches(regexprice)){
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","批发价不能是非数字，必须是1-99999999的数");
                errorRowList.add(errorMap);
                continue;
            }
            supplierCommodityVo.setUnitPrice(DataCompare.roundData(new BigDecimal(unitPrice), 2));
            supplierCommodityVo.setPrice(DataCompare.roundData(new BigDecimal(price), 2));
            supplierCommodityVo.setInventory(0L);
            if (supplierCommodityVo.getPrice().compareTo(BigDecimal.ZERO) !=1||supplierCommodityVo.getPrice().compareTo(CommConstant.MAX_VALUE) == 1) {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","批发价必须是1-99999999的数");
                errorRowList.add(errorMap);
                continue;
            } else if (supplierCommodityVo.getUnitPrice().compareTo(BigDecimal.ZERO) != 1||supplierCommodityVo.getUnitPrice().compareTo(CommConstant.MAX_VALUE) == 1) {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","供货价必须是1-99999999的数");
                errorRowList.add(errorMap);
                continue;
            } else if (supplierCommodityVo.getUnitPrice().compareTo(supplierCommodityVo.getPrice()) == 1) {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","批发价应大于等于供货价");
                errorRowList.add(errorMap);
                continue;
            }
            if ( null != commUnitMap.get(unitName) ) {
                supplierCommodityVo.setUnitId( commUnitMap.get(unitName) );
                supplierCommodityVo.setUnitName(unitName);
            } else {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","规格单位不存在");
                errorRowList.add(errorMap);
                continue;
            }
            if("".equals(minOrderQuantity)){
                supplierCommodityVo.setMinOrderQuantity(1);
            }else {
                if(minOrderQuantity.matches(regexnum)&&minOrderQuantity.length()<=3&&Integer.parseInt(minOrderQuantity)>0 &&Integer.parseInt(minOrderQuantity) <= 999){
                    supplierCommodityVo.setMinOrderQuantity(Integer.parseInt(minOrderQuantity));
                }else {
                    Map<String, Object> errorMap =new HashMap<String, Object>();
                    errorMap.put("rowNum",rowNum);
                    errorMap.put("message","最小起订量必须是1到999的正整数");
                    errorRowList.add(errorMap);
                    continue;
                }
            }
            if(guaranteePeriod.matches(regex)&&guaranteePeriod.length()<=4&&Integer.parseInt(guaranteePeriod)>0&&Integer.parseInt(guaranteePeriod)<=9999){
                supplierCommodityVo.setGuaranteePeriod(Integer.parseInt(guaranteePeriod));
            }else {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","商品有效期必须是1到9999的正整数");
                errorRowList.add(errorMap);
                continue;
            }

            supplierCommodityVo.setGuaranteePeriodUnit(guaranteePeriodUnit);
            if(DateUtil.isDate(productionDate)){
                supplierCommodityVo.setProductionDate(DateUtil.stringToDate(productionDate));
            }else {
                Map<String, Object> errorMap =new HashMap<String, Object>();
                errorMap.put("rowNum",rowNum);
                errorMap.put("message","生产日期格式不正确");
                errorRowList.add(errorMap);
                continue;
            }

            commodityList.add(supplierCommodityVo);
            commodityBatchInput.setCommodityList(commodityList);
            commodityBatchInputs.add(commodityBatchInput);
        }
        return  commodityBatchInputs;
    }



    /**
     * 解压文件
     * @param tempPath
     * @param filename
     * @param newFileName
     * @param excelmap
     * @return newFileName
     */
    private String deCompressFile(String tempPath, String filename, String newFileName, Map excelmap) throws Exception{
        try {
            ZipUtil.deCompress(tempPath + filename,tempPath + newFileName,true);
            //解压完成后 遍历文件夹 如果有中文则修改为数字
            FileUtil.getreNameFile(tempPath + newFileName, newFileName);
            File file = new File(tempPath + newFileName);
            File[] tempList = file.listFiles();
            for (int i = 0; i < tempList.length; i++) {
                if (tempList[i].isFile()) {
                    String type = tempList[i].toString().substring(tempList[i].toString().lastIndexOf(".") + 1);
                    if(type.indexOf("xls")>=0 || type.indexOf("xlsx")>=0){
                        excelmap.put("excel", tempList[i].getName());
                    }
                }
                if(tempList[i].isDirectory()) {
                    newFileName = newFileName + "/" +tempList[i].getName();
                    File[] tempListdir = tempList[i].listFiles();
                    for (int j = 0; j < tempListdir.length; j++) {
                        if (tempListdir[j].isFile()) {//是文件
                            String type = tempListdir[j].toString().substring(tempListdir[j].toString().lastIndexOf(".") + 1);
                            if(type.indexOf("xls")>=0 || type.indexOf("xlsx")>=0){
                                excelmap.put("excel", tempListdir[j].getName());
                            }
                        }
                    }
                }
            }
        } catch (Exception e) {
            logger.error("解压失败!", e);
            throw new Exception("解压失败!");
        }
        return  newFileName;
    }

    /**
     * 文件移动
     * @param request
     * @param tempPath
     * @return filename
     */
    private String transferTo(HttpServletRequest request, String tempPath){
        String   filename = "";
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        if(multipartResolver.isMultipart(request)){
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
            //取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            try {
                if(iter.hasNext()){
                    //取得上传文件
                    MultipartFile file = multiRequest.getFile(iter.next());
                    if(file != null){
                        //取得当前上传文件的文件名称
                        filename = file.getOriginalFilename();
                        File fullFile = new File(file.getOriginalFilename().trim());
                        File newFile = new File(tempPath + fullFile.getName());
                        if (!new File(tempPath).isDirectory()) {
                            new File(tempPath).mkdirs();
                        }
                        file.transferTo(newFile);
                    }
                }
            } catch (IOException e) {
                logger.error("移动文件失败.", e);
                e.printStackTrace();
            }
        }
        return  filename;
    }

    /**
     * 商品批量导出
     * @param request
     * @param response
     * @param commExportInput
     * @return
     */
    @Override
    public Result exportExcel(HttpServletRequest request, HttpServletResponse response , CommExportInput commExportInput) {
        //判断导出模板是否存在
        if (urlFile.isEmpty()) {
            return Result.fail("指定模板文件不存在！");
        }
        List<Integer> pageNumList;
        //获取区间列表
        //用于导出区间列表
        List<CommodityExportOutput> commodityList = commodityDao.findByIds(commExportInput);

        if (commExportInput.getPageNum() != null && commExportInput.getPageNum().length() > 0
                    && commExportInput.getPageSize() != null && commExportInput.getPageSize() >0) {
            //将区间pageNum转化为list
            pageNumList = Arrays.asList(commExportInput.getPageNum().toString().split(","))
                            .stream().map(s -> Integer.parseInt(s.trim())).collect(Collectors.toList());
            //获取区间页
            if(pageNumList.size() > 1 ) {
                commodityList = (List<CommodityExportOutput>)PageTool.getListByPage(commodityList, pageNumList.get(0), pageNumList.get(1), commExportInput.getPageSize());
            }
            //获取当前页列表
            if(pageNumList.size() == 1){
                commodityList = (List<CommodityExportOutput>) PageTool.getListByPage(commodityList, pageNumList.get(0), pageNumList.get(0), commExportInput.getPageSize());
            }
        }
        List<Object[]> dataList = new ArrayList<>();
        commodityList.forEach(commodityExportOutput -> {
            Object[] key = commodityExportOutput.toString().split(",");
            dataList.add(key);
        });
        POIExcelUtil.writeOutExcel(urlFile, dataList, CommConstant.POI_START_ROW, response, CommConstant.SHEET_NAME, CommConstant.FILE_NAME);
        return Result.success("导出商品成功！");
    }


    /**
     * 根据供应商更新商品失效
     *
     * @param accountStatus 供应商状态
     * @return
     */
    @Override
    public void updateCommInvalidStatus(Long supplierId, Integer accountStatus) {

        SupplierCommodity supplierCommodity = new SupplierCommodity();
        supplierCommodity.setSupplierId(supplierId);
        //如果供应商被禁用，则商品失效
        if(accountStatus == CommConstant.ACCOUNT_INVALID_STATUS){
            supplierCommodity.setInvalidStatus(CommConstant.COMM_INVALID_STATUS);
        }
        if(accountStatus == CommConstant.ACCOUNT_ACTIVE_STATUS){
            supplierCommodity.setInvalidStatus(CommConstant.COMM_ACTIVE_STATUS);
        }
        supplierCommodity.setUpdatedAt(new Date());
        supplierCommodityDao.updateInvalidStatus(supplierCommodity);

    }
    private Result saveBatchCommodity(@Valid CommodityBatchInput commodityBatchInput,Long supplierId){

        //获取当前供应商
        Account account = accountDao.selectByPrimaryKey(supplierId);
        if(null == account){
            return Result.fail("供应商不存在！");
        }
        CommodityImageVo commodityImageVo= new CommodityImageVo();
        commodityImageVo.setImage(commodityBatchInput.getImage());
        commodityImageVo.setCode69(commodityBatchInput.getCommodityList().get(0).getCode69());
        //验证请求体
        Long categoryOneId = commodityBatchInput.getCategoryOneId();
        Long categoryTwoId = commodityBatchInput.getCategoryTwoId();
        Long categoryThreeId = commodityBatchInput.getCategoryThreeId();
        //获取三级分类list
        List<CommCategory> commCategoryList = commCategoryDao.findByIds(categoryOneId, categoryTwoId, categoryThreeId);
        if(commCategoryList.size() < CommConstant.CATEGORY_LEVEL_NUMBER){
            return Result.fail("商品科属有分类不存在！");
        }
        CommCategory commCategoryOne = null;
        CommCategory commCategoryTwo = null;
        CommCategory commCategoryThree = null;
        for(CommCategory commCategory : commCategoryList){
            if(commCategory.getId().equals(categoryOneId)){
                commCategoryOne = commCategory;
            } else if(commCategory.getId().equals(categoryTwoId)){
                commCategoryTwo = commCategory;
            } else if(commCategory.getId().equals(categoryThreeId)){
                commCategoryThree = commCategory;
            }
        }
        //验证三级分类之间的关联是否正确
        if(!verifyAssociation(commCategoryOne, commCategoryTwo, commCategoryThree)){
            return Result.fail("商品科属之间的关联不正确！");
        }
        if(null != commodityBatchInput.getTagId()) {
            //验证商品标签是否存在
            int commTagNum = commTagDao.countById(commodityBatchInput.getTagId());
            if (commTagNum == 0) {
                return Result.fail("商品标签不存在！");
            }
        }
        //拼装商品分类code码
        String commCategoryCode = commCategoryOne.getCode() + commCategoryTwo.getCode() + commCategoryThree.getCode();

        //验证品牌是否存在，不存在则新增
        CommBrand brand = commBrandDao.findByName(commodityBatchInput.getBrandName());
        if (null == brand){
            brand = new CommBrand();
            brand.setCreatedAt(new Date());
            brand.setCreatedBy(supplierId);
            brand.setUpdatedAt(new Date());
            brand.setUpdatedBy(supplierId);
            brand.setName(commodityBatchInput.getBrandName());
            commBrandDao.save(brand);
        }
        //商品规格id集合
        String scIds = "";
        for (SupplierCommodityVo commodityVo : commodityBatchInput.getCommodityList()) {

            String code69 = commodityVo.getCode69();
            //验证商品是否存在,不存在则新增商品
            Commodity commodity = commodityDao.findCommInfoByCode69(code69);
            if(null == commodity) {
                commodity = BeanMapper.map(commodityBatchInput, Commodity.class);
                commodity.setBrandId(brand.getId());
                commodity.setCode69(code69);
                commodity.setCreatedAt(new Date());
                commodity.setCreatedBy(supplierId);
                commodity.setUpdatedAt(new Date());
                commodity.setUpdatedBy(supplierId);
                commodityDao.save(commodity);
            }
            //校验商品条码是否重复
            int scNum = supplierCommodityDao.countByCode69(code69,supplierId);
            if(scNum > 0){
                return Result.fail("商品已存在！");
            }
            //新增商品规格
            SupplierCommodity sc = BeanMapper.map(commodityVo, SupplierCommodity.class);
            sc.setRemark(commodityBatchInput.getRemark());
            sc.setTagId(commodityBatchInput.getTagId());
            sc.setStatus(CommConstant.COMM_ST_OFF_SHELVES);
            sc.setInventoryMinimum(CommConstant.INVENTORY_MINIMUM_DEFAULT_VALUE);
            sc.setInventoryStatus(CommConstant.INVENTORY_WARNING);
            sc.setSupplierId(supplierId);
            sc.setCreatedBy(supplierId);
            sc.setUpdatedBy(supplierId);
            sc.setCreatedAt(new Date());
            sc.setUpdatedAt(new Date());
            //若供应商被禁用，新增的商品是失效状态
            if(account.getAccountStatus() == CommConstant.ACCOUNT_INVALID_STATUS){
                sc.setInvalidStatus(CommConstant.COMM_INVALID_STATUS);
            }else if(account.getAccountStatus() == CommConstant.ACCOUNT_ACTIVE_STATUS){
                sc.setInvalidStatus(CommConstant.COMM_ACTIVE_STATUS);
            }
            String sku =  CommUtil.createSku(commCategoryCode, commodity.getId(), supplierId);
            sc.setSku(sku);
            supplierCommodityDao.save(sc);
            Long scId = sc.getId();
            if(scIds.equals("")){
                scIds += scId.toString();
            }else{
                scIds += "," + scId.toString();
            }
            commodityImageVo.setScId(sc.getId());
        }
        appCommSalesService.saveCommSales(scIds);
        return Result.success("校验通过", commodityImageVo);
    }
    /**
     * 批量审核
     * @param request
     * @param commAuditInput
     * @return
     */
    @Override
    public Result auditBatch(HttpServletRequest request , CommAuditInput commAuditInput){

        Long [] ids=commAuditInput.getIds();
        if(null==ids){
            return Result.fail("失败");
        }
        for(Long id:ids){
            SupplierCommodityAudit supplierCommodityAudit=supplierCommodityAuditDao.findSupplierCommodityAuditById(id);
            if(null ==  supplierCommodityAudit ){
                continue;
            }
            int status= supplierCommodityAudit.getStatus();
            int auditResult =commAuditInput.getAuditResult();
            Long scId=supplierCommodityAudit.getScId();
            SupplierCommodity supplierCommodity=supplierCommodityDao.findOne(scId);
            if (CommConstant.COMM_EDIT_AUDIT == status){
                //编辑待审核
                if(CommConstant.AUDIT_NOT_PASS == auditResult){
                    //审核未通过
                    supplierCommodityDao.updateSupplierCommodityStatusById(scId,supplierCommodity.getStatus(),new Date());
                    status=supplierCommodity.getStatus();
                }else   if(CommConstant.AUDIT_PASS == auditResult){
                    //审核通过 Tmp表覆盖 supplierCommodity 表  覆盖图片表
                    SupplierCommodityTmp supplierCommodityTmp =supplierCommodityTmpDao.findSupplierCommodityTmpByScaId(id);

                    if(null != supplierCommodity) {
                        supplierCommodity = BeanMapper.map(supplierCommodityTmp, SupplierCommodity.class);
                        supplierCommodity.setId(scId);
                        supplierCommodityDao.update(supplierCommodity);
                    }
                    List <Long> scids= new ArrayList<Long>();
                    scids.add(scId);
                    List<CommImgeTmp> commImgeTmpList= commImgeTmpDao.findTmp(id);
                    //保存图片
                    List<CommImge> commImges = new ArrayList<>();
                    commImgeTmpList.forEach(imgeVo->{
                        CommImge imge = BeanMapper.map(imgeVo, CommImge.class);
                        imge.setScId(scId);
                        commImges.add(imge);
                    });
                    if(commImges.size()>0){
                        commImgeDao.deleteByScIds(scids);
                        commImgeDao.batchSave(commImges);
                    }
                    status=supplierCommodity.getStatus();
                }

            }else if (CommConstant.COMM_ST_ON_SHELVES_AUDIT == status){
                //上架审核
                if(CommConstant.AUDIT_NOT_PASS  == auditResult){
                    //审核未通过
                    supplierCommodityDao.updateSupplierCommodityStatusById(scId,supplierCommodity.getStatus(),new Date());
                    status=supplierCommodity.getStatus();

                }else   if(CommConstant.AUDIT_PASS  == auditResult){
                    //审核通过
                    supplierCommodityDao.updateSupplierCommodityStatusById(scId,CommConstant.COMM_ST_ON_SHELVES,new Date());
                    status=CommConstant.COMM_ST_ON_SHELVES;
                }

            }else if (CommConstant.COMM_ST_OFF_SHELVES_AUDIT == status){
                //下架审核
                if(CommConstant.AUDIT_NOT_PASS  == auditResult){
                    //审核未通过
                    supplierCommodityDao.updateSupplierCommodityStatusById(scId,supplierCommodity.getStatus(),new Date());
                    status=supplierCommodity.getStatus();
                }else   if(CommConstant.AUDIT_PASS  == auditResult){
                    //审核通过
                    supplierCommodityDao.updateSupplierCommodityStatusById(scId,CommConstant.COMM_ST_OFF_SHELVES,new Date());
                    status=CommConstant.COMM_ST_OFF_SHELVES;
                }
            }
            //更新audit表
            supplierCommodityAudit.setStatus(status);
            supplierCommodityAudit.setAuditResult(commAuditInput.getAuditResult());
            supplierCommodityAudit.setAuditOpinion(commAuditInput.getAuditOpinion());
            supplierCommodityAudit.setAuditBy(commAuditInput.getUserId());
            supplierCommodityAudit.setUpdatedAt(new Date());
            supplierCommodityAuditDao.updateSupplierCommodityAuditById(supplierCommodityAudit);
        }
        return Result.success("成功");
    }
    /**
     * 根据条件查询商品审核列表
     * @param commodityAuditInput 查询条件
     * @return 商品审核数据
     */
    @Override
    public Result<PageInfo> serachCommodityAudit(CommodityAuditInput commodityAuditInput){
        //分页
        PageTool.startPage(commodityAuditInput.getPageNum(), commodityAuditInput.getPageSize());
        //查询当前所有待审核数据集合
        List<CommodityAuditinputVo> list = supplierCommodityAuditDao.findCommodityAudit(commodityAuditInput);
        if(list!=null&&list.size()>0) {
            PageInfo pageInfo = new PageInfo(list);
            return Result.success("查询成功",pageInfo);
        }
        return Result.fail("当前审核记录为空");
    }

    /**
     * 根据 id 和入参对象 查询出商品的 图片信息
     * @param id
     * @param commodityOutput
     * @return
     */
    public CommodityOutput readImgData(Long id, CommodityOutput commodityOutput) {
        List<CommImge> commImgeList;
        if (null != commodityOutput) {
            //根据供应商商品ID获取图片列表信息
            commImgeList = commImgeDao.find(id);
            List<CommImgeVo> commImgeVoList = new ArrayList<>();
            commImgeList.forEach(commImge -> {
                CommImgeVo commImgeVo = BeanMapper.map(commImge, CommImgeVo.class);
                commImgeVoList.add(commImgeVo);
            });
            commodityOutput.setImgeList(commImgeVoList);
        }
        return commodityOutput;
    }

    /**
     * 根据审核表ID查询审核记录详情
     * @param id 审核表ID
     * @return
     */
    @Override
    public Result findAuditDetail(Long id){
        int num = supplierCommodityTmpDao.countTemByScaId(id);
        List<CommImge> commImgeList  = null;
        CommodityOutput auditDetail = null;
        if(num > 0){
          auditDetail = supplierCommodityTmpDao.findAuditDetailTmp(id);
            if (null != auditDetail) {
                //根据供应商商品ID获取图片列表信息
                commImgeList = commImgeTmpDao.findImgTmp(id);
                List<CommImgeVo> commImgeVoList = new ArrayList<>();
                commImgeList.forEach(commImge -> {
                    CommImgeVo commImgeVo = BeanMapper.map(commImge, CommImgeVo.class);
                    commImgeVoList.add(commImgeVo);
                });
                auditDetail.setImgeList(commImgeVoList);
            }
            return Result.success("查询审核记录详情成功！",auditDetail);
        }
        auditDetail = supplierCommodityAuditDao.findAuditDetail(id);
        readImgData(auditDetail.getId(),auditDetail); // 获取图片信息
        return Result.success("查询审核记录详情成功！",auditDetail);
    }

    /**
     * 供应商首页-供应商商品部分信息统计
     * @param supplierId 供应商id
     * @return
     */
    @Override
    public Result countCommDetail(Long supplierId) {
        CountCommDetailOutput countCommDetailOutput = supplierCommodityDao.countCommDetail(supplierId);
        return  Result.success("查询成功!",countCommDetailOutput);
    }
    /**
     * 根据商品ID获取商品原始数据(不是最新数据)
     * TODO 后期须优化
     * @param id
     * @return
     */
    @Override
    public Result getOldCommodity(Long id) {
        //根据商品 id 判断该商品有没有审核记录
        int auditReult = supplierCommodityAuditDao.countByScidAndAuditResult(id);
        //存放查询到的商品详细信息
        CommodityOutput commodityOutput = null;
        List<CommImge> commImgeList;
        //商品有审核状态
        if(auditReult > 0){
            //根据商品的id查询出商品的状态
            int status = supplierCommodityDao.findSupplierCommStatus(id);
            //如果是商品的状态是 6 (编辑待审核),则根据供应商商品ID获取编辑后的商品信息
            if (status > 0) {
                commodityOutput = supplierCommodityDao.findDetail(id);
                if (Ognl.isNull(commodityOutput.getCartonId())){
                    commodityOutput.setCartonName(null);
                }else {
                    // 查询箱规名称
                    CommCarton commCarton = commCartonDao.findOne(commodityOutput.getCartonId());
                    commodityOutput.setCartonName(commCarton.getName());
                }
                commodityOutput.setStatus(supplierCommodityDao.findAuditStatus(id));
                if (null != commodityOutput) {
                    //根据供应商商品ID获取图片列表信息
                    commImgeList = commImgeTmpDao.findImgTmp(commodityOutput.getScaId());
                    List<CommImgeVo> commImgeVoList = new ArrayList<>();
                    commImgeList.forEach(commImge -> {
                        CommImgeVo commImgeVo = BeanMapper.map(commImge, CommImgeVo.class);
                        commImgeVoList.add(commImgeVo);
                    });
                    commodityOutput.setImgeList(commImgeVoList);
                }
            }else {
                //商品没状态不为 6(编辑待审核),则根据供应商商品ID获取商品信息
                commodityOutput = supplierCommodityDao.findDetail(id);
                if (Ognl.isNull(commodityOutput)){
                    return Result.success("商品不存在", commodityOutput);
                }
                if (Ognl.isNull(commodityOutput.getCartonId())){
                    commodityOutput.setCartonName(null);
                }else {
                    // 查询箱规名称
                    CommCarton commCarton = commCartonDao.findOne(commodityOutput.getCartonId());
                    commodityOutput.setCartonName(commCarton.getName());
                }
                commodityOutput.setStatus(supplierCommodityDao.findAuditStatus(id));
                readImgData(id,commodityOutput); //获取图片信息
            }
        } else {
            //商品没有审核记录,则根据供应商商品ID获取商品信息
            commodityOutput = supplierCommodityDao.findDetail(id);
            if (Ognl.isNull(commodityOutput)){
                return Result.success("商品不存在", commodityOutput);
            }
            if (Ognl.isNull(commodityOutput.getCartonId())){
                commodityOutput.setCartonName(null);
            }else {
                // 查询箱规名称
                CommCarton commCarton = commCartonDao.findOne(commodityOutput.getCartonId());
                commodityOutput.setCartonName(commCarton.getName());
            }
            readImgData(id,commodityOutput); // 获取图片信息
        }
        return Result.success("查询成功", commodityOutput);
    }

}
