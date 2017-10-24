package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.input.CommInventoryInfoInput;
import so.sao.shop.supplier.pojo.input.CommInventoryInput;
import so.sao.shop.supplier.pojo.output.CommInventoryInfoOutput;
import so.sao.shop.supplier.pojo.output.CommInventoryOutput;

import java.util.List;

/**
 * @author gxy on 2017/10/13.
 */
public interface CommInventoryService {
    /**
     * 商品库存查询接口
     * @param commInventoryInput 库存检索条件
     * @return List<CommInventoryOutput>
     * @throws Exception Exception
     */
    List<CommInventoryOutput> search(CommInventoryInput commInventoryInput) throws Exception;

    /**
     * 获取某商品库存信息
     * @param id 商品Id
     * @return CommInventoryInfoOutput
     * @throws Exception Exception
     */
    CommInventoryInfoOutput getInventoryById(Long id) throws Exception;

    /**
     * 更新某商品库存信息
     * @param commInventoryInfoInput commInventoryInfoInput
     * @throws Exception Exception
     */
    void updateInventoryById(CommInventoryInfoInput commInventoryInfoInput) throws Exception;

    /**
     * 更新商品库存
     * @param goodsList 商品ID list
     * @throws Exception Exception
     */
    void updateInventoryStatus(List<Long> goodsList) throws Exception;
}
