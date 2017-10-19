package so.sao.shop.supplier.dao;

import io.swagger.models.auth.In;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.pojo.input.QualificationInput;
import so.sao.shop.supplier.pojo.output.QualificationListOut;
import so.sao.shop.supplier.pojo.output.QualificationOut;
import so.sao.shop.supplier.domain.Qualification;

import java.util.Date;
import java.util.List;

/**
 * Created by liugang on 2017/10/11.
 * 供应商资质DAO层
 */
@Mapper
public interface QualificationDao {

    /**
     * 资质审核 - 更新资质状态和时间,拒绝原因
     * @param accountId
     * @param qualificationStatus
     * @param reason
     * @param updateDate
     */
    void updateQualificationStatus(@Param("accountId") Long accountId, @Param("qualificationStatus")Integer qualificationStatus,@Param("reason") String reason, @Param("updateDate")Date updateDate );

    /**
     * 查询登录供应商的资质状态
     * @param accountId
     * @return
     */
    Integer getAccountQualificationStatus( @Param("accountId") Long accountId );

    /**
     * 根据供应商id查询资质详情
     * @param accountId 供应商id
     * @return 返回资质详情
     */
    public List<QualificationOut> findByAccountId(Long accountId);

    /**
     * 根据条件查询资质列表
     * @param qualificationInput
     * @return 返回供应商及资质信息
     */
    public List<QualificationListOut> findPage(QualificationInput qualificationInput);

    /**
     * 添加供应商资质
     * @param qualification
     * @return
     */
    boolean save(Qualification qualification);

    /**
     * 根据供应商ID删除资质表（更新资质表删除状态位）
     * @param accountId 供应商ID
     * @return
     */
    boolean delete(Long accountId);

    /**
     * 更新资质状态消息已读状态
     * @param accountId
     */
    void updateQualificationMessageRead(Long accountId);

    /**
     * 判断资质消息是否已读
     * @param accountId
     * @return
     */
    Integer findQualificationStatusIsRead(Long accountId);
}
