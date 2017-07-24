package com.sao.so.shop.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.sao.so.shop.dao.*;
import com.sao.so.shop.domain.*;
import com.sao.so.shop.pojo.BaseResult;
import com.sao.so.shop.pojo.input.CommodityInput;
import com.sao.so.shop.pojo.output.CommodityExportOutput;
import com.sao.so.shop.pojo.output.CommodityOutput;
import com.sao.so.shop.pojo.vo.*;
import com.sao.so.shop.service.CommodityService;
import com.sao.so.shop.util.BaseResultUtil;
import com.sao.so.shop.util.Constant;
import com.sao.so.shop.util.ExcelReader;
import com.sao.so.shop.util.PageUtil;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    @Value("${IMAGE_BASE_URL}")
    private String IMAGE_BASE_URL;

    @Override
    public BaseResult saveCommodity(HttpServletRequest request,CommodityInput commodityInput) {
        BaseResult result=new BaseResult();
        //获取供应商ID
        Long supplierId = ((User) request.getAttribute(Constant.REQUEST_USER)).getId();
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
        //建立供应商和商品关系
        if(null!=commodityInput.getCommodityList()){
            for (SupplierCommodityVo commodityVo:commodityInput.getCommodityList()) {
                SupplierCommodity supplierCommodity = supplierCommodityDao.findSupplierCommodityInfo(commodityVo.getCode69());
                if(supplierCommodity != null){
                    result.setCode(com.sao.so.shop.config.Constant.CodeConfig.CODE_FAILURE);
                    result.setMessage("商品编码已存在");
                    return result;
                }
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
                sc.setCreatedAt(new Date().getTime());
                sc.setUpdatedAt(new Date().getTime());
                supplierCommodityDao.save(sc);
                //保存图片
                if(null!=commodityInput.getImgeList()){
                    for (CommImgeVo imgeVo:commodityInput.getImgeList()) {
                        CommImge imge=new CommImge();
                        imge.setScId(sc.getId());
                        imge.setName(imgeVo.getName());
                        imge.setSize(imgeVo.getSize());
                        imge.setType(imgeVo.getType());
                        imge.setUrl(imgeVo.getUrl());
                        commImgeDao.save(imge);
                    }
                }
            }
        }
        result.setCode(com.sao.so.shop.config.Constant.CodeConfig.CODE_SUCCESS);
        result.setMessage("添加商品成功");
        return result;
    }

    @Override
    public BaseResult updateCommodity(HttpServletRequest request, CommodityInput commodityInput) {
        BaseResult result=new BaseResult();
        //获取供应商ID
        Long supplierId = ((User) request.getAttribute(Constant.REQUEST_USER)).getId();
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
                sc.setUpdatedAt(new Date().getTime());
                sc.setUpdatedBy(supplierId);
                SupplierCommodity supplierCommodity = supplierCommodityDao.findSupplierCommodityInfo(commodityVo.getCode69());
                if (commodityVo.getId() != null) {
                    SupplierCommodity supplierComm = supplierCommodityDao.findOne(commodityVo.getId());
                    if(supplierComm != null && !supplierComm.getCode69().equals(commodityVo.getCode69())){
                        result.setCode(com.sao.so.shop.config.Constant.CodeConfig.CODE_FAILURE);
                        result.setMessage("商品编码已存在");
                        return result;
                    }
                    sc.setId(commodityVo.getId());
                    supplierCommodityDao.update(sc);
                } else {
                    if(supplierCommodity != null){
                        result.setCode(com.sao.so.shop.config.Constant.CodeConfig.CODE_FAILURE);
                        result.setMessage("商品编码已存在");
                        return result;
                    }
                    supplierCommodityDao.save(sc);
                }
                //保存图片
                if (null != commodityInput.getImgeList()) {
                    for (CommImgeVo imgeVo : commodityInput.getImgeList()) {
                        CommImge imge = new CommImge();
                        imge.setScId(sc.getId());
                        imge.setName(imgeVo.getName());
                        imge.setSize(imgeVo.getSize());
                        imge.setType(imgeVo.getType());
                        imge.setUrl(imgeVo.getUrl());
                        if (imgeVo.getId() != null) {
                            imge.setId(imgeVo.getId());
                            commImgeDao.update(imge);
                        } else {
                            commImgeDao.save(imge);
                        }
                    }
                }
            }
        }
        result.setCode(com.sao.so.shop.config.Constant.CodeConfig.CODE_SUCCESS);
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
        commodityOutput.setCategoryOneId(supplierCommodity.getCategoryOneId());
        commodityOutput.setCategoryTwoId(supplierCommodity.getCategoryTwoId());
        commodityOutput.setCategoryThreeId(supplierCommodity.getCategoryThreeId());
        commodityOutput.setName(supplierCommodity.getName());
        commodityOutput.setRemark(supplierCommodity.getRemark());
        commodityOutput.setDescription(supplierCommodity.getDescription());
        commodityOutput.setCode(supplierCommodity.getCode());
        commodityOutput.setCode69(supplierCommodity.getCode69());
        commodityOutput.setRuleName(supplierCommodity.getRuleName());
        commodityOutput.setRuleVal(supplierCommodity.getRuleVal());
        commodityOutput.setUnit(supplierCommodity.getUnit());
        commodityOutput.setInventory(supplierCommodity.getInventory());
        if ( null != supplierCommodity.getMinImg() && !("").equals(supplierCommodity.getMinImg()))
        {
            commodityOutput.setMinImg(IMAGE_BASE_URL+supplierCommodity.getMinImg());
        }
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
                                      Double minPrice, Double maxPrice, int pageNum, int pageSize) {
        Page page = new Page(pageNum, pageSize);
        //入参校验
        priceCheck(minPrice, maxPrice);
        //分页参数校验
        page = PageUtil.pageCheck(page);
        //开始分页
        PageHelper.startPage(page.getPageNum(),page.getRows());
        List<SupplierCommodity> suppCommList = supplierCommodityDao.find(supplierId, commCode69, commId, suppCommCode, commName, status, typeId, minPrice, maxPrice);
        List<SuppCommSearchVo> respList = new ArrayList<SuppCommSearchVo>();
        //重新组装VO
        for (SupplierCommodity supplierCommodity : suppCommList)
        {
            SuppCommSearchVo suppCommSearchVo = new SuppCommSearchVo();
            suppCommSearchVo.setId(supplierCommodity.getId());
           if ( null != supplierCommodity.getMinImg() && !("").equals(supplierCommodity.getMinImg()))
        {
            suppCommSearchVo.setMinImg(IMAGE_BASE_URL+supplierCommodity.getMinImg());
        }
            suppCommSearchVo.setCommId(supplierCommodity.getCommodityId());
            suppCommSearchVo.setCode69(supplierCommodity.getCode69());
            suppCommSearchVo.setCode(supplierCommodity.getCode());
            suppCommSearchVo.setBrandName(supplierCommodity.getName());
            suppCommSearchVo.setCommName(supplierCommodity.getName());
            suppCommSearchVo.setUnit(supplierCommodity.getUnit());
            suppCommSearchVo.setRuleName(supplierCommodity.getRuleName());
            suppCommSearchVo.setRuleVal(supplierCommodity.getRuleVal());
            suppCommSearchVo.setInventory(supplierCommodity.getInventory());
            suppCommSearchVo.setStatus(Constant.getStatus(supplierCommodity.getStatus()));
            suppCommSearchVo.setCreatedAt(supplierCommodity.getCreatedAt());
            suppCommSearchVo.setUpdatedAt(supplierCommodity.getUpdatedAt());
            respList.add(suppCommSearchVo);
        }

        PageInfo<SuppCommSearchVo> pageInfo = new PageInfo<SuppCommSearchVo>(respList);
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
    public BaseResult deleteCommodity(Long id) {
        BaseResult result = new BaseResult();
        //根据商品与供应商关系ID获取供应商商品对象
        SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(id);
        if(null != supplierCommodity){
            //根据商品与供应商关系ID查询商品状态
            int status = supplierCommodityDao.findStatus(id);
            //商品需下架才可删除
            if(status != Constant.COMM_ST_XJ){
                result.setCode(com.sao.so.shop.config.Constant.CodeConfig.CODE_FAILURE);
                result.setMessage("商品需下架才可删除");
                return result;
            }
            //删除商品,deleted更新为1
            supplierCommodityDao.deleteById(id, true, System.currentTimeMillis());
        }
        result.setCode(com.sao.so.shop.config.Constant.CodeConfig.CODE_SUCCESS);
        result.setMessage("删除商品成功");
        return result;
    }

    /**
     * 批量删除商品
     * @param ids 供应商商品ID
     * @return
     */
    @Override
    public BaseResult deleteCommodities(Long[] ids) {
        BaseResult baseResult = new BaseResult();
        //可删除商品ID集合
        TreeSet<Long> idList = new TreeSet<>();
        //不可删除商品ID集合
        TreeSet<Long> idNotList = new TreeSet<>();
        for(long id : ids){
            //根据商品与供应商关系ID获取供应商商品对象
            SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(id);
            if(null != supplierCommodity){
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
            supplierCommodityDao.deleteById(id, true, System.currentTimeMillis());
        }
        if(null != idNotList && idNotList.size() > 0){
            baseResult.setCode(com.sao.so.shop.config.Constant.CodeConfig.CODE_FAILURE);
            baseResult.setMessage("商品需下架才可删除,id:"+idNotList);
            return baseResult;
        }
        baseResult.setCode(com.sao.so.shop.config.Constant.CodeConfig.CODE_SUCCESS);
        baseResult.setMessage("删除商品成功");
        return baseResult;
    }

    @Override
    public BaseResult updateStatusSj(Long id) {
        BaseResult result = new BaseResult();
        SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(id);
        if(null == supplierCommodity){
            result.setCode(com.sao.so.shop.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("该商品不存在");
            return result;
        }
        supplierCommodity = assemblyObject(id,Constant.COMM_ST_SJ);
        boolean flag = supplierCommodityDao.updateStatusSXj(supplierCommodity);
        return BaseResultUtil.transTo(flag,"上架商品成功","上架商品失败");
    }

    private SupplierCommodity assemblyObject(Long id,int status){
        SupplierCommodity supplierCommodity = new SupplierCommodity();
        supplierCommodity.setId(id);
        supplierCommodity.setStatus(status);
        supplierCommodity.setUpdatedAt(new Date().getTime());
        return supplierCommodity;
    }

    @Override
    public BaseResult updateStatusXj(Long id) {
        BaseResult result = new BaseResult();
        SupplierCommodity supplierCommodity = supplierCommodityDao.findOne(id);
        if(null == supplierCommodity){
            result.setCode(com.sao.so.shop.config.Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("该商品不存在");
            return result;
        }
        supplierCommodity = assemblyObject(id,Constant.COMM_ST_XJ);
        boolean flag = supplierCommodityDao.updateStatusSXj(supplierCommodity);
        return BaseResultUtil.transTo(flag,"下架商品成功","下架商品失败");
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
    public BaseResult importExcel(MultipartFile multipartFile, HttpServletRequest request) {
        BaseResult baseResult=new BaseResult();
        List<Map<String, String>> list=null;
        List<CommodityInput>  commodityInputs=new ArrayList<CommodityInput>();
        int code=0;
        String  message="";
        //判断文件是否选择文件
        if (null == multipartFile) {
            code=0;
            message= "文件为空,请选择文件";
            baseResult.setCode(code);
            baseResult.setMessage(message);
            return baseResult;
        }


        String realPath = request.getSession().getServletContext().getRealPath("/upload");
        // String path = "E:\\demo";
        //容错处理
        File dir = new File(realPath);
        if(!dir.exists()) {
            dir.mkdirs();
        }
        //Excel 文件名称
        String originalFilename = multipartFile.getOriginalFilename();//report.xls
        // String fileName2 = multipartFile.getName();//excelFile

        //文件后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."), originalFilename.length());
        // String     imgpath = username + suffix;
        String excelpath="";
        //文件copy到项目路径下
        try {
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), new File(
                    realPath, originalFilename));
            excelpath=realPath+"/"+originalFilename;

        } catch (IOException e) {
            e.printStackTrace();
        }

        //使用工具类 获取Excel 内容
        if(!"".equals(excelpath)){
            list=  ExcelReader.readExcelContent(excelpath,1);
        }
        Map<String, String> map = null;
        List<CommRuleVo> ruleList=new ArrayList<CommRuleVo>();

        // Excel 内容转 CommodityInput对象
        if(null!=list){
            for (int i = 0; i < list.size(); i++) {
                map = list.get(i);
                Map.Entry<String, String> entry = null;
                CommodityInput commodityInput=new CommodityInput();
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
                        commRuleVo.setName(value);
                    }else if("商品规格值".equals(key)){
                        commRuleVo.setValue(value);
                    }else if("成本价".equals(key)){
                        if(!"".equals(value)) {
                            commRuleVo.setUnitPrice(Double.parseDouble(value));
                        }
                    }else if("市场价".equals(key)){
                        if(!"".equals(value)) {
                            commRuleVo.setPrice(Double.parseDouble(value));
                        }
                    }else if("库存".equals(key)){
                        if(!"".equals(value)) {
                            supplierCommodityVo.setInventory(Double.parseDouble(value));
                            commRuleVo.setInventory(Double.parseDouble(value));
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
                if (!"".equals(code69)) {
                    SupplierCommodity suppliercommodity = supplierCommodityDao.findSupplierCommodityInfo(code69);
                    if (null != suppliercommodity) {
                        message += "商品编码:" + code69 + "未导入成功！";
                        it.remove();

                    }

                }


            }


        }

        for(int n=0;n<commodityInputs.size();n++){
            BaseResult baseResult1=  saveCommodity(request,commodityInputs.get(n));
            String code69 = commodityInputs.get(n).getCommodityList().get(0).getCode69() == null ? "" : commodityInputs.get(n).getCommodityList().get(0).getCode69();
            if(1!=baseResult1.getCode()){
                code=1;
                message+="商品编码:" + code69 + "未导入成功！";
                continue;
            }else {
                code=2;
                message+="商品编码:" + code69+"成功导入！";
            }

        }
        baseResult.setCode(code);
        baseResult.setMessage(message);

        return baseResult;
    }
}
