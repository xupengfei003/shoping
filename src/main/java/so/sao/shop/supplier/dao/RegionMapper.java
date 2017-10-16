package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.Region;

import java.util.List;

public interface RegionMapper {

    List<Region> findListForRegion(@Param("parentId") Integer parentId, @Param("level") Integer level);

    List<String> selectShen();

    List<String> selectShiByShen(String name);

    List<String> selectQuByShi(String name);

    List<Region> getAllRegion();
}