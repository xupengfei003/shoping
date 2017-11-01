package so.sao.shop.supplier.service.impl;

import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.ReceiptDao;
import so.sao.shop.supplier.domain.Receipt;
import so.sao.shop.supplier.service.ReceiptService;

import javax.annotation.Resource;

/**
 * <p>Version: supplier2 V1.1.0 </p>
 * <p>Title: ReceiptServiceImpl</p>
 * <p>Description: </p>
 *
 * @author: zhenhai.zheng
 * @Date: Created in 2017/11/1 10:08
 */
@Service
public class ReceiptServiceImpl implements ReceiptService {

    @Resource
    private ReceiptDao receiptDao;

    /**
     * 发票记录录入
     * @return
     */
    @Override
    public boolean insertReceipt(Receipt receipt) {

        /**
         * 1.根据发票类型校验参数
         *     若发票类型为1 表示增值税普通单位发票 单位名称，纳税人识别号，发票内容为必传参数
         *     若发票类型为2 表示增值税专用发票  单位名称，纳税人识别号，注册地址，注册电话，开户银行，银行账户为必传参数
         * 2.插入数据
         */
        return receiptDao.insertReceipt(receipt) > 0 ? true : false ;
    }

    /**
     * 发票信息更改
     *  根据发票ID修改发票记录
     * @param receipt
     * @return
     */
    @Override
    public boolean updateReceiptByUserId(Receipt receipt) {

        /**
         * 1.根据发票类型校验参数
         *     若发票类型为1 表示增值税普通单位发票 单位名称，纳税人识别号，发票内容为必传参数
         *     若发票类型为2 表示增值税专用发票  单位名称，纳税人识别号，注册地址，注册电话，开户银行，银行账户为必传参数
         * 2.更改数据
         */
        return receiptDao.updateReceiptByUserId(receipt) > 0 ? true : false;
    }

    /**
     * 根据主键查询发票记录
     * @param receiptId
     * @return
     */
    @Override
    public Receipt getReceiptById(Long receiptId) {
        return null;
    }

    /**
     * 根据门店ID和发票类型获取发票记录
     * @param userId
     * @param receiptType 1增值税普通单位发票 2增值税专用发票
     * @return
     */
    @Override
    public Receipt getReceiptByUserIdAndType(Long userId, Integer receiptType) {

        return receiptDao.getReceiptByUserIdAndType(userId,receiptType);
    }
}
