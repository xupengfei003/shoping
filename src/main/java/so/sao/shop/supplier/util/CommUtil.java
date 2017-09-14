package so.sao.shop.supplier.util;

public class CommUtil {

    /**
     * 生成sku
     * @param commCategoryCode 商品分类code码
     * @param commId 商品Id
     * @param supplierId 供应商Id
     * @return sku
     */
    public static String createSku(String commCategoryCode, Long commId, Long supplierId){
        //前6位是商品分类，每级分类占两位,中间6位是商品表id自增(100000开始)
        Long commIdCode = 100000 + commId;
        //后面5位是供应商id字增(10000开始)
        Long supplierIdCode = 10000 + supplierId;
        return commCategoryCode + commIdCode.toString() + supplierIdCode.toString();
    }
}
