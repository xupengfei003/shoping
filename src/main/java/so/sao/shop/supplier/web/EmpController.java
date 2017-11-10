package so.sao.shop.supplier.web;

import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.Emp;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.EmpInput;
import so.sao.shop.supplier.pojo.input.EmpUpdateInput;
import so.sao.shop.supplier.service.EmpService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * <p>Title: EmpController</p>
 * <p>Description: 员工接口</p>
 * <p>Company:透云-中软-西安项目组 </p>
 * @author clown
 * @date 2017年8月11日
 */
@RestController
@RequestMapping("/emp")
public class EmpController {
	/**
	 * 初始化日志
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 注入员工业务层
	 */
	@Autowired
	private EmpService empService;

	/**
	 * 新增员工信息
	 * @param emp 员工对象
	 * @return 返回插入结果
	 */
	@PutMapping("/create")
	public Result create(HttpServletRequest request,@Valid @RequestBody Emp emp) throws Exception{
		User user = (User) request.getAttribute(Constant.REQUEST_USER);
		if(user == null) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
		}
    	/**
    	 * 插入员工信息
    	 */
        emp.setUserId(user.getId());
		return empService.saveEmp(emp);
	}

    /**
     * 查找员工列表
     * @param empInput 入参对象
     * @param request
     * @return 返回查询结果
     */
    @GetMapping("/findemp")
    public Result<PageInfo> search(EmpInput empInput, HttpServletRequest request) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //验证是否登录
        if (user == null) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }        
        empInput.setUserId(user.getId());
        return empService.serachEmp(empInput);
    }

    /**
     * 修改员工状态
     * @param empUpdateInput 入参对象
     * @param request
     * @return 返回修改状态结果
     */
    @PutMapping("/updateStatus")
    public Result updateStatus(@Valid @RequestBody EmpUpdateInput empUpdateInput, HttpServletRequest request) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
      //验证是否登录
        if (user == null) {
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
        }
        empUpdateInput.setUserId(user.getId());
        return empService.updateEmpStatus(empUpdateInput);
    }
}
