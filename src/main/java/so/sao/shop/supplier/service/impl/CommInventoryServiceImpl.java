package so.sao.shop.supplier.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.dao.SupplierCommodityDao;
import so.sao.shop.supplier.pojo.input.CommInventoryInfoInput;
import so.sao.shop.supplier.pojo.input.CommInventoryInput;
import so.sao.shop.supplier.pojo.output.CommInventoryInfoOutput;
import so.sao.shop.supplier.pojo.output.CommInventoryOutput;
import so.sao.shop.supplier.service.CommInventoryService;
import so.sao.shop.supplier.util.PageTool;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author gxy on 2017/10/13.
 */
@Service
public class CommInventoryServiceImpl implements CommInventoryService {
    @Resource
    private SupplierCommodityDao supplierCommodityDao;

    /**
     * 商品库存查询接口
     * @param commInventoryInput 库存检索条件
     * @return List<CommInventoryOutput>
     * @throws Exception Exception
     */
    @Override
    public List<CommInventoryOutput> search(CommInventoryInput commInventoryInput) throws Exception {
        PageTool.startPage(commInventoryInput.getPageNum(), commInventoryInput.getPageSize());
        return supplierCommodityDao.searchCommInventory(commInventoryInput);
    }

    /**
     * 获取某商品库存信息
     * @param id 商品Id
     * @return CommInventoryInfoOutput
     * @throws Exception Exception
     */
    @Override
    public CommInventoryInfoOutput getInventoryById(Long id) throws Exception {
        /*//根据商品的id查询出商品的状态
        int status = supplierCommodityDao.findSupplierCommStatus(id);
        //如果是商品的状态是 6 (编辑待审核),则根据供应商商品ID获取编辑后的商品信息
        CommInventoryInfoOutput commInventoryInfoOutput;
        if (status > 0) {
            commInventoryInfoOutput = supplierCommodityDao.getInventoryByTmpId(id);
        } else {
            commInventoryInfoOutput = supplierCommodityDao.getInventoryById(id);
        }*/
        return supplierCommodityDao.getInventoryById(id);
    }

    /**
     * 更新某商品库存信息
     * @param commInventoryInfoInput commInventoryInfoInput
     * @throws Exception Exception
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateInventoryById(CommInventoryInfoInput commInventoryInfoInput) throws Exception {
        if(commInventoryInfoInput.getInventory() <= commInventoryInfoInput.getInventoryMinimum()){
            commInventoryInfoInput.setInventoryStatus(1);
        } else {
            commInventoryInfoInput.setInventoryStatus(0);
        }
        //根据商品的id查询出商品的状态
        int status = supplierCommodityDao.findSupplierCommStatus(commInventoryInfoInput.getId());
        if (status > 0) { //如果是商品的状态是6(编辑待审核),则更新商品tmp表中的数据
            supplierCommodityDao.updateInventoryByTmpScaId(commInventoryInfoInput);
        }
        supplierCommodityDao.updateInventoryById(commInventoryInfoInput);
    }
}
