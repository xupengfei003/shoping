package so.sao.shop.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.NotificationDao;
import so.sao.shop.supplier.domain.Notification;
import so.sao.shop.supplier.pojo.input.NotificationInput;
import so.sao.shop.supplier.pojo.output.NotificationOutput;
import so.sao.shop.supplier.service.NotificationService;
import so.sao.shop.supplier.util.NumberGenerate;
import so.sao.shop.supplier.util.Ognl;
import so.sao.shop.supplier.util.PageTool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author gxy on 2017/8/15.
 */
@Service
public class NotificationServiceImpl implements NotificationService {

    @Autowired
    private NotificationDao notificationDao;

    /**
     * 批量为每个供应商插入消息通知
     *
     * @param notificationInput
     * @throws Exception
     */
    @Override
    public void createNotifi(NotificationInput notificationInput) throws Exception {
        List<Notification> notificationList = new ArrayList<>();
        //获取供应商Id集合
        List<Long> accountIds = notificationDao.getAccountIds();
        //标记
        String sigin = NumberGenerate.generateId();
        if (null != accountIds && accountIds.size() > 0) {
            accountIds.forEach(accountId -> {
                Notification notification = new Notification();
                notification.setAccountId(accountId);
                notification.setOrderId("");
                notification.setNotifiType(1); //消息类型:0订单，1系统
                notification.setNotifiDetail(notificationInput.getNotifiDetail());
                notification.setComment(notificationInput.getComment());
                notification.setCreatedAt(new Date());
                notification.setSigin(sigin);
                notification.setNotifiStatus(0); //消息状态:0未读,1已读
                notificationList.add(notification);
            });
            notificationDao.saveNotifications(notificationList);
        }
    }

    /**
     * 分页查询消息通知
     *
     * @param pageNum    pageNum
     * @param pageSize   pageSize
     * @param accountId  供应商ID
     * @param notifiType 消息类型
     * @return List<Notification>
     * @throws Exception Exception
     */
    @Override
    public List<Notification> search(Integer pageNum, Integer pageSize, Long accountId, Integer notifiType) throws Exception {
        PageTool.startPage(pageNum, pageSize);
        List<Notification> notificationList;
        if (Ognl.isEmpty(accountId)) {    //管理员
            notificationList = notificationDao.searchAdminNotifi();
        } else {    //供应商
            notificationList = notificationDao.searchSupplierNotifi(accountId, notifiType);
        }
        return notificationList;
    }

    /**
     * 获取未读消息总数
     *
     * @param accountId accountId
     * @return int
     * @throws Exception Exception
     */
    @Override
    public int getTotal(long accountId) throws Exception {
        return notificationDao.getTotal(accountId);
    }

    /**
     * 获取某条记录详细信息
     *
     * @param notifiId notifiId
     * @return NotificationOutput
     * @throws Exception Exception
     */
    @Override
    public NotificationOutput getNotificationById(Integer notifiId) throws Exception {
        NotificationOutput notificationOutput = new NotificationOutput();
        Notification notification = notificationDao.selectByPrimaryKey(notifiId);
        if (null != notification) {
            notificationOutput.setNotifiId(notification.getNotifiId());
            notificationOutput.setNotifiDetail(notification.getNotifiDetail());
            notificationOutput.setComment(notification.getComment());
            notificationOutput.setCreatedAt(notification.getCreatedAt());
            notificationOutput.setNotifiStatus(notification.getNotifiStatus());
        }
        return notificationOutput;
    }

    /**
     * 系统消息删除接口
     *
     * @param sigin sigin
     * @throws Exception Exception
     */
    @Override
    public void deleteBySigin(String sigin) throws Exception {
        notificationDao.deleteBySigin(sigin);
    }

    /**
     * 供应商更改消息状态-已读
     *
     * @param notifiId notifiId
     * @throws Exception Exception
     */
    @Override
    public void updateStatus(Integer notifiId) throws Exception {
        notificationDao.updateByPrimaryKey(notifiId);
    }

    /**
     * 消息跑马灯显示
     *
     * @param accountId accountId
     * @return String
     * @throws Exception Exception
     */
    @Override
    public String marqueeShow(Long accountId) throws Exception {
        return notificationDao.marqueeShow(accountId);
    }

    /**
     * 查询未读消息列表(供应商操作)
     *
     * @param accountId  供应商ID
     * @param notifiType 消息类型
     * @param count      返回多少条
     * @return List<Notification>
     * @throws Exception Exception
     */
    @Override
    public List<Notification> searchUnread(Long accountId, Integer notifiType, Integer count) throws Exception {
        List<Notification> dataList = notificationDao.searchUnread(accountId, notifiType, count == null ? 5 : count);
        if (null != dataList && dataList.size() > 0) {
            dataList.forEach(notification -> {
                String detail = notification.getNotifiDetail();
                if (notification.getNotifiType() == 0) { //订单消息 设定前端展示规则
                    if (null != detail && !"".equals(detail)) {
                        List<String> strings = Arrays.asList(detail.split(",")).stream().map(s -> s.trim()).collect(Collectors.toList());
                        if (null != strings && strings.size() > 0) {
                            notification.setTitle(strings.get(0));
                            notification.setContent(strings.get(1));
                        }
                    }
                }
            });
        }
        return dataList;
    }
}
