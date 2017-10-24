package so.sao.shop.supplier.pojo.vo;

/**
 * Created by liugang on 2017/10/11.
 */
public class QualificationImageVo extends QualificationImageUploadVo{

    /**
     * 资质类型（1、开户银行许可证 2、营业执照3、授权报告 4、质检报告 5、食品流通许可证）
     */
    private Integer qualificationType;

    public Integer getQualificationType() {
        return qualificationType;
    }

    public void setQualificationType(Integer qualificationType) {
        this.qualificationType = qualificationType;
    }
}
