package so.sao.shop.supplier.pojo.input;


/**
 * Created by acer on 2017/8/14.
 */
public class EmpInput {
    /**
     * 状态
     */
    private String status;
    /**
     * 页数
     */
    private Integer pageNum;
    /**
     * 每页的数量
     */

     private Integer pageSize;
    /**
     * 员工姓名或电话
     */
    private String empNameOrTel;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 供应商id
     */
    private Long accountId;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getPageNum() {
            return pageNum;
        }

    public void setPageNum(Integer pageNum) {
        this.pageNum = pageNum;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

    public String getEmpNameOrTel() {
        return empNameOrTel;
    }

    public void setEmpNameOrTel(String empNameOrTel) {
        this.empNameOrTel = empNameOrTel;
    }

    public Long getAccountId() {
        return accountId;
        }

    public void setAccountId(Long accountId) {
    this.accountId = accountId;
}
}
