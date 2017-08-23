package so.sao.shop.supplier.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
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
@Api(description = "消息管理-所有接口")
public class NotificationController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private NotificationService notificationService;

    /**
     * 管理员为每个供应商插入消息通知
     *
     * @param request           request
     * @param notificationInput notificationInput
     * @param br                BindingResult
     * @return Result
     */
    @PostMapping("/createNotifi")
    @ApiOperation(value = "消息发送", notes = "消息发送")
    public Result createNotifi(HttpServletRequest request, @Valid NotificationInput notificationInput, BindingResult br) {
        Result result = new Result();
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (null == user) {
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
            return result;
        }
        //判断是否为管理员
        if (!Constant.ADMIN_STATUS.equals(user.getIsAdmin())) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage(Constant.MessageConfig.ADMIN_AUTHORITY_EERO);
            return result;
        }
        if (br.hasErrors()) {
            List<ObjectError> list = br.getAllErrors();
            for (ObjectError error : list) {
                result.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
                result.setMessage(error.getDefaultMessage());
                return result;
            }
        } else {
            try {
                //管理员给每个供应商添加消息通知
                notificationService.createNotifi(notificationInput);
                result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
                result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setCode(Constant.CodeConfig.CODE_FAILURE);
                result.setMessage(Constant.MessageConfig.MSG_FAILURE);
                return result;
            }
        }
        return result;
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
    public Result search(HttpServletRequest request, Integer pageNum, Integer pageSize, Integer notifiType) {
        Result result = new Result();
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (null == user) {
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
            return result;
        }
        //判断消息类型是否有值
        if (null == notifiType) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage(Constant.MessageConfig.MSG_NOT_EMPTY);
            return result;
        }
        List<Notification> dataList;
        //判断是否为管理员
        if (!Constant.ADMIN_STATUS.equals(user.getIsAdmin())) { //非管理员 accountId 必须传
            try {
                dataList = notificationService.search(pageNum, pageSize, user.getAccountId(), notifiType);
                result.setCode(Constant.CodeConfig.CODE_SUCCESS);
                result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
                result.setData(new PageInfo<>(dataList));
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setCode(Constant.CodeConfig.CODE_FAILURE);
                result.setMessage(Constant.MessageConfig.MSG_FAILURE);
                return result;
            }
        } else { //管理员 accountId-传null,notifiType-传1 系统消息
            try {
                dataList = notificationService.search(pageNum, pageSize, null, 1);
                result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
                result.setCode(Constant.CodeConfig.CODE_SUCCESS);
                result.setData(new PageInfo<>(dataList));
            } catch (Exception e) {
                logger.error(e.getMessage());
                result.setMessage(Constant.MessageConfig.MSG_FAILURE);
                result.setCode(Constant.CodeConfig.CODE_FAILURE);
                return result;
            }
        }
        return result;
    }

    /**
     * 获取未读消息总数
     *
     * @param request request
     * @return Result
     */
    @GetMapping("/getTotal")
    @ApiOperation(value = "获取未读消息总数", notes = "获取未读消息总数")
    public Result getTotal(HttpServletRequest request) {
        Result result = new Result();
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (null == user) {
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
            return result;
        }
        try {
            int total = notificationService.getTotal(user.getAccountId());
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
            result.setData(total);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage(Constant.MessageConfig.MSG_FAILURE);
            return result;
        }
        return result;
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
    public Result searchUnread(HttpServletRequest request, Integer notifiType, Integer count) {
        Result result = new Result();
        //判断是否登陆
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if (null == user) {
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
            return result;
        }
        //判断消息类型是否有值
        if (null == notifiType) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage(Constant.MessageConfig.MSG_NOT_EMPTY);
            return result;
        }
        try {
            List<Notification> dataList = notificationService.searchUnread(user.getAccountId(), notifiType, count);
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
            result.setData(dataList);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage(Constant.MessageConfig.MSG_FAILURE);
            return result;
        }
        return result;
    }

    /**
     * 获取某条记录详细信息
     *
     * @param notifiId notifiId
     * @return Result
     */
    @GetMapping("/getNotificationById/{notifiId}")
    @ApiOperation(value = "获取某条记录详细信息", notes = "获取某条记录详细信息")
    public Result getNotificationById(HttpServletRequest request, @PathVariable Integer notifiId) {
        Result result = new Result();
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (null == user) {
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
            return result;
        }
        try {
            NotificationOutput notificationOutput = notificationService.getNotificationById(notifiId);
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
            result.setData(notificationOutput);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage(Constant.MessageConfig.MSG_FAILURE);
            return result;
        }
        return result;
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
    public Result deleteBySigin(HttpServletRequest request, String sigin) {
        Result result = new Result();
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (null == user) {
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
            return result;
        }
        //判断消息类型是否有值
        if (null == sigin || "".equals(sigin)) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage(Constant.MessageConfig.MSG_NOT_EMPTY);
            return result;
        }
        //判断是否为管理员
        if (!Constant.ADMIN_STATUS.equals(user.getIsAdmin())) {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage(Constant.MessageConfig.ADMIN_AUTHORITY_EERO);
            return result;
        }
        try {
            notificationService.deleteBySigin(sigin);
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage(Constant.MessageConfig.MSG_FAILURE);
            return result;
        }
        return result;
    }

    /**
     * 供应商更改消息状态-已读
     *
     * @param notifiId 消息ID
     * @return Result
     */
    @PostMapping("/updateStatus/{notifiId}")
    @ApiOperation(value = "供应商更改消息状态", notes = "供应商更改消息状态")
    public Result updateStatus(@PathVariable Integer notifiId) {
        Result result = new Result();
        try {
            notificationService.updateStatus(notifiId);
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage(Constant.MessageConfig.MSG_FAILURE);
            return result;
        }
        return result;
    }

    /**
     * 消息跑马灯显示
     *
     * @return Result
     */
    @GetMapping("/marqueeShow")
    @ApiOperation(value = "消息跑马灯显示", notes = "消息跑马灯显示")
    public Result marqueeShow() {
        Result result = new Result();
        try {
            String show = notificationService.marqueeShow();
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
            result.setData(show);
        } catch (Exception e) {
            logger.error(e.getMessage());
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage(Constant.MessageConfig.MSG_FAILURE);
            return result;
        }
        return result;
    }
}
