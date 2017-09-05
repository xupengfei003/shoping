package so.sao.shop.supplier.pojo.output;

import so.sao.shop.supplier.pojo.vo.PurchaseInfoVo;

/**
 * <p>
 * 订单返回实体
 * </p>
 *
 * @author 透云-中软-西安项目组-wh
 * @since 2017-07-19
 */
public class PurchaseInfoOutput {
    private PurchaseInfoVo purchaseInfoVo;

    public PurchaseInfoVo getPurchaseInfoVo() {
        return purchaseInfoVo;
    }

    public void setPurchaseInfoVo(PurchaseInfoVo purchaseInfoVo) {
        this.purchaseInfoVo = purchaseInfoVo;
    }
}
