package so.sao.shop.supplier.service.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.config.sms.SmsService;
import so.sao.shop.supplier.dao.AccountDao;
import so.sao.shop.supplier.dao.EmpDao;
import so.sao.shop.supplier.dao.UserDao;
import so.sao.shop.supplier.domain.Emp;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.EmpInput;
import so.sao.shop.supplier.pojo.input.EmpUpdateInput;
import so.sao.shop.supplier.service.EmpService;
/**
 * <p>Title: EmpServiceImpl</p>
 * <p>Description: 员工业务实现</p>
 * <p>Company:透云-中软-西安项目组 </p>
 * @author clown
 * @date 2017年8月11日
 */
@Service
public class EmpServiceImpl implements EmpService {
	
	/**
	 * 初始化日志
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 员工表对应dao
	 */
	@Autowired
	private EmpDao empDao;
	
	/**
	 * 供应商对应dao
	 */
	@Autowired
	private AccountDao accountDao;
	
	/**
	 * 发送短信业务
	 */
	@Autowired
    private SmsService smsService;

	/**
	 * redisTemplate
	 */
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
    
    /**
     * 用户对应dao
     */
    @Autowired
    private UserDao userDao;
	
	/**
	 * 创建发送短信线程
	 */
	ExecutorService tpe = Executors.newFixedThreadPool(1);
	
	/**
     * 第一次发送密码
     */
    @Value("${shop.aliyun.sms.sms-template-code2}")
    String smsTemplateCode2;
	
	/**
	 * 新增员工信息
	 * @param emp 员工对象
	 * @return 返回插入结果
	 */
    @Transactional(rollbackFor = Exception.class)
	public Result saveEmp(Emp emp) throws Exception{
		//定义返回值
		Result baseResult = new Result();
		//判断登录用户是否为员工
		Long accountId = accountDao.findAccountIdByUserId(emp.getUserId());
        if(accountId == null) {
        	baseResult.setMessage("此登录账户为员工，无添加员工权限");
            baseResult.setCode(Constant.CodeConfig.CODE_FAILURE);
            return baseResult;
        }
		//利用redis的setnx，获取锁成功返回true
		Boolean lock = redisTemplate.opsForValue().setIfAbsent(Constant.REDIS_KEY_PREFIX+emp.getEmpTel(),"1");
        try {
        	 //返回值为1，根据员工电话号码增加用户和员工信息
			if(lock!=null&&lock) {
				User user1 = userDao.findByLoginName(emp.getEmpTel());
				if (user1 == null) {
					User user = new User();
                    user.setUsername(emp.getEmpTel());
                    String password = smsService.getVerCode();
                    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                    user.setPassword(encoder.encode(password));
                    user.setLastPasswordResetDate(new Date());
                    user.setIsAdmin("0");
                    userDao.add(user);
                    emp.setUserId(user.getId());
            		emp.setAccountId(accountId);
            		emp.setCreateAt(new Date());
            		emp.setUpdateAt(new Date());
            		emp.setEmpStatus("1");
            		empDao.save(emp);
            		//用户和员工信息增加成功后，采用多线程发送密码至员工手机
            		tpe.execute(new Runnable() {
                        @Override
                        public void run() {
                            smsService.sendSms(Collections.singletonList(emp.getEmpTel()),Arrays.asList("phone","password"), Arrays.asList(emp.getEmpTel(),password), smsTemplateCode2);
                        }
                    });
            		baseResult.setMessage("员工信息添加成功！");
                    baseResult.setCode(Constant.CodeConfig.CODE_SUCCESS);
                    return baseResult;
				}
			}
			baseResult.setMessage("此员工信息已经存在！");
	        baseResult.setCode(Constant.CodeConfig.CODE_FAILURE);
		} catch (Exception e) {
			logger.error("插入员工信息异常！",e);
			throw new Exception(e.getMessage());
		} finally {
			redisTemplate.delete(Constant.REDIS_KEY_PREFIX+emp.getEmpTel());
		}
        return baseResult;
	}

	/**
	 * 供应商查询员工信息
	 * @param empInput 查询入参对象
	 * @return 返回查询结果
	 */
	 @Override
    public Result< PageInfo> serachEmp(EmpInput empInput) {
        Result result = new Result();
        Long accountId = accountDao.findAccountIdByUserId(empInput.getUserId());
        if(accountId == null) {
        	result.setMessage("此登录账户不是供应商，无查询权限");
        	result.setCode(Constant.CodeConfig.CODE_FAILURE);
        	return result;
        }
        //判断当前页码和每页显示的条数是否为空
        if (null == empInput.getPageNum()){
			empInput.setPageNum(1);
        }
        if (null == empInput.getPageSize()){
			empInput.setPageSize(10);
        }
        //分页
        PageHelper.startPage(empInput.getPageNum(), empInput.getPageSize());
        empInput.setAccountId(accountId);
        PageInfo pageInfo = new PageInfo(empDao.findPage(empInput));
        result.setData(pageInfo);
        result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
        result.setMessage("查询成功");
        return result;
    }

	/**
	 * 修改员工状态
	 * @param empUpdateInput
	 * @return 返回修改状态
	 */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateEmpStatus(EmpUpdateInput empUpdateInput) {
            Result result = new Result();
            Long accountId = accountDao.findAccountIdByUserId(empUpdateInput.getUserId());
            if(accountId == null) {
            	result.setMessage("此登录账户不是供应商，无更新权限");
            	result.setCode(Constant.CodeConfig.CODE_FAILURE);
            	return result;
            }
			empUpdateInput.setUpdateAt(new Date());
            int updateNumber = empDao.updateEmpStatusById(empUpdateInput);
            //判断受影响的行数
            if (updateNumber > 0){
                result.setCode(Constant.CodeConfig.CODE_SUCCESS);
                result.setMessage("修改成功！");
                return result;
            }
            result.setCode(Constant.CodeConfig.CODE_FAILURE);
            result.setMessage("修改失败！");
            return result;
    }
}
