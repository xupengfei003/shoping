package so.sao.shop.supplier.service;

import com.github.pagehelper.PageInfo;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommodityInput;
import so.sao.shop.supplier.pojo.output.CommodityExportOutput;
import so.sao.shop.supplier.pojo.output.CommodityImportOutput;
import so.sao.shop.supplier.pojo.output.CommodityOutput;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * Created by QuJunLong on 2017/7/17.
 */
public interface CommodityService {

    /**
     * 新增商品
     * @param commodityInput 商品信息对象
     * @return 保存结果
     */
    public BaseResult saveCommodity(HttpServletRequest request,CommodityInput commodityInput);

    /**
     * 修改商品
     * @param commodityInput 商品信息对象
     * @return 修改结果
     */
    public BaseResult updateCommodity(HttpServletRequest request, CommodityInput commodityInput);

    /**
     * 根据供应商商品ID获取商品详细信息
     * @param id
     * @return
     */
    CommodityOutput getCommodity(Long id);

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
     * 删除商品图片
     * @param id
     * @return
     */
    public BaseResult deleteCommImge(Long id);
    /**
     * 根据查询条件查询商品详情
     * @author liugang
     * @param supplierId 供应商ID
     * @param commCode69 商品编码
     * @param commId 商品ID
     * @param suppCommCode 商家商品编码
     * @param commName 商品名称
     * @param status 状态
     * @param typeId 类型ID
     * @param minPrice 价格（低）
     * @param maxPrice 价格（高）
     * @param pageNum 当前页号
     * @param pageSize 页面大小
     * @return PageInfo pageInfo对象
     */
    PageInfo searchCommodities(Long supplierId, String commCode69, Long commId, String suppCommCode, String commName, Integer status, Long typeId,
                               Double minPrice, Double maxPrice, int pageNum, int pageSize);

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

    public  List<CommodityImportOutput> importExcel(MultipartFile multipartFile , HttpServletRequest request);
}
