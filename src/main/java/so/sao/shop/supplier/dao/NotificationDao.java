package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.Notification;

import java.util.List;

/**
 * @author gxy on 2017/8/15.
 */
public interface NotificationDao {
    /**
     * 获取供应商Id集合
     * @return List<Long>
     */
    List<Long> getAccountIds();

    /**
     * 批量为每个供应商插入消息通知
     * @param notificationList notificationList
     */
    void saveNotifications(List<Notification> notificationList);

    /**
     * 分页查询供应商消息列表
     * @param accountId accountId
     * @param notifiType notifiType
     * @return List<Notification>
     */
    List<Notification> searchSupplierNotifi(@Param("accountId") Long accountId, @Param("notifiType") Integer notifiType);

    /**
     * 分页查询管理员消息列表
     * @return List<Notification>
     */
    List<Notification> searchAdminNotifi();

    /**
     * 获取未读消息总数
     * @param accountId accountId
     * @return int
     */
    int getTotal(@Param("accountId") long accountId);

    /**
     * 获取某条记录详细信息
     * @param notifiId notifiId
     * @return Notification
     */
    Notification selectByPrimaryKey(Integer notifiId);

    /**
     * 系统消息删除接口
     * @param sigin sigin
     */
    void deleteBySigin(String sigin);

    /**
     * 供应商更改消息状态-已读
     * @param notifiId notifiId
     */
    void updateByPrimaryKey(Integer notifiId);

    /**
     * 插入一条消息通知
     * @param notification notification
     */
    void insert(Notification notification);

    /**
     * 消息跑马灯显示
     * @param accountId accountId
     * @return String
     */
    String marqueeShow(@Param("accountId") Long accountId);

    /**
     * 查询未读消息列表
     * @param accountId 供应商ID
     * @param notifiType 消息类型 0订单,1系统
     * @param count 未读消息数
     * @return List<Notification>
     */
    List<Notification> searchUnread(@Param("accountId") Long accountId, @Param("notifiType") Integer notifiType, @Param("count") Integer count);
}
