package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.CommUnitUpdateInput;

/**
 * Created by Renzhiping on 2017/8/14.
 */
public interface CommUnitService {
    /**
     * 新增库存单位
     * @param supplierId 供应商id
     * @return 新增结果
     */
    Result saveCommUnit(Long supplierId,String name);

    /**
     * 修改库存单位
     * @param  supplierId 供应商id
     * @param commUnitUpdateInput 库存单位入参
     * @return 修改结果
     */
    Result updateCommunit(Long supplierId,CommUnitUpdateInput commUnitUpdateInput);

    /**
     * 删除库存单位
     * @param supplierId 供应商id
     * @param id 库存单位id
     * @return 删除结果
     */
    Result deleteCommUnit(Long supplierId, Long id);

    /**
     * 查询库存单位集合
     * @param supplierId 供应商id
     * @return 库存单位结果集
     */
    Result searchCommUnit(Long supplierId);
}
