package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import so.sao.shop.supplier.domain.QualificationImage;
import so.sao.shop.supplier.pojo.vo.QualificationImagesVo;

import java.util.List;

/**
 * Created by liugang on 2017/10/11.
 * 供应商资质图片DAO层
 */
@Mapper
public interface QualificationImageDao {

    /**
     * 添加资质图片
     * @param imgs
     * @return
     */
    boolean save(List<QualificationImage> imgs);

    /**
     * 根据供应商资质ID删除资质图片（更新资质图片删除状态位）
     * @param qualificationId 供应商资质ID
     * @return
     */
    boolean delete(Long qualificationId);
}
