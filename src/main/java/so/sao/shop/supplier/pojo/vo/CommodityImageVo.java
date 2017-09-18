package so.sao.shop.supplier.pojo.vo;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by acer on 2017/9/11.
 */
public class CommodityImageVo {


    /**
     * scIdk供应商商品表id
     */
    private Long scId;
    /**
     * 商品条码
     */
    @Length(max = 20,message = "商品条码长度不能大于20")
    private String code69;
    /**
     *
     * Excel 图片
     */
    @NotBlank(message = "商品图片不能为空！")
    private  String  image;

    /**
     * 图片集合
     */
    @NotEmpty(message = "图片不能为空")
    @Valid
    private List<CommImgeVo> imgeList;

    public Long getScId() {
        return scId;
    }

    public void setScId(Long scId) {
        this.scId = scId;
    }

    public String getCode69() {
        return code69;
    }

    public void setCode69(String code69) {
        this.code69 = code69;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<CommImgeVo> getImgeList() {
        return imgeList;
    }

    public void setImgeList(List<CommImgeVo> imgeList) {
        this.imgeList = imgeList;
    }
}
