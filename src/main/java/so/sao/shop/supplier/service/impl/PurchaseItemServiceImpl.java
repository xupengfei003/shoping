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
         * 1  判断当前页码和每页显示条数是否为空
         *      1.1 若为空或输入数据不恰当（非正数） 赋予默认值（当前页码为1,每页显示条数为10）
         * 2  访问持久化层#findById(String orderId),根据订单编号检索该订单数据
         * 3  判断#findById(String orderId) 返回数据是否为空
         *      3.1 若为空，返回‘暂无订单数据’的信息
         * 4  使用分页工具进行分页设置
         * 5  访问持久化层#findPage(String orderId)。根基订单编号检索改订单下所有订单明细
         * 6  判断#findPage(String orderId)返回数据市口为空
         *      6.1 若为空，返回‘暂无订单数据’的信息
         * 7  将订单及改订单下的订单明细封装到出参中，并封装成功信息   封装分页信息，  并返回
         */
        Result<RecordToPurchaseItemOutput> result = new Result<>();//出参对象
        result.setCode(Constant.CodeConfig.CODE_NOT_FOUND_RESULT);
        result.setMessage(Constant.MessageConfig.MSG_NOT_FOUND_RESULT);
        result.setData(null);

        //1.1 判断当前页码和每页显示条数是否为空
        if (null==pageNum || 0 >= pageNum){
            pageNum = 1;
        }
        if (null==pageSize || 0 >= pageSize){
            pageSize = 10;
        }

        //2.获取该订单编号对应的数据
        Purchase purchase = purchaseDao.findById(orderId);

        //3. 判断数据是否为空,若为空，返回‘暂无订单数据’的信息
        if (null == purchase){
            return result;
        }

        //4. 分页
        PageHelper.startPage(pageNum,pageSize);

        //5. 访问持久化层获取数据
        List<AccountPurchaseItemVo> purchaseItemList = purchaseItemDao.findPage(orderId);

        if(null != purchaseItemList && !purchaseItemList.isEmpty()){
            PageInfo<AccountPurchaseItemVo> pageInfo = new PageInfo<>(purchaseItemList);

            //4.将转化后的数据集合封装到PageInfo对象中,封装成功信息和code
            PurchaseInListVo purchaseInListVo = new PurchaseInListVo(purchase.getOrderId(),purchase.getOrderPrice(),purchase.getOrderReceiverName());
            RecordToPurchaseItemOutput output = new RecordToPurchaseItemOutput();
            output.setPurchaseInListVo(purchaseInListVo);
            output.setPageInfo(pageInfo);
            result.setCode(Constant.CodeConfig.CODE_SUCCESS);
            result.setMessage(Constant.MessageConfig.MSG_SUCCESS);
            result.setData(output);
        }
        return result;
    }

}
