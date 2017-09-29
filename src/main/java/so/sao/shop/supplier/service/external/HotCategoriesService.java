package so.sao.shop.supplier.service.external;

import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.domain.external.HotCategories;
import so.sao.shop.supplier.pojo.Result;

import java.util.List;

public interface HotCategoriesService {

    /**
     * 查询热门分类集合
     * @param pageNum 当前页号
     * @param pageSize 页面大小/记录条数
     * @return Result
     */
    Result searchHotCategories(Integer pageNum, Integer pageSize);

    /**
     * 更换热门分类图片
     * @param file 分类图片
     * @return Result
     */
    Result updateIcon(MultipartFile file);

    /**
     * 批量添加热门分类
     * @param hotCategories  热门分类集合
     * @return
     */
    Result saveHotCategories(List<HotCategories> hotCategories);
}
