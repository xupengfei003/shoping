package so.sao.shop.supplier.dao.external;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.external.HotCommodities;
import so.sao.shop.supplier.pojo.input.HotCommoditySaveInput;
import so.sao.shop.supplier.pojo.output.AddHotCommOutput;
import so.sao.shop.supplier.pojo.vo.AddHotCommVo;

import java.util.List;

public interface HotCommoditiesDao {

    /**
     *查询所有热门商品列表
     * @param isSalesVolume 按照销量排序（自动）
     * @param isSort    按照sort排序（手动）
     * @param inputvalue 商品编号和商品名称模糊查询
     * @param categoryOneId 商品一级科属
     * @param categoryTwoId 商品二级科属
     * @param categoryThreeId  商品三级科属
     * @return
     */
    List<HotCommodities> findAll(@Param("isSalesVolume")String isSalesVolume, @Param("isSort")String isSort,
                                 @Param("inputvalue")String inputvalue, @Param("categoryOneId")Long categoryOneId,
                                 @Param("categoryTwoId")Long categoryTwoId, @Param("categoryThreeId")Long categoryThreeId);

    /**
     * 查询所有商品列表
     * @param inputvalue  商品编号和商品名称模糊查询
     * @param categoryOneId 商品一级科属
     * @param categoryTwoId 商品二级科属
     * @param categoryThreeId 商品三级科属
     * @return
     */
    List<AddHotCommOutput> find(@Param("inputvalue")String inputvalue, @Param("categoryOneId")Long categoryOneId,
                                @Param("categoryTwoId")Long categoryTwoId, @Param("categoryThreeId")Long categoryThreeId);

    /**
     * 保存所有热门商品
     * @param hotCommoditiesList
     */
    void saveAll(List<HotCommodities> hotCommoditiesList);

    /**
     * 清空热门商品表
     */
    void truncateHotCommodity();
}
