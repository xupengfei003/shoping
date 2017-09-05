package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Mapper;

import so.sao.shop.supplier.domain.Emp;
import so.sao.shop.supplier.pojo.input.EmpInput;
import so.sao.shop.supplier.pojo.input.EmpUpdateInput;


import java.util.List;
/**
 * <p>Title: EmpDao</p>
 * <p>Description: 员工对象数据操作</p>
 * <p>Company:透云-中软-西安项目组 </p>
 * @author clown
 * @date 2017年8月11日
 */
@Mapper
public interface EmpDao {
	/**
	 * 新增员工信息
	 * @param emp 员工对象
	 * @return
	 */
	void save(Emp emp);

	/**
     * 根据条件查询出员工列表
     * @empInput empInput 入参
     * @return 返回查询结果
     */
    List<Emp> findPage(EmpInput empInput);

    /** 修改员工状态
	 * @param empUpdateInput 入参对象
	 * @return 返回修改状态
	 */
	void updateEmpStatusById(EmpUpdateInput empUpdateInput);
  
}
