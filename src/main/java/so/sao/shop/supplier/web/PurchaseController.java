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
     * @param request
     * @param pageNum
     * @param rows
     * @param purchaseSelectInput
     * @return PurchaseSelectOutput
     */
    @GetMapping(value = "/search")
    @ApiOperation(value = "查询订单列表", notes = "*")
    public Result search(HttpServletRequest request, Integer pageNum, Integer rows, PurchaseSelectInput purchaseSelectInput) throws Exception {
        //获取当前登陆账户
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (null == user) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        //判断是否为管理员
        if (Constant.ADMIN_STATUS.equals(user.getIsAdmin())) {
            if (null == purchaseSelectInput.getStoreId()) {
                return Result.fail(Constant.MessageConfig.STORE_ID_NOT_NULL);
            }
        } else {
            purchaseSelectInput.setStoreId(BigInteger.valueOf(user.getAccountId()));
        }
        //判断时间格式
        if (!verifyDate(purchaseSelectInput)) {
            return Result.fail(Constant.MessageConfig.MSG_DATE_INPUT_FORMAT_ERROR);
        }
        //对比开始时间和结束时间
        if (restrictDate(purchaseSelectInput)) {
            return Result.fail(Constant.MessageConfig.DateNOTLate);
        }
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
     * 发货接口
     *
     * @param orderId 订单ID
     * @param map     map
     * @return Result
     * @throws Exception Exception
     */
    @PostMapping(value = "/deliverGoods/{orderId}")
    @ApiOperation(value = "发货接口", notes = "发货接口")
    public Result deliverGoods(@RequestBody @PathVariable("orderId") String orderId, @RequestBody Map map) throws Exception {
        if (!verifyOrderStatus(orderId, Constant.OrderStatusConfig.ISSUE_SHIP)) {
            return Result.fail(Constant.MessageConfig.ORDER_STATUS_EERO);
        }
        if (!StringUtils.isEmpty(map.get("name")) && !StringUtils.isEmpty(map.get("number"))) {
            //更改订单状态操作
            purchaseService.deliverGoods(orderId, (Integer) map.get("receiveMethod"), (String) map.get("name"), (String) map.get("number"));
            return Result.success(Constant.MessageConfig.MSG_SUCCESS);
        }
        return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
    }

    /**
     * 删除订单，批量删除和单个删除
     *
     * @param orderIds
     * @return BaseResult
     */
    @RequestMapping(value = "/delete/purchases", method = RequestMethod.POST)
    @ApiOperation(value = "删除订单", notes = "")
    public Result delete(String orderIds) {
        if (orderIds != null) {
            purchaseService.deletePurchase(orderIds);
            return Result.success(Constant.MessageConfig.MSG_SUCCESS);
        }
        return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
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

    /**
     * 查询打印商品条目接口
     *
     * 根据订单编号查询打印页面的信息及订单对应的商品条目
     * 1.验证参数合法性
     * 2.查询打印信息
     *
     * @param orderId 订单编号
     * @return
     */
    @ApiOperation(value = "查询打印商品条目接口", notes = "根据订单编号查询打印页面的信息及订单对应的商品条目【负责人：杨恒乐】")
    @GetMapping("/searchPrintItems")
    public Result searchPrintItems(String orderId) throws Exception {
        // 请求参数未传入orderId返回失败
        if (Ognl.isEmpty(orderId)) { // 参数为空
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }
        PurchaseItemPrintOutput output = purchaseService.getPrintItems(orderId); // 打印页面信息封装的对象
        if (null == output) {
            return Result.success(Constant.MessageConfig.MSG_NO_DATA);
        }
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, output);
    }

    /**
     * // FIXME: 2017/9/4 此接口优化时去掉，只保留service方法
     * 生成收货二维码接口
     *
     * 根据订单编号生成二维码图片，上传云端并将二维码信息保存到数据库
     * 1.验证参数合法性
     * 2.生成二维码图片并保存到数据库
     *
     * @param params 入参Map类型，key包含orderId，表示订单编号
     * @return
     */
    @ApiOperation(value = "生成收货二维码接口", notes = "生成收货二维码接口【负责人：杨恒乐】")
    @PostMapping("createReceivingQrcode")
    public Result createReceivingQrcode(@RequestBody Map params) throws Exception {
        String orderId = (String) params.get("orderId");

        // 1.验证参数合法性
        if (Ognl.isEmpty(orderId)) { // 参数为空
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }

        // 2.生成二维码图片,上传云端并保存到数据库
        purchaseService.createReceivingQrcode(orderId);

        return Result.success(Constant.MessageConfig.MSG_SUCCESS);
    }

    /**
     * 扫码收货接口
     * 根据订单编号验证订单，修改订单状态，并将二维码状态设置为失效
     * 1.验证参数
     * 2.确认收货
     *
     * @param params 入参Map类型，key包含orderId，表示订单编号
     * @return
     */
    @ApiOperation(value = "扫码收货接口", notes = "根据订单编号验证订单，修改订单状态，并将二维码状态设置为失效【负责人：杨恒乐】")
    @PostMapping("/sweepReceiving")
    public Result sweepReceiving(@RequestBody Map params) throws Exception {
        String orderId = (String) params.get("orderId");

        // 验证失败，验证参数的合法性
        if (Ognl.isEmpty(orderId)) {  // 参数为空
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY);
        }
        // 确认收货
        Map map = purchaseService.sweepReceiving(orderId);
        boolean flag = (boolean) map.get("flag");
        if (flag) { // 成功
            return Result.success(Constant.MessageConfig.MSG_SUCCESS);
        }
        return Result.fail((String) map.get("message"));
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
        if (!verifyOrderStatus(refuseOrderInput.getOrderId(), Constant.OrderStatusConfig.REJECT)) {
            return Result.fail(Constant.MessageConfig.ORDER_STATUS_EERO);
        }
        purchaseService.refuseOrder(refuseOrderInput);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS);
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
        Map<String, Object> map = purchaseService.searchRefuseReasonByOrderId(orderId);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, map);
    }

    /**
     * 新增取消订单原因
     *
     * @param cancelReasonInput 封装了订单编号，取消理由
     * @return Result 返回状态码code 、 状态描述message 及 数据data
     * @throws Exception
     */
    @ApiOperation("新增取消订单原因接口")
    @PostMapping("/insertCancelReason")
    public Result insertCancelReason(@RequestBody @Valid CancelReasonInput cancelReasonInput) throws Exception {
        if (!verifyOrderStatus(cancelReasonInput.getOrderId(), Constant.OrderStatusConfig.CANCEL_ORDER)) {
            return Result.fail(Constant.MessageConfig.ORDER_STATUS_EERO);
        }
        purchaseService.cancelOrder(cancelReasonInput);
        return Result.success(Constant.MessageConfig.MSG_SUCCESS);
    }

    /**
     * 查看取消订单原因
     *
     * @param orderId 订单编号
     * @return Result 返回状态码code 、 状态描述message 及 数据data
     * @throws Exception
     */
    @ApiOperation("查看取消订单原因接口")
    @GetMapping("/scanCancelReason/{orderId}")
    public Result scanCancelReason(@PathVariable("orderId") String orderId) throws Exception {
        String cancelReason = purchaseService.searchCancelReasonByOrderId(orderId);
        if (!StringUtils.isEmpty(cancelReason)) {
            return Result.success(Constant.MessageConfig.MSG_SUCCESS, cancelReason);
        }
        return Result.success(Constant.MessageConfig.MSG_SUCCESS, Constant.MessageConfig.MSG_NO_DATA);
    }

    /**
     * 实现退款逻辑
     *
     * 根据订单编号调用退款接口退款并修改订单状态
     *
     * @param orderId 订单编号
     * @return Result
     * @throws Exception
     */
    @ApiOperation(value = "退款", notes = "根据订单编号调用退款接口退款并修改订单状态【负责人：杨恒乐】")
    @PostMapping("/refund/{orderId}")
    public Result refund(@PathVariable("orderId") String orderId) throws Exception {
        // 订单编号为空，返回
        if (Ognl.isEmpty(orderId)) {
            return Result.fail(Constant.MessageConfig.MSG_NOT_EMPTY); // 不允许为空
        }
        Map map = purchaseService.refundByOrderId(orderId);
        boolean flag = (boolean) map.get("flag");
        if (flag) { // 退款成功
            return Result.success(Constant.MessageConfig.MSG_SUCCESS);
        } else { // 退款失败，返回失败原因
            return Result.fail((String) map.get("message"));
        }
    }

    //验证订单状态
    private boolean verifyOrderStatus(String orderId, Integer orderStatus) {
        boolean flag = false;
        Integer getOrderStatus = purchaseService.findOrderStatus(orderId);
        //待付款 --> 待发货
        if (getOrderStatus == Constant.OrderStatusConfig.PAYMENT && orderStatus == Constant.OrderStatusConfig.PENDING_SHIP) {
            flag = true;
        }
        //待付款 --> 已取消 / 待发货 --> 已取消
        if ((getOrderStatus == Constant.OrderStatusConfig.PAYMENT || getOrderStatus == Constant.OrderStatusConfig.PENDING_SHIP) && orderStatus == Constant.OrderStatusConfig.CANCEL_ORDER) {
            flag = true;
        }
        //待发货 --> 已发货
        if (getOrderStatus == Constant.OrderStatusConfig.PENDING_SHIP && orderStatus == Constant.OrderStatusConfig.ISSUE_SHIP) {
            flag = true;
        }
        //已发货 --> 已完成 / 已发货 --> 已拒收
        if (getOrderStatus == Constant.OrderStatusConfig.ISSUE_SHIP && (orderStatus == Constant.OrderStatusConfig.RECEIVED || orderStatus == Constant.OrderStatusConfig.REJECT)) {
            flag = true;
        }
        //已拒收 --> 已退款 / 已取消 --> 已退款
        if ((getOrderStatus == Constant.OrderStatusConfig.REJECT || getOrderStatus == Constant.OrderStatusConfig.CANCEL_ORDER) && orderStatus == Constant.OrderStatusConfig.REFUNDED) {
            flag = true;
        }
        return flag;
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
    private boolean restrictDate(PurchaseSelectInput purchaseSelectInput) {
        boolean flag = false;
        try {
            if (!StringUtils.isEmpty(purchaseSelectInput.getBeginDate()) && !StringUtils.isEmpty(purchaseSelectInput.getEndDate())) {
                flag = DataCompare.compareDate(purchaseSelectInput.getBeginDate(), purchaseSelectInput.getEndDate());
            }
            if (!StringUtils.isEmpty(purchaseSelectInput.getOrderPaymentBeginTime()) && !StringUtils.isEmpty(purchaseSelectInput.getOrderPaymentEndTime())) {
                flag = DataCompare.compareDate(purchaseSelectInput.getOrderPaymentBeginTime(), purchaseSelectInput.getOrderPaymentEndTime());
            }
            if (!StringUtils.isEmpty(purchaseSelectInput.getOrderReceiveBeginTime()) && !StringUtils.isEmpty(purchaseSelectInput.getOrderReceiveEndTime())) {
                flag = DataCompare.compareDate(purchaseSelectInput.getOrderReceiveBeginTime(), purchaseSelectInput.getOrderReceiveEndTime());
            }
        } catch (ParseException e) {
            logger.error("系统异常", e);
            e.printStackTrace();
        }
        return flag;
    }
}