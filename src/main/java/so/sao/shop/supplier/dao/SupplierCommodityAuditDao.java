package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.SupplierCommodityAudit;

import java.util.List;

/**
 * 供应商商品审核记录
 * @author zhaoyan
 * @create 2017/9/19 11:11
 */
@Mapper
public interface SupplierCommodityAuditDao {

    /**
     * 新增供应商商品审核记录
     * @param supplierCommodityAudit 供应商商品审核记录
     */
    void save(SupplierCommodityAudit supplierCommodityAudit);

    /**
     * 批量新增供应商商品审核记录
     * @param list 批量供应商商品审核记录
     */
    void saveBatch(List<SupplierCommodityAudit> list);

    /**
     * 根据scId和audit_result判断该供应商商品是否已处于待审核状态
     * @param scId 供应商商品ID
     */
    int countByScidAndAuditResult(Long scId);

    /**
     * 根据scId数组和audit_result判断该供应商商品数组中是否已处于待审核状态
     * @param scIds 供应商商品ID数组
     */
    int countByScidArrayAndAuditResult(@Param("ids") Long[] scIds);

    /**
     * 更新
     */
    void updateAuditFlagByScId(@Param("scId") Long scId, @Param("auditFlag") int auditFlag);
	
	/**
     * 批量更新
     */
	void updateAuditFlagByList(@Param("list") List<SupplierCommodityAudit> list, @Param("auditFlag")int status);

    /** 通过id 查询审核记录表
     *
     * @param id
     * @return
     */
    SupplierCommodityAudit findSupplierCommodityAuditById(@Param("id") Long id);

    /**
     * 审核修改
     * @param
     */
    void updateSupplierCommodityAuditById(SupplierCommodityAudit supplierCommodityAudit);

}
