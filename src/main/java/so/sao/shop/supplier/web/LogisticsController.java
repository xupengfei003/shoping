package so.sao.shop.supplier.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.LogisticsService;

import java.util.Map;

/**
 * Created by wyy on 2017/8/16.
 */
@RestController
@RequestMapping(value = "/logistics")
public class LogisticsController {

    @Autowired
    private LogisticsService logisticsService;

    @GetMapping(value = "/{num}")
    public Result<Object> findLogisticInfo(@PathVariable("num")String num){
        return logisticsService.findLogisticInfo(num);
    }
    /**
     * 管理员确认送达
     *
     * @param map 订单ID
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/artificialAutomaticRecive",method = RequestMethod.POST)
    public Result artificialAutomaticReceive(@RequestBody Map<String,Object> map) throws Exception {
        String orderId = String.valueOf(map.get("orderId"));
        if(null == orderId){
            return Result.fail(Constant.MessageConfig.MSG_FAILURE,"请选择订单");
        }
        Map<String,Object> resultMap = logisticsService.insertReceivedOrder(orderId);
        if("fail".equals(resultMap.get("flag"))){
            return Result.fail(Constant.MessageConfig.MSG_FAILURE,resultMap.get("msg"));
        }
        return Result.success("该订单七日后自动确认收货");
    }
}
