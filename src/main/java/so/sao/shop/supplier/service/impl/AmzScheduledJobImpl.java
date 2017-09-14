package so.sao.shop.supplier.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.config.CommConstant;
import so.sao.shop.supplier.config.azure.AzureBlobService;
import so.sao.shop.supplier.dao.*;
import so.sao.shop.supplier.domain.*;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.job.*;
import so.sao.shop.supplier.pojo.vo.CommBlobUpload;
import so.sao.shop.supplier.pojo.vo.CommImgeVo;
import so.sao.shop.supplier.service.AmzScheduledJobService;
import so.sao.shop.supplier.util.BeanMapper;
import so.sao.shop.supplier.util.CommUtil;
import so.sao.shop.supplier.util.FileUtil;
import so.sao.shop.supplier.util.HttpRequestUtil;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;

@Service
public class AmzScheduledJobImpl implements AmzScheduledJobService {

    @Autowired
    private SupplierCommodityDao supplierCommodityDao;

    @Autowired
    private CommCategoryDao commCategoryDao;

    @Autowired
    private CommodityDao commodityDao;

    @Autowired
    private CommImgeDao commImgeDao;

    @Autowired
    private TyCommImagDao tyCommImagDao;

    @Autowired
    private AzureBlobService azureBlobService;

    /**
     * 亚马劲商品列表url
     */
    @Value("${amz.jobs.product.list.url}")
    private String PRODUCT_LIST_URL;

    /**
     * 亚马劲商品详情url
     */
    @Value("${amz.jobs.product.info.url}")
    private String PRODUCT_INFO_URL;

    /**
     * 亚马劲供应商id
     */
    @Value("${amz.jobs.supplier.id}")
    private Long SUPPLIER_ID;

    /**
     * 亚马劲数据对接
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result amzScheduledJob(){
        try {
            //获取亚马劲数据所有商品列表
            ProListOutput proListOutput = getAmzProductList();
            //获取亚马劲所有商品id
            List<Long> prodIds = new ArrayList<>();
            proListOutput.getResponse().getData().forEach(data->{
                prodIds.add(data.getProd_id());
            });
            //当前数据库所有商品条码集合
            List<String> code69s = new ArrayList<>();
            Map<String, Long> code69Map = new HashMap<>();
            //当前供应商数据集合
            List<SupplierCommodity> scList = supplierCommodityDao.findBySupplierId(SUPPLIER_ID);
            for(SupplierCommodity sc : scList){
                //获取当前数据库code69集合
                code69s.add(sc.getCode69());
                code69Map.put(sc.getCode69(), sc.getId());
            }
            //循环插入数据
            code69s = insertAmaData(prodIds, code69s, code69Map);
            //多余数据置为失效
            for(String code69 : code69s) {
                supplierCommodityDao.updateInvalidStatusById(code69Map.get(code69), CommConstant.COMM_INVALID_STATUS, new Date());
            }
        }catch (Exception e){
            return Result.fail("数据导入异常", e);
        }
        return Result.success("数据导入成功");
    }

    /**
     * 获取亚马劲列表数据
     * @return ProListOutput
     */
    private ProListOutput getAmzProductList() throws Exception{
        ObjectMapper mapper = new ObjectMapper();
        //透云商品列表url
        String proListUrl = PRODUCT_LIST_URL;
        //透云商品列表对象
        ProListOutput proListOutput = null;
        //http请求返回结果
        String result;
        //游标结尾
        String after = "";
        //获取亚马劲所有列表数据
        for (int i = 1; i > 0; i++) {
            if (i == 1) {
                result = HttpRequestUtil.httpRequest(proListUrl);
                proListOutput = mapper.readValue(result, ProListOutput.class);
                if (null != proListOutput.getResponse().getPaging().getNext()) {
                    after = proListOutput.getResponse().getPaging().getNext().getAfter();
                    continue;
                } else {
                    break;
                }
            } else {
                String url = proListUrl + "&after=" + after;
                result = HttpRequestUtil.httpRequest(url);
                ProListOutput proList = mapper.readValue(result, ProListOutput.class);
                proListOutput.getResponse().getData().addAll(proList.getResponse().getData());
                if (null != proList.getResponse().getPaging().getNext()) {
                    after = proList.getResponse().getPaging().getNext().getAfter();
                    continue;
                } else {
                    break;
                }
            }
        }
        return proListOutput;
    }

    /**
     * 设置CommodityInput对象数据
     * @param proInfoRespOutput
     * @return
     */
    private Commodity setCommodityData(ProInfoRespOutput proInfoRespOutput){
        Commodity commodity = new Commodity();
        //商品名称
        commodity.setName(proInfoRespOutput.getProd_name());
        //商品条码
        commodity.setCode69(proInfoRespOutput.getBarcode());
        commodity.setCreatedAt(new Date());
        commodity.setCreatedBy(SUPPLIER_ID);
        commodity.setUpdatedAt(new Date());
        commodity.setUpdatedBy(SUPPLIER_ID);
        //商品分类三级id和code
        Long categoryOneId;
        Long categoryTwoId;
        Long categoryThreeId;
        String categoryOneCode;
        String categoryTwoCode;
        String categoryThreeCode;
        //亚马劲商品分类名称
        String CatName = proInfoRespOutput.getCat_name();
        List<CommCategory> commCategorys = commCategoryDao.findByName(CatName);
        if(commCategorys.size() > 0){
            int flag = 0;
            for(CommCategory commCategory : commCategorys){
                if(commCategory.getLevel() == 3){
                    categoryThreeId = commCategory.getId();
                    categoryThreeCode = commCategory.getCode();
                    Long categoryThreePid = commCategory.getPid();
                    CommCategory commCategoryTwo = commCategoryDao.findById(categoryThreePid);
                    categoryTwoId = commCategoryTwo.getId();
                    categoryTwoCode = commCategoryTwo.getCode();
                    Long categoryTwoPid = commCategoryTwo.getPid();
                    CommCategory commCategoryOne = commCategoryDao.findById(categoryTwoPid);
                    categoryOneId = commCategoryOne.getId();
                    categoryOneCode = commCategoryOne.getCode();
                    commodity.setCategoryOneId(categoryOneId);
                    commodity.setCategoryTwoId(categoryTwoId);
                    commodity.setCategoryThreeId(categoryThreeId);
                    commodity.setCategoryCode(categoryOneCode + categoryTwoCode + categoryThreeCode);
                    break;
                }else {
                    flag++;
                }
            }
            if(commCategorys.size() == flag){
                commodity.setCategoryOneId(CommConstant.COMM_CATEGORY_ID);
                commodity.setCategoryCode(CommConstant.COMM_CATEGORY_CODE);
            }
        }else{
            commodity.setCategoryOneId(CommConstant.COMM_CATEGORY_ID);
            commodity.setCategoryCode(CommConstant.COMM_CATEGORY_CODE);
        }
        return commodity;
    }

    /**
     * 设置SupplierCommodityVo对象数据
     * @param proInfoRespOutput
     * @return
     */
    private SupplierCommodity setSupplierCommodityData(ProInfoRespOutput proInfoRespOutput){
        SupplierCommodity sc = new SupplierCommodity();
        //商品条码
        sc.setCode69(proInfoRespOutput.getBarcode());
        //市场价
        sc.setPrice(proInfoRespOutput.getComm_price());
        //成本价
        sc.setUnitPrice(proInfoRespOutput.getPrice());
        sc.setStatus(CommConstant.COMM_ST_NEW);
        sc.setSupplierId(SUPPLIER_ID);
        sc.setCreatedBy(SUPPLIER_ID);
        sc.setUpdatedBy(SUPPLIER_ID);
        sc.setCreatedAt(new Date());
        sc.setUpdatedAt(new Date());
        sc.setInvalidStatus(CommConstant.COMM_ACTIVE_STATUS);
        //商品说明
        String remark;
        //获取商品说明
        List<ProInfoRespTabsOutput> proInfoRespTabsOutputs = proInfoRespOutput.getTabs();
        for(ProInfoRespTabsOutput proInfoRespTabsOutput : proInfoRespTabsOutputs){
            if(proInfoRespTabsOutput.getPosition() == 1){
                List<ProInfoRespTabsContOutput> proInfoRespTabsContOutputs = proInfoRespTabsOutput.getContents();
                for(ProInfoRespTabsContOutput proInfoRespTabsContOutput : proInfoRespTabsContOutputs){
                    if(proInfoRespTabsContOutput.getPosition() == 1 && proInfoRespTabsContOutput.getType().equals("TEXT")){
                        remark = proInfoRespTabsContOutput.getData().getText();
                        sc.setRemark(remark);
                    }
                }
            }
        }
        return sc;
    }

    /**
     * 获取图片信息
     * @param proInfoRespOutput
     * @return
     * @throws Exception
     */
    private List<CommImgeVo> getCommImgeVos (ProInfoRespOutput proInfoRespOutput) throws Exception{
        //获取图片信息
        List<ProInfoRespImagesOutput> proInfoRespImagesOutputs = proInfoRespOutput.getImages();
        List<String> urls = new ArrayList<>();
        List<CommImgeVo> commImgeVos = new ArrayList<>();
        for (ProInfoRespImagesOutput proInfoRespImagesOutput : proInfoRespImagesOutputs){
            urls.add(proInfoRespImagesOutput.getUrl());
        }
        //上传图片
        Result result = azureBlobService.uploadFiles(urls);
        //根据上传结果获取上传的图片信息
        if(result.getCode() == 1){
            List<CommBlobUpload> blobUploadEntities = (List<CommBlobUpload>)result.getData();
            blobUploadEntities.forEach(commBlobUpload->{
                CommImgeVo commImgeVo = BeanMapper.map(commBlobUpload, CommImgeVo.class);
                commImgeVo.setName(commBlobUpload.getFileName());
                commImgeVo.setThumbnailUrl(commBlobUpload.getMinImgUrl());
                commImgeVos.add(commImgeVo);
            });
        }
        return commImgeVos;
    }

    /**
     * 循环插入亚马劲数据
     * @param prodIds
     * @param code69s
     * @return
     * @throws Exception
     */
    private List<String> insertAmaData(List<Long> prodIds, List<String> code69s,Map<String, Long> code69Map) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        //透云商品详情url
        String proInfoUrl = PRODUCT_INFO_URL;
        //要删除的图片对应scId集合
        List<Long> scIds = new ArrayList<>();
        //要删除的公共图片对应code69集合
        List<String> code69List = new ArrayList<>();
        //图片新增集合
        List<CommImge> commImgesIns = new ArrayList<>();
        //公共图片新增集合
        List<TyCommImge> tyCommImgesIns = new ArrayList<>();
        //循环获取数据
        for (Long pordId : prodIds) {
            String url = proInfoUrl + pordId;
            String proInfoResult = HttpRequestUtil.httpRequest(url);
            ProInfoOutput proInfoOutput = mapper.readValue(proInfoResult, ProInfoOutput.class);
            ProInfoRespOutput proInfoRespOutput = proInfoOutput.getResponse();
            if (null == proInfoRespOutput) {
                continue;
            }
            //设置SupplierCommodity对象数据
            SupplierCommodity sc = setSupplierCommodityData(proInfoRespOutput);
            //设置Commodity对象数据
            Commodity commodity = setCommodityData(proInfoRespOutput);
            //商品条码
            String code69 = proInfoRespOutput.getBarcode();
            //获取图片信息
            List<CommImgeVo> commImgeVos = getCommImgeVos(proInfoRespOutput);
            //公共图片集合
            List<TyCommImge> tyCommImgeList = new ArrayList<>();
            //设置图片信息
            commImgeVos.forEach(imgeVo -> {
                TyCommImge tyCommImge = BeanMapper.map(imgeVo, TyCommImge.class);
                tyCommImge.setCode69(code69);
                tyCommImge.setCreatedAt(new Date());
                tyCommImge.setUpdatedAt(new Date());
                tyCommImgeList.add(tyCommImge);
            });
            //缩略图url
            if (commImgeVos.size() > 0) {
                sc.setMinImg(commImgeVos.get(0).getUrl());
            }
            //当前数据库列表有该商品条码的数据，则修改
            if (code69s.contains(code69)) {
                //修改商品信息
                Commodity comm = commodityDao.findCommInfoByCode69(code69);
                if (null == comm) {
                    comm = commodity;
                    commodityDao.save(comm);
                } else {
                    commodity.setId(comm.getId());
                    commodityDao.update(commodity);
                }
                //修改规格信息
                String sku = CommUtil.createSku(commodity.getCategoryCode(), comm.getId(), SUPPLIER_ID);
                sc.setSku(sku);
                sc.setId(code69Map.get(code69));
                supplierCommodityDao.updateAmz(sc);
                //拼装图片和公共图片集合
                commImgeVos.forEach(imgeVo -> {
                    CommImge imge = BeanMapper.map(imgeVo, CommImge.class);
                    imge.setScId(sc.getId());
                    commImgesIns.add(imge);
                });
                code69List.add(code69);
                scIds.add(sc.getId());
                tyCommImgesIns.addAll(tyCommImgeList);
            } else {
                //新增商品信息
                Commodity commo = commodityDao.findCommInfoByCode69(code69);
                if (null == commo) {
                    commodityDao.save(commodity);
                    //拼装公共库图片
                    tyCommImgesIns.addAll(tyCommImgeList);
                }else {
                    commodity.setId(commo.getId());
                }
                //新增商品规格
                String sku = CommUtil.createSku(commodity.getCategoryCode(), commodity.getId(), SUPPLIER_ID);
                sc.setSku(sku);
                supplierCommodityDao.save(sc);
                //拼装图片集合
                commImgeVos.forEach(imgeVo -> {
                    CommImge imge = BeanMapper.map(imgeVo, CommImge.class);
                    imge.setScId(sc.getId());
                    commImgesIns.add(imge);
                });
            }
            code69s.remove(code69);
        }
        if(code69List.size() > 0){
            //批量删除公共图片
            tyCommImagDao.deleteByCode69s(code69List);
        }
        if(scIds.size() > 0){
            //批量删除图片
            commImgeDao.deleteByScIds(scIds);
        }
        //公共图片批量新增
        tyCommImagDao.batchSave(tyCommImgesIns);
        //图片批量新增
        commImgeDao.batchSave(commImgesIns);
        return code69s;
    }
}
