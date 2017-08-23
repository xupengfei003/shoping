package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.FeedbackService;

import javax.servlet.http.HttpServletRequest;

/**
 * 供应商反馈管理
 *
 * @author gxy on 2017/8/14.
 */
@RestController
@RequestMapping("/feedback")
@Api(description = "反馈管理-所有接口")
public class FeedbackController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FeedbackService feedbackService;

    /**
     * 提交反馈
     *
     * @param suggest
     * @return baseResult
     */
    @PostMapping("/createFeedback")
    @ApiOperation(value = "提交反馈", notes = "提交反馈")
    public Result createFeedback(HttpServletRequest request, String suggest) {
        Result result = new Result();
        //验证供应商是否登陆并取出accountId
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if (null == user) {   //验证用户是否登陆
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
            return result;
        }
        if (null == suggest || "".equals(suggest)) {
            result.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
            result.setMessage(Constant.MessageConfig.MSG_NOT_EMPTY);
            return result;
        }
        try {
            feedbackService.createFeedback(user.getAccountId(), suggest);
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
        } catch (Exception e) {
            logger.error("系统异常", e.getMessage());
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage(Constant.MessageConfig.MSG_FAILURE);
            return result;
        }
        return result;
    }
}
