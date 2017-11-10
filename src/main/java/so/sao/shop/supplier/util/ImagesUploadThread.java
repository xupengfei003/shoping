package so.sao.shop.supplier.util;

import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.config.azure.AzureBlobService;
import so.sao.shop.supplier.dao.CommImgeDao;
import so.sao.shop.supplier.dao.CommodityDao;
import so.sao.shop.supplier.dao.SupplierCommodityDao;
import so.sao.shop.supplier.dao.TyCommImagDao;
import so.sao.shop.supplier.domain.CommImge;
import so.sao.shop.supplier.domain.TyCommImge;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.vo.CommBlobUpload;
import so.sao.shop.supplier.pojo.vo.CommImgeVo;
import so.sao.shop.supplier.pojo.vo.CommodityImageVo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by panshuaishuai on 2017/9/12.
 */
public class ImagesUploadThread implements  Runnable{

    private CommImgeDao commImgeDao;
    private TyCommImagDao tyCommImagDao;
    private SupplierCommodityDao supplierCommodityDao;
    private AzureBlobService azureBlobService;
    private CommodityDao commodityDao;
    private List<CommodityImageVo> commodityImageVoList;
    private  String filePath;
    private  Long  supplierId;

    public ImagesUploadThread(List<CommodityImageVo> commodityImageVoList, String filePath, Long supplierId, AzureBlobService azureBlobService , CommImgeDao commImgeDao, TyCommImagDao tyCommImagDao, SupplierCommodityDao supplierCommodityDao, CommodityDao commodityDao){

        this.commodityImageVoList = commodityImageVoList;
        this.filePath = filePath;
        this.supplierId = supplierId;
        this.commImgeDao = commImgeDao;
        this.tyCommImagDao = tyCommImagDao;
        this.supplierCommodityDao = supplierCommodityDao;
        this.azureBlobService = azureBlobService;
        this.commodityDao = commodityDao;
    }
    @Override
    public void run() {
        for(int g = 0;   g < commodityImageVoList.size(); g++ ){
            CommodityImageVo commodityImageVo=commodityImageVoList.get(g);
            String[] imgs = commodityImageVo.getImage().replaceAll("，", ",").split(",");
            List<String> imgList = Arrays.asList(imgs);
            // 上传图片
            List<CommImgeVo> commImgeVoList = batchUploadCommPicture(filePath, imgList,3);

                //保存 ty_comm_imge    comm_imge   , 更新  supplier_commoidity
                //保存公共库图片
                List<TyCommImge> tyCommImges = new ArrayList<>();
                commImgeVoList.forEach( tyImgeVo->{
                    TyCommImge tyCommImge = BeanMapper.map(tyImgeVo, TyCommImge.class);
                    tyCommImge.setCode69(commodityImageVo.getCode69());
                    tyCommImge.setCreatedAt(new Date());
                    tyCommImge.setUpdatedAt(new Date());
                    tyCommImges.add(tyCommImge);
                });
                if(tyCommImges.size()>0){
                    tyCommImagDao.batchSave(tyCommImges);
                }
                //保存图片
                List<CommImge> commImges = new ArrayList<>();
                commImgeVoList.forEach(imgeVo->{
                    CommImge imge = BeanMapper.map(imgeVo, CommImge.class);
                    imge.setScId(commodityImageVo.getScId());
                    commImges.add(imge);
                });
                if(commImges.size()>0) {
                    commImgeDao.batchSave(commImges);
                }
                // 更新  supplier_commoidity
                if(commImgeVoList.size()>0) {
                    supplierCommodityDao.updateMinImg(commImgeVoList.get(0).getThumbnailUrl(), new Date(), supplierId, commodityImageVo.getScId());
                }
            }
    }

    public  List<CommImgeVo> batchUploadCommPicture(String path , List<String> files, int number) {
        // 1.调用uploadFilesComm方法上传
        List<Result> uploadResult = azureBlobService.uploadFilesComm(path, files); // 上传图片
        List<CommImgeVo> commImgeVoList = new ArrayList<CommImgeVo>();
        // 2.分离出上传成功和失败的
        boolean isAllSuccess = false; // 是否全部上传成功
        List uploadFailList = new ArrayList(); // 上传失败文件名集合
        // 3.上传成功的结果保存起来
        List<CommBlobUpload> blobUploadEntities = null; // 本次上传成功实体集合
        for (int i = 0; i < uploadResult.size(); i++) { // 循环返回结果
            Result result = uploadResult.get(i);
            if ( Constant.CodeConfig.CODE_SUCCESS.equals(result.getCode()) && ( (List<CommBlobUpload>) result.getData() ).size()>0  ) {
                blobUploadEntities = (List<CommBlobUpload>) result.getData();
                for (int t = 0; t < blobUploadEntities.size(); t++) {
                    CommBlobUpload blobUpload = blobUploadEntities.get(t);
                    CommImgeVo commImgeVo = BeanMapper.map(blobUpload, CommImgeVo.class);
                    commImgeVo.setThumbnailUrl(blobUpload.getMinImgUrl());
                    commImgeVo.setName(blobUpload.getFileName());
                    commImgeVoList.add(commImgeVo);
                }
                if (blobUploadEntities.size() == files.size()) { // 上传成功的文件数量和上传数量一致，则上传成功
                    isAllSuccess = true;
                    break;
                }
            } else {
                uploadFailList.addAll((List) result.getData()); // 上传失败文件名添加到list
            }
        }

        if (isAllSuccess) { // 全部上传成功
            return commImgeVoList;
        }
        // 4.上传失败的结果，如果尝试次数number大于等于0，重新上传
        if (number >= 0) { // 再次上传
            List<CommImgeVo> successList = batchUploadCommPicture(path, uploadFailList, --number); // 失败文件再次上传
            if (null != successList  &&  successList.size()>0){ // 本次上传部分成功
                commImgeVoList.addAll(successList); // 本次成功的和下次成功的合并
            }
        } else { // 上传次数超限
            for (int t = 0; t <  uploadFailList.size(); t++) {
                CommImgeVo commImgeVo = new CommImgeVo();
                commImgeVo.setThumbnailUrl("ERROR");
                commImgeVo.setUrl("ERROR");
                commImgeVo.setName(uploadFailList.get(t).toString());
                commImgeVoList.add(commImgeVo);
            }
        }
        return commImgeVoList;
    }
}
