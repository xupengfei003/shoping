package so.sao.shop.supplier.web;

import com.github.pagehelper.PageInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.domain.*;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.output.AccountBalanceOutput;
import so.sao.shop.supplier.service.*;
import so.sao.shop.supplier.util.Constant;
import so.sao.shop.supplier.util.ExcelImportUtils;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.*;
import java.net.URL;
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
     * @throws IOException
     */
    @ApiOperation("添加供应商信息")
    @PostMapping("/save")
    public BaseResult save(@Valid @RequestBody Account account,BindingResult result) {
        BaseResult baseResult = new BaseResult();
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
        	baseResult.setMessage(accountService.saveUserAndAccount(account));
            baseResult.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_SUCCESS);
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
    public BaseResult update(@Valid @RequestBody Account account,BindingResult result) {
        BaseResult baseResult = new BaseResult();
        //判断验证是否通过。true 未通过  false通过
        if(result.hasErrors()) {
            List<ObjectError> list = result.getAllErrors();
            for (ObjectError error : list) {
                baseResult.setCode(so.sao.shop.supplier.config.Constant.CodeConfig.CODE_NOT_EMPTY);
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
    public BaseResult delete(@PathVariable Long id) {
        int num = accountService.delete(id);
        BaseResult baseResult = new BaseResult();
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
            User authenticationRequest, HttpServletResponse response) throws AuthenticationException, IOException {

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
        return authService.logout(user.getId());
    }

    /**
     * 根据用户ID查询用户的实时余额并更新用户表余额
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据用户ID查询用户余额", notes = "根据用户的ID，统计用户订单中未统计的余额，即增额，查询用户的固有余额，求和得到用户余额")
    @GetMapping(value = "/selectAccountBalance/{id}")//访问路径
    public AccountBalanceOutput getBalance(@PathVariable("id") @Validated Long id) {//入参为用户ID
        return accountService.getAccountBalance(id);//根据Service方法，返回余额对象
    }

    /**
     * 忘记密码,手机号获取密码
     *
     * @param tel
     * @return
     * @throws AuthenticationException
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
    public PageInfo search(Condition condition) {
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
    public Account get(@RequestParam(required = false) Long id, HttpServletRequest request) {
        if(id == null || id == 0){
            User user = (User) request.getAttribute(Constant.REQUEST_USER);
            id = accountService.selectByUserId(user.getId()).getAccountId();
        }
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
        if(id == null || id == 0){
            User user = (User) request.getAttribute(Constant.REQUEST_USER);
            id = accountService.selectByUserId(user.getId()).getAccountId();
        }
        return accountService.selectById0(id);
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
     * 发送验证码
     *
     * @param tel
     * @return
     * @throws IOException
     */
    @ApiOperation("发送验证码")
    @GetMapping(value = "/sendCode/{tel}")
    public BaseResult sendCode(@PathVariable String tel) throws IOException {
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
        return authService.verifySmsCode(user.getId(), code);
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
    public BaseResult updatePassword(HttpServletRequest request, @PathVariable String encodedPassword) {
        User user = (User) request.getAttribute(Constant.REQUEST_USER);
        return authService.updatePassword(user.getId(), encodedPassword);
    }

    /**
     * 供应商信息模板下载
     *
     * @param request
     * @param response
     * @throws IOException
     */
    @ApiOperation("供应商信息模板下载")
    @GetMapping("/down")
    public void downLoadExcel(HttpServletRequest request, HttpServletResponse response) throws IOException {
        URL save = Thread.currentThread().getContextClassLoader().getResource("");
        String str = save.toString();
        str = str.substring(6, str.length());
        str = str.replaceAll("%20", " ");
        int num = str.lastIndexOf("supplier");//supplier 为项目名，应用到不同的项目中，这个需要修改！
        str = str.substring(0, num + "supplier".length());
        str = str + "/file/Commodity.xls";//Excel模板所在的路径。
        File f = new File(str);
        // 设置response参数，可以打开下载页面
        response.reset();
        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        String filename = "商品信息.xls";
        filename = new String(filename.getBytes("Utf-8"), "iso-8859-1");
        response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-Type", "application/octet-stream");
        ServletOutputStream out = response.getOutputStream();
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(f));
            bos = new BufferedOutputStream(out);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (final IOException e) {
            throw e;
        } finally {
            if (bis != null)
                bis.close();
            if (bos != null)
                bos.close();
        }
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

}
