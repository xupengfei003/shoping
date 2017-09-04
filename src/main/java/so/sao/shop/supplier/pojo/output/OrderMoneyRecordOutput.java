package so.sao.shop.supplier.pojo.output;

import com.github.pagehelper.PageInfo;
import so.sao.shop.supplier.pojo.vo.OrderMoneyRecordVo;


/**
 * Created by fangzhou on 2017/7/20.
 */
public class OrderMoneyRecordOutput {

    private PageInfo<OrderMoneyRecordVo> pageInfo;

    public PageInfo<OrderMoneyRecordVo> getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageInfo<OrderMoneyRecordVo> pageInfo) {
        this.pageInfo = pageInfo;
    }

}
