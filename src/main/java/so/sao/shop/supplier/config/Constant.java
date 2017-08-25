package so.sao.shop.supplier.config;


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
     * request中存user的key
     */
    public static final String REQUEST_USER = "user";

    /**
     * 供货平台在redis中key前缀
     */
    public static final String REDIS_KEY_PREFIX = "supplier_platform_";

    /**
     * 供货平台在redis中登陆信息key前缀
     */
    public static final String REDIS_LOGIN_KEY_PREFIX = "supplier_platform_login_";

    /**
     * 判断是否是管理员 1是
     */
    public static final String ADMIN_STATUS = "1";

    /**
     * code定义
     */
    interface CodeConfig{
        /**
         * 失败
         */
        public static final Integer CODE_FAILURE = 0;
        /**
         * 成功
         */
        public static final Integer CODE_SUCCESS = 1;
        /**
         * 不允许为空
         */
        public static final Integer CODE_NOT_EMPTY = 2;
        /**
         * 系统异常
         */
        public static final Integer CODE_SYSTEM_EXCEPTION = 3;
        /**
         * 未查找到结果
         */
        public static final Integer CODE_NOT_FOUND_RESULT = 4;
        /**
         * 入参时间格式不正确
         */
        public static final Integer CODE_DATE_INPUT_FORMAT_ERROR = 5;
        /**
         * 未登录
         */
        public static final Integer CODE_USER_NOT_LOGIN = 6;

        /**
         * 起始时间不能晚于结束时间
         */
        public static final Integer DateNOTLate = 7;

        /**
         * 最小金额不能大于最大金额
         */
        public static final Integer MoneyNOTLate = 8;
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
        public static final String MSG_NOT_FOUND_RESULT = "暂无订单数据";

        /**
         * 入参时间格式不正确
         */
        public static final String MSG_DATE_INPUT_FORMAT_ERROR = "时间格式不正确";
        /**
         * 未登录
         */
        public static final String MSG_USER_NOT_LOGIN = "请先登录";
        /**
         *  验证登陆用户是否是管理员
         */
        public static final String ADMIN_AUTHORITY_EERO = "该登陆用户不是管理员";

        /**
         * 订单状态不合法
         */
        public static final String ORDER_STATUS_EERO = "订单状态不合法";

        /**
         *  供应商ID不能为空
         */
        public static final String STORE_ID_NOT_NULL = "供应商ID不能为空";

        /**
         * 起始时间不能晚于结束时间
         */
        public static final String DateNOTLate = "起始时间不能晚于结束时间";

        /**
         * 最小金额不能大于最大金额
         */
        public static final String MoneyNOTLate = "最小金额不能大于最大金额";
		 /**
         * 权限不足
         */
        public static final String PERMISSION_DENIED_ERROR = "权限不足";

        /**
         * 暂无数据
         */
        public static final String MSG_NO_DATA = "暂无数据";
    }

    interface OrderStatusConfig{
        /**
         * 待付款
         */
        public static final Integer PAYMENT = 1;
        /**
         * 待发货
         */
        public static final Integer PENDING_SHIP = 2;
        /**
         * 已发货
         */
        public static final Integer ISSUE_SHIP = 3;
        /**
         * 已完成
         */
        public static final Integer RECEIVED = 4;
        /**
         * 已拒收
         */
        public static final Integer REJECT = 5;
        /**
         * 已退款
         */
        public static final Integer REFUNDED = 6;
        /**
         * 已取消
         */
        public static final Integer CANCEL_ORDER = 7;

    }

    interface OrderMessageConfig{
        /**
         * 1
         */
        public static final String PAYMENT = "待付款";
        /**
         * 2
         */
        public static final String PENDING_SHIP = "待发货";
        /**
         * 3
         */
        public static final String ISSUE_SHIP = "已发货";
        /**
         * 4
         */
        public static final String RECEIVED = "已完成";
        /**
         * 5
         */
        public static final String REJECT = "已拒收";
        /**
         * 6
         */
        public static final String REFUNDED = "已退款";
        /**
         * 7
         */
        public static final String CANCEL_ORDER = "已取消";
    }

    interface PaymentStatusConfig{
        /**
         * 支付宝
         */
        public static final Integer ALIPAY = 1;
        /**
         * 微信
         */
        public static final Integer WECHAT = 2;
    }

    interface PaymentMsgConfig{
        /**
         * 支付宝
         */
        public static final String ALIPAY = "支付宝";
        /**
         * 微信
         */
        public static final String WECHAT = "微信";
    }

    /**
     * 根据主键查找信息不存在的信息配置
     */
    interface NoExistMessageConfig{
        /**
         * 不存在该订单
         */
        public static final String NOORDER = "此订单不存在";
        /**
         *不存在该商家
         */
        public static final String NOSTORE = "此商家不存在";
    }

    interface NoExistCodeConfig {
        /**
         * 不存在该订单
         */
        public static final int NOORDER = 101;
        /**
         *不存在该商家
         */
        public static final int NOSTORE = 102;
    }

    /**
     * 更改订单状态对应的消息通知
     */
    interface NotifiConfig{
        /**
         * 1
         */
        public static final String PAYMENT_NOTIFI = "您好:您有一笔新的订单通知,订单编号:";
        /**
         * 2
         */
        public static final String PENDING_SHIP_NOTIFI = "您好:您有一笔待发货的订单通知,订单编号:";
        /**
         * 3
         */
        public static final String ISSUE_SHIP_NOTIFI = "您好:您有一笔确认发货的订单通知,订单编号:";
        /**
         * 4
         */
        public static final String RECEIVED_NOTIFI = "您好:您有一笔已完成的订单通知,订单编号:";
        /**
         * 5
         */
        public static final String REJECT_NOTIFI = "您好:您有一笔拒收的订单通知,订单编号:";
        /**
         * 6
         */
        public static final String REFUNDED_NOTIFI = "您好:您有一笔已退款的订单通知,订单编号:";

        /**
         * 7
         */
        public static final String CANCEL_ORDER = "您好:您有一笔已取消的订单通知,订单编号:";
    }

    interface AccountCodeConfig{
        /**
         * 该账户不存在
         */
        public static final int CODE_NOT_EXIST_ACCOUNT = 201;

        /**
         * 结算状态错误
         */
        public static final int CODE_SETTLED_STATE_ERROR = 202;

    }

    interface AccountMessageConfig{
        /**
         * 账户不存在
         */
        public static final String MESSAGE_NOT_EXIST_ACCOUNT = "该账户不存在";

        /**
         * 结算状态错误
         */
        public static final String MESSAGE_SETTLED_STATE_ERROR = "结算状态错误";
    }

    interface CheckMaxLength{
        /**
         * 商品标签名称最大长度
         */
        public static final int MAX_TAG_NAME_LENGTH = 64;
        /**
         * 商品单位名称最大长度
         */
        public static final int MAX_UNIT_NAME_LENGTH = 64;
        /**
         * 商品计量规格名称最大长度
         */
        public static final int MAX_MEASURESPEC_NAME_LENGTH = 64;
    }
}
