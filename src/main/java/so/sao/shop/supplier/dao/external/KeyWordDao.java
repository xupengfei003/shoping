package so.sao.shop.supplier.dao.external;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.external.KeyWord;

import java.util.List;

@Mapper
public interface KeyWordDao {

    void save( List<KeyWord> keyWords);

    void update(KeyWord keyWord);

    List<KeyWord> findAll();

    void delete();

    int countByKeyWordValue(@Param("keyWordValue") String keyWordValue);

}
