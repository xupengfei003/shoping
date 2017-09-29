package so.sao.shop.supplier.service.external;

import so.sao.shop.supplier.domain.external.KeyWord;
import so.sao.shop.supplier.pojo.Result;

import java.util.List;

/**
 * 搜索关键字Service
 */
public interface KeyWordService {

    /**
     * 添加关键字配置
     *
     * @param keyWords KeyWord对象集合
     * @return Result添加结果
     */
    Result saveKeyWord(List<KeyWord> keyWords);

    /**
     * 编辑关键字
     * @param keyWord 关键字对象
     * @return 修改结果
     */
    Result updateKeyWord(KeyWord keyWord);

    /**
     * 查询所有关键字配置
     * @param pageNum
     * @param pageSize
     * @return 关键字集合
     */
    Result searchKeyWords(Integer pageNum,Integer pageSize);
}
