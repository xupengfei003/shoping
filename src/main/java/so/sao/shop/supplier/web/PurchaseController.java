package so.sao.shop.supplier.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.*;
import so.sao.shop.supplier.pojo.output.PurchaseInfoOutput;
import so.sao.shop.supplier.pojo.output.PurchaseItemPrintOutput;
import so.sao.shop.supplier.pojo.vo.PurchasesVo;
import so.sao.shop.supplier.service.PurchaseService;
import so.sao.shop.supplier.util.DataCompare;
import so.sao.shop.supplier.util.DateUtil;
import so.sao.shop.supplier.util.Ognl;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.math.BigInteger;
import java.text.ParseException;
import java.util.HashMap;
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

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    @Resource
    private PurchaseService purchaseService;

    /**
     * 保存订单
     *
     * @param purchase 订单对象
     * @return
     */
    @RequestMapping(value = "/createPurchase", method = RequestMethod.POST)
    @ApiOperation(value = "生成订单", notes = "生成订单")
    public Result createPurchase(@RequestBody @Valid PurchaseInput purchase) throws Exception {
        Map<String, Object> resMap = purchaseService.savePurchase(purchase);
        Integer status = Integer.parseInt(String.valueOf(resMap.get("status")));
        if (status == 1) {
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("orderId", resMap.get("orderId"));
            resultMap.put("totalMoney", resMap.get("totalMoney"));
            return Result.success(Constant.MessageConfig.MSG_SUCCESS, resultMap);
        }
        return Result.fail(Constant.MessageConfig.MSG_FAILURE);
    }

    /**
     * 根据订单ID获取订单详情
     *
     * @param orderId orderId
     * @return PurchaseOutput
     */
    @RequestMapping(value = "/purchase/{orderId}", method = RequestMethod.GET)
    @ApiOperation(value = "获取订单详情", notes = "获取订单详情")
    public Result findById(@PathVariable String orderId) throws Exception {
        PurchaseInfoOutput output = purchaseService.findById(orderId);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, output);
    }

    /**
     * POI导出(当前页/所选页/全部)订单列表
     *
     * @param request  request
     * @param response response
     * @param pageNum  pageNum
     * @param pageSize pageSize
     */
    @RequestMapping(value = "/export", method = RequestMethod.GET)
    @ApiOperation(value = "POI批量导出订单列表", notes = "POI批量导出订单列表")
    @ResponseBody
    public Result exportExcel(HttpServletRequest request, HttpServletResponse response, String pageNum, Integer pageSize,
                                  @RequestParam(required = false) Long accountId, PurchaseSelectInput purchaseSelectInput) throws Exception {
        //判断时间格式
        if (!verifyDate(purchaseSelectInput)) {
            return Result.fail(Constant.MessageConfig.MSG_DATE_INPUT_FORMAT_ERROR);
        }
        if (restrictDate(purchaseSelectInput)) {
            return Result.fail(Constant.MessageConfig.DateNOTLate);
        }
        purchaseService.exportExcel(request, response, pageNum, pageSize, accountId, purchaseSelectInput);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS);
    }

    /**
     * 查询全部订单列表
     *
     * @param request             request
     * @param pageNum             pageNum
     * @param rows                rows
     * @param purchaseSelectInput purchaseSelectInput
     * @return PurchaseSelectOutput
     */
    @GetMapping(value = "/search")
    @ApiOperation(value = "查询订单列表", notes = "查询订单列表")
    public Result search(HttpServletRequest request, Integer pageNum, Integer rows, PurchaseSelectInput purchaseSelectInput) throws Exception {
        Result result = new Result();
        result.setCode(Constant.CodeConfig.CODE_DATE_INPUT_FORMAT_ERROR);
        result.setMessage(Constant.MessageConfig.MSG_DATE_INPUT_FORMAT_ERROR);
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (null == user) {
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        } else {
            //判断是否为管理员
            if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())) {
                if (null == purchaseSelectInput.getStoreId()) {
                    result.setCode(Constant.CodeConfig.CODE_FAILURE);
                    result.setMessage(Constant.MessageConfig.STORE_ID_NOT_NULL);
                    return result;
                }
            } else {
                purchaseSelectInput.setStoreId(BigInteger.valueOf(user.getAccountId()));
            }
            //判断时间格式
            if (!verifyDate(purchaseSelectInput)) {
                return result;
            }
            //对比开始时间和结束时间
            if (restrictDate(purchaseSelectInput)) {
                return result;
            }
            //查询订单
            if (rows == null || rows <= 0) {
                rows = 10;
            }
            if (pageNum == null || pageNum <= 0) {
                pageNum = 1;
            }
            PageInfo<PurchasesVo> pageInfo = purchaseService.searchOrders(pageNum, rows, purchaseSelectInput);
            result.setData(pageInfo);
        }
        return result;
    }

    /**
     * 发货接口
     *
     * @param orderId       订单号
     * @param receiveMethod 配送方式 1自配送 2物流公司
     * @param name          配送公司/配送人
     * @param number        物流单号/电话号码
     * @return BaseResult
     */
    @RequestMapping(value = "/deliverGoods", method = RequestMethod.POST)
    @ApiOperation(value = "发货接口", notes = "发货接口")
    public BaseResult deliverGoods(@PathVariable("orderId") String orderId, Integer receiveMethod, String name, String number) throws Exception {
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(Constant.CodeConfig.CODE_SUCCESS);
        baseResult.setMessage(Constant.MessageConfig.MSG_SUCCESS);
        if (orderId != null) {
            //更改订单状态操作
            boolean flag = purchaseService.deliverGoods(orderId, receiveMethod, name, number);
            if (!flag) {
                baseResult.setCode(Constant.CodeConfig.CODE_FAILURE);
                baseResult.setMessage(Constant.MessageConfig.MSG_FAILURE);
            }
        } else {
            baseResult.setCode(Constant.CodeConfig.CODE_NOT_EMPTY);
            baseResult.setMessage(Constant.MessageConfig.MSG_NOT_EMPTY);
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
    @ApiOperation(value = "删除订单", notes = "删除订单")
    public BaseResult delete(String orderIds) throws Exception {
        BaseResult baseResult = new BaseResult();
        baseResult.setCode(Constant.CodeConfig.CODE_SUCCESS);
        baseResult.setMessage(Constant.MessageConfig.MSG_SUCCESS);
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
        return baseResult;
    }

    /**
     * 账户收入明细查询(高级查询)
     * 1.获取当前登录用户
     * 2.校验入参中的条件检索类
     * 3.访问业务层，获取数据
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示条数
     * @param input    查询条件封装类
     * @return 出参
     */
    @ApiOperation(value = "收入明细查询(高级查询)", notes = " 根据商户id及查询条件（起始创建订单-结束创建订单时间/起始下单时间-结束下单时间/支付方式;订单编号/收货人名称/收货人联系方式）分页显示订单【负责人:郑振海】")
    @GetMapping(value = "/account/PurchasesHigh")
    public Result<PageInfo> searchHigh(Integer pageNum, Integer pageSize, @Validated AccountPurchaseInput input, HttpServletRequest request) throws ParseException {
        //1.取出当前登录用户
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if (null == user || Ognl.isEmpty(user.getAccountId())) {   //验证用户是否登陆
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        //2.校验入参中的条件检索类
        if (Ognl.isNotEmpty(input)) {
            if (Ognl.isNotEmpty(input.getPayBeginTime()) && !DateUtil.isDate(input.getPayBeginTime())) {//开始时间（支付时间）
                return Result.fail(Constant.MessageConfig.MSG_DATE_INPUT_FORMAT_ERROR);
            }
            if (Ognl.isNotEmpty(input.getPayEndTime()) && !DateUtil.isDate(input.getPayEndTime())) {//结束时间（支付时间）
                return Result.fail(Constant.MessageConfig.MSG_DATE_INPUT_FORMAT_ERROR);
            }
            if (Ognl.isNotEmpty(input.getCreateBeginTime()) && !DateUtil.isDate(input.getCreateBeginTime())) {//开始时间（创建时间）
                return Result.fail(Constant.MessageConfig.MSG_DATE_INPUT_FORMAT_ERROR);
            }
            if (Ognl.isNotEmpty(input.getCreateEndTime()) && !DateUtil.isDate(input.getCreateEndTime())) {//结束时间（创建时间）
                return Result.fail(Constant.MessageConfig.MSG_DATE_INPUT_FORMAT_ERROR);
            }
            if (Ognl.isNotEmpty(input.getOrderPaymentMethod()) && 0 == input.getOrderPaymentMethod()) {//支付方式
                input.setOrderPaymentMethod(null);
            }
        }
        //3.访问业务层,获取数据
        return purchaseService.searchPurchasesHigh(pageNum, pageSize, input, user.getAccountId());
    }

    /**
     * 账户收入明细查询(普通查询)
     * 1.取出当前登录用户
     * 2.校验入参中的条件检索类
     * 3.访问业务层，获取数据
     *
     * @param pageNum  当前页码
     * @param pageSize 每页显示条数
     * @param input    查询条件封装类
     * @return 出参
     */
    @ApiOperation(value = "收入明细查询(普通查询)", notes = " 根据商户id及查询条件（起始创建订单-结束创建订单时间/支付流水号/订单编号/收货人名称）分页显示订单【负责人:郑振海】")
    @GetMapping(value = "/account/PurchasesLow")
    public Result<PageInfo> searchLow(Integer pageNum, Integer pageSize, AccountPurchaseLowInput input, HttpServletRequest request) throws ParseException {
        //1.取出当前登录用户
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if (null == user || Ognl.isEmpty(user.getAccountId())) {   //验证用户是否登陆
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        //2.校验入参中的条件检索类
        if (Ognl.isNotEmpty(input)) {
            if (Ognl.isNotEmpty(input.getCreateBeginTime()) && !DateUtil.isDate(input.getCreateBeginTime())) {//开始时间（创建时间）
                return Result.fail(Constant.MessageConfig.MSG_DATE_INPUT_FORMAT_ERROR);
            }
            if (Ognl.isNotEmpty(input.getCreateEndTime()) && !DateUtil.isDate(input.getCreateEndTime())) {//结束时间（创建时间）
                return Result.fail(Constant.MessageConfig.MSG_DATE_INPUT_FORMAT_ERROR);
            }
        }
        //3.访问业务层。获取数据
        return purchaseService.searchPurchasesLow(pageNum, pageSize, input, user.getAccountId());
    }

    /**
     * 根据商户ID查询订单状态，并计算总金额
     *
     * @return
     */
    @ApiOperation(value = "获取商户总金额", notes = "商户的历史总金额【负责人：巨江坤】")
    @GetMapping(value = "/findincome")
    public Result findOrderStatus(HttpServletRequest request) throws Exception {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        //获取业务层数据-历史总金额
        String income = purchaseService.findOrderStatus(user.getAccountId());
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, income);

    }

    // TODO 验证订单状态，本期不考虑
    /*private Integer verifyOrderStatus(String orderId,Integer orderStatus) {
        Integer getOrderStatus = purchaseService.findOrderStatus(orderId);
        if (getOrderStatus == Constant.OrderStatusConfig.PAYMENT && orderStatus == Constant.OrderStatusConfig.PENDING_SHIP){
            getOrderStatus = Constant.OrderStatusConfig.PENDING_SHIP;
        } else if (getOrderStatus == Constant.OrderStatusConfig.PENDING_SHIP && orderStatus == Constant.OrderStatusConfig.ISSUE_SHIP){
            getOrderStatus = Constant.OrderStatusConfig.ISSUE_SHIP;
        } else if (getOrderStatus == Constant.OrderStatusConfig.ISSUE_SHIP && orderStatus == Constant.OrderStatusConfig.RECEIVED){
            getOrderStatus = Constant.OrderStatusConfig.RECEIVED;
        } else if (getOrderStatus == Constant.OrderStatusConfig.ISSUE_SHIP && orderStatus == Constant.OrderStatusConfig.REJECT){
            getOrderStatus = Constant.OrderStatusConfig.REJECT;
        } else if (getOrderStatus == Constant.OrderStatusConfig.RECEIVED && orderStatus == Constant.OrderStatusConfig.REJECT){
            getOrderStatus = Constant.OrderStatusConfig.REJECT;
        } else if (getOrderStatus == Constant.OrderStatusConfig.REJECT && orderStatus == Constant.OrderStatusConfig.REFUNDED){
            getOrderStatus = Constant.OrderStatusConfig.REFUNDED;
        } else {
            getOrderStatus = 0;
        }
        return getOrderStatus;
    }*/

    /**
     * 生成收货二维码接口
     * <p>
     * 根据订单编号生成二维码图片，并将二维码信息保存到数据库
     * 1.验证参数合法性
     * 2.生成二维码图片并保存到数据库
     *
     * @param m 订单编号
     * @return
     */
    @PostMapping("createReceivingQrcode")
    @ApiOperation(value = "生成收货二维码接口", notes = "生成收货二维码接口")
    public Result createReceivingQrcode(@RequestBody Map m) throws Exception {
        String orderId = (String) m.get("orderId");
        // 1.验证参数合法性
        if (Ognl.isEmpty(orderId)) { // 参数为空
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }
        // 2.生成二维码图片并保存到数据库
        Map map = purchaseService.createReceivingQrcode(orderId);
        boolean flag = (boolean) map.get("flag");
        // 成功
        if (flag) {
            return Result.success(Constant.MessageConfig.MSG_SUCCESS);
        }
        return Result.fail(Constant.MessageConfig.MSG_FAILURE);
    }

    /**
     * 查询打印商品条目接口
     * <p>
     * 根据订单编号查询打印页面的信息及订单对应的商品条目
     * 1.验证参数合法性
     * 2.查询打印信息
     *
     * @param orderId 订单编号
     * @return
     */
    @ApiOperation("查询打印商品条目接口")
    @GetMapping("/searchPrintItems")
    public Result searchPrintItems(String orderId) throws Exception {
        // 请求参数未传入orderId返回失败
        if (Ognl.isEmpty(orderId)) { // 参数为空
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }
        PurchaseItemPrintOutput output = purchaseService.getPrintItems(orderId); // 打印页面信息封装的对象
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, output);
    }

    /**
     * 扫码收货接口
     * <p>
     * 1.验证订单编号
     * 1.1.验证失败，返回失败信息
     * 1.2.验证通过，执行步骤2
     * 2.扫描收货二维码
     * 2.1.将订单状态改为已收货
     * 2.2.将二维码状态改为失效，并记录失效时间
     *
     * @param m 订单编号
     * @return
     */
    @ApiOperation("扫码收货接口")
    @PostMapping("/sweepReceiving")
    public Result sweepReceiving(@RequestBody Map m) throws Exception {
        String orderId = (String) m.get("orderId");
        // 验证失败，验证参数的合法性
        if (Ognl.isEmpty(orderId)) {  // 参数为空
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }
        // 扫描收货二维码
        Map map = purchaseService.sweepReceiving(orderId);
        boolean flag = (boolean) map.get("flag");
        if (flag) { // 成功
            return Result.success(Constant.MessageConfig.MSG_SUCCESS);
        }
        return Result.fail(Constant.MessageConfig.MSG_FAILURE);
    }

    /**
     * 添加拒收货信息
     *
     * @param refuseOrderInput 封装了订单编号，拒收理由，拒收图片
     * @return Result 返回状态码code 、 状态描述message 及 数据data
     */
    @ApiOperation("拒收货接口")
    @PostMapping("/refuseOrder")
    public Result refuseOrder(@RequestBody @Valid RefuseOrderInput refuseOrderInput) throws Exception {
        boolean flag = purchaseService.refuseOrder(refuseOrderInput);
        if (flag) {
            return Result.success(Constant.MessageConfig.MSG_SUCCESS);
        }
        return Result.success(Constant.MessageConfig.MSG_FAILURE);
    }

    /**
     * 根据订单ID获取该订单的拒收原因
     *
     * @param orderId 订单ID
     * @return Result 返回状态码code 、 状态描述message 及 数据data
     */
    @ApiOperation("查看拒收理由接口")
    @GetMapping("/scanRefuseOrderReason/{orderId}")
    public Result scanRefuseOrderReason(@PathVariable("orderId") String orderId) throws Exception {
        Result result = new Result();// 返回对象
        //默认result为失败
        result.setCode(Constant.CodeConfig.CODE_FAILURE);
        result.setMessage(Constant.MessageConfig.MSG_FAILURE);
        if (!StringUtils.isEmpty(orderId)) {
            try {
                Map<String, Object> map = purchaseService.searchRefuseReasonByOrderId(orderId);
                if (null != map) {
                    result.setCode(Constant.CodeConfig.CODE_SUCCESS);
                    result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
                    result.setData(map);
                    return result;
                }
            } catch (Exception e) {
                logger.error("系统异常", e);
                result.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
                result.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
            }
        } else {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("订单编号不能为空");
        }
        return result;
    }

    @ApiOperation("新增取消订单原因接口")
    @PostMapping("/insertCancelReason")
    public Result insertCancelReason(@RequestBody @Valid CancelReasonInput cancelReasonInput) throws Exception {
        boolean flag = purchaseService.cancelOrder(cancelReasonInput);
        if (flag) {
            return Result.success(Constant.MessageConfig.MSG_SUCCESS);
        }
        return Result.success(Constant.MessageConfig.MSG_FAILURE);
    }

    @ApiOperation("查看取消订单原因接口")
    @GetMapping("/scanCancelReason/{orderId}")
    public Result scanCancelReason(@PathVariable("orderId") String orderId) throws Exception {
        Result result = new Result();// 返回对象
        //默认result为失败
        result.setCode(Constant.CodeConfig.CODE_FAILURE);
        result.setMessage(Constant.MessageConfig.MSG_FAILURE);
        if (!StringUtils.isEmpty(orderId)) {
            String cancelReason = purchaseService.searchCancelReasonByOrderId(orderId);
            if (!StringUtils.isEmpty(cancelReason)) {
                result.setCode(Constant.CodeConfig.CODE_SUCCESS);
                result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
                result.setData(cancelReason);
            } else {
                result.setCode(Constant.CodeConfig.CODE_SUCCESS);
                result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
                result.setData(Constant.MessageConfig.MSG_NO_DATA);
            }
        } else {
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("订单编号不能为空");
        }
        return result;
    }

    //验证时间格式
    private boolean verifyDate(PurchaseSelectInput purchaseSelectInput) {
        boolean flag = true;
        //订单创建时间
        if (!StringUtils.isEmpty(purchaseSelectInput.getBeginDate())) {
            flag = DateUtil.isDate(purchaseSelectInput.getBeginDate());
            if (!flag) {
                return flag;
            }
        }
        if (!StringUtils.isEmpty(purchaseSelectInput.getEndDate())) {
            flag = DateUtil.isDate(purchaseSelectInput.getEndDate());
            if (!flag) {
                return flag;
            }
        }
        //订单付款时间
        if (!StringUtils.isEmpty(purchaseSelectInput.getOrderPaymentBeginTime())) {
            flag = DateUtil.isDate(purchaseSelectInput.getOrderPaymentBeginTime());
            if (!flag) {
                return flag;
            }
        }
        if (!StringUtils.isEmpty(purchaseSelectInput.getOrderPaymentEndTime())) {
            flag = DateUtil.isDate(purchaseSelectInput.getOrderPaymentEndTime());
            if (!flag) {
                return flag;
            }
        }
        //收货时间
        if (!StringUtils.isEmpty(purchaseSelectInput.getOrderReceiveBeginTime())) {
            flag = DateUtil.isDate(purchaseSelectInput.getOrderReceiveBeginTime());
            if (!flag) {
                return flag;
            }
        }
        if (!StringUtils.isEmpty(purchaseSelectInput.getOrderReceiveEndTime())) {
            flag = DateUtil.isDate(purchaseSelectInput.getOrderReceiveEndTime());
            if (!flag) {
                return flag;
            }
        }
        return flag;
    }

    //限制开始时间小于结束时间
    private boolean restrictDate(PurchaseSelectInput purchaseSelectInput) throws Exception {
        boolean flag = false;
        if (!StringUtils.isEmpty(purchaseSelectInput.getBeginDate()) && !StringUtils.isEmpty(purchaseSelectInput.getEndDate())) {
            flag = DataCompare.compareDate(purchaseSelectInput.getBeginDate(), purchaseSelectInput.getEndDate());
        }
        if (!StringUtils.isEmpty(purchaseSelectInput.getOrderPaymentBeginTime()) && !StringUtils.isEmpty(purchaseSelectInput.getOrderPaymentEndTime())) {
            flag = DataCompare.compareDate(purchaseSelectInput.getOrderPaymentBeginTime(), purchaseSelectInput.getOrderPaymentEndTime());
        }
        if (!StringUtils.isEmpty(purchaseSelectInput.getOrderReceiveBeginTime()) && !StringUtils.isEmpty(purchaseSelectInput.getOrderReceiveEndTime())) {
            flag = DataCompare.compareDate(purchaseSelectInput.getOrderReceiveBeginTime(), purchaseSelectInput.getOrderReceiveEndTime());
        }
        return flag;
    }
}