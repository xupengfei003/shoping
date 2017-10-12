package so.sao.shop.supplier.service;

import so.sao.shop.supplier.pojo.Result;

/**
 * Created by liugang on 2017/10/11.
 */
public interface QualificationService {

    Result updateQualificationStatus(Integer accountId, Integer qualificationStatus );

    Result getAccountQualificationStatus( Integer accountId );
}
