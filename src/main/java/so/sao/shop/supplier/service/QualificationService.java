package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.QualificationInput;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.pojo.input.QualificationSaveInput;

/**
 * Created by liugang on 2017/10/11.
 */
public interface QualificationService {

    Result updateQualificationStatus(Integer accountId, Integer qualificationStatus, String reason );

    Result getAccountQualificationStatus( Long accountId );

    /**
     * 根据供应商id查询资质详情
     * @param accountId 供应商id
     * @return 返回资质详情
     */
    public Result findByAccountId(Long accountId);

    /**
     * 根据条件查询资质列表
     * @param qualificationInput 查询条件
     * @return 返回查询结果
     */
    public Result searchQualifications(QualificationInput qualificationInput);

    /**
     * 资质文件上传
     * @param multipartFile  资质文件
     * @return
     */
    Result uploadQualificationFile(MultipartFile multipartFile);

    /**
     * 添加供应商资质图片
     * @param qualificationInput
     * @return
     */
    Result saveQualification(QualificationSaveInput qualificationInput);

    /**
     * 供应商资质删除
     * @param accountID
     * @return
     */
    Result deleteQualification(Long accountID);
}
