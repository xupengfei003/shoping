package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import so.sao.shop.supplier.domain.CommImgeTmp;

import java.util.List;

@Mapper
public interface CommImgeTmpDao {

    /**
     * 新增
     */
    void batchSave(List<CommImgeTmp> commImgeTmps);
}
