package so.sao.shop.supplier.service.impl;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.dao.QualificationDao;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.QualificationInput;
import so.sao.shop.supplier.pojo.output.QualificationListOut;
import so.sao.shop.supplier.pojo.output.QualificationOut;
import so.sao.shop.supplier.service.QualificationService;
import so.sao.shop.supplier.util.Ognl;
import so.sao.shop.supplier.util.PageTool;

import java.util.Date;
import java.util.List;

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
     * 资质审核 - 更新资质状态和时间,拒绝原因
     * @param accountId
     * @param qualificationStatus
     * @param reason
     * @return Result
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateQualificationStatus(Integer accountId, Integer qualificationStatus, String reason) {
        if( !(Constant.QUALIFICATION_VERIFY_PASS == qualificationStatus)
                && !(Constant.QUALIFICATION_VERIFY_NOT_PASS == qualificationStatus)
                && !(Constant.QUALIFICATION_AWAIT_VERIFY == qualificationStatus ) ){
            logger.info("无效的审核参数");
            return Result.fail("无效的审核参数");
        }
        Date updateDate = new Date();
        qualificationDao.updateQualificationStatus( accountId, qualificationStatus,updateDate );
        qualificationDao.updateQualificationReason( reason, accountId );
        return Result.success("更新成功");
    }

    /**
     * 查询登录供应商的资质状态
     * @param accountId
     * @return Result
     */
    @Override
    public Result getAccountQualificationStatus(Long accountId) {
        Integer qualificationStatus = qualificationDao.getAccountQualificationStatus( accountId );
        if(Ognl.isNull(qualificationStatus)){
            logger.info("暂无数据");
            return Result.fail("暂无数据");
        }
        return Result.success("查询成功", qualificationStatus );
    }

    @Override
    public Result findByAccountId(Long accountId) {
        List<QualificationOut> qualificationOuts = qualificationDao.findByAccountId(accountId);
        if(qualificationOuts.size()>0) {
            return Result.success("根据供应商id查询供应商资质详情成功", qualificationOuts);
        }
        return Result.fail("没有符合条件的供应商资质详情");
    }

    @Override
    public Result searchQualifications(QualificationInput qualificationInput) {
        PageTool.startPage(qualificationInput.getPageNum(), qualificationInput.getPageSize());
        List<QualificationListOut> qualificationListOuts = qualificationDao.findPage(qualificationInput);
        if(qualificationListOuts!=null && qualificationListOuts.size()>0){
            PageInfo pageInfo = new PageInfo(qualificationListOuts);
            return Result.success("查询成功",pageInfo);
        }
        return Result.fail("没有符合条件的资质信息");
    }
}
