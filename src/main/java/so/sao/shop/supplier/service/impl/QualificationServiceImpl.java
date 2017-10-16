package so.sao.shop.supplier.service.impl;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.config.azure.AzureBlobService;
import so.sao.shop.supplier.dao.QualificationDao;
import so.sao.shop.supplier.dao.QualificationImageDao;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.QualificationInput;
import so.sao.shop.supplier.pojo.input.QualificationSaveInput;
import so.sao.shop.supplier.pojo.output.QualificationListOut;
import so.sao.shop.supplier.pojo.output.QualificationOut;
import so.sao.shop.supplier.service.QualificationService;
import so.sao.shop.supplier.util.Ognl;
import so.sao.shop.supplier.util.PageTool;
import so.sao.shop.supplier.pojo.vo.CommBlobUpload;
import so.sao.shop.supplier.pojo.vo.QualificationImageVo;
import so.sao.shop.supplier.pojo.vo.QualificationImagesVo;
import so.sao.shop.supplier.domain.Qualification;
import so.sao.shop.supplier.domain.QualificationImage;
import so.sao.shop.supplier.exception.BusinessException;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.config.CommConstant;
import org.apache.commons.lang.StringUtils;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by liugang on 2017/10/11.
 * 供应商资质service
 */
@Service
public class QualificationServiceImpl implements QualificationService{

    @Autowired
    private AzureBlobService azureBlobService;


    @Autowired
    private QualificationDao qualificationDao;

    @Autowired
    private QualificationImageDao qualificationImageDao;


    /**
     * 初始化日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 资质审核 - 更新资质状态和时间,拒绝原因
     * @param accountId
     * @param qualificationStatus
     * @param reason
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateQualificationStatus(Integer accountId, Integer qualificationStatus, String reason) {
        if( !(Constant.QUALIFICATION_VERIFY_PASS == qualificationStatus)
                && !(Constant.QUALIFICATION_VERIFY_NOT_PASS == qualificationStatus)
                && !(Constant.QUALIFICATION_AWAIT_VERIFY == qualificationStatus ) ){
            logger.info("无效的审核参数");
            return Result.fail("无效的审核参数");
        }
        Date updateDate = new Date();
        qualificationDao.updateQualificationStatus( accountId, qualificationStatus,updateDate );
        qualificationDao.updateQualificationReason( reason, accountId );
        return Result.success("更新成功");
    }

    /**
     * 查询登录供应商的资质状态
     * @param accountId
     * @return Result
     */
    @Override
    public Result getAccountQualificationStatus(Long accountId) {
        Integer qualificationStatus = qualificationDao.getAccountQualificationStatus( accountId );
        if(Ognl.isNull(qualificationStatus)){
            logger.info("暂无数据");
            return Result.fail("暂无数据");
        }
        return Result.success("查询成功", qualificationStatus );
    }

    @Override
    public Result findByAccountId(Long accountId) {
        List<QualificationOut> qualificationOuts = qualificationDao.findByAccountId(accountId);
        if(qualificationOuts.size()>0) {
            return Result.success("根据供应商id查询供应商资质详情成功", qualificationOuts);
        }
        return Result.fail("没有符合条件的供应商资质详情");
    }

    @Override
    public Result searchQualifications(QualificationInput qualificationInput) {
        PageTool.startPage(qualificationInput.getPageNum(), qualificationInput.getPageSize());
        List<QualificationListOut> qualificationListOuts = qualificationDao.findPage(qualificationInput);
        if(qualificationListOuts!=null && qualificationListOuts.size()>0){
            PageInfo pageInfo = new PageInfo(qualificationListOuts);
            return Result.success("查询成功",pageInfo);
        }
        return Result.fail("没有符合条件的资质信息");
    }

    /**
     * 资质文件上传
     *
     * @param multipartFile 资质文件/图片
     * @return
     */
    @Override
    public Result uploadQualificationFile(MultipartFile multipartFile) {
        List<CommBlobUpload> blobUploadList = new ArrayList<CommBlobUpload>();
        try {
            if (!multipartFile.isEmpty()) {
                MultipartFile[] multipartFiles = {multipartFile};
                Result result = azureBlobService.uploadFilesComm(multipartFiles, blobUploadList);
                if(blobUploadList.isEmpty()){
                    return result;
                }else{
                    String url = blobUploadList.get(0).getUrl();       //获取云端原图url
                    String minImgUrl = blobUploadList.get(0).getMinImgUrl(); //获取云端缩略图url
                    //获取云端名称
                    String cloudName = StringUtils.substringAfter(url, CommConstant.AZURE_CONTAINER+"/");
                    //获取缩略图云端名称
                    String minCloudName = StringUtils.substringAfter(minImgUrl, CommConstant.AZURE_CONTAINER+"/");
                    QualificationImageVo qualificationImageVo = new QualificationImageVo();
                    qualificationImageVo.setFileName(blobUploadList.get(0).getFileName());
                    qualificationImageVo.setCloudName(cloudName);
                    qualificationImageVo.setMinCloudName(minCloudName);
                    qualificationImageVo.setUrl(url);
                    qualificationImageVo.setMinImgUrl(minImgUrl);
                    return Result.success("文件上传成功", qualificationImageVo);
                }
            }else{
                return Result.fail("请选择需要上传的文件");
            }
        } catch (Exception e) {
            logger.error("文件上传异常", e);
            return Result.fail("文件上传异常");
        }
    }

    /**
     * 添加供应商资质图片
     *
     * @param qualificationInput  供应商资质入参
     * @return
     */
    @Override
    public Result saveQualification(QualificationSaveInput qualificationInput) {
        Result result = new Result();
        //入参校验
        checkInputParam(qualificationInput);
        Qualification qualification = new Qualification();
        qualification.setAccountId(qualificationInput.getAccountId());
        qualification.setQualificationStatus(1);
        qualification.setCreateTime(new Date());
        qualification.setUpdateTime(new Date());
        //供应商资质入库
        qualificationDao.save(qualification);
        //资质图片入库
        List<QualificationImagesVo> imgs = qualificationInput.getImgs();
        List<QualificationImage> list = new ArrayList<>();
        //构建资质图片入库参数
        for (int i = 0; i < imgs.size(); i++){
            List<QualificationImageVo> qualificationImage = imgs.get(i).getList();
            for (QualificationImageVo qualificationImageVo :qualificationImage){
                QualificationImage qualificationImages = new QualificationImage();
                qualificationImages.setQualificationId(qualification.getId());
                qualificationImages.setQualificationType(imgs.get(i).getQualificationType());
                qualificationImages.setFileName(qualificationImageVo.getFileName());
                qualificationImages.setCloudName(qualificationImageVo.getCloudName());
                qualificationImages.setMinCloudName(qualificationImageVo.getMinCloudName());
                qualificationImages.setUrl(qualificationImageVo.getUrl());
                qualificationImages.setMinImgUrl(qualificationImageVo.getMinImgUrl());
                qualificationImages.setCreateTime(new Date());
                qualificationImages.setUpdateTime(new Date());
                list.add(qualificationImages);
            }
            //供应商资质图片入库
            qualificationImageDao.save(list);
        }

        result.setCode(Constant.CodeConfig.CODE_SUCCESS);
        result.setMessage("添加供应商资质成功");
        return result;
    }

    /**
     * 供应商资质删除
     *
     * @param accountID 供应商ID
     * @return
     */
    @Override
    public Result deleteQualification(Long accountID) {
        Result result = new Result();
        //通过供应商ID查询资质ID
        Long qualificationId = 0L;
        //通过供应商ID删除该供应商下的所有资质信息（软删除）
        qualificationDao.delete(accountID);
        //通过资质ID同步删除供应商资质图片信息（软删除）
        qualificationImageDao.delete(qualificationId);
        result.setCode(Constant.CodeConfig.CODE_SUCCESS);
        result.setMessage("删除供应商资质信息成功");
        return result;
    }

    /**
     * 校验供应商资质入参
     * @param qualificationInput  资质入参
     */
    private void checkInputParam(QualificationSaveInput qualificationInput){
        List<QualificationImagesVo> imgs = qualificationInput.getImgs();
        //资质图片集合不能为空
        if (imgs == null || imgs.size()==0){
            throw new BusinessException("资质图片不能为空");
        }else if (imgs.size() > Constant.QualificationConfig.AllQualificationImgNum){
            throw new BusinessException("资质图片总数不能超过 "+Constant.QualificationConfig.AllQualificationImgNum+" 张");
        }
        //校验各类资质必填项及数量
        for (QualificationImagesVo qualificationImagesVo : imgs){

            checkQualificationImgNum(qualificationImagesVo,Constant.QualificationConfig.BankLicense,"开户银行许可证");
            checkQualificationImgNum(qualificationImagesVo,Constant.QualificationConfig.BusinessLicense,"营业执照");
            checkQualificationImgMaxNum(qualificationImagesVo,Constant.QualificationConfig.AuthorizationReport,"授权报告");
            checkQualificationImgMaxNum(qualificationImagesVo,Constant.QualificationConfig.InspectionReport,"质检报告");
            checkQualificationImgMaxNum(qualificationImagesVo,Constant.QualificationConfig.FoodDistributionLicense,"食品流通许可证");
        }
    }

    /**
     *校验资质类型是否必传及最大传数量
     * @param qualificationImagesVo 资质图片集合VO
     * @param qualificationType 资质类型
     * @param qualificationName 资质类型（1、开户银行许可证 2、营业执照 3、授权报告 4、质检报告 5、食品流通许可证）
     */
    private void checkQualificationImgNum(QualificationImagesVo qualificationImagesVo, Integer qualificationType ,String qualificationName){
        if (qualificationImagesVo.getQualificationType() == qualificationType){
            if (qualificationImagesVo.getList() == null || qualificationImagesVo.getList().size() == 0){
                throw new BusinessException(qualificationName + "图片不能为空");
            }else {
                checkQualificationImgMaxNum(qualificationImagesVo,qualificationType,qualificationName);
            }
        }
    }

    /**
     *
     * @param qualificationImagesVo  资质图片集合VO
     * @param qualificationType  资质类型
     * @param qualificationName  资质类型（1、开户银行许可证 2、营业执照3、授权报告 4、质检报告 5、食品流通许可证）
     */
    private void checkQualificationImgMaxNum(QualificationImagesVo qualificationImagesVo, Integer qualificationType ,String qualificationName){
        if (qualificationImagesVo.getQualificationType() == qualificationType){
            if (qualificationImagesVo.getList().size() > Constant.QualificationConfig.MaxImgNumber){
                throw new BusinessException(qualificationName + "图片不能超过 " + Constant.QualificationConfig.MaxImgNumber + " 张");
            }
        }
    }
}
