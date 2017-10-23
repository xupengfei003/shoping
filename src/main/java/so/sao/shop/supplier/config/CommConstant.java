package so.sao.shop.supplier.config;

import java.math.BigDecimal;

/**
 * 常量
 *
 * @author
 * @create 2017-07-15 17:38
 **/
public class CommConstant {

    /**
     * 未上架
     */
    public static final String[] imgArray ={"jpg","jpeg","png","gif"};
    /**
     * 未上架
     */
    public static final int COMM_ST_NEW = 0;
    /**
     * 删除
     */
/*    public static final int COMM_ST_FC = 1;*/
    /**
     * 上架
     */
    public static final int COMM_ST_ON_SHELVES = 2;
    /**
     * 下架
     */
    public static final int COMM_ST_OFF_SHELVES = 3;
    /**
     * 上架待审核
     */
    public static final int COMM_ST_ON_SHELVES_AUDIT = 4;
    /**
     * 下架待审核
     */
    public static final int COMM_ST_OFF_SHELVES_AUDIT = 5;
    /**
     * 编辑待审核
     */
    public static final int COMM_EDIT_AUDIT = 6;
    /**
     * 库存正常状态
     */
    public static final int INVENTORY_NORMAL = 0;
    /**
     * 库存预警状态
     */
    public static final int INVENTORY_WARNING = 1;
    /**
     * 库存默认值
     */
    public static final Double INVENTORY_DEFAULT_VALUE = 0.0;
    /**
     * 库存下限默认值
     */
    public static final long INVENTORY_MINIMUM_DEFAULT_VALUE = 0L;
    /**
     * 商品一级分类pid
     */
    public static final long CATEGORY_ONE_PID = 0L;

    /**
     * 商品科属分类级别数
     */
    public static final int CATEGORY_LEVEL_NUMBER = 3;


    public static String getStatus(int status){
        String st="待上架";
        switch (status){
            case CommConstant.COMM_ST_NEW:
                st="待上架";
                break;
/*            case Constant.COMM_ST_FC:
                st="已废除";
                break;*/
            case CommConstant.COMM_ST_ON_SHELVES:
                st="已上架";
                break;
            case CommConstant.COMM_ST_OFF_SHELVES:
                st="已下架";
                break;
            case CommConstant.COMM_ST_ON_SHELVES_AUDIT:
                st="上架待审核";
                break;
            case CommConstant.COMM_ST_OFF_SHELVES_AUDIT:
                st="下架待审核";
                break;
            case CommConstant.COMM_EDIT_AUDIT:
                st="编辑待审核";
                break;
        }
        return st;
    }
    /**
     * 失效状态:正常invalidStatus
     */
    public static final int INVALIDSTATUS_ON = 1;
    /**
     * 失效状态:失效
     */
    public static final int INVALIDSTATUS_OFF = 0;
    public static String getinvalidStatus(int status){
        String st="正常";
        switch (status){
            case CommConstant.INVALIDSTATUS_ON:
                st="正常";
                break;
            case CommConstant.INVALIDSTATUS_OFF:
                st="失效";
                break;
        }
        return st;
    }
         /*Page分页常量*/

    /**
     * pagesize默认值10
     */
    public static final int PAGE_SIZE_DEFAULT = 10 ;
    /**
     * pagenume默认值1
     */
    public static final int PAGE_NUM_DEFAULT = 1 ;
    /**
     * pagesize最大默认值50
     */
    public static final int PAGE_SIZE_MAX_DEFAULT = 50 ;
    /**
     * pagenume最大默认值500
     */
    public static final int PAGE_NUM_MAX_DEFAULT = 500 ;

    /**
     * 图片格式常量JPG格式
     */
    public static final String IMG_FILE_JPG = ".jpg";
    /**
     * 图片格式常量JPEG格式
     */
    public static final String IMG_FILE_JPEG = ".jpeg";
    /**
     * 图片格式常量PNG格式
     */
    public static final String IMG_FILE_PNG = ".png";
    /**
     * 图片格式常量BMP格式
     */
    public static final String IMG_FILE_BMP = ".bmp";
    /**
     * 图片格式常量GIF格式
     */
    public static final String IMG_FILE_GIF = ".gif";


     /*Azure微软云上传相关常量*/
    /**
     * 容器名称
     */
    public static final String AZURE_CONTAINER = "shop-supplier";
    /**
     * 缩略图默认宽度
     */
    public static final int THUMBNAIL_DEFAULT_WIDTH = 150;
    /**
     * 缩略图默认高度
     */
    public static final int THUMBNAIL_DEFAULT_HEIGHT = 150;

    /**
     * 图片压缩默认宽度
     */
    public static final int THUMB_DEFAULT_WIDTH = 320;
    /**
     * 图片压缩默认高度
     */
    public static final int THUMB_DEFAULT_HEIGHT = 320;
    /**
     * 管理员标识 1
     */
    public static final String IS_ADMIN = "1";
    /**
     * 非管理员标识 0
     */
    public static final String IS_NOT_ADMIN = "0";

    /**
     * 上传图片格式
     */
    public static final String[] IMAGE_TYPE = {"image/jpg","image/jpeg","image/png","image/gif"};
    public static final String IMAGE_JPEG = "image/jpeg";
    public static final String IMAGE_JPG = "jpg";

    /**
     * Excel的开始行
     */
    public static final int POI_START_ROW = 1;

    /**
     * Excel的sheet名
     */
    public static final String SHEET_NAME = "商品列表";

    /**
     * Excel文件名
     */
    public static final String FILE_NAME = "商品列表";

    /**
     * 商品失效状态
     */
    public static final int COMM_INVALID_STATUS = 0;

    /**
     * 商品正常状态
     */
    public static final int COMM_ACTIVE_STATUS = 1;

    /**
     * 供应商被激活
     */
    public static final int ACCOUNT_ACTIVE_STATUS = 1;

    /**
     * 供应商被停用
     */
    public static final int ACCOUNT_INVALID_STATUS = 2;

    /**
     * 供应商被删除
     */
    public static final int ACCOUNT_DELETE_STATUS = 0;
    /**
     * 供应商未发送30天到期短信
     */
    public static final int ACCOUNT_NOSENDSMS_STATUS = 0;

    /**
     * 食品保健食品一级大类id
     */
    public static final Long COMM_CATEGORY_ID = 1L;

    /**
     * 商品分类code码
     */
    public static final String COMM_CATEGORY_CODE = "010000";
    /**
     * 批量上传模板表头名称
     *
     */
    public static final String[] EXCEL_TITLES ={"序号","*商品条码","商品商家编码","*商品名称","*商品图片（1-10张）","*商品品牌","*商品分类一级","*商品分类二级","*商品分类三级","*商品产地","*库存单位","*规格单位","*规格","*箱规","*供货价","*批发价","*生产日期","*有效期(天)","*最小起订量","*企业名称","供应商id","商品标签","*商品说明"};

    /**
     * 批量上传： 每条线程执行的条数
     */
    public static final int THREAD_NUM = 200;

    /**
     * 审核结果-审核未通过
     */
    public static final int AUDIT_NOT_PASS = 0;

    /**
     * 审核结果-审核通过
     */
    public static final int AUDIT_PASS = 1;

    /**
     * 审核结果-待审核
     */
    public static final int UN_AUDIT = 2;

    /**
     * 审核标志-历史审核记录
     */
    public static final int AUDIT_RECORD = 0;

    /**
     * 审核标志-当前审核记录
     */
    public static final int CURRENT_AUDIT = 1;


    /**
     * 批量导出模板表头名称
     *
     */
    public static final String[] EXCEL_OUT_TITLES ={"序号","*商品条码","商品商家编码","*商品名称","*商品图片（1-10张）","*商品品牌","*商品分类一级","*商品分类二级","*商品分类三级","*商品产地","*库存单位","*规格单位","*规格","*箱规","*供货价","*批发价","*生产日期","*有效期(天)","*最小起订量","*企业名称","供应商id","商品标签","*商品说明"};

    /**
     * 批量导出 每个工作表导出最大行数
     */
    public static final int MAX_ROWNUM = 600000;
    /**
     * 价格最大值
     */
    public static final BigDecimal MAX_VALUE = new BigDecimal(99999999);
}
