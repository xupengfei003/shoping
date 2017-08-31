package so.sao.shop.supplier.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.Notification;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.NotificationInput;
import so.sao.shop.supplier.pojo.output.NotificationOutput;
import so.sao.shop.supplier.service.NotificationService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author gxy on 2017/8/15.
 */
@RestController
@RequestMapping("/notification")
@Api(description = "消息管理-所有接口【负责人：郭兴业】")
public class NotificationController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NotificationService notificationService;

    /**
     * 管理员为每个供应商插入消息通知
     *
     * @param request           request
     * @param notificationInput notificationInput
     * @return Result
     */
    @PostMapping("/createNotifi")
    @ApiOperation(value = "消息发送", notes = "消息发送")
    public Result createNotifi(HttpServletRequest request, @Valid NotificationInput notificationInput) throws Exception {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        //判断是否为管理员
        if (!Constant.ADMIN_STATUS.equals(user.getIsAdmin())) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        //管理员给每个供应商添加消息通知
        notificationService.createNotifi(notificationInput);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS);
    }

    /**
     * 分页查询消息通知 区分管理员/供应商
     *
     * @param request    request
     * @param pageNum    pageNum
     * @param pageSize   pageSize
     * @param notifiType 消息类型
     * @return Result
     */
    @GetMapping("/getPage")
    @ApiOperation(value = "分页查询消息通知", notes = "分页查询消息通知")
    public Result search(HttpServletRequest request, Integer pageNum, Integer pageSize, Integer notifiType) throws Exception {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        //判断消息类型是否有值
        if (null == notifiType) {
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }
        List<Notification> dataList;
        //判断是否为管理员
        if (!Constant.ADMIN_STATUS.equals(user.getIsAdmin())) { //非管理员 accountId 必须传
            dataList = notificationService.search(pageNum, pageSize, user.getAccountId(), notifiType);
        } else { //管理员 accountId-传null,notifiType-传1 系统消息
            dataList = notificationService.search(pageNum, pageSize, null, 1);
        }
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, new PageInfo<>(dataList));
    }

    /**
     * 获取未读消息总数
     *
     * @param request request
     * @return Result
     */
    @GetMapping("/getTotal")
    @ApiOperation(value = "获取未读消息总数", notes = "获取未读消息总数")
    public Result getTotal(HttpServletRequest request) throws Exception {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        int total = notificationService.getTotal(user.getAccountId());
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, total);
    }

    /**
     * 查询未读消息列表(供应商操作)
     * TODO 此接口改变2017-08-23描述 点击🔔获取订单/系统前五条消息 不区分已读未读
     *
     * @param request    request
     * @param notifiType 消息通知类型 0-订单 1-系统
     * @param count      返回多少条未读消息
     * @return Result
     */
    @GetMapping("/searchUnread")
    @ApiOperation(value = "查询未读消息列表(供应商操作)", notes = "查询未读消息列表(供应商操作)")
    public Result searchUnread(HttpServletRequest request, Integer notifiType, Integer count) throws Exception {
        //判断是否登陆
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        //判断消息类型是否有值
        if (null == notifiType) {
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }
        List<Notification> dataList = notificationService.searchUnread(user.getAccountId(), notifiType, count);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, dataList);
    }

    /**
     * 获取某条记录详细信息
     *
     * @param notifiId notifiId
     * @return Result
     */
    @GetMapping("/getNotificationById/{notifiId}")
    @ApiOperation(value = "获取某条记录详细信息", notes = "获取某条记录详细信息")
    public Result getNotificationById(HttpServletRequest request, @PathVariable Integer notifiId) throws Exception {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        NotificationOutput notificationOutput = notificationService.getNotificationById(notifiId);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, notificationOutput);
    }

    /**
     * 系统消息删除接口
     *
     * @param request request
     * @param sigin   系统消息标识
     * @return Result
     */
    @PostMapping("/deleteBySigin")
    @ApiOperation(value = "系统消息删除接口", notes = "系统消息删除接口(管理员操作)")
    public Result deleteBySigin(HttpServletRequest request, String sigin) throws Exception {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        //判断消息类型是否有值
        if (null == sigin || "".equals(sigin)) {
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }
        //判断是否为管理员
        if (!Constant.ADMIN_STATUS.equals(user.getIsAdmin())) {
            return Result.fail(Constant.MessageConfig.ADMIN_AUTHORITY_EERO);
        }
        notificationService.deleteBySigin(sigin);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS);
    }

    /**
     * 供应商更改消息状态-已读
     *
     * @param notifiId 消息ID
     * @return Result
     */
    @PostMapping("/updateStatus/{notifiId}")
    @ApiOperation(value = "供应商更改消息状态", notes = "供应商更改消息状态")
    public Result updateStatus(@PathVariable Integer notifiId) throws Exception {
        notificationService.updateStatus(notifiId);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS);
    }

    /**
     * 消息跑马灯显示
     *
     * @return Result
     */
    @GetMapping("/marqueeShow")
    @ApiOperation(value = "消息跑马灯显示", notes = "消息跑马灯显示")
    public Result marqueeShow() throws Exception {
        String show = notificationService.marqueeShow();
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, show);
    }
}
