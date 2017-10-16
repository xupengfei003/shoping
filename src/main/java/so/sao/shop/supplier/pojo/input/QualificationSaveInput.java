package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotEmpty;
import so.sao.shop.supplier.pojo.vo.QualificationImagesVo;

import java.util.List;

public class QualificationSaveInput {
        /**
         * ID
         */
        @NotEmpty(message = "供应商ID不能为空")
        private Long accountId;
        /**
         * 资质图片
         */
        @NotEmpty(message = "资质类型不能为空")
        private List<QualificationImagesVo> imgs;

        public Long getAccountId() {
            return accountId;
        }

        public void setAccountId(Long accountId) {
            this.accountId = accountId;
        }

        public List<QualificationImagesVo> getImgs() {
            return imgs;
        }

        public void setImgs(List<QualificationImagesVo> imgs) {
            this.imgs = imgs;
        }
    }

