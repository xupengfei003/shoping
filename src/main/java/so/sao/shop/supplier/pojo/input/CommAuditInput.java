package so.sao.shop.supplier.pojo.input;

/**
 * Created by acer on 2017/9/19.
 */
public class CommAuditInput {

    /**
     * 审核人
     */
    private  Long userId;
    /**
     * 审核结果
     */
    private  int auditResult;
    /**
     * 审核意见
     */

    private  String auditOpinion;
    /**
     * 审核记录集合
     */

    private Long[] ids;

    public Long getUserId() {return userId;}

    public void setUserId(Long userId) {this.userId = userId;}

    public int getAuditResult() {return auditResult;}

    public void setAuditResult(int auditResult) {this.auditResult = auditResult;}

    public String getAuditOpinion() {return auditOpinion;}

    public void setAuditOpinion(String auditOpinion) {this.auditOpinion = auditOpinion;}

    public Long[] getIds() {return ids;}

    public void setIds(Long[] ids) {this.ids = ids;}
}
