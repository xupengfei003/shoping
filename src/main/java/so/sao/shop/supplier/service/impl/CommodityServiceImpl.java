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
import so.sao.shop.supplier.pojo.output.CommodityOutput;
import so.sao.shop.supplier.pojo.vo.*;
import so.sao.shop.supplier.service.CommodityService;
import so.sao.shop.supplier.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
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
    private CommBrandDao commBrandDao;
    @Autowired
    private SupplierCommodityDao supplierCommodityDao;
    @Autowired
    private AccountDao accountDao;

    @Override
    @Transactional
    public BaseResult saveCommodity(HttpServletRequest request,@Valid CommodityInput commodityInput,Long supplierId) throws Exception {
        BaseResult result=new BaseResult();
        //校验供应商ID
        supplierId = CheckUtil.supplierIdCheck(request,supplierId);
        //验证品牌是否存在，不存在则新增
        CommBrand brand = commBrandDao.findByName(commodityInput.getBrand());
        if (null==brand){
            brand=new CommBrand();
            brand.setName(commodityInput.getBrand());
            commBrandDao.save(brand);
        }
        //验证商品是否存在,不存在则新增商品
        Commodity commodity = commodityDao.findByName(commodityInput.getName());
        if(null==commodity) {
            commodity = new Commodity();
            commodity.setName(commodityInput.getName());
            commodity.setBrandId(brand.getId());
            commodityDao.save(commodity);
        }
        //校验所有插入规格的69码是否已存在
         for (SupplierCommodityVo commodityVo:commodityInput.getCommodityList()) {
             if (StringUtil.isNull(commodityVo.getCode69()))
             {
                 result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                 result.setMessage("商品编码不能为空");
                 return result;
             }
            SupplierCommodity supplierCommodity = supplierCommodityDao.findSupplierCommodityInfo(commodityVo.getCode69(),supplierId);
            if (supplierCommodity != null) {
                result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                result.setMessage("商品编码已存在：" + commodityVo.getCode69());
                return result;
            }
        }
        //建立供应商和商品关系
        if(null!=commodityInput.getCommodityList()){
            for (SupplierCommodityVo commodityVo:commodityInput.getCommodityList()) {
                SupplierCommodity sc=new SupplierCommodity();
                sc.setBrandId(brand.getId());
                sc.setBrand(brand.getName());
                sc.setCommodityId(commodity.getId());
                sc.setSupplierId(supplierId);
                sc.setDescription(commodityInput.getDescription());
                sc.setRemark(commodityInput.getRemark());
                sc.setCategoryOneId(commodityInput.getCategoryOneId());
                sc.setCategoryTwoId(commodityInput.getCategoryTwoId());
                sc.setCategoryThreeId(commodityInput.getCategoryThreeId());
                sc.setName(commodityInput.getName());
                sc.setCode(commodityVo.getCode());
                sc.setCode69(commodityVo.getCode69());
                sc.setInventory(commodityVo.getInventory());
                sc.setMinImg(commodityVo.getMinImg());
                sc.setRuleName(commodityVo.getRuleName());
                sc.setRuleVal(commodityVo.getRuleVal());
                sc.setUnit(commodityVo.getUnit());
                sc.setPrice(commodityVo.getPrice());
                sc.setUnitPrice(commodityVo.getUnitPrice());
                sc.setStatus(Constant.COMM_ST_XZ);
                sc.setCreatedBy(supplierId);
                sc.setUpdatedBy(supplierId);
                sc.setCreatedAt(new Date());
                sc.setUpdatedAt(new Date());
                supplierCommodityDao.save(sc);
                //保存图片
                if(null!=commodityVo.getImgeList()){
                    for (CommImgeVo imgeVo:commodityVo.getImgeList()) {
                        CommImge imge=new CommImge();
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

    @Override
    @Transactional
    public BaseResult updateCommodity(HttpServletRequest request, CommodityInput commodityInput,Long supplierId) throws Exception {
        BaseResult result=new BaseResult();
        //校验供应商ID
        supplierId = CheckUtil.supplierIdCheck(request,supplierId);
        //验证品牌是否存在，不存在则新增
        CommBrand brand = commBrandDao.findByName(commodityInput.getBrand());
        if (null == brand) {
            brand = new CommBrand();
            brand.setName(commodityInput.getBrand());
            commBrandDao.save(brand);
        }
        //验证商品是否存在,不存在则新增商品
        Commodity commodity = commodityDao.findByName(commodityInput.getName());
        if (null == commodity) {
            commodity = new Commodity();
            commodity.setBrandId(brand.getId());
            commodity.setName(commodityInput.getName());
            commodity.setBrandId(brand.getId());
            commodityDao.save(commodity);
        }
        //建立供应商和商品关系
        if (null != commodityInput.getCommodityList()) {
            for (SupplierCommodityVo commodityVo : commodityInput.getCommodityList()) {
                //校验code69是否为空
                if (StringUtil.isNull(commodityVo.getCode69()))
                {
                    result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                    result.setMessage("商品编码不能为空");
                    return result;
                }

                SupplierCommodity sc = new SupplierCommodity();
                sc.setCommodityId(commodity.getId());
                sc.setBrandId(brand.getId());
                sc.setBrand(brand.getName());
                sc.setSupplierId(supplierId);
                sc.setDescription(commodityInput.getDescription());
                sc.setRemark(commodityInput.getRemark());
                sc.setCategoryOneId(commodityInput.getCategoryOneId());
                sc.setCategoryTwoId(commodityInput.getCategoryTwoId());
                sc.setCategoryThreeId(commodityInput.getCategoryThreeId());
                sc.setName(commodityInput.getName());
                sc.setCode(commodityVo.getCode());
                sc.setCode69(commodityVo.getCode69());
                sc.setInventory(commodityVo.getInventory());
                sc.setMinImg(commodityVo.getMinImg());
                sc.setRuleName(commodityVo.getRuleName());
                sc.setRuleVal(commodityVo.getRuleVal());
                sc.setUnit(commodityVo.getUnit());
                sc.setPrice(commodityVo.getPrice());
                sc.setUnitPrice(commodityVo.getUnitPrice());
                sc.setStatus(Constant.COMM_ST_XZ);
                sc.setUpdatedAt(new Date());
                sc.setUpdatedBy(supplierId);
                SupplierCommodity supplierCommodity = supplierCommodityDao.findSupplierCommodityInfo(commodityVo.getCode69(),supplierId);
                if (commodityVo.getId() != null) {
                    SupplierCommodity supplierComm = supplierCommodityDao.findOne(commodityVo.getId());
                    if(supplierComm != null && !supplierComm.getCode69().equals(commodityVo.getCode69())){
                        result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                        result.setMessage("商品编码已存在");
                        return result;
                    }

                    sc.setId(commodityVo.getId());
                    supplierCommodityDao.update(sc);
                } else {
                    if(supplierCommodity != null){
                        result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE);
                        result.setMessage("商品编码已存在");
                        return result;
                    }
                    supplierCommodityDao.save(sc);
                }

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
    public CommodityOutput getCommodity(Long id) {
        //根据供应商商品ID获取商品信息
        SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(id);
        if(null == supplierCommodity){
            return null;
        }
        CommodityOutput commodityOutput = new CommodityOutput();
        commodityOutput.setId(supplierCommodity.getId());
        commodityOutput.setBrand(supplierCommodity.getBrand());
        commodityOutput.setName(supplierCommodity.getName());
        commodityOutput.setRemark(supplierCommodity.getRemark());
        commodityOutput.setDescription(supplierCommodity.getDescription());
        commodityOutput.setCode(supplierCommodity.getCode());
        commodityOutput.setCode69(supplierCommodity.getCode69());
        commodityOutput.setRuleName(supplierCommodity.getRuleName());
        commodityOutput.setRuleVal(supplierCommodity.getRuleVal());
        commodityOutput.setUnit(supplierCommodity.getUnit());
        commodityOutput.setInventory(supplierCommodity.getInventory());
        commodityOutput.setCreatedAt(supplierCommodity.getCreatedAt());
        commodityOutput.setUpdatedAt(supplierCommodity.getUpdatedAt());
        commodityOutput.setStatus(supplierCommodity.getStatus());
        commodityOutput.setCategoryOneId(supplierCommodity.getCategoryOneId());
        commodityOutput.setCategoryTwoId(supplierCommodity.getCategoryTwoId());
        commodityOutput.setCategoryThreeId(supplierCommodity.getCategoryThreeId());
        String categoryOneName = commCategoryDao.findNameById(supplierCommodity.getCategoryOneId());
        String categoryTwoName = commCategoryDao.findNameById(supplierCommodity.getCategoryTwoId());
        String categoryThreeName = commCategoryDao.findNameById(supplierCommodity.getCategoryThreeId());
        if(!StringUtil.isNull(categoryOneName)){
            commodityOutput.setCategoryOneName(categoryOneName);
        }
        if(!StringUtil.isNull(categoryTwoName)){
            commodityOutput.setCategoryTwoName(categoryTwoName);
        }
        if(!StringUtil.isNull(categoryThreeName)){
            commodityOutput.setCategoryThreeName(categoryThreeName);
        }
       commodityOutput.setMinImg(supplierCommodity.getMinImg());
        commodityOutput.setPrice(supplierCommodity.getPrice());
        commodityOutput.setUnitPrice(supplierCommodity.getUnitPrice());
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
        return commodityOutput;
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
     *
     * @param supplierId 供应商ID
     * @param commCode69 商品编码
     * @param commId 商品ID
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
    public PageInfo searchCommodities(Long supplierId, String commCode69, Long commId, String suppCommCode, String commName, Integer status, Long typeId,
                                      Double minPrice, Double maxPrice, Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum, pageSize);
        //入参校验
        commCode69 = stringParamCheck(commCode69);
        suppCommCode = stringParamCheck(suppCommCode);
        commName =stringParamCheck(commName);
        priceCheck(minPrice, maxPrice);
        //分页参数校验
        page = PageUtil.pageCheck(page);
        //开始分页
        PageHelper.startPage(page.getPageNum(),page.getRows());
        List<SupplierCommodity> suppCommList = supplierCommodityDao.find(supplierId, commCode69, commId, suppCommCode, commName, status, typeId, minPrice, maxPrice);
        Long countTotal = supplierCommodityDao.countTotal(supplierId, commCode69, commId, suppCommCode, commName, status, typeId, minPrice, maxPrice);
        List<SuppCommSearchVo> respList = new ArrayList<SuppCommSearchVo>();
        //重新组装VO
        for (SupplierCommodity supplierCommodity : suppCommList)
        {
            SuppCommSearchVo suppCommSearchVo = new SuppCommSearchVo();
            suppCommSearchVo.setId(supplierCommodity.getId());
            suppCommSearchVo.setMinImg(supplierCommodity.getMinImg());
            suppCommSearchVo.setCommId(supplierCommodity.getCommodityId());
            suppCommSearchVo.setCode69(supplierCommodity.getCode69());
            suppCommSearchVo.setCode(supplierCommodity.getCode());
            suppCommSearchVo.setBrandName(supplierCommodity.getBrand());
            suppCommSearchVo.setCommName(supplierCommodity.getName());
            suppCommSearchVo.setUnit(supplierCommodity.getUnit());
            suppCommSearchVo.setRuleName(supplierCommodity.getRuleName());
            suppCommSearchVo.setRuleVal(supplierCommodity.getRuleVal());
            suppCommSearchVo.setInventory(supplierCommodity.getInventory());
            suppCommSearchVo.setStatus(Constant.getStatus(supplierCommodity.getStatus()));
            suppCommSearchVo.setStatusNum(supplierCommodity.getStatus());
            suppCommSearchVo.setPrice(supplierCommodity.getPrice());
            suppCommSearchVo.setUnitPrice(supplierCommodity.getUnitPrice());
            suppCommSearchVo.setCreatedAt(supplierCommodity.getCreatedAt());
            suppCommSearchVo.setUpdatedAt(supplierCommodity.getUpdatedAt());
            respList.add(suppCommSearchVo);
        }

        PageInfo<SuppCommSearchVo> pageInfo = new PageInfo<SuppCommSearchVo>(respList);
        pageInfo.setTotal(countTotal);
        return pageInfo;
    }

    /**
     * 价格校验
     * @param minPrice
     * @param maxPrice
     */
    private void priceCheck(Double minPrice, Double maxPrice)
    {
        if (null != minPrice && null !=maxPrice)
        {
            if (minPrice < 0 || maxPrice < 0 || minPrice > maxPrice)
            {
                throw new RuntimeException("param is unavailable");
            }
        }
    }

    /**
     * 删除商品
     * @param id 供应商商品ID
     * @return
     */
    @Override
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
    public Result deleteCommodities(Long[] ids) {
        Result result = new Result();
        Map<String,Long> map = new HashMap<>();
        //可删除商品ID集合
        TreeSet<Long> idList = new TreeSet<>();
        //不可删除商品ID集合
        TreeSet<Long> idNotList = new TreeSet<>();
        //不可删除商品商品编码集合
        TreeSet<String> codeNotList = new TreeSet<>();
        for(long id : ids){
            //根据商品与供应商关系ID获取供应商商品对象
            SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(id);
            if(null != supplierCommodity){
                map.put("supplierId",supplierCommodity.getSupplierId());
                //根据商品与供应商关系ID查询商品状态
                int status = supplierCommodityDao.findStatus(id);
                String code69 = supplierCommodity.getCode69();
                //商品需下架才可删除
                if(status != Constant.COMM_ST_XJ){
                    idNotList.add(id);
                    codeNotList.add(code69);
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
            result.setMessage("商品需下架才可删除,商品编码:"+codeNotList);
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
    public  Map<String ,List> importExcel(MultipartFile multipartFile, HttpServletRequest request, StorageConfig storageConfig,Long supplierId) throws Exception {
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
            return outmap;
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
                }else if(type.indexOf("jpg")>=0||type.indexOf("JPG")>=0){
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
                for (Iterator< Map.Entry<String, String>> it = map.entrySet().iterator(); it.hasNext(); ) {
                    entry = it.next();
                    String key=entry.getKey()==null?"":entry.getKey();
                    String value=entry.getValue()==null?"":entry.getValue();
                    if("商品编码".equals(key)){
                        supplierCommodityVo.setCode69(value);
                    }else  if("商品品牌".equals(key)){
                        commodityInput.setBrand(value);
                    }else  if("图片".equals(key)){
                        if(!"".equals(value)) {
                            String[] imgs = new String[15];
                            if(value.contains(",")||value.contains("，")){
                                if(value.contains(",")){
                                    imgs=value.split(",");
                                }else if(value.contains("，")){
                                    imgs=value.split("，");
                                }
                            }else {
                                imgs[0] = value;
                            }
                            // 上传图片
                            List<Result> results = fileutil.UploadFiles(filezspath + ffname, imgs, storageConfig);
                            for (Result result : results)
                            {
                                if (result.getCode()==so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS)
                                {
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
                    }else if("商品名称".equals(key)){
                        commodityInput.setName(value);
                    }else  if("商家编码".equals(key)){
                        supplierCommodityVo.setCode(value);
                    }else  if("商品分类一级".equals(key)){
                        if(!"".equals(value)){
                            CommCategory commCategoryone=    commCategoryDao.findCommCategoryByName(value);
                            if(null!=commCategoryone){
                                commodityInput.setCategoryOneId(commCategoryone.getId());
                            }
                        }
                    }else if("商品分类二级".equals(key)){
                        if(!"".equals(value)) {
                            CommCategory commCategorytwo = commCategoryDao.findCommCategoryByName(value);
                            if(null!=commCategorytwo) {
                                commodityInput.setCategoryTwoId(commCategorytwo.getId());
                            }
                        }
                    }else if("商品分类三级".equals(key)){
                        if(!"".equals(value)) {
                            CommCategory commCategorythree = commCategoryDao.findCommCategoryByName(value);
                            if(null!=commCategorythree) {
                                commodityInput.setCategoryThreeId(commCategorythree.getId());
                            }
                        }
                    }else if("商品描述".equals(key)){
                        commodityInput.setRemark(value);
                    }else if("商品介绍".equals(key)){
                        commodityInput.setDescription(value);
                    }else if("总库存数量".equals(key)){
                        // commodityInput.setRemark(value);
                    }else if("商品规格".equals(key)){
                        supplierCommodityVo.setRuleName(value);
                    }else if("商品规格值".equals(key)){
                        supplierCommodityVo.setRuleVal(value);
                    }else if("成本价".equals(key)){
                        if(!"".equals(value)) {
                            supplierCommodityVo.setUnitPrice(Double.parseDouble(value));
                        }
                    }else if("市场价".equals(key)){
                        if(!"".equals(value)) {
                            supplierCommodityVo.setPrice(Double.parseDouble(value));
                        }
                    }else if("库存".equals(key)){
                        if(!"".equals(value)) {
                            supplierCommodityVo.setInventory(Double.parseDouble(value));
                            //  commRuleVo.setInventory(Double.parseDouble(value));
                        }
                    }else if("计量单位".equals(key)){
                        supplierCommodityVo.setUnit(value);
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
                        //校验供应商ID
                        supplierId = CheckUtil.supplierIdCheck(request,supplierId);
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
            BaseResult baseResult1=  saveCommodity(request,commodityInputs.get(n),supplierId);
            String code69 = commodityInputs.get(n).getCommodityList().get(0).getCode69() == null ? "" : commodityInputs.get(n).getCommodityList().get(0).getCode69();
            String sjcode = commodityInputs.get(n).getCommodityList().get(0).getCode() == null ? "" : commodityInputs.get(n).getCommodityList().get(0).getCode();
            String brand = commodityInputs.get(n).getBrand() == null ? "" : commodityInputs.get(n).getBrand();
            String name = commodityInputs.get(n).getName()== null ? "" : commodityInputs.get(n).getName();
            String rulename = commodityInputs.get(n).getCommodityList().get(0).getRuleName() == null ? "" : commodityInputs.get(n).getCommodityList().get(0).getRuleName();
            String ruleval = commodityInputs.get(n).getCommodityList().get(0).getRuleVal()== null ? "" : commodityInputs.get(n).getCommodityList().get(0).getRuleVal();
            String unit = commodityInputs.get(n).getCommodityList().get(0).getUnit() == null ? "" : commodityInputs.get(n).getCommodityList().get(0).getUnit();
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
                commodityImportOutput.setName(name);
                commodityImportOutput.setRuleName(rulename);
                commodityImportOutput.setRuleVal(ruleval);
                commodityImportOutput.setUnit(unit);
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
        return outmap;
    }

    /**
     * 根据查询条件查询商品详情
     * @param id           scID
     * @param commName     商品名称
     * @param code69       商品编码
     * @param suppCommCode 商家商品编码
     * @param typeId       类型ID
     * @param minPrice     价格（低）
     * @param maxPrice     价格（高）
     * @param pageNum      当前页号
     * @param pageSize     页面大小
     * @return PageInfo pageInfo对象
     */
    @Override
    public PageInfo searchAllCommodities(Long id, String commName, String code69, String suppCommCode, Long typeId, Double minPrice, Double maxPrice, Integer pageNum, Integer pageSize) {
        Page page = new Page(pageNum, pageSize);
        //入参校验
        commName = stringParamCheck(commName);
        code69 = stringParamCheck(code69);
        suppCommCode =stringParamCheck(suppCommCode);
        priceCheck(minPrice, maxPrice);
        //分页参数校验
        page = PageUtil.pageCheck(page);
        //开始分页
        PageHelper.startPage(page.getPageNum(),page.getRows());
        List<SupplierCommodity> suppCommList = supplierCommodityDao.findAll(id, commName, code69, suppCommCode, typeId, minPrice, maxPrice);
        Long countTotal = supplierCommodityDao.countAllTotal(id, commName, code69, suppCommCode, typeId, minPrice, maxPrice);
        List<SuppCommSearchVo> respList = new ArrayList<SuppCommSearchVo>();
        //重新组装VO
        for (SupplierCommodity supplierCommodity : suppCommList)
        {
            SuppCommSearchVo suppCommSearchVo = new SuppCommSearchVo();
            suppCommSearchVo.setId(supplierCommodity.getId());
            suppCommSearchVo.setMinImg(supplierCommodity.getMinImg());
            suppCommSearchVo.setCommId(supplierCommodity.getCommodityId());
            suppCommSearchVo.setCode69(supplierCommodity.getCode69());
            suppCommSearchVo.setCode(supplierCommodity.getCode());
            suppCommSearchVo.setBrandName(supplierCommodity.getName());
            suppCommSearchVo.setCommName(supplierCommodity.getName());
            suppCommSearchVo.setUnit(supplierCommodity.getUnit());
            suppCommSearchVo.setRuleName(supplierCommodity.getRuleName());
            suppCommSearchVo.setRuleVal(supplierCommodity.getRuleVal());
            suppCommSearchVo.setInventory(supplierCommodity.getInventory());
            suppCommSearchVo.setStatusNum(supplierCommodity.getStatus());
            suppCommSearchVo.setStatus(Constant.getStatus(supplierCommodity.getStatus()));
            suppCommSearchVo.setPrice(supplierCommodity.getPrice());
            suppCommSearchVo.setUnitPrice(supplierCommodity.getUnitPrice());
            suppCommSearchVo.setCreatedAt(supplierCommodity.getCreatedAt());
            suppCommSearchVo.setUpdatedAt(supplierCommodity.getUpdatedAt());
            respList.add(suppCommSearchVo);
        }
        PageInfo<SuppCommSearchVo> pageInfo = new PageInfo<SuppCommSearchVo>(respList);
        pageInfo.setTotal(countTotal);
        return pageInfo;
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
