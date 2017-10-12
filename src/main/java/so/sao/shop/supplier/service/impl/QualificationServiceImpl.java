package so.sao.shop.supplier.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import so.sao.shop.supplier.dao.QualificationDao;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.QualificationService;
import so.sao.shop.supplier.util.Ognl;

import java.util.Date;

/**
 * Created by liugang on 2017/10/11.
 * 供应商资质service
 */
@Service
public class QualificationServiceImpl implements QualificationService{

    @Autowired
    private QualificationDao qualificationDao;


    /**
     * 初始化日志
     */
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    /**
     * 资质审核 - 更新资质状态和时间
     * @param accountId
     * @param qualificationStatus
     * @return Result
     */

    @Override
    public Result updateQualificationStatus(Integer accountId, Integer qualificationStatus) {
        Date updateDate = new Date();
        qualificationDao.updateQualificationStatus( accountId, qualificationStatus,updateDate );
        return Result.success("更新成功");
    }

    /**
     * 查询登录供应商的资质状态
     * @param accountId
     * @return Result
     */
    @Override
    public Result getAccountQualificationStatus(Integer accountId) {
        Integer qualificationStatus = qualificationDao.getAccountQualificationStatus( accountId );
        if(Ognl.isNull(qualificationStatus)){
            logger.info("暂无数据");
            return Result.fail("暂无数据");
        }
        return Result.success("查询成功", qualificationStatus );
    }

}
