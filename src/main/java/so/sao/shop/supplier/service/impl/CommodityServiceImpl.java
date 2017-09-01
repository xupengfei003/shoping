package so.sao.shop.supplier.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile; 
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import so.sao.shop.supplier.config.CommConstant;
import so.sao.shop.supplier.config.StorageConfig;
import so.sao.shop.supplier.config.azure.AzureBlobService;
import so.sao.shop.supplier.dao.*;
import so.sao.shop.supplier.domain.*;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommSearchInput;
import so.sao.shop.supplier.pojo.input.CommSimpleSearchInput;
import so.sao.shop.supplier.pojo.input.CommodityInput;
import so.sao.shop.supplier.pojo.input.CommodityUpdateInput;
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
    private AzureBlobService azureBlobService;

    /**
     * 新增商品
     * @param commodityInput 商品信息对象
     * @param supplierId 供应商Id
     * @return Result结果集
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result saveCommodity(@Valid CommodityInput commodityInput,Long supplierId){
        //验证请求体
        Result result = validateCommodityInput(commodityInput);
        if(result.getCode() == 0) return result;
        //获取商品分类code码
        String commCategoryCode = (String)result.getData();
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
        for (SupplierCommodityVo commodityVo : commodityInput.getCommodityList()) {
            //验证计量单位是否存在
            if(null != commodityVo.getUnitId()){
                int commUnitNum = commUnitDao.countById(commodityVo.getUnitId());
                if(commUnitNum == 0){
                    return Result.fail("包装单位不存在！商品条码：" + commodityVo.getCode69());
                }
            }
            //验证计量规格是否存在
            if(null != commodityVo.getMeasureSpecId()){
                int commMeasureSpecNum = commMeasureSpecDao.countById(commodityVo.getMeasureSpecId());
                if(commMeasureSpecNum == 0){
                    return Result.fail("计量规格不存在！商品条码：" + commodityVo.getCode69());
                }
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
            }
            //校验商品条码是否重复
            int scNum = supplierCommodityDao.countByCode69(code69,supplierId);
            if(scNum > 0){
                return Result.fail("商品已存在！");
            }
            //新增商品规格
            SupplierCommodity sc = BeanMapper.map(commodityVo, SupplierCommodity.class);
            sc.setRemark(commodityInput.getRemark());
            sc.setTagId(commodityInput.getTagId());
            sc.setStatus(CommConstant.COMM_ST_XZ);
            sc.setSupplierId(supplierId);
            sc.setCreatedBy(supplierId);
            sc.setUpdatedBy(supplierId);
            sc.setCreatedAt(new Date());
            sc.setUpdatedAt(new Date());
            String sku = createSku(commCategoryCode, commodity.getId(), supplierId);
            sc.setSku(sku);
            supplierCommodityDao.save(sc);
            //保存图片
            List<CommImge> commImges = new ArrayList<>();
            commodityVo.getImgeList().forEach(imgeVo->{
                CommImge imge = BeanMapper.map(imgeVo, CommImge.class);
                imge.setScId(sc.getId());
                commImges.add(imge);
            });
            commImgeDao.batchSave(commImges);
        }
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
        for (SupplierCommodityVo commodityVo : commodityInput.getCommodityList()) {
            String code69 = commodityVo.getCode69();
            code69Sets.add(code69);
        }
        //校验插入数据是否有重复69码
        if (code69Sets.size() < commodityInput.getCommodityList().size()) {
            return Result.fail("存在重复的商品条码，请修改后重新添加！");
        }
        Long categoryOneId = commodityInput.getCategoryOneId();
        Long categoryTwoId = commodityInput.getCategoryTwoId();
        Long categoryThreeId = commodityInput.getCategoryThreeId();
        //验证是否选择商品科属
        if(null == categoryTwoId && null != categoryThreeId){
            return Result.fail("未选择商品科属二级分类！");
        }
        //获取三级分类list
        List<CommCategory> commCategoryList = commCategoryDao.findByIds(categoryOneId, categoryTwoId, categoryThreeId);
        List<Long> ids = new ArrayList<>();
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
            ids.add(commCategory.getId());
        }
        //验证商品一级分类是否存在
        if(!ids.contains(categoryOneId)){
            return Result.fail("商品科属一级分类不存在！");
        }
        //验证商品二级分类是否存在
        if(null != categoryTwoId){
            if(!ids.contains(categoryTwoId)){
                return Result.fail("商品科属二级分类不存在！");
            }
        }
        //验证商品三级分类是否存在
        if(null != categoryThreeId){
            if(!ids.contains(categoryThreeId)){
                return Result.fail("商品科属三级分类不存在！");
            }
        }
        //验证三级分类之间的关联是否正确
        if(!verifyAssociation(commCategoryOne, commCategoryTwo, commCategoryThree)){
            return Result.fail("商品科属之间的关联不正确！");
        }
        if(null != commodityInput.getTagId()){
            //验证商品标签是否存在
            int commTagNum = commTagDao.countById(commodityInput.getTagId());
            if(commTagNum == 0){
                return Result.fail("商品标签不存在！");
            }
        }
        //拼装商品分类code码
        String categoryOneCode = commCategoryOne.getCode();
        String categoryTwoCode = CommConstant.CATEGORY_NULL_CODE;
        String categoryThreeCode = CommConstant.CATEGORY_NULL_CODE;
        if(null != commCategoryTwo){
            categoryTwoCode = commCategoryTwo.getCode();
        }
        if(null != commCategoryThree){
            categoryThreeCode = commCategoryThree.getCode();
        }
        String commCategoryCode = categoryOneCode + categoryTwoCode + categoryThreeCode;
        return Result.success("校验通过", commCategoryCode);
    }

    /**
     * 生成sku
     * @param commCategoryCode 商品分类code码
     * @param commId 商品Id
     * @param supplierId 供应商Id
     * @return sku
     */
    private String createSku(String commCategoryCode, Long commId, Long supplierId){
        //前6位是商品分类，每级分类占两位,中间6位是商品表id自增(100000开始)
        Long commIdCode = 100000 + commId;
        //后面5位是供应商id字增(10000开始)
        Long supplierIdCode = 10000 + supplierId;
        return commCategoryCode + commIdCode.toString() + supplierIdCode.toString();
    }

    /**
     * 验证商品分类关联关系
     * @param commCategoryOne
     * @param commCategoryTwo
     * @param commCategoryThree
     * @return
     */
    private boolean verifyAssociation(CommCategory commCategoryOne, CommCategory commCategoryTwo, CommCategory commCategoryThree){
        if(null != commCategoryTwo && null == commCategoryThree){
            if(!(commCategoryOne.getPid().equals(CommConstant.CATEGORY_ONE_PID) && commCategoryOne.getId().equals(commCategoryTwo.getPid()))){
                return false;
            }
        }
        if(null != commCategoryTwo && null != commCategoryThree){
            if(!(commCategoryOne.getPid().equals(CommConstant.CATEGORY_ONE_PID) && commCategoryOne.getId().equals(commCategoryTwo.getPid()) && commCategoryTwo.getId().equals(commCategoryThree.getPid()))){
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
        if(null != commodityUpdateInput.getTagId()){
            //验证商品标签是否存在
            int commTagNum = commTagDao.countById(commodityUpdateInput.getTagId());
            if(commTagNum == 0){
                return Result.fail("商品标签不存在！");
            }
        }
        for (SupplierCommodityUpdateVo commodityVo : commodityUpdateInput.getCommodityList()) {
            //验证商品是否存在
            int supplierCommodityNum = supplierCommodityDao.countById(commodityVo.getId());
            if (supplierCommodityNum == 0) {
                return Result.fail("商品不存在！");
            }
            //验证包装单位是否存在
            int commUnitNum = commUnitDao.countById(commodityVo.getUnitId());
            if (commUnitNum == 0) {
                return Result.fail("包装单位不存在！");
            }
            //验证计量规格是否存在
            int commMeasureSpecNum = commMeasureSpecDao.countById(commodityVo.getMeasureSpecId());
            if (commMeasureSpecNum == 0) {
                return Result.fail("计量规格不存在！");
            }
            //修改商品规格
            SupplierCommodity sc = BeanMapper.map(commodityVo, SupplierCommodity.class);
            sc.setRemark(commodityUpdateInput.getRemark());
            sc.setTagId(commodityUpdateInput.getTagId());
            sc.setUpdatedBy(supplierId);
            sc.setUpdatedAt(new Date());
            sc.setId(commodityVo.getId());
            supplierCommodityDao.update(sc);
            //清空原有大图数据信息
            List<Long> ids = commImgeDao.findIdsByScId(sc.getId());
            if (ids != null && ids.size() > 0)
            {
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
        }
        return Result.success("更新商品信息成功");
    }
    /**
     * 根据供应商商品ID获取商品详情
     * @param id
     * @return
     */
    @Override
    public Result getCommodity(Long id) {
        //根据供应商商品ID获取商品信息
        CommodityOutput commodityOutput = supplierCommodityDao.findDetail(id);
        if(null != commodityOutput){
            //根据供应商商品ID获取图片列表信息
            List<CommImge> commImgeList = commImgeDao.find(id);
            List<CommImgeVo> commImgeVoList = new ArrayList<>();
            commImgeList.forEach(commImge->{
                CommImgeVo commImgeVo = BeanMapper.map(commImge, CommImgeVo.class);
                commImgeVoList.add(commImgeVo);
            });
            commodityOutput.setImgeList(commImgeVoList);
        }
        return Result.success("查询成功", commodityOutput);
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
     * @param commSimpleSearchInput 简单查询请求
     * @return
     */
    @Override
    public Result simpleSearchCommodities(CommSimpleSearchInput commSimpleSearchInput) {
        //入参校验
        Date beginCreateAt = commSimpleSearchInput.getBeginCreateAt();
        Date endCreateAt = commSimpleSearchInput.getEndCreateAt();
        String createAtMessage = DataCompare.createAtCheck(beginCreateAt, endCreateAt);
        if(!"".equals(createAtMessage)){
            return Result.fail(createAtMessage);
        }
        //开始分页
        PageTool.startPage(commSimpleSearchInput.getPageNum(), commSimpleSearchInput.getPageSize());
        List<SuppCommSearchVo> respList = supplierCommodityDao.findSimple(commSimpleSearchInput.getSupplierId(), commSimpleSearchInput.getInputvalue(), beginCreateAt, endCreateAt);
        respList.forEach(suppCommSearchVo->{
            int statusNum = Integer.parseInt(suppCommSearchVo.getStatus());
            suppCommSearchVo.setStatusNum(statusNum);
            suppCommSearchVo.setStatus(CommConstant.getStatus(statusNum));
            //转换金额为千分位
            suppCommSearchVo.setUnitPrice(NumberUtil.number2ThousandFormat(suppCommSearchVo.getUnitPrice()));
            suppCommSearchVo.setPrice(NumberUtil.number2ThousandFormat(suppCommSearchVo.getPrice()));
        });
        PageInfo<SuppCommSearchVo> pageInfo = new PageInfo<SuppCommSearchVo>(respList);
        return Result.success("查询完成", pageInfo);
    }

    /**
     * 高级搜索
     * @param commSearchInput 高级搜索查询请求
     * @return
     */
    @Override
    public Result searchCommodities(CommSearchInput commSearchInput) {
        //入参校验
        String priceMessage = DataCompare.priceCheck(commSearchInput.getMinPrice(), commSearchInput.getMaxPrice());
        if(!"".equals(priceMessage)){
            return Result.fail(priceMessage);
        }
        String createAtMessage = DataCompare.createAtCheck(commSearchInput.getBeginCreateAt(),commSearchInput.getEndCreateAt());
        if(!"".equals(createAtMessage)){
            return Result.fail(createAtMessage);
        }
        //开始分页
        PageTool.startPage(commSearchInput.getPageNum(), commSearchInput.getPageSize());
        List<SuppCommSearchVo> respList = supplierCommodityDao.find(commSearchInput);
        respList.forEach(suppCommSearchVo->{
            int statusNum = DataCompare.formatInteger(suppCommSearchVo.getStatus());
            suppCommSearchVo.setStatusNum(statusNum);
            suppCommSearchVo.setStatus(CommConstant.getStatus(statusNum));
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
            //商品需下架才可删除
            if(supplierCommodity.getStatus() != CommConstant.COMM_ST_XJ){
                return Result.fail("商品需下架才可删除", map);
            }
            //删除商品,deleted更新为1
            supplierCommodityDao.deleteById(id, true, new Date());
        }
        return Result.success("删除商品成功", map);
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
        //不可删除商品ID集合
        TreeSet<Long> idNotList = new TreeSet<>();
        //不可删除商品商品编码集合
        for(long id : ids){
            //根据商品与供应商关系ID获取供应商商品对象
            SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(id);
            if(null != supplierCommodity){
                map.put("supplierId", supplierCommodity.getSupplierId());
                //商品需下架才可删除
                if(supplierCommodity.getStatus() != CommConstant.COMM_ST_XJ){
                    idNotList.add(id);
                }else{
                    idList.add(id);
                }
            }
        }
        if(idList.size() > 0){
            List<SupplierCommodity> supplierCommodityList = new ArrayList<>();
            for(long id : idList){
                SupplierCommodity supplierCommodity = new SupplierCommodity();
                supplierCommodity.setId(id);
                supplierCommodity.setUpdatedAt(new Date());
                supplierCommodity.setDeleted(1);
                supplierCommodityList.add(supplierCommodity);
            }
            supplierCommodityDao.deleteByIds(supplierCommodityList);
        }
        if(idNotList.size() > 0){
            return Result.fail("商品需下架才可删除", map);
        }
        return Result.success("删除商品成功", map);
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
        supplierCommodity = assemblyObject(id,CommConstant.COMM_ST_SJ);
        boolean flag = supplierCommodityDao.updateStatusSXj(supplierCommodity);
        if (flag)
        {
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage("上架商品成功");
            map.put("status",CommConstant.COMM_ST_SJ);
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
        supplierCommodity = assemblyObject(id,CommConstant.COMM_ST_XJ);
        boolean flag = supplierCommodityDao.updateStatusSXj(supplierCommodity);
        if (flag)
        {
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage("下架商品成功");
            map.put("status",CommConstant.COMM_ST_XJ);
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
                SupplierCommodity supplierCommodity = assemblyObject(ids[i],CommConstant.COMM_ST_SJ);
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
                SupplierCommodity supplierCommodity = assemblyObject(ids[i],CommConstant.COMM_ST_XJ);
                supplierCommodityDao.updateStatusSXj(supplierCommodity);
                flag = true;
            }
        }
        return BaseResultUtil.transTo(flag,"批量下架商品成功","批量下架商品失败");
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
        //接下来开始解压缩文件
        String newFileName = DateUtil.getStringDateTime();
        Map excelmap = new HashMap();
        if(!"".equals(filename.trim())){
            newFileName= deCompressFile(tempPath, filename, newFileName, excelmap);//解压缩文件
        }
        String excelpath = tempPath + newFileName +"/"+excelmap.get("excel").toString();
        //使用工具类 获取Excel 内容
        Map<String, Map> maps = ExcelReader.readExcelContent(excelpath,1);
        //Excel中正确记录信息
        Map<Integer,Map<String, String>> mapRight = ( Map<Integer,Map<String, String>>)maps.get("mapright");
        //Excel中错误行号
        Map<Integer,Map<String, String>> mapError =( Map<Integer,Map<String, String>>)maps.get("maperror");
        Map<String, String> map = null;
        List<CommodityInput> commodityInputs = new ArrayList<CommodityInput>();
        List<CommRuleVo> ruleList = new ArrayList<CommRuleVo>();
        // Excel 内容转 CommodityInput对象
        if(mapRight != null){
            for (Map.Entry<Integer,Map<String, String>> itmap : mapRight.entrySet()) {
                map = itmap.getValue();
                CommodityInput commodityInput=new CommodityInput();
                commodityInput.setRowNum(itmap.getKey());
                List<SupplierCommodityVo> commodityList = new ArrayList<SupplierCommodityVo>();
                SupplierCommodityVo supplierCommodityVo = new SupplierCommodityVo();
                CommRuleVo commRuleVo = new CommRuleVo();
                long pid = 0;
                for (Map.Entry<String, String> cellMap: map.entrySet()) {
                    String key = cellMap.getKey() == null ? "" : cellMap.getKey();
                    String value = cellMap.getValue() == null ? "" : cellMap.getValue();
                    //封装数据
                    pid= checkCellData(key, value, commodityInput, supplierCommodityVo, pid, tempPath + newFileName, supplierId);
                }

                commodityList.add(supplierCommodityVo);
                ruleList.add(commRuleVo);
                commodityInput.setCommodityList(commodityList);
                commodityInputs.add(commodityInput);
            }
        }
        //导入数据库(校验有效数据记录)
        List<Integer> errorList = new ArrayList<>();
        for (CommodityInput commodityInput : commodityInputs) {
            for (SupplierCommodityVo supplierCommodityVo : commodityInput.getCommodityList()) {
                String code69 = supplierCommodityVo.getCode69() == null ? "" : supplierCommodityVo.getCode69();
                List<CommImgeVo> imgeList = supplierCommodityVo.getImgeList();
                int rowNum = commodityInput.getRowNum();
                if (null == imgeList || imgeList.isEmpty()){
                    errorList.add(rowNum);
                }else if ( !"".equals(code69) ) {
                    if(!Tools.isNumeric(code69)){
                        errorList.add(rowNum);
                    }else {
                        //通过code69,supplierId,deleted=0判断商品是否存在
                        SupplierCommodity suppliercommodity = supplierCommodityDao.findSupplierCommodityInfo(code69,supplierId);
                        if (null != suppliercommodity) {
                            errorList.add(rowNum);
                        }
                    }
                }
            }
        }
        //入庫
        for (CommodityInput commodityInput : commodityInputs) {
            int rowNum = commodityInput.getRowNum();
            if(errorList.contains(rowNum)){//过滤错误数据记录
                continue;
            }
            Result baseResult = saveCommodity(commodityInput, supplierId);
            String code69 = commodityInput.getCommodityList().get(0).getCode69();
            if(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS != baseResult.getCode()){
                errorList.add(rowNum);
            }else {
                CommodityImportOutput commodityImportOutput = new CommodityImportOutput();
                commodityImportOutput.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
                commodityImportOutput.setMessage("商品编码:" + code69+"成功导入！");
                commodityImportOutput.setCode69(code69);
                commodityImportOutput.setBrand(commodityInput.getBrandName());
                commodityImportOutput.setSjcode(commodityInput.getCommodityList().get(0).getCode());
                commodityImportOutput.setRowNum(rowNum);
                commodityImportOutput.setTagName(commodityInput.getTagName());
                commodityImportOutput.setUnit(commodityInput.getCommodityList().get(0).getUnitName());
                commodityImportOutput.setMeasureSpecName(commodityInput.getCommodityList().get(0).getMeasureSpecName());
                commodityImportOutput.setCompanyName(commodityInput.getCompanyName());
                commodityImportOutput.setOriginPlace(commodityInput.getOriginPlace());
                commodityImportOutput.setMarketTime(commodityInput.getMarketTime());
                commodityImportOutput.setName(commodityInput.getName());
                commodityImportOutput.setRuleVal(commodityInput.getCommodityList().get(0).getRuleVal());
                commodityImportOutput.setInventory(commodityInput.getCommodityList().get(0).getInventory());
                commodityImportOutputList.add(commodityImportOutput);
            }
        }
        if(null != mapError) {
            for (Map.Entry<Integer,Map<String, String>> itmap : mapError.entrySet()) {
                int rownum = itmap.getKey();
                errorList.add(rownum);
            }
        }
        Collections.sort(errorList);
        FileUtil.deleteDirectory(tempPath + newFileName);
        outmap.put("rightlist",commodityImportOutputList);
        outmap.put("errolist",errorList);
        return Result.success("成功导入！", outmap);
    }

    /**
     * 校验单元格数据
     * @param key
     * @param value
     * @param commodityInput
     * @param supplierCommodityVo
     * @param pid
     * @param filePath
     * @param supplierId
     */
    private Long checkCellData(String key, String value, CommodityInput commodityInput, SupplierCommodityVo supplierCommodityVo,
                               long pid, String filePath, long supplierId){
        switch (key) {
            case "商品条码":
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
                    List<String> imgList = Arrays.asList(imgs);
                    // 上传图片
                    List<Result> results = azureBlobService.uploadFilesComm(filePath, imgList);
                    for (Result result : results) {
                        if (result.getCode() == so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS) {
                            List<CommBlobUpload> blobUploadEntities = (List<CommBlobUpload>) result.getData();
                            List<CommImgeVo> commImgeVoList = new ArrayList<CommImgeVo>();
                            for (int t = 0; t < blobUploadEntities.size(); t++) {
                                CommBlobUpload blobUpload = blobUploadEntities.get(t);
                                if (t == 0) {
                                    supplierCommodityVo.setMinImg(blobUpload.getMinImgUrl());
                                }
                                CommImgeVo commImgeVo = BeanMapper.map(blobUpload, CommImgeVo.class);
                                commImgeVo.setName(blobUpload.getFileName());
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
                        pid = commCategoryone.getId();
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
            case "计量规格":
                if (!"".equals(value)) {
                    supplierCommodityVo.setMeasureSpecName(value);
                    List<CommMeasureSpec> commMeasureSpeclist = commMeasureSpecDao.findByName(supplierId,value);
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
                }else {
                    supplierCommodityVo.setInventory(0.0);
                }
                break;
            case "包装单位":
                if (!"".equals(value)) {
                    supplierCommodityVo.setUnitName(value);
                    List<CommUnit> commUnitList = commUnitDao.findNameAndSupplierId(supplierId,value);
                    if(null!=commUnitList&&commUnitList.size()>0){
                        supplierCommodityVo.setUnitId(commUnitList.get(0).getId());
                    }
                }

                break;
            case "商品标签":
                if (!"".equals(value)) {
                    List<CommTag> commTagList = commTagDao.findByNameAndSupplierId(value,supplierId);
                    if(null!=commTagList&&commTagList.size()>0){
                        commodityInput.setTagId(commTagList.get(0).getId());
                        commodityInput.setTagName(commTagList.get(0).getName());
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
                commodityInput.setMarketTime(DateUtil.stringToDate(value));
                break;
        }
        return  pid;
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


}
