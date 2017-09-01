package so.sao.shop.supplier.web;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.ApiOperation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.domain.Emp;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.EmpInput;
import so.sao.shop.supplier.pojo.input.EmpUpdateInput;
import so.sao.shop.supplier.service.EmpService;

/**
 * <p>Title: EmpController</p>
 * <p>Description: 员工接口</p>
 * <p>Company:透云-中软-西安项目组 </p>
 * @author clown
 * @date 2017年8月11日
 */
@RestController
@RequestMapping("/emp")
@Api(description = "员工接口")
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
	@ApiOperation(value="新增员工信息", notes="")
	@PutMapping("/create")
	public Result create(HttpServletRequest request,@Valid @RequestBody Emp emp,BindingResult result) throws Exception{
		User user = (User) request.getAttribute(Constant.REQUEST_USER);
		if(user == null) {
            return Result.fail("请登录后再操作");
		}
		//判断验证是否通过。true 未通过  false通过
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError error : list) {
                return Result.fail(error.getDefaultMessage());
            }
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
    @ApiOperation(value = "查找员工列表",notes = "负责人：唐文斌")
    public Result<PageInfo> search(EmpInput empInput, HttpServletRequest request) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //验证是否登录
        if (user == null) {
            return Result.fail("请登录后再操作");
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
    @ApiOperation(value = "修改员工状态",notes = "负责人：唐文斌")
    public Result updateStatus(@Valid @RequestBody EmpUpdateInput empUpdateInput, HttpServletRequest request,BindingResult bndresult) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
      //验证是否登录
        if (user == null) {
            return Result.fail("请登录后再操作");
        }
        
      //判断验证是否通过。true 未通过  false通过
        if(bndresult.hasErrors()) {
            List<ObjectError> list = bndresult.getAllErrors();
            for (ObjectError error : list) {
            	return Result.fail(error.getDefaultMessage());
            }
        }
        empUpdateInput.setUserId(user.getId());
        return empService.updateEmpStatus(empUpdateInput);
    }
}
