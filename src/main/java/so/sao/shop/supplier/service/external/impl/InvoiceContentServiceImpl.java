package so.sao.shop.supplier.service.external.impl;

import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.config.CommConstant;
import so.sao.shop.supplier.dao.external.InvoiceContentDao;
import so.sao.shop.supplier.domain.external.InvoiceContent;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.external.InvoiceContentService;
import so.sao.shop.supplier.util.PageTool;

import java.util.Date;
import java.util.List;

/**
 * <p>Version: 运维平台 V0.9.0 </p>
 * <p>Title: InvoiceContent</p>
 * <p>Description:运维平台-发票内容serviceImpl</p>
 *
 * @author: sha.chen
 * @Date: Created in 2017/10/30 15:00
 */
@Service
public class InvoiceContentServiceImpl implements InvoiceContentService {

    @Autowired
    private InvoiceContentDao invoiceContentDao;

    /**
     * 添加发票内容
     * @param invoiceContent
     * @return
     */
    @Override
    public Result saveInvocieContent(InvoiceContent invoiceContent) {
        int count = invoiceContentDao.countInvoiceContent(invoiceContent.getInvoiceContentName());
        if(count>0){
            return Result.fail("发票内容已存在！");
        }
        invoiceContent.setSort(CommConstant.SORT++);
        invoiceContent.setCreateAt(new Date());
        invoiceContent.setUpdateAt(new Date());
        int countTotal = invoiceContentDao.countTotal();
        if(countTotal>=10){
            return Result.fail("发票内容不能超过10条");
        }
        invoiceContentDao.save(invoiceContent);
        return Result.success("添加发票内容成功!");
    }

    /**
     * 编辑发票内容
     * @param invoiceContent
     * @return
     */
    @Override
    public Result updateInvoiceContent(InvoiceContent invoiceContent) {
        int count = invoiceContentDao.countInvoiceContent(invoiceContent.getInvoiceContentName());
        if(count>0){
            return Result.fail("发票内容已存在！");
        }
        invoiceContent.setUpdateAt(new Date());
        invoiceContentDao.update(invoiceContent);
        return Result.success("编辑发票内容成功!");
    }

    /**
     * 根据id删除发票内容
     * @param id
     * @return
     */
    @Override
    public Result delete(Long id) {
        invoiceContentDao.delete(id);
        return Result.success("删除成功！");
    }

    /**
     * 查询发票内容列表
     * @param pageNum
     * @param pageSize
     * @return
     */
    @Override
    public Result searchInvoiceContents(Integer pageNum, Integer pageSize) {
        PageTool.startPage(pageNum,pageSize);
        List<InvoiceContent> list = invoiceContentDao.findAll();
        PageInfo<InvoiceContent> pageInfo = new PageInfo<>(list);
        return Result.success("查询列表成功：",pageInfo);
    }
}
