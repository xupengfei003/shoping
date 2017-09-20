package so.sao.shop.supplier.service;

import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.config.StorageConfig;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.*;
import so.sao.shop.supplier.pojo.output.CommodityExportOutput;
import so.sao.shop.supplier.pojo.output.CommodityOutput;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

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
    Result updateCommodity(CommodityUpdateInput commodityUpdateInput, Long supplierId, boolean isAdmin);

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
     * @return Result Result对象
     */
    Result searchCommodities(CommSearchInput commSearchInput);

    /**
     * 根据查询条件查询商品详情(简单条件查询)
     * @param commSimpleSearchInput  简单条件查询请求
     * @return
     */
    Result simpleSearchCommodities(CommSimpleSearchInput commSimpleSearchInput);

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
    Result onShelvesBatch(Long[] ids);

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
     * 批量审核
     * @param request
     * @param commAuditInput
     * @return
     */
    Result auditBatch(HttpServletRequest request , CommAuditInput commAuditInput);
}
