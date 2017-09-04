package so.sao.shop.supplier.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.CommAppDao;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.output.CategoryOutput;
import so.sao.shop.supplier.service.CommAppService;
import so.sao.shop.supplier.util.DataCompare;
import so.sao.shop.supplier.util.PageTool;
import so.sao.shop.supplier.util.PageUtil;
import so.sao.shop.supplier.pojo.output.CommAppSeachOutput;
import so.sao.shop.supplier.pojo.vo.Page;
import java.math.BigDecimal;

import java.util.*;

@Service
public class CommAppServiceImpl implements CommAppService {

    @Autowired
    private CommAppDao commAppDao;

    /**
     * 根据查询条件查询商品详情
     * @param sku          SKU(商品ID)
     * @param supplierId     供应商ID
     * @param code69       商品条码
     * @param suppCommCode 商家商品编码
     * @param inputValue   输入值（商品名称或科属名称）
     * @param minPrice     价格（低）
     * @param maxPrice     价格（高）
     * @param pageNum      当前页号
     * @param pageSize     页面大小
     * @return PageInfo pageInfo对象
     */
    @Override
    public Result searchAllCommodities(Long supplierId, String sku, String code69, String suppCommCode, String inputValue, BigDecimal minPrice, BigDecimal maxPrice, Integer pageNum, Integer pageSize) {

        //金额校验
        String msg = DataCompare.priceCheck(minPrice,maxPrice);
        if(null != msg && msg.length()>0) {
            return Result.fail(msg);
        }
        //开始分页
        PageTool.startPage(pageNum,pageSize);
        List<CommAppSeachOutput> respList  = commAppDao.findAll(supplierId, sku, code69, suppCommCode, inputValue, minPrice, maxPrice);
        PageInfo<CommAppSeachOutput> pageInfo = new PageInfo<CommAppSeachOutput>(respList);
        return Result.success("查询成功",pageInfo);
    }

    /**
     * 根据code69查询供应商列表
     * @param code69
     * @return 供应商列表
     */
    @Override
    public Result searchSuppliers(String code69) {
        List<Long> list = commAppDao.searchSuppliersByCode69(code69);
        return Result.success("成功",list);
    }


    /**
     * 查询商品科属
     * @return Result结果集
     */
    @Override
    public Result searchCategories() {
        List<CategoryOutput> categoryOutputs = commAppDao.searchCategories();
        return Result.success("查询商品科属成功",categoryOutputs);
    }
}
