package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import so.sao.shop.supplier.domain.DictItem;

import java.util.List;
@Mapper
public interface DictItemMapper {

    List<String> selectBank();
    List<String> selectHangYe();
    List<DictItem> selectHangYeDict();
}