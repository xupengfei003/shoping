package so.sao.shop.supplier.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.PurchaseSelectInput;
import so.sao.shop.supplier.pojo.vo.PurchasesVo;
import so.sao.shop.supplier.service.PurchaseService;
import so.sao.shop.supplier.util.DataCompare;
import so.sao.shop.supplier.util.DateUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by acer on 2017/10/11.
 */
@RestController
@RequestMapping("/order")
@Api(description = "管理员订单类-所有接口")
public class AdminPurchaseController {
    @Resource
    private PurchaseService purchaseService;

    /**
     * 管理员订单列表查询接口
     *
     * @param pageNum             页码
     * @param rows                每页显示行数
     * @param purchaseSelectInput 封装了检索条件
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "管理员获取订单列表", notes = "管理员获取所有订单【负责人：白治华】")
    @GetMapping("/adminSearch")
    public Result adminSearch(Integer pageNum, Integer rows, PurchaseSelectInput purchaseSelectInput) throws Exception {
        purchaseSelectInput.setStoreId(null);
        //判断时间格式
        List<String> dateList = Arrays.asList(purchaseSelectInput.getBeginDate(), purchaseSelectInput.getEndDate(),
                purchaseSelectInput.getOrderReceiveBeginTime(), purchaseSelectInput.getOrderReceiveEndTime(),
                purchaseSelectInput.getOrderPaymentBeginTime(), purchaseSelectInput.getOrderPaymentEndTime());
        for (String dateInput : dateList) {
            if (!verifyDate(dateInput)) {
                return Result.fail(Constant.MessageConfig.MSG_DATE_INPUT_FORMAT_ERROR);
            }
        }
        //对比开始时间和结束时间
        if (restrictDate(dateList)) return Result.fail(Constant.MessageConfig.DateNOTLate);
        //对比开始金额和结束金额
        if (DataCompare.compareMoney(purchaseSelectInput.getBeginMoney(), purchaseSelectInput.getEndMoney()))
            return Result.fail(Constant.MessageConfig.MoneyNOTLate);
        //查询订单
        if (rows == null || rows <= 0) {
            rows = 10;
        }
        if (pageNum == null || pageNum <= 0) {
            pageNum = 1;
        }
        PageInfo<PurchasesVo> pageInfo = purchaseService.searchOrders(pageNum, rows, purchaseSelectInput);

        return Result.success(Constant.MessageConfig.MSG_SUCCESS, pageInfo);
    }

    /**
     * 管理员导出订单列表
     *
     * @param request
     * @param response
     * @param pageNum             页码
     * @param pageSize            每页显示行数
     * @param purchaseSelectInput 封装了检索条件
     * @return
     * @throws Exception
     */
    @GetMapping(value = "/adminExport")
    @ApiOperation(value = "管理员POI批量导出订单列表", notes = "管理员POI批量导出订单列表【负责人：白治华】")
    public Result adminExport(HttpServletRequest request, HttpServletResponse response, String pageNum, Integer pageSize,
                              PurchaseSelectInput purchaseSelectInput) throws Exception {
        Long accountId = null;
        //判断时间格式
        List<String> dateList = Arrays.asList(purchaseSelectInput.getBeginDate(), purchaseSelectInput.getEndDate(),
                purchaseSelectInput.getOrderReceiveBeginTime(), purchaseSelectInput.getOrderReceiveEndTime(),
                purchaseSelectInput.getOrderPaymentBeginTime(), purchaseSelectInput.getOrderPaymentEndTime());
        for (String dateInput : dateList) {
            if (!verifyDate(dateInput)) {
                return Result.fail(Constant.MessageConfig.MSG_DATE_INPUT_FORMAT_ERROR);
            }
        }
        //对比开始时间和结束时间
        if (restrictDate(dateList)) return Result.fail(Constant.MessageConfig.DateNOTLate);
        //对比开始金额和结束金额
        if (DataCompare.compareMoney(purchaseSelectInput.getBeginMoney(), purchaseSelectInput.getEndMoney()))
            return Result.fail(Constant.MessageConfig.MoneyNOTLate);
        purchaseService.exportExcel(request, response, pageNum, pageSize, accountId, purchaseSelectInput);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS);
    }

    //验证时间格式
    private boolean verifyDate(String inputDate) {
        //订单创建时间
        if (!StringUtils.isEmpty(inputDate)) {
            return DateUtil.isDate(inputDate);
        }
        return true;
    }

    //对比时间大小
    private boolean restrictDate(List<String> dateList) throws ParseException {
        boolean flag = true;
        int i = 0;
        while (flag) {
            if (DataCompare.compareDate(dateList.get(i), dateList.get(++i))) {
                return flag;
            }
            i++;
            if (i == 6) {
                flag = false;
            }
        }
        return flag;
    }
}
