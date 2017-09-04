package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.LogisticsService;

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
}
