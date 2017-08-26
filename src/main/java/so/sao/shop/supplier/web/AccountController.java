package so.sao.shop.supplier.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.domain.*;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.service.*;
import so.sao.shop.supplier.util.ExcelImportUtils;
import so.sao.shop.supplier.config.Constant;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.math.BigDecimal;
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
    private SupplierRecordService supplierRecordService;

    @Autowired
    private SysRegionService sysRegionService;

    @Autowired
    private ImportExcel importExcel;

    /**
     * 单次添加供应商信息
     *
     * @param account 供应商信息
     * @return 返回添加结果
     */
    @ApiOperation("添加供应商信息")
    @PostMapping("/save")
    public Result save(HttpServletRequest request, @Valid @RequestBody Account account,BindingResult result) throws Exception{
        Result baseResult = new Result();
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user==null || !user.getIsAdmin().equals(Constant.ADMIN_STATUS)){
            baseResult.setCode(0);
            baseResult.setMessage(so.sao.shop.supplier.config.Constant.MessageConfig.ADMIN_AUTHORITY_EERO);
            return baseResult;
        }
        //判断验证是否通过。true 未通过  false通过
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError error : list) {
                baseResult.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_NOT_EMPTY);
                baseResult.setMessage(error.getDefaultMessage());
            }
        }else{
            /**
             * 插入用户和供应商信息
             */
			return accountService.saveUserAndAccount(account);
        }
        return baseResult;
    }

   /* *//**
     * 根据id查询供应商详细信息
     * @param id
     * @return
     *//*
    @ApiOperation("根据ID查询供应商信息")
    @GetMapping("/selectById/{id}")
    public Account selectById(@PathVariable Long id){
        return accountService.selectById(id);
    }*/

    /**
     * 跟新供应商信息
     *
     * @param account 供应商对象
     * @return 返回更新成功或者失败
     */
    @ApiOperation("修改供应商信息")
    @PutMapping("/update")
    public BaseResult update(@Valid @RequestBody Account account,BindingResult result, HttpServletRequest request) {
        BaseResult baseResult = new BaseResult();
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user==null || !user.getIsAdmin().equals(Constant.ADMIN_STATUS)){
            baseResult.setCode(0);
            baseResult.setMessage(so.sao.shop.supplier.config.Constant.MessageConfig.ADMIN_AUTHORITY_EERO);
            return baseResult;
        }
        //判断验证是否通过。true 未通过  false通过
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError error : list) {
                baseResult.setCode(0);
                baseResult.setMessage(error.getDefaultMessage());
            }
        }else{
            //修改用户登录名
            accountService.updateUser(account.getUserId(), account.getContractResponsiblePhone());
            // 修改用户信息
            int num = accountService.update(account);
            if (num < 0) {
                baseResult.setCode(0);
                baseResult.setMessage("修改失败");
            } else {
                baseResult.setCode(1);
                baseResult.setMessage("修改成功");
            }
        }
        return baseResult;
    }

    /**
     * 根据id删除供应商信息
     *
     * @param id
     * @return 返回成功或失败
     */
    @ApiOperation("删除供应商信息")
    @DeleteMapping("/delete/{id}")
    public BaseResult delete(@PathVariable Long id, HttpServletRequest request) {
        BaseResult baseResult = new BaseResult();
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user==null || !user.getIsAdmin().equals(Constant.ADMIN_STATUS)){
            baseResult.setCode(0);
            baseResult.setMessage(so.sao.shop.supplier.config.Constant.MessageConfig.ADMIN_AUTHORITY_EERO);
            return baseResult;
        }
        int num = accountService.delete(id);
        if (num < 0) {
            baseResult.setCode(0);
            baseResult.setMessage("删除失败");
        } else {
            baseResult.setCode(1);
            baseResult.setMessage("删除成功");
        }
        return baseResult;
    }

    /**
     * 初始化银行信息
     *
     * @return
     */
    @ApiOperation("银行信息")
    @GetMapping("/selectBank")
    public List<String> selectBank() {
        return accountService.selectBank();
    }

    /**
     * 初始化行业信息
     *
     * @return
     */
    @ApiOperation("行业信息")
    @GetMapping("/selectHangYe")
    public List<String> selectHangYe() {
        return accountService.selectHangYe();
    }

    /**
     * 初始化行业信息
     *
     * @return
     */
    @ApiOperation("行业信息(字典项)")
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
    @ApiOperation("登陆")
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
    @ApiOperation("获取admin标记")
    @GetMapping(value = "/isAdmin")
    public Result isAdmin(HttpServletRequest request){
        Map result = new HashMap();
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        result.put("isAdmin",user.getIsAdmin());
        return new Result(1,"",result);
    }

    /**
     * 获取当前登陆供应商名
     * @param request
     * @return
     */
    @ApiOperation("获取当前登陆供应商名")
    @GetMapping(value = "/username")
    public Result getUserName(HttpServletRequest request){
        Map result = new HashMap();
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        Account account = accountService.selectByUserId(user.getId());
        result.put("username",account!=null?account.getProviderName():"");
        return new Result(1,"",result);
    }

    /**
     * 登出,设置登出时间
     *
     * @return
     */
    @ApiOperation("登出")
    @PostMapping(value = "/logout")
    public Result<String> logout(HttpServletRequest request) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        return authService.logout(user);
    }

    /**
     * 根据用户ID查询用户的实时余额并更新用户余额
     *
     * @param request
     * @return
     */
    @ApiOperation(value = "根据用户的账户查询用户余额", notes = "根据用户的账户ID,查询未结算金额")
    @GetMapping(value = "/selectAccountBalance")
    public Result getBalance(HttpServletRequest request) {
        Result result = new Result<>();
        Map map = new HashMap();
        map.put("balance","0.00");
        //获取用户
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        //判断是否登陆
        if (null == user){
            result.setCode(Constant.CodeConfig.CODE_USER_NOT_LOGIN);
            result.setMessage(Constant.MessageConfig.MSG_USER_NOT_LOGIN);
            result.setData(map);
            return result;
        }
        try{
            result = accountService.getAccountBalance(user.getAccountId());
        }catch (Exception e){
            e.printStackTrace();
            result.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);
            result.setMessage(so.sao.shop.supplier.config.Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);
            result.setData(map);
        }
        return result;
    }

    /**
     * 忘记密码,手机号获取密码
     *
     * @param tel
     * @return
     * @throws IOException
     */
    @ApiOperation("忘记密码")
    @GetMapping(value = "/findPassword/{tel}")
    public BaseResult getPassword(HttpServletRequest request, @PathVariable String tel) throws IOException {
        return authService.getPassword(tel);
    }

    /**
     * 根据条件查询供应商列表
     *
     * @param condition
     * @return
     */
    @GetMapping(value = "/account")
    @ApiOperation(value = "查询供应商列表")
    public PageInfo search(Condition condition, HttpServletRequest request)  {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user==null || !user.getIsAdmin().equals(Constant.ADMIN_STATUS)){
            throw new RuntimeException("unauthorized access");
        }
        return accountService.searchAccount(condition);
    }

    /**
     * 根据id查询供应商详细信息
     *
     * @param id
     * @return
     */

    @GetMapping("/search")
    @ApiOperation(value = "根据id查询供应商(省市区汉字)")
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
    @ApiOperation(value = "根据id查询供应商(省市区编码)")
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
     * 查询当前登陆供应商
     * @param id
     * @param request
     * @return
     */
    @GetMapping("/searchSelf")
    @ApiOperation(value = "查询当前登陆供应商")
    public Account getSelf(@RequestParam Long id, HttpServletRequest request) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        return accountService.selectById(accountService.selectByUserId(user.getId()).getAccountId());
    }


    /**
     * 查询当前登录用户登录手机号码
     * @return 当前登录用户登录手机号码
     */
    @GetMapping("/LoginPhone")
    @ApiOperation(value = "查询当前登录用户手机号码")
    public Result getLoginPhone(HttpServletRequest request){
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user!=null) {
            String tel=user.getUsername();
            return new Result(Constant.CodeConfig.CODE_SUCCESS,"查询成功",tel);
        }else {
            return new Result(Constant.CodeConfig.CODE_FAILURE,"获取用户登录信息异常","");
        }
    }

    /**
     * 发送验证码
     * @param request
     * @return 验证码信息
     * @throws IOException
     */
    @ApiOperation("发送验证码")
    @GetMapping(value = "/sendCode")
    public BaseResult sendCode(HttpServletRequest request) throws IOException {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(null==user){
            return new BaseResult(Constant.CodeConfig.CODE_FAILURE, "登录验证不通过");
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
    @ApiOperation("检验验证码")
    @GetMapping(value = "/verifySmsCode/{code}")
    public BaseResult verifySmsCode(HttpServletRequest request, @PathVariable String code) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(null==user){
            return new BaseResult(Constant.CodeConfig.CODE_FAILURE, "登录验证不通过");
        }
        if(null==code||code==""){
            return new BaseResult(Constant.CodeConfig.CODE_FAILURE, "验证码无效");
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
    @ApiOperation("密码修改")
    @PutMapping(value = "/updatePassword/{encodedPassword}")
    public Result updatePassword(HttpServletRequest request, @PathVariable String encodedPassword) throws IOException {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(null==user){
            return new Result(Constant.CodeConfig.CODE_FAILURE, "登录验证不通过",null);
        }
        return authService.updatePassword(user.getId(), encodedPassword);
    }

    /**
     * 根据上传开始时间、结束时间、上传方式查询供应商上传记录
     *
     * @param condition 查询参数
     * @return 供应商信息列表
     */

    @ApiOperation("供应商上传记录")
    @GetMapping("/record")
    public PageInfo<SupplierRecord> findAccount(Condition condition) {
        return supplierRecordService.searchAccountRecord(condition);
    }

    /**
     * 获取地区下级列表
     *
     * @param pid 地区id
     * @return 返回下级列表
     */
    @ApiOperation("获取地区下级列表")
    @GetMapping("get")
    public List<SysRegion> findByPid(@RequestParam(required = false) Integer pid) {
        return sysRegionService.findByPid(pid);
    }

    /**
     * 供应商信息批量上传
     */
    @ApiOperation("供应商信息上传")
    @PostMapping("/upload")
    public String excelUpload(HttpServletRequest request, HttpServletResponse response, MultipartFile file) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user==null || !user.getIsAdmin().equals(Constant.ADMIN_STATUS)){
            throw new RuntimeException("unauthorized access");
        }
        //判断文件是否为空
        if (file == null) {
            return "文件不能为空";
        }

        //获取文件名
        String fileName = file.getOriginalFilename();

        //验证文件名是否合格
        if (!ExcelImportUtils.validateExcel(fileName)) {
            return "上传的不是模板文件";
        }

        //进一步判断文件内容是否为空（即判断其大小是否为0或其名称是否为null）
        long size = file.getSize();
        if (StringUtils.isEmpty(fileName) || size <= 9700) {
            return "文件内容为空";
        }

        //批量导入
        String message = importExcel.batchImport(fileName, file);
        return message;
    }

    /**
     * 统计已入驻供应商数量
     * @return 已入驻供应商数量
     */
    @ApiOperation("统计已入驻供应商数量")
    @GetMapping(value = "/AccountNumber")
    public Result getAccountNumber(){
         int num=accountService.selectAccountNumber();
         return new Result(Constant.CodeConfig.CODE_SUCCESS,"查询成功",num);
    }
    
    /**
     * 供应商合同上传
     * @param request
     * @param multipartFile 合同
     * @return 返回合同云存储地址
     */
    @ApiOperation("供应商合同上传")
    @PostMapping(value = "/uploadContract")
    public Result uploadContract(HttpServletRequest request,@RequestPart("file") MultipartFile multipartFile,String blobName) {
    	User user = (User) request.getAttribute(Constant.REQUEST_USER);
        if(user==null || !user.getIsAdmin().equals(Constant.ADMIN_STATUS)){
           return new Result(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_FAILURE, so.sao.shop.supplier.config.Constant.MessageConfig.ADMIN_AUTHORITY_EERO, "");
        }
    	return accountService.uploadContract(multipartFile,blobName);
    }
}
