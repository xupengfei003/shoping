package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * Created by liugang on 2017/10/11.
 * 供应商资质DAO层
 */
@Mapper
public interface QualificationDao {

    void updateQualificationStatus(@Param("accountId") Integer accountId, @Param("qualificationStatus")Integer qualificationStatus, @Param("updateDate")Date updateDate );

    void updateQualificationReason(@Param("reason") String reason, @Param("accountId") Integer accountId );

    Integer getAccountQualificationStatus( @Param("accountId") Long accountId );
}
