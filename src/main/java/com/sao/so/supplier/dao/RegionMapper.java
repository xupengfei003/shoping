package com.sao.so.supplier.dao;

import com.sao.so.supplier.domain.Region;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RegionMapper {

    List<Region> findListForRegion(@Param("parentId") Integer parentId, @Param("level") Integer level);

    List<String> selectShen();

    List<String> selectShiByShen(String name);

    List<String> selectQuByShi(String name);
}