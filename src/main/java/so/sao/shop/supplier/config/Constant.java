package so.sao.shop.supplier.config;

import java.math.BigDecimal;

/**
 * <p>
 *  系统常量定义
 * </p>
 *
 * @author 透云-中软-西安项目组
 * @since 2017-07-19
 */
public interface Constant {
    /**
     * code定义
     */
    interface CodeConfig{
        /**
         * 失败
         */
        public final Integer CODE_FAILURE = 0;
        /**
         * 成功
         */
        public final Integer CODE_SUCCESS = 1;
        /**
         * 不允许为空
         */
        public final Integer CODE_NOT_EMPTY = 2;
        /**
         * 系统异常
         */
        public final Integer CODE_SYSTEM_EXCEPTION = 3;
        /**
         * 未查找到结果
         */
        public final Integer CODE_NOT_FOUND_RESULT = 4;
    }
    /**
     * Message定义
     */
    interface MessageConfig{
        /**
         * 失败
         */
        public static final String MSG_FAILURE = "失败";
        /**
         * 成功
         */
        public static final String MSG_SUCCESS = "成功";
        /**
         * 不允许为空
         */
        public static final String MSG_NOT_EMPTY = "不允许为空";
        /**
         * 系统异常
         */
        public static final String MSG_SYSTEM_EXCEPTION = "系统异常";
        /**
         * 未查找到结果
         */
        public static final String MSG_NOT_FOUND_RESULT = "未查找到结果";
    }

    interface OrderStatusConfig{
        /**
         * 待付款
         */
        public final Integer PAYMENT = 1;
        /**
         * 待发货
         */
        public final Integer PENDING_SHIP = 2;
        /**
         * 已发货
         */
        public final Integer ISSUE_SHIP = 3;
        /**
         * 已收货
         */
        public final Integer RECEIVED = 4;
        /**
         * 已拒收
         */
        public final Integer REJECT = 5;
        /**
         * 已退款
         */
        public final Integer REFUNDED = 6;
        /**
         * 已完成
         */
        public final Integer COMPLETE = 7;
    }


    /**
     * 提现申请时间段（每月10至15号可提现）
     */
    interface OMRTimePeriodConfig {
        /**
         * 每月可提现开始时间
         */
        public static final int START_DATE = 10;

        /**
         * 每月可提现结束时间
         */
        public static final int END_DATE = 15;
    }

    /**
     * 提现申请code定义
     */
    interface OMRCodeConfig {
        /**
         * 参数错误
         */
        public static final Integer OMR_CODE_PARAM_ERROR = -4;

        /**
         * 该账户不存在
         */
        public static final Integer OMR_CODE_ACCOUNT_NOT_EXIST = -3;

        /**
         * 当前时间不在提现日期内
         */
        public static final Integer OMR_CODE_NOT_TIME_PERIOD = -2;

        /**
         * 提现金额小于最小提现金额，不能提现
         */
        public static final Integer OMR_CODE_MIN_WITHDRAW = -1;

        /**
         * 提现申请失败
         */
        public static final Integer OMR_CODE_FAILURE = 0;

        /**
         * 提现申请成功
         */
        public static final Integer OMR_CODE_SUCCESS = 1;


        //提现申请状态定义
        /**
         * 提现申请中，不能操作
         */
        public static final Integer OMR_CODE_APPLY = 2;

        /**
         * 审核已通过
         */
        public static final Integer OMR_CODE_PASSED = 3;

        /**
         * 提现已完成
         */
        public static final Integer OMR_CODE_DONE = 4;

    }

    /**
     * 提现申请Message定义
     */
    interface OMRMessageConfig {

        /**
         * 参数错误
         */
        public static final String OMR_MSG_PARAM_ERROR = "参数错误";

        /**
         * 该账户不存在
         */
        public static final String OMR_MSG_ACCOUNT_NOT_EXIST = "该账户不存在";

        /**
         * 当前时间不在提现日期内
         */
        public static final String OMR_MSG_NOT_TIME_PERIOD = "当前时间不在提现日期内";

        /**
         * 提现金额小于最小提现金额，不能提现
         */
        public static final String OMR_MSG_MIN_WITHDRAW = "提现金额小于最小提现金额，不能提现";

        /**
         * 提现申请失败
         */
        public static final String OMR_MSG_FAILURE = "提现申请失败";

        /**
         * 提现申请成功
         */
        public static final String OMR_MSG_SUCCESS = "提现申请成功";


        //提现申请状态定义
        /**
         * 提现申请中，不能操作
         */
        public static final String OMR_MSG_APPLY = "提现申请中，不能操作";

        /**
         * 审核已通过
         */
        public static final String OMR_MSG_PASSED = "审核已通过";

        /**
         * 提现已完成
         */
        public static final String OMR_MSG_DONE = "提现已完成";

    }

    interface OMRMinWithdrawConfig {
        /**
         * 最小提现金额
         */
        public static final BigDecimal MIN_TOTAL_MONEY = new BigDecimal(500.00);
    }

    interface OrderMessageConfig{
        /**
         * 1
         */
        public final String PAYMENT = "待付款";
        /**
         * 2
         */
        public final String PENDING_SHIP = "待发货";
        /**
         * 3
         */
        public final String ISSUE_SHIP = "已发货";
        /**
         * 4
         */
        public final String RECEIVED = "已收货";
        /**
         * 5
         */
        public final String REJECT = "已拒收";
        /**
         * 6
         */
        public final String REFUNDED = "已退款";
        /**
         * 7
         */
        public final String COMPLETE = "已完成";
    }

    interface PaymentStatusConfig{
        /**
         * 支付宝
         */
        public final Integer ALIPAY = 1;
        /**
         * 微信
         */
        public final Integer WECHAT = 2;
    }

    interface PaymentMsgConfig{
        /**
         * 支付宝
         */
        public final String ALIPAY = "支付宝";
        /**
         * 微信
         */
        public final String WECHAT = "微信";
    }

    /**
     * 根据主键查找信息不存在的信息配置
     */
    interface NoExistMessageConfig{
        /**
         * 不存在该订单
         */
        public final String NOORDER = "此订单不存在";
        /**
         *不存在该商家
         */
        public final String NOSTORE = "此商家不存在";
    }

    interface NoExistCodeConfig {
        /**
         * 不存在该订单
         */
        public final int NOORDER = 101;
        /**
         *不存在该商家
         */
        public final int NOSTORE = 102;
    }

}
