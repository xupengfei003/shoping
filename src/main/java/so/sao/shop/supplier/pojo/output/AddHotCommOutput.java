package so.sao.shop.supplier.pojo.output;

import so.sao.shop.supplier.config.CommConstant;
import so.sao.shop.supplier.pojo.vo.AddHotCommVo;

/**
 * Created by acer on 2017/9/19.
 */
public class AddHotCommOutput extends AddHotCommVo{
    /**
     * 商品销量
     */
    private Integer  salesVolume;

    /**
     * 商品状态名
     */
    private String statusName;


    public int getSalesVolume() {
        return salesVolume;
    }

    public void setSalesVolume(int salesVolume) {
        this.salesVolume = salesVolume;
    }

    public String getStatusName() {
        return this.statusName = CommConstant.getStatus(super.getStatus());
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        return super.toString() + ","
                + salesVolume + ","
                 + statusName;
    }
}
