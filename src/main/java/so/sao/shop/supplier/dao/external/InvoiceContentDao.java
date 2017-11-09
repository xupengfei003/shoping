package so.sao.shop.supplier.dao.external;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.external.InvoiceContent;

import java.util.List;

/**
 * <p>Version: 运维平台 V0.9.0 </p>
 * <p>Title: InvoiceContent</p>
 * <p>Description:运维平台-发票内容Dao</p>
 *
 * @author: sha.chen
 * @Date: Created in 2017/10/30 15:00
 */
@Mapper
public interface InvoiceContentDao {
    /**
     * 添加发票内容
     * @param invoiceContent
     */
    void save(InvoiceContent invoiceContent);

    /**
     * 编辑某条发票内容
     * @param invoiceContent
     */
    void update(InvoiceContent invoiceContent);

    /**
     * 根据id删除发票内容
     * @param id
     */
    void delete(Long id);

    /**
     * 查询发票内容列表
     * @return 返回list集合
     */
    List<InvoiceContent> findAll();

    /**
     * 校验发票内容是否重复
     * @param invoiceContentName
     * @return
     */
    int countInvoiceContent(@Param("invoiceContentName") String invoiceContentName);

    /**
     * 统计发票内容总条数
     */
    int countTotal();
}
