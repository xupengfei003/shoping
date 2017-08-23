package so.sao.shop.supplier.service;

import so.sao.shop.supplier.domain.Notification;
import so.sao.shop.supplier.pojo.input.NotificationInput;
import so.sao.shop.supplier.pojo.output.NotificationOutput;

import java.util.List;

/**
 * @author gxy on 2017/8/15.
 */
public interface NotificationService {
    /**
     * 批量为每个供应商插入消息通知
     * @param notificationInput notificationInput
     * @throws Exception Exception
     */
    void createNotifi(NotificationInput notificationInput) throws Exception;

    /**
     * 分页查询消息通知
     * @param pageNum pageNum
     * @param pageSize pageSize
     * @param accountId 供应商ID
     * @param notifiType 消息类型
     * @return List<NotificationOutput>
     * @throws Exception Exception
     */
    List<Notification> search(Integer pageNum, Integer pageSize, Long accountId, Integer notifiType) throws Exception;

    /**
     * 获取未读消息总数
     * @param accountId 供应商ID
     * @return int
     * @throws Exception Exception
     */
    int getTotal(long accountId) throws Exception;

    /**
     * 获取某条记录详细信息
     * @param notifiId notifiId
     * @return NotificationOutput
     * @throws Exception Exception
     */
    NotificationOutput getNotificationById(Integer notifiId) throws Exception;

    /**
     * 系统消息删除接口
     * @param sigin sigin
     * @throws Exception Exception
     */
    void deleteBySigin(String sigin) throws Exception;

    /**
     * 供应商更改消息状态-已读
     * @param notifiId notifiId
     * @throws Exception Exception
     */
    void updateStatus(Integer notifiId) throws Exception;

    /**
     * 消息跑马灯显示
     * @return String
     * @throws Exception Exception
     */
    String marqueeShow() throws Exception;

    /**
     * 查询未读消息列表(供应商操作)
     * @param accountId 供应商ID
     * @param notifiType 消息类型
     * @param count 返回多少条
     * @return List<NotificationOutput>
     * @throws Exception Exception
     */
    List<Notification> searchUnread(Long accountId, Integer notifiType, Integer count) throws Exception;
}
