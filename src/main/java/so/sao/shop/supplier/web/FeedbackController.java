package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.FeedbackService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 供应商反馈管理
 *
 * @author gxy on 2017/8/14.
 */
@RestController
@RequestMapping("/feedback")
@Api(description = "反馈管理-所有接口 【负责人:郭兴业】")
public class FeedbackController {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private FeedbackService feedbackService;

    /**
     * 提交反馈
     *
     * @param map
     * @return baseResult
     */
    @PostMapping("/createFeedback")
    @ApiOperation(value = "提交反馈", notes = "提交反馈")
    public Result createFeedback(HttpServletRequest request, @RequestBody Map map) throws Exception {
        //验证供应商是否登陆并取出accountId
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if (null == user) {   //验证用户是否登陆
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        String suggest = (String) map.get("suggest");
        if (null == suggest || "".equals(suggest)) {
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }
        feedbackService.createFeedback(user.getAccountId(), suggest);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS);
    }
}
