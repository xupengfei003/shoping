package so.sao.shop.supplier.pojo.input;

import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * Created by acer on 2017/8/14.
 */
public class EmpUpdateInput {
    /**
     * 用户id
     */
    @NotNull(message = "员工id")
    private Long empId;
    /**
     * 状态（1：启用；0禁用）
     */
    @NotEmpty(message = "员工状态")
    private String status;
    /**
     * 更新时间
     */
    private Date  updateAt;

    public Date getUpdateAt() {
        return updateAt;
    }

    public void setUpdateAt(Date updateAt) {
        this.updateAt = updateAt;
    }

    /**
     * 登录用户id
     */
    private Long userId;

    public Long getEmpId() {
        return empId;
    }

    public void setEmpId(Long empId) {
        this.empId = empId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
    	this.status = status;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
