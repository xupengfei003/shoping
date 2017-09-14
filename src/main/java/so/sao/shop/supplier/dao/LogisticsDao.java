package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 物流信息转化(com转中文)
 * </p>
 *
 * @author 透云-中软-西安项目组-zhenhai.zheng
 * @since 2017年9月14日 14:51:15
 **/
public interface LogisticsDao {
    /**
     * 将传过来的快递公司英文code转化成中文
     * @param comStr 快递公司code
     * @return 快递公司中文
     */
    String findCompanyNameByCom(@Param("comStr") String comStr);
}
