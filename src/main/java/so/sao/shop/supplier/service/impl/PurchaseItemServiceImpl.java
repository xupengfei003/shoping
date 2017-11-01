package so.sao.shop.supplier.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.PurchaseDao;
import so.sao.shop.supplier.dao.PurchaseItemDao;
import so.sao.shop.supplier.domain.Purchase;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.output.RecordToPurchaseItemOutput;
import so.sao.shop.supplier.pojo.vo.AccountPurchaseItemVo;
import so.sao.shop.supplier.pojo.vo.PurchaseInListVo;
import so.sao.shop.supplier.service.PurchaseItemService;
import so.sao.shop.supplier.util.NumberUtil;
import so.sao.shop.supplier.util.PageTool;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

/**
 * Created by niewenchao on 2017/7/19.
 */
@Service
public class PurchaseItemServiceImpl implements PurchaseItemService {

    @Autowired
    PurchaseItemDao purchaseItemDao;
    @Autowired
    PurchaseDao purchaseDao;

    /**
     * 根据订单编号查询所有相关的订单明细记录（分页）
     * @param pageNum 当前页码
     * @param pageSize 每页显示条数
     * @param orderId 订单编号
     * @return 相关记录的集合
     */
    @Override
    public Result<RecordToPurchaseItemOutput> searchPurchaseItems(Integer pageNum, Integer pageSize, String orderId) {
        /**
         * 1  访问持久化层#findById(String orderId),根据订单编号检索该订单数据
         *      1.1  判断#findById(String orderId) 返回数据是否为空，为空返回
         * 2  使用分页工具进行分页设置
         * 3  访问持久化层#findPage(String orderId)。根据订单编号检索该订单下的明细
         * 4  封装出参信息(code、message、data)并返回
         */

        //1.获取该订单编号对应的数据
        Purchase purchase = purchaseDao.findById(orderId);
        if (null == purchase){
            return Result.success(Constant.MessageConfig.MSG_NO_DATA);
        }

        //2. 分页
        PageTool.startPage(pageNum,pageSize);

        //3. 访问持久化层获取数据
        List<AccountPurchaseItemVo> purchaseItemList = purchaseItemDao.findPage(orderId);
        if(null != purchaseItemList && !purchaseItemList.isEmpty()){
            //分页信息
            PageInfo<AccountPurchaseItemVo> pageInfo = new PageInfo<>(purchaseItemList);
            //订单明细所属的订单信息
            PurchaseInListVo purchaseInListVo = new PurchaseInListVo(purchase.getOrderId(),purchase.getOrderPrice(),purchase.getOrderReceiverName());
            //出参中的数据对象
            RecordToPurchaseItemOutput output = new RecordToPurchaseItemOutput();
            //将展示数据封装到出参数据对象中
            output.setPurchaseInListVo(purchaseInListVo);
            //将分页对象封装到出参对象中
            output.setPageInfo(pageInfo);
            return Result.success(Constant.MessageConfig.MSG_SUCCESS,output);
        }
        return Result.success(Constant.MessageConfig.MSG_NO_DATA);

    }


}
