package so.sao.shop.supplier.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.domain.*;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.AccountInput;
import so.sao.shop.supplier.service.*;
import so.sao.shop.supplier.util.DownloadAzureFile;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.util.Ognl;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by xujc on 2017/7/18.
 */
@RestController
@RequestMapping("/account")
@Api(description = "供应商管理-所有接口")
public class AccountController {
	/**
	 * 初始化日志
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private AccountService accountService;

    @Autowired
    private AuthService authService;

    @Autowired
    private SysRegionService sysRegionService;

    /**
     * 单次添加供应商信息
     *
     * @param account 供应商信息
     * @return 返回添加结果
     */
    @ApiOperation(value = "添加供应商信息",notes = "添加供应商信息【负责人：张腾飞】")
    @PostMapping("/save")
    public Result save(HttpServletRequest request, @Valid @RequestBody Account account) throws Exception{
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user==null || !user.getIsAdmin().equals(Constant.ADMIN_STATUS)){
            return Result.fail(Constant.MessageConfig.ADMIN_AUTHORITY_EERO);
        }
        /**
         * 插入用户和供应商信息
         */
		return accountService.saveUserAndAccount(account);
    }

    /**
     * 更新供应商信息
     *
     * @param account 供应商对象
     * @return 返回更新成功或者失败
     */
    @ApiOperation(value = "修改供应商信息",notes = "修改供应商信息【负责人：】")
    @PutMapping("/update")
    public Result update(@Valid @RequestBody Account account, HttpServletRequest request) throws Exception{
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user==null || !user.getIsAdmin().equals(Constant.ADMIN_STATUS)){
            return Result.fail(Constant.MessageConfig.ADMIN_AUTHORITY_EERO);
        }
        return accountService.updateAccountAndUser(account);
    }

    /**
     * 根据id删除供应商信息
     *
     * @param id
     * @return 返回成功或失败
     */
    @ApiOperation(value = "删除供应商信息",notes = "删除供应商信息【负责人：】")
    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id, HttpServletRequest request) throws Exception{
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user==null || !user.getIsAdmin().equals(Constant.ADMIN_STATUS)){
            return Result.fail(Constant.MessageConfig.ADMIN_AUTHORITY_EERO);
        }
        return accountService.delete(id);
    }

    /**
     * 初始化银行信息
     *
     * @return
     */
    @ApiOperation(value = "银行信息",notes = "")
    @GetMapping("/selectBank")
    public List<String> selectBank() {
        return accountService.selectBank();
    }

    /**
     * 初始化行业信息
     *
     * @return
     */
    @ApiOperation(value = "行业信息",notes = "")
    @GetMapping("/selectHangYe")
    public List<String> selectHangYe() {
        return accountService.selectHangYe();
    }

    /**
     * 初始化行业信息
     *
     * @return
     */
    @ApiOperation(value = "行业信息(字典项)",notes = "")
    @GetMapping("/selectHangYeDict")
    public List<DictItem> selectHangYeDict() {
        return accountService.selectHangYeDict();
    }

    /**
     * 登陆获取token
     *
     * @param authenticationRequest
     * @return
     * @throws AuthenticationException
     * @throws IOException
     */
    @ApiOperation(value = "登陆",notes = "")
    @PostMapping(value = "${jwt.route.authentication.path}")
    public Result<String> createAuthenticationToken(
            @RequestBody User authenticationRequest, HttpServletResponse response) throws AuthenticationException, IOException {

        return authService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());
    }

    /**
     * 获取admin标记(一期暂时)
     * @param request
     * @return
     */
    @ApiOperation(value = "获取admin标记",notes = "")
    @GetMapping(value = "/isAdmin")
    public Result isAdmin(HttpServletRequest request){
        Map result = new HashMap();
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        result.put("isAdmin",user.getIsAdmin());
        return Result.success("", result);
    }

    /**
     * 获取当前登陆供应商名
     * @param request
     * @return
     */
    @ApiOperation(value = "获取当前登陆供应商名",notes = "")
    @GetMapping(value = "/username")
    public Result getUserName(HttpServletRequest request){
        Map result = new HashMap();
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        Account account = accountService.selectById(user.getAccountId());
        result.put("username",account!=null?account.getProviderName():"");
        return Result.success("", result);
    }

    /**
     * 登出,设置登出时间
     *
     * @return
     */
    @ApiOperation(value = "登出",notes = "")
    @PostMapping(value = "/logout")
    public Result<String> logout(HttpServletRequest request) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        return authService.logout(user);
    }

    /**
     * 根据用户ID查询用户的实时余额
     * @param request
     * @return
     */
    @ApiOperation(value = "根据账户ID查询用户余额", notes = "根据用户的账户ID,查询未结算金额【负责人：方洲】")
    @GetMapping(value = "/selectAccountBalance")
    public Result getBalanceByAccountId(HttpServletRequest request) throws Exception{
        //获取用户
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //定义map存放余额
        Map<String,String > map = new HashMap();
        //判断是否登陆
        if (Ognl.isNull(user)){
            //若没登陆，返回余额0.00
            map.put("balance","0.00");
            return Result.fail(Constant.MessageConfig.MSG_USER_NOT_LOGIN,map);
        }
        //查询用户余额
        map = accountService.getAccountBalance(user.getAccountId());
        return Result.success(Constant.MessageConfig.MSG_SUCCESS,map);
    }

    /**
     * 忘记密码,手机号获取密码
     *
     * @param tel
     * @return
     * @throws IOException
     */
    @ApiOperation(value = "忘记密码",notes = "")
    @GetMapping(value = "/findPassword/{tel}")
    public Result getPassword(HttpServletRequest request, @PathVariable String tel) throws IOException {
        return authService.getPassword(tel);
    }

    /**
     * 根据条件查询供应商列表
     *
     * @param accountInput
     * @return
     */
    @GetMapping(value = "/findAccount")
    @ApiOperation(value = "查询供应商列表" , notes = "负责人：唐文斌")
    public PageInfo search(AccountInput accountInput, HttpServletRequest request)  {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user==null || !user.getIsAdmin().equals(Constant.ADMIN_STATUS)){
            throw new RuntimeException("unauthorized access");

        }
        return accountService.searchAccount(accountInput);
    }

    /**
     * 根据id查询供应商详细信息
     *
     * @param id
     * @return
     */

    @GetMapping("/search")
    @ApiOperation(value = "根据id查询供应商(省市区汉字)",notes = "")
    public Account get(@RequestParam(required = false) Long id, HttpServletRequest request){
        id = getAccountId(id, request);
        return accountService.selectById(id);
    }

    /**
     * 根据id查询供应商详细信息
     *
     * @param id
     * @return
     */
    @GetMapping("/search0")
    @ApiOperation(value = "根据id查询供应商(省市区编码)",notes = "")
    public Account get0(@RequestParam(required = false) Long id, HttpServletRequest request) {
        id = getAccountId(id, request);
        return accountService.selectById0(id);
    }

    /**
     * 根据当前登录人角色获取accountId
     * @param id
     * @param request
     * @return
     */
    private Long getAccountId(Long id, HttpServletRequest request){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user==null){
            throw new RuntimeException("unauthorized access");
        }else if(user.getIsAdmin().equals(Constant.ADMIN_STATUS )&&(id == null || id == 0)){
            throw new RuntimeException("unauthorized access");
        }else if(!user.getIsAdmin().equals(Constant.ADMIN_STATUS )){
            id = user.getAccountId();
        }
        return id;
    }

    /**
     * 查询当前登录用户登录手机号码
     * @return 当前登录用户登录手机号码
     */
    @GetMapping("/LoginPhone")
    @ApiOperation(value = "查询当前登录用户手机号码",notes = "查询当前登录用户手机号码【负责人：汪涛】")
    public Result getLoginPhone(HttpServletRequest request){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user!=null) {
            String tel=user.getUsername();
            return Result.success("查询成功",tel);
        }else {
            return Result.fail("获取用户登录信息异常");
        }
    }

    /**
     * 发送验证码
     * @param request
     * @return 验证码信息
     * @throws IOException
     */
    @ApiOperation(value = "发送验证码",notes = "发送验证码【负责人：汪涛】")
    @GetMapping(value = "/sendCode")
    public Result sendCode(HttpServletRequest request) throws IOException {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(null==user){
            return Result.fail("登录验证不通过");
        }
        String tel=user.getUsername();
        return authService.sendCode(tel);
    }

    /**
     * 检验验证码
     *
     * @param code
     * @return
     */
    @ApiOperation(value = "检验验证码",notes = "检验验证码【负责人：汪涛】")
    @GetMapping(value = "/verifySmsCode/{code}")
    public Result verifySmsCode(HttpServletRequest request, @PathVariable String code) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(null==user){
            return Result.fail("登录验证不通过");
        }
        if(null==code||code==""){
            return Result.fail("验证码无效");
        }
        return authService.verifySmsCode(user, code);
    }

    /**
     * 密码修改
     *
     * @param request
     * @param encodedPassword
     * @return
     */
    @ApiOperation(value = "密码修改",notes = "密码修改【负责人：汪涛】")
    @PutMapping(value = "/updatePassword/{encodedPassword}")
    public Result updatePassword(HttpServletRequest request, @PathVariable String encodedPassword) throws IOException {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(null==user){
            return Result.fail("登录验证不通过");
        }
        return authService.updatePassword(user.getId(), encodedPassword);
    }

    /**
     * 获取地区下级列表
     *
     * @param pid 地区id
     * @return 返回下级列表
     */
    @ApiOperation(value = "获取地区下级列表",notes = "获取地区下级列表【负责人：张腾飞】")
    @GetMapping("get")
    public List<SysRegion> findByPid(@RequestParam(required = false) Integer pid) {
        return sysRegionService.findByPid(pid);
    }

    /**
     * 统计已入驻供应商数量
     * @return 已入驻供应商数量
     */
    @ApiOperation(value = "统计已入驻供应商数量",notes = "统计已入驻供应商数量【负责人：汪涛】")
    @GetMapping(value = "/AccountNumber")
    public Result getAccountNumber(){
         int num=accountService.selectAccountNumber();
         return Result.success("查询成功",num);
    }
    
    /**
     * 供应商合同上传
     * @param request
     * @param multipartFile 合同
     * @return 返回合同云存储地址
     */
    @ApiOperation(value = "供应商合同上传",notes = "供应商合同上传【负责人：张腾飞】")
    @PostMapping(value = "/uploadContract")
    public Result uploadContract(HttpServletRequest request,@RequestPart("file") MultipartFile multipartFile,String blobName) {
    	User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user==null || !user.getIsAdmin().equals(Constant.ADMIN_STATUS)){
        	return Result.fail(so.sao.shop.supplier.config.Constant.MessageConfig.ADMIN_AUTHORITY_EERO);
        }
    	return accountService.uploadContract(multipartFile,blobName);
    }
    
    /**
     * 云端合同下载文件转换
     * @param downloadUrl 文件云端地址
     * @param realFileName 文件云端名称
     * @param request
     * @param response
     * @return
     */
    @ApiOperation(value = "供应商合同下载",notes = "供应商合同下载【负责人：张腾飞】")
    @GetMapping(value = "/downloadContract")
    public Result download(@RequestParam String downloadUrl, @RequestParam String realFileName, HttpServletRequest request, HttpServletResponse response) throws Exception{
    	User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user==null){
        	return Result.fail("请登录后再操作");
        }
        return DownloadAzureFile.downloadFile(downloadUrl, realFileName, request, response);
    }
}
