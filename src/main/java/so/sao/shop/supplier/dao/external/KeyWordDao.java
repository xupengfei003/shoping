package so.sao.shop.supplier.dao.external;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.external.KeyWord;

import java.util.List;

@Mapper
public interface KeyWordDao {

    /**
     * 保存关键字列表
     * @param keyWords
     */
    void save(List<KeyWord> keyWords);

    /**
     * 根据id编辑一条关键字
     * @param keyWord
     */
    void update(KeyWord keyWord);

    /**
     * 查询关键字列表
     * @return
     */
    List<KeyWord> findAll();

    /**
     * 清空关键字表
     */
    void delete();

    /**
     * 校验关键字名称是否存在
     * @param keyWordValue
     * @return
     */
    int countByKeyWordValue(@Param("keyWordValue") String keyWordValue);

}
