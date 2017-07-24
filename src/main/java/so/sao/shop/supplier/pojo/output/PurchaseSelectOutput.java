package so.sao.shop.supplier.pojo.output;

import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.vo.PurchasesVo;

/**
 * Created by bzh on 2017/7/20.
 */
public class PurchaseSelectOutput extends BaseResult {
    /**
     * 分页后的订单列表
     */
    private com.github.pagehelper.PageInfo<PurchasesVo> PageInfo;

    public com.github.pagehelper.PageInfo<PurchasesVo> getPageInfo() {
        return PageInfo;
    }

    public void setPageInfo(com.github.pagehelper.PageInfo<PurchasesVo> pageInfo) {
        PageInfo = pageInfo;
    }
}
