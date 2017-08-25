package so.sao.shop.supplier.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile; 
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import so.sao.shop.supplier.config.StorageConfig;
import so.sao.shop.supplier.dao.*;
import so.sao.shop.supplier.domain.*;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommodityInput;
import so.sao.shop.supplier.pojo.output.CommodityExportOutput;
import so.sao.shop.supplier.pojo.output.CommodityImportOutput;
import so.sao.shop.supplier.pojo.output.CommodityInfoOutput;
import so.sao.shop.supplier.pojo.output.CommodityOutput;
import so.sao.shop.supplier.pojo.vo.*;
import so.sao.shop.supplier.service.CommodityService;
import so.sao.shop.supplier.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 商品管理Service
 * Created by QuJunLong on 2017/7/18.
 */
@Service
public class CommodityServiceImpl implements CommodityService {

    @Autowired
    private CommCategoryDao commCategoryDao;
    @Autowired
    private CommCategoryShipDao commCategoryShipDao;
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

    /**
     * 新增商品
     * @param commodityInput 商品信息对象
     * @param supplierId 供应商Id
     * @return Result结果集
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveCommodity(@Valid CommodityInput commodityInput,Long supplierId){
        //返回的结果集
        Result result = new Result();
        //商品品牌为空校验
        if(StringUtil.isNull(commodityInput.getBrandName())){
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品品牌不能为空！");
            return result;
        }
        //用于存放商品69码
        Set<String> code69Sets = new TreeSet<>();
        //拼装69码集合
        for (SupplierCommodityVo commodityVo : commodityInput.getCommodityList()) {
            String code69 = commodityVo.getCode69();
            if(StringUtil.isNull(code69)){
                result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                result.setMessage("商品条码不能为空！");
                return result;
            }
            code69Sets.add(code69);
        }
        //校验插入数据是否有重复69码
        if (code69Sets.size() < commodityInput.getCommodityList().size()) {
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("存在重复的商品条码，请修改后重新添加！");
            return result;
        }
        Long categoryOneId = commodityInput.getCategoryOneId();
        Long categoryTwoId = commodityInput.getCategoryTwoId();
        Long categoryThreeId = commodityInput.getCategoryThreeId();
        //验证是否选择商品分类
        if(null == categoryOneId && null == categoryTwoId && null == categoryThreeId){
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("未选择商品科属！");
            return result;
        }
        if(null == categoryOneId){
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("未选择商品科属一级分类！");
            return result;
        }
        if(null != categoryOneId && null == categoryTwoId && null != categoryThreeId){
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("未选择商品科属二级分类！");
            return result;
        }
        //验证商品一级分类是否存在
        if(null != categoryOneId){
            CommCategory commCategoryOne = commCategoryDao.findOne(categoryOneId);
            if(null == commCategoryOne){
                result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                result.setMessage("商品科属一级分类不存在！");
                return result;
            }
        }
        //验证商品二级分类是否存在
        if(null != categoryTwoId){
            CommCategory commCategoryTwo = commCategoryDao.findOne(categoryTwoId);
            if(null == commCategoryTwo){
                result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                result.setMessage("商品科属二级分类不存在！");
                return result;
            }
        }
        //验证商品三级分类是否存在
        if(null != categoryThreeId){
            CommCategory commCategoryThree = commCategoryDao.findOne(categoryThreeId);
            if(null == commCategoryThree){
                result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                result.setMessage("商品科属三级分类不存在！");
                return result;
            }
        }
        //验证三级分类之间的关联是否正确
        if(!verifyAssociation(categoryOneId, categoryTwoId, categoryThreeId)){
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品科属之间的关联不正确！");
            return result;
        }
        if(null != commodityInput.getTagId()){
            //验证商品标签是否存在
            CommTag commTag = commTagDao.findOne(commodityInput.getTagId());
            if(null == commTag){
                result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                result.setMessage("商品标签不存在！");
                return result;
            }
        }
        //验证品牌是否存在，不存在则新增
        CommBrand brand = commBrandDao.findByName(commodityInput.getBrandName());
        if (null == brand){
            brand = new CommBrand();
            brand.setCreatedAt(new Date());
            brand.setCreatedBy(supplierId);
            brand.setUpdatedAt(new Date());
            brand.setUpdatedBy(supplierId);
            brand.setName(commodityInput.getBrandName());
            commBrandDao.save(brand);
        }
        //建立供应商和商品关系
        if(null != commodityInput.getCommodityList()){
            for (SupplierCommodityVo commodityVo : commodityInput.getCommodityList()) {
                //验证计量单位是否存在
                if(null != commodityVo.getUnitId()){
                    CommUnit commUnit = commUnitDao.findOne(commodityVo.getUnitId());
                    if(null == commUnit){
                        result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                        result.setMessage("包装单位不存在！商品条码：" + commodityVo.getCode69());
                        return result;
                    }
                }
                //验证计量规格是否存在
                if(null != commodityVo.getMeasureSpecId()){
                    CommMeasureSpec commMeasureSpec = commMeasureSpecDao.findOne(commodityVo.getMeasureSpecId());
                    if(null == commMeasureSpec){
                        result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                        result.setMessage("计量规格不存在！商品条码：" + commodityVo.getCode69());
                        return result;
                    }
                }
                String code69 = commodityVo.getCode69();
                //验证商品是否存在,不存在则新增商品
                Commodity commodity = commodityDao.findCommInfoByCode69(code69);
                if(null == commodity) {
                    commodity = new Commodity();
                    commodity.setName(commodityInput.getName());
                    commodity.setBrandId(brand.getId());
                    commodity.setCategoryOneId(categoryOneId);
                    commodity.setCategoryTwoId(categoryTwoId);
                    commodity.setCategoryThreeId(categoryThreeId);
                    commodity.setCompanyName(commodityInput.getCompanyName());
                    commodity.setMarketTime(commodityInput.getMarketTime());
                    commodity.setOriginPlace(commodityInput.getOriginPlace());
                    commodity.setCode69(code69);
                    commodity.setCreatedAt(new Date());
                    commodity.setCreatedBy(supplierId);
                    commodity.setUpdatedAt(new Date());
                    commodity.setUpdatedBy(supplierId);
                    commodityDao.save(commodity);
                    //保存公共库图片
                    if(null != commodityVo.getImgeList()){
                        for (CommImgeVo imgeVo : commodityVo.getImgeList()) {
                            TyCommImge tyCommImge = new TyCommImge();
                            tyCommImge.setCode69(code69);
                            tyCommImge.setName(imgeVo.getName());
                            tyCommImge.setSize(imgeVo.getSize());
                            tyCommImge.setUrl(imgeVo.getUrl());
                            tyCommImge.setType(imgeVo.getType());
                            tyCommImge.setThumbnailUrl(imgeVo.getThumbnailUrl());
                            tyCommImge.setCreatedAt(new Date());
                            tyCommImge.setUpdatedAt(new Date());
                            tyCommImagDao.save(tyCommImge);
                        }
                    }
                }
                //校验sku是否重复
                String sku = getSku(categoryOneId, categoryTwoId, categoryThreeId, commodity.getId(), supplierId);
                SupplierCommodity supplierCommodity = supplierCommodityDao.findBySku(sku);
                if(null != supplierCommodity){
                    result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                    result.setMessage("商品已存在！商品ID:" + sku);
                    return result;
                }
                //新增商品规格
                SupplierCommodity sc = new SupplierCommodity();
                sc.setRemark(commodityInput.getRemark());
                sc.setTagId(commodityInput.getTagId());
                sc.setCode(commodityVo.getCode());
                sc.setCode69(code69);
                sc.setInventory(commodityVo.getInventory());
                sc.setMinImg(commodityVo.getMinImg());
                sc.setMeasureSpecId(commodityVo.getMeasureSpecId());
                sc.setRuleVal(commodityVo.getRuleVal());
                sc.setPrice(commodityVo.getPrice());
                sc.setUnitPrice(commodityVo.getUnitPrice());
                sc.setUnitId(commodityVo.getUnitId());
                sc.setStatus(Constant.COMM_ST_XZ);
                sc.setSupplierId(supplierId);
                sc.setCreatedBy(supplierId);
                sc.setUpdatedBy(supplierId);
                sc.setCreatedAt(new Date());
                sc.setUpdatedAt(new Date());
                sc.setSku(sku);
                supplierCommodityDao.save(sc);
                //保存图片
                if(null != commodityVo.getImgeList()){
                    for (CommImgeVo imgeVo : commodityVo.getImgeList()) {
                        CommImge imge = new CommImge();
                        imge.setScId(sc.getId());
                        imge.setName(imgeVo.getName());
                        imge.setSize(imgeVo.getSize());
                        imge.setUrl(imgeVo.getUrl());
                        imge.setType(imgeVo.getType());
                        commImgeDao.save(imge);
                    }
                }
            }
        }
        result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
        result.setMessage("添加商品成功");
        return result;
    }

    /**
     * 生成sku
     * @param categoryOneId 商品一级分类Id
     * @param categoryTwoId 商品二级分类Id
     * @param categoryThreeId 商品三级分类Id
     * @param commId 商品Id
     * @param supplierId 供应商Id
     * @return sku
     */
    private String getSku(Long categoryOneId, Long categoryTwoId, Long categoryThreeId, Long commId, Long supplierId){
        //前6位是商品分类，每级分类占两位
        String categoryOneCode = commCategoryDao.findCodeById(categoryOneId);
        String categoryTwoCode = commCategoryDao.findCodeById(categoryTwoId);
        String categoryThreeCode = commCategoryDao.findCodeById(categoryThreeId);
        //一级分类为空
        if(StringUtil.isNull(categoryOneCode)){
            categoryOneCode = "00";
        }
        //二级分类为空
        if(StringUtil.isNull(categoryTwoCode)){
            categoryTwoCode = "00";
        }
        //三级分类为空
        if(StringUtil.isNull(categoryThreeCode)){
            categoryThreeCode = "00";
        }
        //中间6位是商品表id自增(100000开始)
        Long commIdCode = 100000 + commId;
        //后面5位是供应商id字增(10000开始)
        Long supplierIdCode = 10000 + supplierId;
        return categoryOneCode +  categoryTwoCode + categoryThreeCode + commIdCode.toString() + supplierIdCode.toString();
    }

    /**
     * 验证商品分类关联关系
     * @param categoryOneId
     * @param categoryTwoId
     * @param categoryThreeId
     * @return
     */
    private boolean verifyAssociation(Long categoryOneId, Long categoryTwoId, Long categoryThreeId){
        if(null != categoryOneId && null != categoryTwoId && null == categoryThreeId){
            CommCategory commCategoryOne = commCategoryDao.findOne(categoryOneId);
            CommCategory commCategoryTwo = commCategoryDao.findOne(categoryTwoId);
            if(!(commCategoryOne.getPid().equals(0l) && commCategoryOne.getId().equals(commCategoryTwo.getPid()))){
                return false;
            }
        }
        if(null != categoryOneId && null != categoryTwoId && null != categoryThreeId){
            CommCategory commCategoryOne = commCategoryDao.findOne(categoryOneId);
            CommCategory commCategoryTwo = commCategoryDao.findOne(categoryTwoId);
            CommCategory commCategoryThree = commCategoryDao.findOne(categoryThreeId);
            if(!(commCategoryOne.getPid().equals(0l) && commCategoryOne.getId().equals(commCategoryTwo.getPid()) && commCategoryTwo.getId().equals(commCategoryThree.getPid()))){
                return false;
            }
        }
        return true;
    }
    /**
     * 根据code69查询商品信息
     * @param code69
     * @return 商品信息
     */
    @Override
    public Result findCommodity(String code69) {
        Result result = new Result();
        CommodityInfoOutput commodityInfoOutput = commodityDao.findByCode69(code69);
        if(null == commodityInfoOutput){
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品不存在");
            return result;
        } else {
            List<TyCommImge> imgeList = tyCommImagDao.findByCode69(code69);    //获取图片集合
            commodityInfoOutput.setImgeList(imgeList);
            commodityInfoOutput.setCategoryOneName(commCategoryDao.findNameById(commodityInfoOutput.getCategoryOneId()));
            commodityInfoOutput.setCategoryTwoName(commCategoryDao.findNameById(commodityInfoOutput.getCategoryTwoId()));
            commodityInfoOutput.setCategoryThreeName(commCategoryDao.findNameById(commodityInfoOutput.getCategoryThreeId()));
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage("成功");
            result.setData(commodityInfoOutput);
            return result;
        }
    }

    /**
    /**
     * 修改商品
     * @param commodityInput 商品信息对象
     * @param supplierId
     * @return
     * @throws Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateCommodity(CommodityInput commodityInput,Long supplierId){
        //返回的结果集
        Result result = new Result();
        //建立供应商和商品关系
        if (null != commodityInput.getCommodityList()) {
            if(null != commodityInput.getTagId()){
                //验证商品标签是否存在
                CommTag commTag = commTagDao.findOne(commodityInput.getTagId());
                if(null == commTag){
                    result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                    result.setMessage("商品标签不存在！");
                    return result;
                }
            }
            for (SupplierCommodityVo commodityVo : commodityInput.getCommodityList()) {
                //验证计量单位是否存在
                if(null != commodityVo.getUnitId()){
                    CommUnit commUnit = commUnitDao.findOne(commodityVo.getUnitId());
                    if(null == commUnit){
                        result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                        result.setMessage("包装单位不存在！");
                        return result;
                    }
                }
                //验证计量规格是否存在
                if(null != commodityVo.getMeasureSpecId()){
                    CommMeasureSpec commMeasureSpec = commMeasureSpecDao.findOne(commodityVo.getMeasureSpecId());
                    if(null == commMeasureSpec){
                        result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                        result.setMessage("计量规格不存在！");
                        return result;
                    }
                }
                //修改商品规格
                SupplierCommodity sc = new SupplierCommodity();
                sc.setRemark(commodityInput.getRemark());
                sc.setTagId(commodityInput.getTagId());
                sc.setCode(commodityVo.getCode());
                sc.setInventory(commodityVo.getInventory());
                sc.setMinImg(commodityVo.getMinImg());
                sc.setMeasureSpecId(commodityVo.getMeasureSpecId());
                sc.setRuleVal(commodityVo.getRuleVal());
                sc.setPrice(commodityVo.getPrice());
                sc.setUnitPrice(commodityVo.getUnitPrice());
                sc.setUnitId(commodityVo.getUnitId());
                sc.setUpdatedBy(supplierId);
                sc.setUpdatedAt(new Date());
                sc.setId(commodityVo.getId());
                supplierCommodityDao.update(sc);
                //清空原有大图数据信息
                List<CommImge> imges = commImgeDao.find(sc.getId());
                if (imges != null && imges.size() > 0)
                {
                    List<Long> ids = new ArrayList<>();
                    for (CommImge commImge : imges)
                    {
                        Long id = commImge.getId();
                        ids.add(id);
                    }
                    commImgeDao.deleteByIds(ids);
                }
                //保存图片
                if (null != commodityVo.getImgeList()) {
                    for (CommImgeVo imgeVo : commodityVo.getImgeList()) {
                        CommImge imge = new CommImge();
                        imge.setScId(sc.getId());
                        imge.setUrl(imgeVo.getUrl());
                        imge.setName(imgeVo.getName());
                        imge.setSize(imgeVo.getSize());
                        imge.setType(imgeVo.getType());
                        commImgeDao.save(imge);
                    }
                }
            }
        }
        result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
        result.setMessage("更新商品信息成功");
        return result;
    }
    /**
     * 根据供应商商品ID获取商品详情
     * @param id
     * @return
     */
    @Override
    public Result getCommodity(Long id) {
        Result result = new Result();
        //根据供应商商品ID获取商品信息
        CommodityOutput commodityOutput = supplierCommodityDao.findDetail(id);
        if(null == commodityOutput){
            return null;
        }
        String categoryOneName = commCategoryDao.findNameById(commodityOutput.getCategoryOneId());
        String categoryTwoName = commCategoryDao.findNameById(commodityOutput.getCategoryTwoId());
        String categoryThreeName = commCategoryDao.findNameById(commodityOutput.getCategoryThreeId());
        if(!StringUtil.isNull(categoryOneName)){
            commodityOutput.setCategoryOneName(categoryOneName);
        }
        if(!StringUtil.isNull(categoryTwoName)){
            commodityOutput.setCategoryTwoName(categoryTwoName);
        }
        if(!StringUtil.isNull(categoryThreeName)){
            commodityOutput.setCategoryThreeName(categoryThreeName);
        }

        //根据供应商商品ID获取图片列表信息
        List<CommImge> commImgeList = commImgeDao.find(id);
        List<CommImgeVo> commImgeVoList = new ArrayList<CommImgeVo>();
        for(CommImge commImge : commImgeList){
            CommImgeVo commImgeVo = new CommImgeVo();
            commImgeVo.setId(commImge.getId());
            commImgeVo.setScId(commImge.getScId());
            commImgeVo.setUrl(commImge.getUrl());
            commImgeVo.setType(commImge.getType());
            commImgeVo.setSize(commImge.getSize());
            commImgeVo.setName(commImge.getName());
            commImgeVoList.add(commImgeVo);
        }
        commodityOutput.setImgeList(commImgeVoList);
        result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
        result.setMessage("查询成功");
        result.setData(commodityOutput);
        return result;
    }

    @Override
    public List<CommodityExportOutput> findByIds(Long[] ids) {
        return commodityDao.findByIds(ids);
    }

    @Override
    public Account findAccountByUserId(Long userId){
        return accountDao.findByUserId(userId);
    }

    @Override
    public BaseResult deleteCommImge(Long id) {
        boolean flag = commImgeDao.deleteById(id);
        return BaseResultUtil.transTo(flag,"删除图片成功","删除图片失败");
    }

    /**
     * 根据查询条件查询商品详情(简单条件查询)
     *
     * @param supplierId    供应商ID
     * @param inputvalue    输入参数
     * @param beginCreateAt 创建时间（起始）
     * @param endCreateAt   创建时间（终止）
     * @param pageNum       当前页号
     * @param pageSize      页面大小
     * @return
     */
    @Override
    public Result simpleSearchCommodities(Long supplierId, String inputvalue, Date beginCreateAt, Date endCreateAt, Integer pageNum, Integer pageSize) {
        Result result = new Result();
        Page page = new Page(pageNum, pageSize);
        //入参校验
        creatAtCheck(beginCreateAt,endCreateAt);
        inputvalue = stringParamCheck(inputvalue);
        //分页参数校验
        page = PageUtil.pageCheck(page);
        //开始分页
        PageHelper.startPage(page.getPageNum(),page.getRows());
        List<SuppCommSearchVo> respList = supplierCommodityDao.findSimple(supplierId, inputvalue, beginCreateAt, endCreateAt);
        Long countTotal = supplierCommodityDao.countTotalSimple(supplierId, inputvalue, beginCreateAt, endCreateAt);
        //查无数据直接返回
        if (respList.size()==0)
        {
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage("暂无商品数据");
            return result;
        }

        for (SuppCommSearchVo suppCommSearchVo : respList)
        {
            int statusNum = Integer.parseInt(suppCommSearchVo.getStatus());
            suppCommSearchVo.setStatusNum(statusNum);
            suppCommSearchVo.setStatus(Constant.getStatus(statusNum));
            //转换金额为千分位
            suppCommSearchVo.setUnitPrice("￥"+NumberUtil.number2Thousand(new BigDecimal(suppCommSearchVo.getUnitPrice())));
            suppCommSearchVo.setPrice("￥"+NumberUtil.number2Thousand(new BigDecimal(suppCommSearchVo.getPrice())));
        }
        PageInfo<SuppCommSearchVo> pageInfo = new PageInfo<SuppCommSearchVo>(respList);
        pageInfo.setTotal(countTotal);
        result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
        result.setMessage("查询完成");
        result.setData(pageInfo);
        return result;
    }

    /**
     *
     * @param supplierId 供应商ID
     * @param commCode69 商品编码
     * @param sku  SKU（商品ID）
     * @param suppCommCode 商家商品编码
     * @param commName 商品名称
     * @param status 状态
     * @param typeId 类型ID
     * @param minPrice 价格（低）
     * @param maxPrice 价格（高）
     * @param pageNum 当前页号
     * @param pageSize 页面大小
     * @return
     */
    @Override
    public Result searchCommodities(Long supplierId, String commCode69, String sku, String suppCommCode, String commName, Integer status, Long typeId,
                                    BigDecimal minPrice, BigDecimal maxPrice, Date beginCreateAt, Date endCreateAt, Integer pageNum, Integer pageSize) {
        Result result = new Result();
        Page page = new Page(pageNum, pageSize);
        //入参校验
        commCode69 = stringParamCheck(commCode69);
        sku = stringParamCheck(sku);
        suppCommCode = stringParamCheck(suppCommCode);
        commName =stringParamCheck(commName);
        priceCheck(minPrice, maxPrice);
        creatAtCheck(beginCreateAt,endCreateAt);
        //分页参数校验
        page = PageUtil.pageCheck(page);
        //开始分页
        PageHelper.startPage(page.getPageNum(),page.getRows());
        List<SuppCommSearchVo> respList = supplierCommodityDao.find(supplierId, commCode69, sku, suppCommCode, commName, status, typeId, minPrice, maxPrice, beginCreateAt, endCreateAt);
        Long countTotal = supplierCommodityDao.countTotal(supplierId, commCode69, sku, suppCommCode, commName, status, typeId, minPrice, maxPrice, beginCreateAt, endCreateAt);
        //查无数据直接返回
        if (respList.size()==0)
        {
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage("暂无商品数据");
            return result;
        }

        for (SuppCommSearchVo suppCommSearchVo : respList)
        {
            int statusNum = Integer.parseInt(suppCommSearchVo.getStatus());
            suppCommSearchVo.setStatusNum(statusNum);
            suppCommSearchVo.setStatus(Constant.getStatus(statusNum));
            //转换金额为千分位
            suppCommSearchVo.setPrice("￥"+NumberUtil.number2Thousand(new BigDecimal(suppCommSearchVo.getPrice())));
            suppCommSearchVo.setUnitPrice("￥"+NumberUtil.number2Thousand(new BigDecimal(suppCommSearchVo.getUnitPrice())));
        }
		
        PageInfo<SuppCommSearchVo> pageInfo = new PageInfo<SuppCommSearchVo>(respList);
        pageInfo.setTotal(countTotal);
        result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
        result.setMessage("查询完成");
        result.setData(pageInfo);
        return result;
    }

    /**
     * 价格校验
     * @param minPrice
     * @param maxPrice
     */
    private void priceCheck(BigDecimal minPrice, BigDecimal maxPrice)
    {
        if (null != minPrice && null != maxPrice)
        {
            int minCompare = minPrice.compareTo(BigDecimal.ZERO);   //当minCompare == -1，说明minPrice<0;
            int maxCompare = maxPrice.compareTo(BigDecimal.ZERO);   //当maxCompare == -1，说明maxPrice<0;
            int minMax = minPrice.compareTo(maxPrice);   //当minPrice > maxPrice,minMax==1
            if (minCompare == -1 || maxCompare == -1)
            {
                throw new RuntimeException("价格不能为负数");
             }

            if (minMax == 1)
            {
                throw new RuntimeException("最小金额不能大于最大金额");
            }
        }
    }

    /**
     * 价格校验
     * @param beginCreatAt 起始时间
     * @param endCreatAt 结束时间
     */
    private void creatAtCheck(Date beginCreatAt, Date endCreatAt)
    {
        if (null != beginCreatAt && null != endCreatAt)
        {
            if (endCreatAt.before(beginCreatAt))
            {
                throw new RuntimeException("起始时间不能大于终止时间");
            }
        }
    }

    /**
     * 删除商品
     * @param id 供应商商品ID
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteCommodity(Long id) {
        Result result = new Result();
        Map<String,Long> map = new HashMap<>();
        //根据商品与供应商关系ID获取供应商商品对象
        SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(id);
        if(null != supplierCommodity){
            map.put("supplierId",supplierCommodity.getSupplierId());
            //根据商品与供应商关系ID查询商品状态
            int status = supplierCommodityDao.findStatus(id);
            //商品需下架才可删除
            if(status != Constant.COMM_ST_XJ){
                result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                result.setMessage("商品需下架才可删除");
                result.setData(map);
                return result;
            }
            //删除商品,deleted更新为1
            supplierCommodityDao.deleteById(id, true, new Date());
        }

        result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
        result.setMessage("删除商品成功");
        result.setData(map);
        return result;
    }

    /**
     * 批量删除商品
     * @param ids 供应商商品ID
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result deleteCommodities(Long[] ids) {
        Result result = new Result();
        Map<String,Long> map = new HashMap<>();
        //可删除商品ID集合
        TreeSet<Long> idList = new TreeSet<>();
        //不可删除商品ID集合
        TreeSet<Long> idNotList = new TreeSet<>();
        //不可删除商品商品编码集合
        for(long id : ids){
            //根据商品与供应商关系ID获取供应商商品对象
            SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(id);
            if(null != supplierCommodity){
                map.put("supplierId",supplierCommodity.getSupplierId());
                //根据商品与供应商关系ID查询商品状态
                int status = supplierCommodityDao.findStatus(id);
                //商品需下架才可删除
                if(status != Constant.COMM_ST_XJ){
                    idNotList.add(id);
                }else{
                    idList.add(id);
                }
            }
        }
        for(long id : idList){
            //删除商品,deleted更新为1
            supplierCommodityDao.deleteById(id, true, new Date());
        }
        if(null != idNotList && idNotList.size() > 0){
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("商品需下架才可删除");
            result.setData(map);
            return result;
        }
        result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
        result.setMessage("删除商品成功");
        result.setData(map);
        return result;
    }

    /**
     * 商品上架
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateStatusSj(Long id) {
        Result result = new Result();
        Map<String,Integer> map = new HashMap<>();
        SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(id);
        if(null == supplierCommodity){
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);

            result.setMessage("该商品不存在");
            return result;
        }
        supplierCommodity = assemblyObject(id,Constant.COMM_ST_SJ);
        boolean flag = supplierCommodityDao.updateStatusSXj(supplierCommodity);
        if (flag)
        {
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage("上架商品成功");
            map.put("status",Constant.COMM_ST_SJ);
            result.setData(map);
        }else {
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("上架商品失败");
        }
        return result;
    }

    private SupplierCommodity assemblyObject(Long id,int status){
        SupplierCommodity supplierCommodity = new SupplierCommodity();
        supplierCommodity.setId(id);
        supplierCommodity.setStatus(status);
        supplierCommodity.setUpdatedAt(new Date());
        return supplierCommodity;
    }

    /**
     * 商品下架
     * @param id
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateStatusXj(Long id) {
        Result result = new Result();
        Map<String,Integer> map = new HashMap<>();
        SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(id);
        if(null == supplierCommodity){
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("该商品不存在");
            return result;
        }
        supplierCommodity = assemblyObject(id,Constant.COMM_ST_XJ);
        boolean flag = supplierCommodityDao.updateStatusSXj(supplierCommodity);
        if (flag)
        {
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage("下架商品成功");
            map.put("status",Constant.COMM_ST_XJ);
            result.setData(map);
        }else {
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("下架商品失败");
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult updateStatusSjs(Long[] ids) {
        boolean flag = false;
        if(null != ids && ids.length > 0){
            for(int i = 0; i < ids.length; i++){
                SupplierCommodity supplierCommodity = assemblyObject(ids[i],Constant.COMM_ST_SJ);
                supplierCommodityDao.updateStatusSXj(supplierCommodity);
                flag = true;
            }
        }
        return BaseResultUtil.transTo(flag,"批量上架商品成功","批量上架商品失败");
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BaseResult updateStatusXjs(Long[] ids) {
        boolean flag = false;
        if(null != ids && ids.length > 0){
            for(int i = 0; i < ids.length; i++){
                SupplierCommodity supplierCommodity = assemblyObject(ids[i],Constant.COMM_ST_XJ);
                supplierCommodityDao.updateStatusSXj(supplierCommodity);
                flag = true;
            }
        }
        return BaseResultUtil.transTo(flag,"批量下架商品成功","批量下架商品失败");
    }

    @Override
    public Result importExcel(MultipartFile multipartFile, HttpServletRequest request, StorageConfig storageConfig,Long supplierId) throws Exception {
        Result resultdata = new Result();
        Map<String ,List> outmap=new HashMap();
        List<CommodityImportOutput> commodityImportOutputList=new ArrayList<CommodityImportOutput>();
        List<Map<String, String>> list=null;
        List<CommodityInput>  commodityInputs=new ArrayList<CommodityInput>();
        int code=so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE;
        String  message="";
        //判断文件是否选择文件
        if (null == multipartFile) {

            message= "文件为空,请选择文件";
            CommodityImportOutput commodityImportOutput=new CommodityImportOutput();
            commodityImportOutput.setCode(code);
            commodityImportOutput.setMessage(message);
            commodityImportOutputList.add(commodityImportOutput);
            outmap.put("rightlist",commodityImportOutputList);
            resultdata.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
            resultdata.setMessage(message);
            return resultdata;
        }
        //解压文件
        DiskFileItemFactory factory = new DiskFileItemFactory();
        String hcfilepath = request.getSession().getServletContext()
                .getRealPath("")
                + "/";
        factory.setRepository(new File(hcfilepath));// 文件缓存路径
        ServletFileUpload upload = new ServletFileUpload(factory);
        upload.setHeaderEncoding("UTF-8");
        String tempPath = "";
        String filename = "";
        //创建一个通用的多部分解析器
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver(request.getSession().getServletContext());
        //判断 request 是否有文件上传,即多部分请求
        if(multipartResolver.isMultipart(request)){
            //转换成多部分request
            MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest)request;
            //取得request中的所有文件名
            Iterator<String> iter = multiRequest.getFileNames();
            if(iter.hasNext()){
                //取得上传文件
                MultipartFile file = multiRequest.getFile(iter.next());
                if(file != null){
                    //取得当前上传文件的文件名称
                    filename = file.getOriginalFilename();
                    //如果名称不为“”,说明该文件存在，否则说明该文件不存在
                    if(filename.trim() !=""){
                        File fullFile = new File(filename);
                        String filezspath = request.getSession().getServletContext().getRealPath("") + "/file/";
                        tempPath = filezspath;// 文件上传到的文件夹
                        File newFile = new File(tempPath + fullFile.getName());
                        if (!new File(tempPath).isDirectory()) {
                            new File(tempPath).mkdirs();
                        }

                        try {
                            file.transferTo(newFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        //接下来开始解压缩文件
        String filezspath = request.getSession().getServletContext().getRealPath("")+ "/file/";
        String ffname = DateUtil.getStringDate().replaceAll("-", "").replaceAll(" ", "").replaceAll(":", "");
        try {
            ZipUtil.deCompress(filezspath+filename,filezspath+ffname,true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //解压完成后 遍历文件夹 如果有中文则修改为数字
        FileUtil.getreNameFile(filezspath+ffname, ffname);
        File file=new File(filezspath+ffname);
        File[] tempList = file.listFiles();
        Map excelmap = new HashMap();
        List jpglist = new ArrayList();

        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                String type = tempList[i].toString().substring(tempList[i].toString().lastIndexOf(".") + 1);
                if(type.indexOf("xls")>=0 || type.indexOf("xlsx")>=0){
                    excelmap.put("excel", tempList[i].getName());
                }else if(type.indexOf("jpg")>=0||type.indexOf("JPG")>=0||type.indexOf("jpeg")>=0||type.indexOf("JPEG")>=0||type.indexOf("png")>=0||type.indexOf("PNG")>=0||type.indexOf("gif")>=0||type.indexOf("GIF")>=0){
                    jpglist.add(tempList[i].toString());//存放图片路径
                }
            }

            if(tempList[i].isDirectory()) {
                ffname = ffname + "/" +tempList[i].getName();
                File[] tempListdir = tempList[i].listFiles();
                for (int j = 0; j < tempListdir.length; j++) {
                    if (tempListdir[j].isFile()) {//是文件
                        String type = tempListdir[j].toString().substring(tempListdir[j].toString().lastIndexOf(".") + 1);
                        if(type.indexOf("xls")>=0 || type.indexOf("xlsx")>=0){
                            excelmap.put("excel", tempListdir[j].getName());
                        }else if(type.indexOf("jpg")>=0||type.indexOf("JPG")>=0){
                            jpglist.add(tempListdir[j].toString());//存放图片路径
                        }
                    }
                }
            }
        }

        String excelpath="";
        excelpath=filezspath+ffname+"/"+excelmap.get("excel").toString();
        Map<String, Map>  maps = new HashMap<>();
        //Excel中正确记录信息
        Map<Integer,Map<String, String>> mapright = new HashMap<>();
        //Excel中错误行号
        Map<Integer,Map<String, String>> maperror = new HashMap<>();

        List<Integer> errorList = new ArrayList<>();
        //使用工具类 获取Excel 内容
        if(!"".equals(excelpath)){
            maps=  ExcelReader.readExcelContent(excelpath,1);

            mapright = ( Map<Integer,Map<String, String>>)maps.get("mapright");
            maperror =( Map<Integer,Map<String, String>>)maps.get("maperror");
        }
        Map<String, String> map = null;
        List<CommRuleVo> ruleList=new ArrayList<CommRuleVo>();

        FileUtil fileutil =new FileUtil();
        // Excel 内容转 CommodityInput对象
        if(null!=mapright){
            Map.Entry<Integer,Map<String, String>> entrymap = null;
            for (Iterator< Map.Entry<Integer,Map<String, String>>> itmap = mapright.entrySet().iterator(); itmap.hasNext();) {

                entrymap = itmap.next();
                map = entrymap.getValue();
                int rownum =entrymap.getKey();
                Map.Entry<String, String> entry = null;
                CommodityInput commodityInput=new CommodityInput();
                commodityInput.setRowNum(rownum);
                List<SupplierCommodityVo> commodityList=new ArrayList<SupplierCommodityVo>();
                SupplierCommodityVo supplierCommodityVo=new SupplierCommodityVo();
                CommRuleVo commRuleVo=new CommRuleVo();
               long    pid=0;

                for (Iterator< Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext(); ) {
                    entry = it.next();
                    String key = entry.getKey() == null ? "" : entry.getKey();
                    String value = entry.getValue() == null ? "" : entry.getValue();
                    switch (key) {
                        case "商品编码":
                            supplierCommodityVo.setCode69(value);
                            break;
                        case "商品品牌":
                            commodityInput.setBrandName(value);
                            break;
                        case "图片":
                            if (!"".equals(value)) {
                                String[] imgs = new String[15];
                                if (value.contains(",") || value.contains("，")) {
                                    if (value.contains(",")) {
                                        imgs = value.split(",");
                                    } else if (value.contains("，")) {
                                        imgs = value.split("，");
                                    }
                                } else {
                                    imgs[0] = value;
                                }
                                List<String> imglist = new ArrayList<>();
                                for (String img : imgs) {
                                    if (img != null || "".equals(img)) {
                                        imglist.add(img);
                                    }
                                }
                                // 上传图片
                                List<Result> results = fileutil.UploadFiles(filezspath + ffname, imglist, storageConfig);
                                for (Result result : results) {
                                    if (result.getCode() == so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS) {
                                        List<BlobUpload> blobUploadEntities = (List<BlobUpload>) result.getData();
                                        List<CommImgeVo> commImgeVoList = new ArrayList<CommImgeVo>();
                                        for (int t = 0; t < blobUploadEntities.size(); t++) {
                                            BlobUpload blobUpload = blobUploadEntities.get(t);
                                            if (t == 0) {
                                                supplierCommodityVo.setMinImg(blobUpload.getMinImgUrl());
                                            }
                                            CommImgeVo commImgeVo = new CommImgeVo();
                                            commImgeVo.setName(blobUpload.getFileName());
                                            commImgeVo.setSize(blobUpload.getSize());
                                            commImgeVo.setUrl(blobUpload.getUrl());
                                            commImgeVo.setType(blobUpload.getType());
                                            commImgeVoList.add(commImgeVo);
                                        }
                                        supplierCommodityVo.setImgeList(commImgeVoList);
                                    }
                                }

                            }
                            break;
                        case "商品名称":
                            commodityInput.setName(value);
                            break;
                        case "商家编码":
                            supplierCommodityVo.setCode(value);
                            break;
                        case "商品分类一级":
                            if (!"".equals(value)) {
                                CommCategory commCategoryone = commCategoryDao.findCommCategoryByNameAndPid(value,pid);
                                if (null != commCategoryone) {
                                    pid=commCategoryone.getId();
                                    commodityInput.setCategoryOneId(commCategoryone.getId());
                                }
                            }
                            break;
                        case "商品分类二级":
                            if (!"".equals(value)) {
                                CommCategory commCategorytwo = commCategoryDao.findCommCategoryByNameAndPid(value,pid);
                                if (null != commCategorytwo) {
                                    pid=commCategorytwo.getId();
                                    commodityInput.setCategoryTwoId(commCategorytwo.getId());
                                }
                            }
                            break;
                        case "商品分类三级":
                            if (!"".equals(value)) {
                                CommCategory commCategorythree = commCategoryDao.findCommCategoryByNameAndPid(value,pid);
                                if (null != commCategorythree) {

                                    commodityInput.setCategoryThreeId(commCategorythree.getId());
                                }
                            }
                            break;
                        case "商品描述":
                            commodityInput.setRemark(value);
                            break;
                        case "商品介绍":
                            commodityInput.setRemark(value);
                            break;
                        case "商品规格":
                            if (!"".equals(value)) {
                                supplierCommodityVo.setMeasureSpecName(value);
                                List<CommMeasureSpec> commMeasureSpeclist=   commMeasureSpecDao.findByName(supplierId,value);
                                if(null!=commMeasureSpeclist&&commMeasureSpeclist.size()>0){
                                    supplierCommodityVo.setMeasureSpecId(commMeasureSpeclist.get(0).getId());
                                }

                            }
                            break;
                        case "商品规格值":
                            supplierCommodityVo.setRuleVal(value);
                            break;
                        case "成本价":
                            if (!"".equals(value)) {
                                supplierCommodityVo.setUnitPrice(new BigDecimal(value));
                            }
                            break;
                        case "市场价":
                            if (!"".equals(value)) {
                                supplierCommodityVo.setPrice(new BigDecimal(value));
                            }
                            break;
                        case "库存":
                            if (!"".equals(value)) {
                                supplierCommodityVo.setInventory(Double.parseDouble(value));
                            }

                            break;
                        case "计量单位":
                            Long unitId = null;
                            if (!"".equals(value)) {
                                supplierCommodityVo.setUnitName(value);
                                List<CommUnit> commUnitList=      commUnitDao.findNameAndSupplierId(supplierId,value);
                                if(null!=commUnitList&&commUnitList.size()>0){
                                    supplierCommodityVo.setUnitId(commUnitList.get(0).getId());
                                }
                            }

                            break;
                        case "商品标签":
                            Long tagId = null;
                            if (!"".equals(value)) {

                                List<CommTag>  commTagList=    commTagDao.findByNameAndSupplierId(value,supplierId);
                                if(null!=commTagList&&commTagList.size()>0){
                                    commodityInput.setTagId(commTagList.get(0).getId());
                                }
                            }

                            break;
                        case "商品产地":
                            commodityInput.setOriginPlace(value);
                            break;
                        case "企业名称":
                            commodityInput.setCompanyName(value);
                            break;
                        case "上市时间":
                            try {
                                if(!"".equals(value)){

                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                                    Date date = sdf.parse(value.trim());
                                    commodityInput.setMarketTime(date);
                                }



                            } catch (Exception e) {
                            }
                            break;
                    }
                }
                commodityList.add(supplierCommodityVo);
                ruleList.add(commRuleVo);
                commodityInput.setCommodityList(commodityList);
                commodityInputs.add(commodityInput);
            }
        }
        //导入数据库
        Iterator it=commodityInputs.iterator();
        while(it.hasNext()){
            CommodityInput commodityInput= (CommodityInput) it.next();
            for (int g = 0; g < commodityInput.getCommodityList().size(); g++) {
                String code69 = commodityInput.getCommodityList().get(g).getCode69() == null ? "" : commodityInput.getCommodityList().get(g).getCode69();
                int rowNum = commodityInput.getRowNum() ;
                if (!"".equals(code69)) {
                    if(!Tools.isNumeric(code69)){
                        errorList.add(rowNum);
                        it.remove();
                    }else {
                        SupplierCommodity suppliercommodity = supplierCommodityDao.findSupplierCommodityInfo(code69,supplierId);
                        if (null != suppliercommodity) {
                            errorList.add(rowNum);
                            it.remove();
                        }
                    }
                }
            }
        }

        for(int n=0;n<commodityInputs.size();n++){
            Result baseResult1=  saveCommodity(commodityInputs.get(n),supplierId);
            String code69 = commodityInputs.get(n).getCommodityList().get(0).getCode69() == null ? "" : commodityInputs.get(n).getCommodityList().get(0).getCode69();
            String sjcode = commodityInputs.get(n).getCommodityList().get(0).getCode() == null ? "" : commodityInputs.get(n).getCommodityList().get(0).getCode();
            String brand = commodityInputs.get(n).getBrandName() == null ? "" : commodityInputs.get(n).getBrandName();
            String name = commodityInputs.get(n).getName()== null ? "" : commodityInputs.get(n).getName();
            String originPlace = commodityInputs.get(n).getOriginPlace()== null ? "" : commodityInputs.get(n).getOriginPlace();
            Long  tagId = commodityInputs.get(n).getTagId()== null ? 0 : commodityInputs.get(n).getTagId();
            String tagName="";
            if(null!=commodityInputs.get(n).getTagId()){
                tagName=commTagDao.findOne(commodityInputs.get(n).getTagId()).getName();
            }
            String unit=commodityInputs.get(n).getCommodityList().get(0).getUnitName() == null ? "" : commodityInputs.get(n).getCommodityList().get(0).getUnitName();
            String  measureSpecName=commodityInputs.get(n).getCommodityList().get(0).getMeasureSpecName() == null ? "" : commodityInputs.get(n).getCommodityList().get(0).getMeasureSpecName();
            Date marketTime =  commodityInputs.get(n).getMarketTime();
            String companyName = commodityInputs.get(n).getCompanyName()== null ? "" : commodityInputs.get(n).getCompanyName();
            String ruleval = commodityInputs.get(n).getCommodityList().get(0).getRuleVal()== null ? "" : commodityInputs.get(n).getCommodityList().get(0).getRuleVal();
            Double inverntory = commodityInputs.get(n).getCommodityList().get(0).getInventory() == null ? 0.0: commodityInputs.get(n).getCommodityList().get(0).getInventory();
            int row_num=commodityInputs.get(n).getRowNum();
            if(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS!=baseResult1.getCode()){
                errorList.add(row_num);
            }else {
                code=so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS;
                message="商品编码:" + code69+"成功导入！";
                CommodityImportOutput commodityImportOutput=new CommodityImportOutput();
                commodityImportOutput.setCode(code);
                commodityImportOutput.setMessage(message);
                commodityImportOutput.setCode69(code69);
                commodityImportOutput.setBrand(brand);
                commodityImportOutput.setSjcode(sjcode);
                commodityImportOutput.setRowNum(row_num);
                commodityImportOutput.setTagName(tagName);
                commodityImportOutput.setUnit(unit);
                commodityImportOutput.setMeasureSpecName(measureSpecName);
                commodityImportOutput.setCompanyName(companyName);
                commodityImportOutput.setOriginPlace(originPlace);
                commodityImportOutput.setMarketTime(marketTime);
                commodityImportOutput.setName(name);
                commodityImportOutput.setRuleVal(ruleval);
                commodityImportOutput.setInventory(inverntory);
                commodityImportOutputList.add(commodityImportOutput);
            }
        }

        if(null!=maperror) {
            Map.Entry<Integer, Map<String, String>> entrymaperro = null;
            for (Iterator<Map.Entry<Integer, Map<String, String>>> itmaperro = maperror.entrySet().iterator(); itmaperro.hasNext(); ) {
                entrymaperro = itmaperro.next();
                int rownum = entrymaperro.getKey();
                errorList.add(rownum);
            }
        }
        Collections.sort(errorList);
        fileutil.deleteDirectory(filezspath+ffname);
        outmap.put("rightlist",commodityImportOutputList);
        outmap.put("errolist",errorList);
        resultdata.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
        resultdata.setMessage("成功导入！");
        resultdata.setData(outmap);
        return resultdata;
    }

    /**
     * 字符串前后去空格
     * @param string  需要去空格的字符串
     * @return String 处理后的字符串
     */
    private String stringParamCheck(String string) {
        if (null != string)
        {
            string = string.trim();
        }
        return  string;
    }

}
