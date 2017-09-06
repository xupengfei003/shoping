package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotBlank;

import javax.validation.constraints.Size;

/**
 * 接收的-商品品类实体类
 * Created by XuPengFei on 2017/7/20.
 */
public class CommCategoryInput {

    /**
     *  PID
     */
    private Long pid;

    /**
     *  类型名称
     */
    @NotBlank(message = "类型名称不能为空")
    @Size(min = 1,max = 50,message = "商品类型名称长度不能超过50")
    private String name;

    /**
     *  备注
     */
    private String remark;

    /**
     *  类型的排序
     */
    private int sort;

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

}
