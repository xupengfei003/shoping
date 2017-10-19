package so.sao.shop.supplier.service;

import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by QuJunLong on 2017/7/17.
 */
public interface CommodityService {

    /**
     * 新增商品
     * @param commodityInput 商品信息对象
     * @return 保存结果
     */
    Result saveCommodity(CommodityInput commodityInput,Long supplierId);

    /**
     * 修改商品
     * @param commodityUpdateInput 商品信息对象
     * @return 修改结果
     */
    Result updateCommodity(CommodityUpdateInput commodityUpdateInput, Long supplierId);

    /**
     * 根据供应商商品ID获取商品详细信息
     * @param id
     * @return
     */
    Result getCommodity(Long id);

    /**
     * 根据userId查Account实体
     * @param userId
     * @return
     */
    public Account findAccountByUserId(Long userId);

    /**
     * 根据商品条码查询商品信息
     * @param code69
     * @return  商品信息
     */
    Result findCommodity(String code69);

    /**
     * 根据查询条件查询商品详情（高级搜索）
     * @author liugang
     * @param commSearchInput 高级搜索查询请求
     * @param request
     * @return Result Result对象
     */
    Result searchCommodities(CommSearchInput commSearchInput, HttpServletRequest request);

    /**
     * 根据查询条件查询商品详情(简单条件查询)
     * @param commSimpleSearchInput  简单条件查询请求
     * @param request
     * @return
     */
    Result simpleSearchCommodities(CommSimpleSearchInput commSimpleSearchInput, HttpServletRequest request);

    /**
     * 删除商品
     * @param id
     * @return true or false
     */
    Result deleteCommodity(Long id);

    /**
     * 批量删除商品
     * @param ids
     * @return 结果集
     */
    Result deleteCommodities(Long[] ids);

    /**
     * 上架商品
     * @param id
     * @return
     */
    Result onShelves(Long id);

    /**
     * 下架商品
     * @param id
     * @return
     */
    Result offShelves(Long id);

    /**
     * 批量上架商品
     * @param ids
     * @return
     */
    Result onShelvesBatch(Long[] ids, Long supplierId);

    /**
     * 批量下架商品
     * @param ids
     * @return
     */
    Result offShelvesBatch(Long[] ids);


    /**
     *
     * @param multipartFile 文件对象
     * @return 导入结果
     */

    Result importExcel(MultipartFile multipartFile , HttpServletRequest request, Long supplierId) throws Exception;

    /**
     * 批量导出
     * @param request
     * @param response
     * @param commExportInput
     * @return
     */
    Result exportExcel(HttpServletRequest request , HttpServletResponse response , CommExportInput commExportInput);

    /**
     * 根据供应商的激活状态更新商品的激活状态
     * @param accountStatus  供应商状态
     * @return
     */
    void updateCommInvalidStatus(Long supplierId, Integer accountStatus);
	
	/**
     * 根据条件查询商品审核列表
     * @param commodityAuditInput 查询条件
     * @return 商品审核数据
     */
    Result< PageInfo> serachCommodityAudit(CommodityAuditInput commodityAuditInput);

    /**
     * 批量审核
     * @param request
     * @param commAuditInput
     * @return
     */
    Result auditBatch(HttpServletRequest request , CommAuditInput commAuditInput);

    /**
     * 根据审核表ID查询审核记录详情
     * @param id 审核表ID
     * @return
     */
    Result findAuditDetail(Long id);

    /**
     * 供应商首页-供应商商品部分信息统计
     * @param supplierId 供应商id
     * @return
     */
    Result countCommDetail(Long supplierId);
}
