package so.sao.shop.supplier.service;

import com.github.pagehelper.PageInfo;
import so.sao.shop.supplier.domain.Emp;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.EmpInput;
import so.sao.shop.supplier.pojo.input.EmpUpdateInput;

/**
 * <p>Title: EmpService</p>
 * <p>Description: 员工表业务接口</p>
 * <p>Company:透云-中软-西安项目组 </p>
 * @author clown
 * @date 2017年8月11日
 */
public interface EmpService {
	/**
	 * 新增员工信息
	 * @param emp 员工对象
	 * @return 返回插入结果
	 */
	Result saveEmp(Emp emp);

	/**
     * 根据条件查询员工列表
     * @param empInput 页面参数：用户状态，用户名或者电话
     * @return 返回查询结果
     */
    Result<PageInfo> serachEmp(EmpInput empInput);

    /**
     * 根据id更新emp的状态
     * @param empUpdateInput
     * @return 返回更新状态
     */
    Result updateEmpStatus(EmpUpdateInput empUpdateInput);
}
