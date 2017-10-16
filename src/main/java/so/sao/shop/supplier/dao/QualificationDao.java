package so.sao.shop.supplier.dao;

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

    void updateQualificationStatus(@Param("accountId") Integer accountId, @Param("qualificationStatus")Integer qualificationStatus, @Param("updateDate")Date updateDate );

    void updateQualificationReason(@Param("reason") String reason, @Param("accountId") Integer accountId );

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
}
