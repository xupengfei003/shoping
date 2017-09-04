package so.sao.shop.supplier.pojo.output;

import com.github.pagehelper.PageInfo;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.vo.PurchaseInListVo;

import java.math.BigDecimal;


/**
 * <p>
 * 订单明细出参类
 * </p>
 *
 * @author 透云-中软-西安项目组-zhenhai.zheng
 * @since 2017-07-20
 **/
public class RecordToPurchaseItemOutput{
    private PurchaseInListVo purchaseInListVo;//订单明细所属订单
    /**
     * 分页工具
     */
    private PageInfo pageInfo;

    public PurchaseInListVo getPurchaseInListVo() {
        return purchaseInListVo;
    }

    public void setPurchaseInListVo(PurchaseInListVo purchaseInListVo) {
        this.purchaseInListVo = purchaseInListVo;
    }

    public PageInfo getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo pageInfo) {
        this.pageInfo = pageInfo;
    }

    @Override
    public String toString() {
        return "RecordToPurchaseItemOutput{" +
                "purchaseInListVo=" + purchaseInListVo +
                ", pageInfo=" + pageInfo +
                '}';
    }
}