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
     * 热门分类
     */
    private static final String HOT_CATEGEORY = "hotCategeroy";
    /**
     * 热门商品
     */
    private static final String HOT_COMMODITY = "hotCommodity";
    /**
     * 热门分类number
     */
    private static final int HOT_NUMBER = 1;
    /**
     * 未删除的商品
     */
    private static final boolean NOT_DELETED = false;

    /**
     * 根据查询条件查询商品详情
     * @param sku          SKU(商品ID)
     * @param supplierId     商品名称
     * @param code69       商品编码
     * @param suppCommCode 商家商品编码
     * @param inputValue   输入值（商品名称或分类名称）
     * @param minPrice     价格（低）
     * @param maxPrice     价格（高）
     * @param pageNum      当前页号
     * @param pageSize     页面大小
     * @return PageInfo pageInfo对象
     */
    @Override
    public Result searchAllCommodities(Long supplierId, String sku, String code69, String suppCommCode, String inputValue, BigDecimal minPrice, BigDecimal maxPrice, Integer pageNum, Integer pageSize) {
        Result result = new Result();
        Page page = new Page(pageNum, pageSize);
        //入参校验
        sku = stringParamCheck(sku);
        inputValue = stringParamCheck(inputValue);
        code69 = stringParamCheck(code69);
        suppCommCode =stringParamCheck(suppCommCode);
        if (null != minPrice && null != maxPrice)
        {
            if (DataCompare.compareMoney(minPrice,maxPrice))
            {
                throw new RuntimeException("最小金额不能大于最大金额");
            }
        }

        //分页参数校验
        page = PageUtil.pageCheck(page);
        //开始分页
        PageHelper.startPage(page.getPageNum(),page.getRows());
        List<CommAppSeachOutput> respList  = commAppDao.findAll(supplierId, sku, code69, suppCommCode, inputValue, minPrice, maxPrice);
        Long countTotal = commAppDao.countAllTotal(supplierId, sku, code69, suppCommCode, inputValue, minPrice, maxPrice);
        //查无数据直接返回
        if (respList.size()==0)
        {
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage("暂无商品数据");
            return result;
        }
        PageInfo<CommAppSeachOutput> pageInfo = new PageInfo<CommAppSeachOutput>(respList);
        pageInfo.setTotal(countTotal);
        result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
        result.setMessage("查询成功");
        result.setData(pageInfo);
        return result;
    }

    /**
     * 根据code69查询供应商列表
     * @param code69
     * @return 供应商列表
     */
    @Override
    public Result searchSuppliers(String code69) {
        Result result = new Result();
        List<Long> list = commAppDao.searchSuppliersByCode69(code69);
        if (list.size()!=0){
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage("成功");
            result.setData(list);
            return result;
        } else {
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage("查询的信息不存在");
            return result;
        }
    }

    /**
     * 字符串前后去空格
     * @param string  需要去空格的字符串
     * @return String 处理后的字符串
     */
    private String stringParamCheck(String string) {
        if (null != string)
        {
            string = string.trim();
        }
        return  string;
    }

    /**
     * 查询热门分类
     * @return Result结果集
     */
    @Override
    public Result searchCategories() {
        Result result = new Result();
       List<CategoryOutput> categoryOutputs = commAppDao.searchCategories();
        result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
        result.setMessage("查询商品分类成功");
        result.setData(categoryOutputs);
        return result;
    }

}
