package so.sao.shop.supplier.service;

import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.config.StorageConfig;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommSearchInput;
import so.sao.shop.supplier.pojo.input.CommSimpleSearchInput;
import so.sao.shop.supplier.pojo.input.CommodityInput;
import so.sao.shop.supplier.pojo.input.CommodityUpdateInput;
import so.sao.shop.supplier.pojo.output.CommodityExportOutput;
import so.sao.shop.supplier.pojo.output.CommodityOutput;

import javax.servlet.http.HttpServletRequest;
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
    Result updateCommodity(CommodityUpdateInput commodityUpdateInput, Long supplierId);

    /**
     * 根据供应商商品ID获取商品详细信息
     * @param id
     * @return
     */
    Result getCommodity(Long id);

    /**
     * 根据id查询多个商品
     * @param ids
     * @return
     */
    public List<CommodityExportOutput> findByIds(Long[] ids);

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
     * 删除商品图片
     * @param id
     * @return
     */
    public BaseResult deleteCommImge(Long id);
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
    Result updateStatusSj(Long id);

    /**
     * 下架商品
     * @param id
     * @return
     */
    Result updateStatusXj(Long id);

    /**
     * 批量上架商品
     * @param ids
     * @return
     */
    BaseResult updateStatusSjs(Long[] ids);

    /**
     * 批量下架商品
     * @param ids
     * @return
     */
    BaseResult updateStatusXjs(Long[] ids);


    /**
     *
     * @param multipartFile 文件对象
     * @return 导入结果
     */

    Result importExcel(MultipartFile multipartFile , HttpServletRequest request, Long supplierId) throws Exception;
}
