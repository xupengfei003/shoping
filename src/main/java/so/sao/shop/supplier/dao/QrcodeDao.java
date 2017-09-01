package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.Qrcode;

import java.util.Date;
import java.util.List;

/**
 * 二维码接口
 *
 * @author hengle.yang
 * @since 2017/8/11 10:07
 */
@Mapper
public interface QrcodeDao {

    /**
     * 保存二维码信息
     *
     * @param qrcode 二维码对象
     * @return
     */
    public int save(Qrcode qrcode);

    /**
     * 批量保存二维码信息
     *
     * @param qrcodes 二维码对象列表
     * @return
     */
    public void saveQrcodes(List<Qrcode> qrcodes);


    /**
     * 修改二维码状态
     *
     * 根据订单编号修改二维码状态
     *
     * @param orderId 订单编号
     * @param status 状态
     * @param disabledAt 失效时间
     * @param updatedAt 修改时间
     * @return 成功返回true，失败返回false
     */
    boolean updateStatus(@Param("orderId") String orderId, @Param("status")Integer status,
                         @Param("disabledAt")Date disabledAt, @Param("updatedAt")Date updatedAt);

    /**
     * 根据订单编号查询对应的二维码
     *
     * @param orderId 订单编号
     * @return 返回二维码对象集合
     */
    public List<Qrcode> findQrcodeByOrderId(String orderId);
}
