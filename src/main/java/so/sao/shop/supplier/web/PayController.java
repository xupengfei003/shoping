package so.sao.shop.supplier.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.PayInput;
import so.sao.shop.supplier.service.PayService;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

/**
 * Created by bzh on 2017/8/15.
 */
@RestController
@RequestMapping("/pay")
public class PayController {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private PayService payService;
    @RequestMapping(value = "/create",method = RequestMethod.POST)
    public Result create(@Valid PayInput payInput, BindingResult result, String sign)  {
        Result output = new Result();
        //判断验证是否通过。true 未通过  false通过
        if (result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError error : list) {
                output.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
                output.setMessage(error.getDefaultMessage());
            }
        }else{
            try {
                if (payService.savePurchase(sign,payInput)){
                    output.setCode(Constant.CodeConfig.CODE_SUCCESS);
                    output.setMessage(Constant.MessageConfig.MSG_SUCCESS);
                } else {
                    output.setMessage(Constant.MessageConfig.MSG_FAILURE);
                    output.setCode(Constant.CodeConfig.CODE_FAILURE);
                }
            } catch (Exception e) {
                logger.error("系统异常",e);
                output.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
                output.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
            }
        }
        return output;
    }
}
