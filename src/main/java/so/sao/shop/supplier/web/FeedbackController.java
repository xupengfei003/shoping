package so.sao.shop.supplier.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.FeedbackService;
import so.sao.shop.supplier.util.Ognl;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 供应商反馈管理
 *
 * @author gxy on 2017/8/14.
 */
@RestController
@RequestMapping("/feedback")
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    /**
     * 提交反馈
     *
     * @param map
     * @return baseResult
     */
    @PostMapping("/createFeedback")
    public Result createFeedback(HttpServletRequest request, @RequestBody Map map) throws Exception {
        //验证供应商是否登陆并取出accountId
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if (Ognl.isNull(user)) {   //验证用户是否登陆
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        String suggest = (String) map.get("suggest");
        if (Ognl.isEmpty(suggest)) {
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }
        feedbackService.createFeedback(user.getAccountId(), suggest);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS);
    }
}
