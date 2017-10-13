package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
@Api(value = "物流")
public class LogisticsController {

    @Autowired
    private LogisticsService logisticsService;

    @ApiOperation(value = "查询物流信息" ,notes = "根据物流单号查询物流信息")
    @GetMapping(value = "/{num}")
    public Result<Object> findLogisticInfo(@PathVariable("num")String num){
        return logisticsService.findLogisticInfo(num);
    }
    /**
     * 管理员确认送达
     *
     * @param orderId 订单ID
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "管理员确认送达" ,notes = "管理员确认送达订单【白治华】")
    @PostMapping(value = "/artificialAutomaticReceive")
    public Result artificialAutomaticReceive(String orderId) throws Exception {
        Map<String,Object> resultMap = logisticsService.insertReceivedOrder(orderId);
        if("fail".equals(resultMap.get("flag"))){
            return Result.fail(Constant.MessageConfig.MSG_FAILURE,resultMap.get("msg"));
        }
        return Result.success("该订单七日后自动确认收货");
    }
}
