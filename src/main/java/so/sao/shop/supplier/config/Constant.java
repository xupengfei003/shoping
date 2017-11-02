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
     * 供货平台在redis中短信验证码key前缀
     */
    public static final String REDIS_SMSCODE_KEY_PREFIX = "supplier_platform_smscode_";

    /**
     * 判断是否是管理员 1是
     */
    public static final String ADMIN_STATUS = "1";
    /**
     * 资质审核： 0未提交审核，1待审核 2审核通过 3审核未通过
     */
    public static final Integer QUALIFICATION_NOT_VERIFY = 0;
    public static final Integer QUALIFICATION_AWAIT_VERIFY = 1;
    public static final Integer QUALIFICATION_VERIFY_PASS = 2;
    public static final Integer QUALIFICATION_VERIFY_NOT_PASS = 3;
    /**
     *标记消息已经读取： 1已经读取， 0 未读取
     */
    public static final Integer IS_NOT_READ = 0;
    public static final Integer IS_READ = 1;
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

        /**
         * 参数异常
         */
        public static final String PARAMETER_ABNORMITY = "参数异常";
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
         * 已拒收退款审核
         */
        public static final Integer REJECT = 5;
        /**
         * 已退款
         */
        public static final Integer REFUNDED = 6;
        /**
         * 已支付退款审核
         */
        public static final Integer CANCEL_ORDER = 7;
        /**
         * 待付款已取消
         */
        public static final Integer PAYMENT_CANCEL_ORDER = 8;
        /**
         * 确认送达
         */
        public static final Integer CONFIRM_RECEIVED = 19;

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
        public static final String REJECT = "已拒收退款审核";
        /**
         * 6
         */
        public static final String REFUNDED = "已退款";
        /**
         * 7
         */
        public static final String CANCEL_ORDER = "已支付退款审核";
        /**
         * 8
         */
        public static final String PAYMENT_CANCEL_ORDER = "待付款已取消";
        /**
         * 19
         */
        public static final String CONFIRM_RECEIVED = "确认送达";
    }
    interface OrderStatusRule{
        /**
         * 待付款\待发货\已发货\已完成\已拒收\已退款\已付款已取消\待付款已取消
         */

        public static final String[] RULES = {"2#8","3#7","4#5","#","6#","#","6#","#"};
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
        public static final String PAYMENT_NOTIFI = "您好:您有一笔待付款订单,订单编号:";
        /**
         * 2
         */
        public static final String PENDING_SHIP_NOTIFI = "您好:您有一笔订单状态已变成<待发货>,订单编号:";
        /**
         * 3
         */
        public static final String ISSUE_SHIP_NOTIFI = "您好:您有一笔订单状态已变成<已发货>,订单编号:";
        /**
         * 4
         */
        public static final String RECEIVED_NOTIFI = "您好:您有一笔订单状态已变成<已完成>,订单编号:";
        /**
         * 5
         */
        public static final String REJECT_NOTIFI = "您好:您有一笔订单状态已变成<已拒收退款审核>,订单编号:";
        /**
         * 6
         */
        public static final String REFUNDED_NOTIFI = "您好:您有一笔订单状态已变成<已退款>,订单编号:";

        /**
         * 7
         */
        public static final String CANCEL_ORDER = "您好:您有一笔订单状态已变成<已支付退款审核>,订单编号:";

        /**
         * 8
         */
        public static final String PAYMENT_CANCEL_ORDER = "您好:您有一笔订单状态已变成<待付款已取消>,订单编号:";
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
        /**
         * 资质审核状态拒绝原因最大255
         */
        public static final int MAX_QUALIFICATIUON_REJECT_REASON_LENGTH = 255;
    }

    /**
     * 配送规则
     */
    interface FreightRulesCodeConfig{
        /**
         * 通用通则
         */
        public static final int commType = 0;

        /**
         * 配送规则
         */
        public static final int dispatchingType = 1;

        /**
         * 包邮
         */
        public static final int exemption = 0;

        /**
         * 不包邮
         */
        public static final int NotExemption = 1;
    }

    /*供应商资质常量*/
    interface QualificationConfig{
        /**
         * 最大上传图片数量
         */
        public static final int MAX_IMG_NUMBER = 3;

        /*资质类型（1、开户银行许可证 2、营业执照3、授权报告 4、质检报告 5、食品流通许可证）*/
        /**
         * 质检报告
         */
        public static final int BANK_LICENSE = 1;
        /**
         * 营业执照
         */
        public static final int BUSINESS_LICENSE = 2;
        /**
         * 授权报告
         */
        public static final int AUTHORIZATION_REPORT = 3;
         /**
         * 质检报告
         */
        public static final int INSPECTION_REPORT = 4;
        /**
         * 食品流通许可证
         */
        public static final int FOOD_DISTRIBUTIONLICENSE = 5;

        /**
         * 所有资质文件最大图片数量
         */
        public  static final int ALL_QUALIFICATION_IMG_NUM = 15;

        /**
         * 必传资质文件数量
         */
        public  static final int MUST_ADD_QUALIFICATION = 2;

        /*未删除*/
        public  static final int NOT_DELETE = 0;

        /*已删除*/
        public  static final int DELETED = 1;

    }
    
    /**
     * 权限相关常量
     * @author acer
     *
     */
    interface AuthConfig {
    	/**
    	 * 后台接口类型
    	 */
    	public static final Integer PERMISSION_TYPE_INTERFACE = 1;
    	/**
    	 * 前端菜单
    	 */
    	public static final Integer PERMISSION_TYPE_MENU = 2;
    	/**
    	 * 前端按钮
    	 */
    	public static final Integer PERMISSION_TYPE_BUTTON = 3;
    	/**
    	 * 框架提供的api
    	 */
    	public static final Integer PERMISSION_TYPE_SYSTEM = 88;
    	/**
    	 * 业务系统特殊权限
    	 */
    	public static final Integer PERMISSION_TYPE_special_BUSINESS = 99;
    }

    /**
     * 发票相关常量
     * @author acer
     *
     */
    interface ReceiptConfig {
        /**
         * 增值税普通个人发票
         */
        public static final Integer RECEIPTTYPE_PEISION = 0;
        /**
         * 增值税普通单位发票
         */
        public static final Integer RECEIPTTYPE_COMPANY = 1;
        /**
         * 增值税专用发票
         */
        public static final Integer RECEIPTTYPE_SPECIAL = 2;
    }

    /**
     * 供应商发票设置状态常量
     */
    interface InvoiceSetting {
        /**
         * 供应商发票设置【开关】状态--开启
         */
        public static final Integer STATUS_ON = 1;
        /**
         * 供应商发票设置【开关】状态--关闭
         */
        public static final Integer STATUS_OFF = 0;
        /**
         * 供应商发票设置增值税【普通发票】状态--开启
         */
        public static final Integer INVOICE_ON = 1;
        /**
         * 供应商发票设置增值税【普通发票】状态--关闭
         */
        public static final Integer INVOICE_OFF = 0;
        /**
         * 供应商发票设置增值税【专用发票】状态--开启
         */
        public static final Integer SPECIAL_INVOICE_ON = 1;
        /**
         * 供应商发票设置增值税【专用发票】状态--关闭
         */
        public static final Integer SPECIAL_INVOICE_OFF = 0;

    }

    /**
     * 智售app商品查询常量
     */
    interface AppCommSearch{

        /**
         * 通过供应商名称查询
         */
        public static final int SEARCH_BY_SUPPLIER_NAME = 0;

        /**
         * 通过商品名称查询
         */
        public static final int SEARCH_BY_GOODS_NAME = 1;

        /**
         * 通过品牌名称查询
         */
        public static final int SEARCH_BY_BRAND_NAME = 2;


    }
}
