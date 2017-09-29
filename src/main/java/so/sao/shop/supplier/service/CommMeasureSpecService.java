package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommMeasureSpecUpdateInput;

/**
 * Created by XuPengFei on 2017/8/14.
 */
public interface CommMeasureSpecService {

    /**
     * 查询计量规格，查询出自己的和管理员前期添加的公共的
     * @param supplierId 供应商ID
     * @return Result 结果对象
     */
    public Result getCommMeasureSpe(long supplierId);

    /**
     * 新添加计量规格
     * @param supplierId 供应商ID
     * @param name 计量规格名称
     * @return Result 结果对象
     */
    public Result saveCommMeasureSpec(long supplierId, String name);


    /**
     * 根据计量规格主键id 删除对应的计量规格
     * @param id 计量规格主键id
     * @return boolean
     */
    public Result deleteById(Long supplierId, Long id);

    /**
     *根据计量规格的CommMeasureSpecUpdateInput更新计量规格
     * @param supplierId 供应商ID
     * @param commMeasureSpecUpdateInput 计量规格入参对象
     * @return Result 结果对象
     */
    public Result update(Long supplierId, CommMeasureSpecUpdateInput commMeasureSpecUpdateInput);
}
