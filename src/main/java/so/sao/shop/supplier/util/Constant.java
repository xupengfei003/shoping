package so.sao.shop.supplier.util;

/**
 * 常量
 *
 * @author
 * @create 2017-07-15 17:38
 **/
public class Constant {
    /**
     * request中存user的key
     */
    public static final String REQUEST_USER = "user";

    /**
     * 未上架
     */
    public static final String[] imgArray ={"jpg","jpeg","png","gif"};
    /**
     * 未上架
     */
    public static final int COMM_ST_XZ = 0;
    /**
     * 删除
     */
/*    public static final int COMM_ST_FC = 1;*/
    /**
     * 上架
     */
    public static final int COMM_ST_SJ = 2;
    /**
     * 下架
     */
    public static final int COMM_ST_XJ = 3;


    public static String getStatus(int status){
        String st="未上架";
        switch (status){
            case Constant.COMM_ST_XZ:
                st="未上架";
                break;
/*            case Constant.COMM_ST_FC:
                st="已废除";
                break;*/
            case Constant.COMM_ST_SJ:
                st="上架";
                break;
            case Constant.COMM_ST_XJ:
                st="下架";
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
     * 管理员标识 1
     */
    public static final String IS_ADMIN = "1";
    /**
     * 非管理员标识 0
     */
    public static final String IS_NOT_ADMIN = "0";

}
