package so.sao.shop.supplier.web;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.input.AccountPurchaseInput;
import so.sao.shop.supplier.pojo.input.PurchaseInput;
import so.sao.shop.supplier.pojo.input.PurchaseSelectInput;
import so.sao.shop.supplier.pojo.output.*;
import so.sao.shop.supplier.service.PurchaseService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 订单信息  controller 接口
 * </p>
 *
 * @author 透云-中软-西安项目组
 * @since 2017-07-19
 */
@RestController
@RequestMapping("/order")
@Api(description = "订单类-所有接口")
public class PurchaseController {
    @Resource
    private PurchaseService purchaseService;

    /**
     * 保存订单
     *
     * @param purchase 订单对象
     * @return
     */
    @RequestMapping(value = "/createPurchase", method = RequestMethod.POST)
    @ApiOperation(value = "生成订单")
    public PurchaseOutput createPurchase(@Valid PurchaseInput purchase, BindingResult result) {
        PurchaseOutput output = new PurchaseOutput();
        //判断验证是否通过。true 未通过  false通过
        if(result.hasErrors()){
            List<ObjectError> list = result.getAllErrors();
            for(ObjectError error : list){
                output.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
                output.setMessage(error.getDefaultMessage());
            }
        } else{
            try {
                Map<String, Object> resMap = purchaseService.savePurchase(purchase);
                Integer status = Integer.parseInt(String.valueOf(resMap.get("status")));
                if (status == 1) {
                    output.setCode(Constant.CodeConfig.CODE_SUCCESS);
                    output.setMessage(Constant.MessageConfig.MSG_SUCCESS);
                } else {
                    output.setCode(Constant.CodeConfig.CODE_FAILURE);
                    output.setMessage(Constant.MessageConfig.MSG_FAILURE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                output.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
                output.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
            }
        }
        return output;
    }

    /**
     * 根据订单ID获取订单详情
     *
     * @param orderId orderId
     * @return PurchaseOutput
     */
    @RequestMapping(value = "/purchase/{orderId}", method = RequestMethod.GET)
    @ApiOperation(value = "获取订单详情", notes = "获取订单详情")
    public PurchaseInfoOutput findById(@PathVariable String orderId) {
        PurchaseInfoOutput output = new PurchaseInfoOutput();
        try {
            output = purchaseService.findById(orderId);
            output.setCode(Constant.CodeConfig.CODE_SUCCESS);
            output.setMessage(Constant.MessageConfig.MSG_SUCCESS);
        } catch (Exception e) {
            output.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
            output.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
            return output;
        }
        return output;
    }

    /**
     * POI批量导出订单列表
     *
     * @param request  request
     * @param response response
     * @param orderIds  orderIds
     * @param pageNum  pageNum
     * @param pageSize pageSize
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ApiOperation(value = "POI批量导出订单列表", notes = "POI批量导出订单列表")
    @ResponseBody
    public BaseResult exportExcel(HttpServletRequest request, HttpServletResponse response, String orderIds, Integer pageNum, Integer pageSize) {
        BaseResult result = new BaseResult();
        try {
            purchaseService.exportExcel(request, response, orderIds, pageNum, pageSize);
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
        } catch (Exception e) {
            result.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
            result.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
        }

        return result;
    }

    /**
     * 查询全部订单列表
     *
     * @param
     * @param
     * @return
     */

    @GetMapping(value = "/search")
    @ApiOperation(value = "查询订单列表", notes = "*")
    public PurchaseSelectOutput search(Integer pageNum, Integer rows, PurchaseSelectInput purchaseSelectInput) {
        PurchaseSelectOutput purchaseSelectOutputList = new PurchaseSelectOutput();
        try {
            if (pageNum != null && rows != null) {
                purchaseSelectOutputList = purchaseService.searchOrders(pageNum, rows, purchaseSelectInput);
            } else {
                purchaseSelectOutputList = purchaseService.searchOrders(1, 10, purchaseSelectInput);
            }
            if (purchaseSelectOutputList.getPageInfo().getSize() > 0) {
                purchaseSelectOutputList.setCode(Constant.CodeConfig.CODE_SUCCESS);
                purchaseSelectOutputList.setMessage(Constant.MessageConfig.MSG_SUCCESS);
            } else {
                purchaseSelectOutputList.setCode(Constant.CodeConfig.CODE_NOT_FOUND_RESULT);
                purchaseSelectOutputList.setMessage(Constant.MessageConfig.MSG_NOT_FOUND_RESULT);
            }
        } catch (Exception e) {
            purchaseSelectOutputList.setCode(Constant.CodeConfig.CODE_FAILURE);
            purchaseSelectOutputList.setMessage(Constant.MessageConfig.MSG_FAILURE);
        }

        return purchaseSelectOutputList;
    }

    /**
     * 更改订单状态
     *
     * @param orderId
     * @param orderStatus
     * @param receiveMethod
     * @param name
     * @param number
     * @return
     */
    @RequestMapping(value = "/update/{orderId}/orderStatus", method = RequestMethod.POST)
    @ApiOperation(value = "更改订单状态", notes = "")
    public BaseResult update(String orderId, Integer orderStatus, Integer receiveMethod, String name, String number) {
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(Constant.CodeConfig.CODE_SUCCESS);
        baseResult.setMessage(Constant.MessageConfig.MSG_SUCCESS);
        try {
            if (orderId != null && orderStatus != null) {
                boolean flag = purchaseService.updateOrder(orderId, orderStatus, receiveMethod, name, number);
                if (!flag) {
                    baseResult.setCode(Constant.CodeConfig.CODE_FAILURE);
                    baseResult.setMessage(Constant.MessageConfig.MSG_FAILURE);
                }
            } else {
                baseResult.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
                baseResult.setMessage(Constant.MessageConfig.MSG_NOT_EMPTY);
            }
        } catch (Exception e) {
            baseResult.setCode(Constant.CodeConfig.CODE_FAILURE);
            baseResult.setMessage(Constant.MessageConfig.MSG_FAILURE);
        }
        return baseResult;
    }

    /**
     * 删除订单，批量删除和单个删除
     *
     * @param orderIds
     * @return BaseResult
     */
    @RequestMapping(value = "/delete/purchases", method = RequestMethod.POST)
    @ApiOperation(value = "删除订单", notes = "")
    public BaseResult delete(String orderIds) {
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(Constant.CodeConfig.CODE_SUCCESS);
        baseResult.setMessage(Constant.MessageConfig.MSG_SUCCESS);
        try {
            if (orderIds != null) {
                boolean flag = purchaseService.deletePurchase(orderIds);
                if (!flag) {
                    baseResult.setCode(Constant.CodeConfig.CODE_FAILURE);
                    baseResult.setMessage(Constant.MessageConfig.MSG_FAILURE);
                }
            } else {
                baseResult.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
                baseResult.setMessage(Constant.MessageConfig.MSG_NOT_EMPTY);
            }
        } catch (Exception e) {
            baseResult.setCode(Constant.CodeConfig.CODE_FAILURE);
            baseResult.setMessage(Constant.MessageConfig.MSG_FAILURE);
        }
        return baseResult;
    }

    /**
     * 根据商户id及查询条件（起始-结束时间；起始-结束金额范围）分页显示余额明细
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @param input 查询条件封装类
     * @param storeId 商家编号
     * @return output出参
     */
    @ApiOperation(value="余额明细查询", notes=" 根据商户id及查询条件（起始-结束时间；起始-结束金额范围）分页显示订单")
    @GetMapping(value = "/account/Purchases")
    public RecordToPurchaseOutput search(Integer pageNum, Integer pageSize, AccountPurchaseInput input, Long storeId){
        RecordToPurchaseOutput output = new RecordToPurchaseOutput();
        try {
            if (null != storeId){
                output = purchaseService.searchPurchases(pageNum,pageSize,input,storeId);
            } else {
                output.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
                output.setMessage(Constant.MessageConfig.MSG_NOT_EMPTY);
            }
        } catch (Exception e) {
            e.printStackTrace();
            output.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
            output.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
        }
        return output;
    }

    /**
     * 根据商户ID查询订单状态，并计算总金额
     *
     * @param storeId
     * @return SumIncome(BigDicaml类型的字段Income)
     */
    @ApiOperation(value = "获取商户总金额")
    @GetMapping(value = "/findincome/{storeId}")
    public SumIncome findOrderStatus(@PathVariable("storeId") Long storeId) {
        return purchaseService.findOrderStatus(storeId);
    }

}

