package so.sao.shop.supplier.service.impl;


import org.springframework.data.redis.core.RedisTemplate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import so.sao.shop.supplier.config.CommConstant;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.config.azure.AzureBlobService;
import so.sao.shop.supplier.config.azure.BlobUpload;
import so.sao.shop.supplier.config.sms.SmsService;
import so.sao.shop.supplier.dao.*;
import so.sao.shop.supplier.domain.*;
import so.sao.shop.supplier.pojo.Result;
import so.sao.shop.supplier.pojo.input.AccountInput;
import so.sao.shop.supplier.pojo.input.AccountUpdateInput;
import so.sao.shop.supplier.service.AccountService;
import so.sao.shop.supplier.service.CommodityService;
import so.sao.shop.supplier.util.*;

import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by xujc on 2017/7/18.
 */
@Service
public class AccountServiceImpl implements AccountService {
	
	/**
	 * 初始化日志
	 */
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	/**
	 * 创建发送短信线程
	 */
	ExecutorService tpe = Executors.newFixedThreadPool(1);

    @Autowired
    private UserDao userDao;

    /**
     * 账户表对应的DAO（AccountDao）
     */
    @Autowired
    private AccountDao accountDao;

    /**
     * 订单表对应的DAO（PurchaseDao）
     */
    @Autowired
    private PurchaseDao purchaseDao;

    @Autowired
    private RegionMapper regionDao;

    @Autowired
    private DictItemMapper dictItemDao;

    @Autowired
    private SmsService smsService;

    /**
     * redisTemplate
     */
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    /**
     *
     */
    @Autowired
    private AzureBlobService azureBlobService;

    /**
     * 第一次发送密码
     */
    @Value("${shop.aliyun.sms.sms-template-code2}")
    String smsTemplateCode2;

    /**
     * 供应商禁用
     */
    @Value("${shop.aliyun.sms.sms-template-code5}")
    String smsTemplateCode5;

    @Autowired
    private CommodityService commodityService;

    /**
     * 初始化银行信息
     *
     * @return 返回银行列表
     */
    @Override
    public List<String> selectBank() {
        return dictItemDao.selectBank();
    }

    /**
     * 根据id修改供应商状态为删除
     *
     * @param accountId 供应商id
     * @return 返回结果
     */
    @Override
    public Result delete(Long accountId) throws Exception{
        accountDao.deleteByPrimaryKey(accountId);
        return Result.success("删除供应商成功！");
    }

    /**
     * 初始化银行信息
     *
     * @return 返回银行列表
     */
    @Override
    public List<String> selectHangYe() {
        return dictItemDao.selectHangYe();
    }

    /**
     * 初始化银行信息
     *
     * @return 返回银行列表
     */
    @Override
    public List<DictItem> selectHangYeDict() {
        return dictItemDao.selectHangYeDict();
    }


    /**
     * 增加用户信息
     *
     * @param user 用户对象
     * @return 变更条数
     */
    @Override
    public int add(User user) {
        return userDao.add(user);
    }

    /**
     * 修改供应商信息和用户信息
     *
     * @param account 供应商对象
     * @return 返回修改结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateAccountAndUser(Account account) throws Exception{
    	//修改用户信息
    	userDao.update(account.getUserId(), account.getContractResponsiblePhone());
    	//修改供应商信息
        account.setUpdateDate(new Date());
        account.setCreateDate(null);
        //获取供应商合同截止时间
        Date contractEndDate = accountDao.selectById(account.getAccountId()).getContractEndDate();
        //如果不相等则修改过合同时间，恢复短信状态为初始值。
        if (!account.getContractEndDate().equals(contractEndDate)){
            account.setMonthAgoType(CommConstant.ACCOUNT_NOSENDSMS_STATUS);
        }
        accountDao.updateByPrimaryKeySelective(account);
        return Result.success("修改供应商和用户成功");
    }
    
    

    /**
     * 根据userId查供应商信息
     * @param userId
     * @return
     */
    @Override
    public Account selectByUserId(Long userId){
        return accountDao.findByUserId(userId);
    }
    /**
     * 根据id查询供应商信息
     *
     * @param accountId 供应商id(省市区汉字)
     * @return 返回供应商信息
     */
    @Override
    public Account selectById(Long accountId) {
        Account account = accountDao.selectByPrimaryKey(accountId);
        if (account != null) {
            String[] accStr = account.getRegistAddress().split("-");
            String[] conStr = account.getContractRegisterAddress().split("-");
            List<Map> list = new ArrayList<Map>();

            Map<String, Object> accMap = new HashMap<String, Object>();
            Map<String, Object> conMap = new HashMap<String, Object>();
            /**
             * 用来存放分割后的Account的注册地址
             */
            int accStrLength = accStr.length;
            accMap.put("province", accStrLength>0?accStr[0]:"");
            accMap.put("city", accStrLength>1?accStr[1]:"");
            accMap.put("area", accStrLength>1?accStr[2]:"");
            /**
             * 用来存放分割后的合同上的注册地址
             */
            int conStrLength = conStr.length;
            conMap.put("province", conStrLength>0?conStr[0]:"");
            conMap.put("city", conStrLength>1?conStr[1]:"");
            conMap.put("area", conStrLength>2?conStr[2]:"");

            list.add(accMap);
            list.add(conMap);
            account.setAreaList(list);
            //更新合同状态
            updateContractStatus(account);
            return account;
        }
        return new Account();
    }

    /**
     * 根据id查询供应商信息(省市区编码)
     *
     * @param accountId 供应商id
     * @return 返回供应商信息
     */
    @Override
    public Account selectById0(Long accountId) {
        Account account = accountDao.selectById(accountId);
        //更新合同状态
        updateContractStatus(account);
        return account;
    }

    /**
     * 根据账户ID获取用户的可用余额；
     * @param accountId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Map<String,String> getAccountBalance(Long accountId) throws Exception{
        /*
            1、判断账户是否存在,若账户存在，统计账户下订单产生的增额
               a.统计订单状态为已完成，账户状态为未结算的订单金额之和
               b.若统计金额为空或小于等于0，赋值0.00
               c.将所得余额同步到账户表中
            2、若账户不存在，返回余额为0.00
        */
        //根据账户ID查询账户个数
        int accountNum = accountDao.countByAccountId(accountId);
        //定义map存放余额，初始余额为0.00
        Map<String,String> map = new HashMap();
        map.put("balance","0.00");
        // 1.判断账户是否存在,若账户存在，统计账户下订单产生的增额
        if(1 == accountNum){
            // a.统计订单状态为已完成，账户状态为未结算的订单金额之和
            BigDecimal uncountedMoney = purchaseDao.findUncountedMoney(accountId);
            // b.若统计金额为空，赋值0.00
            if (Ognl.isNull(uncountedMoney)){
                uncountedMoney = new BigDecimal("0.00");
            }
            // c.将所得余额同步到账户表中
            Account account = new Account();
            Date date = new Date();              //系统时间
            account.setAccountId(accountId);     //账户
            account.setBalance(uncountedMoney);  //用户余额
            account.setUpdateDate(date);         //更新时间
            accountDao.updateUserBalance(account);
            // 返回数据
            String balance = NumberUtil.number2Thousand(uncountedMoney);// 余额格式化
            map.put("balance",balance);
        }
        //返回对象
        return map;
    }

    /**
     * 多条件分页查询供应商列表
     *
     * @param accountInput 入参对象
     * @return 分页对象
     */
    @Override
    public List<Account> searchAccount(AccountInput accountInput) {
        PageTool.startPage(accountInput.getPageNum(), accountInput.getPageSize());
        List<Account> accountList = accountDao.findPage(accountInput);
        for (Account account : accountList) {
            //合同状态设置
            updateContractStatus(account);
        }
        return accountList;
    }

    /**
     * 供应商列表和详情页面显示合同状态
     * 正常状态:0 -只显示合同时间 / 即将过期:1 / 已过期:2
     * @param account
     * @return
     */
    private Account updateContractStatus(Account account){
        if (account != null){
            long endDate = account.getContractEndDate().getTime();
            long currentDate = DateUtil.stringToDate(DateUtil.getStringDate()).getTime();
            long betweenDate = (endDate - currentDate) / (1000 * 60 * 60 * 24);
            if (betweenDate <= 0){
                account.setContractStatus(2);
            }else if(betweenDate > 0 && betweenDate <= 30){
                account.setContractStatus(1);
            }
        }
        return account;
    }

    /**
     * 插入用户和供应商信息
     *
     * @return 返回用户id
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public  Result saveUserAndAccount(Account account) throws Exception{
        Boolean lock = redisTemplate.opsForValue().setIfAbsent(Constant.REDIS_KEY_PREFIX+account.getContractResponsiblePhone(),"1");
        try {
            if(lock!=null&&lock) {
                //需要加锁的代码
                User user1 = userDao.findByUsername(account.getContractResponsiblePhone());
                if (user1 == null) {
                    User user = new User();
                    user.setUsername(account.getContractResponsiblePhone());
                    user.setLastPasswordResetDate(new Date());
                    user.setIsAdmin("0");
                    userDao.add(user);
                    account.setCreateDate(new Date());
                    account.setUpdateDate(new Date());
                    account.setAccountStatus(CommConstant.ACCOUNT_INVALID_STATUS);
                    account.setUserId(user.getId());
                    account.setLastSettlementDate(new Date());
                    account.setMonthAgoType(CommConstant.ACCOUNT_NOSENDSMS_STATUS);
                    accountDao.insert(account);
                    return Result.success("用户和供应商添加成功！");
                }
            }
        } catch (Exception e) {
        	logger.error("增加供应商异常",e);
        	throw new Exception("增加供应商异常",e);
        }finally {
            redisTemplate.delete(Constant.REDIS_KEY_PREFIX+account.getContractResponsiblePhone());
        }
        return Result.fail("此供应商已经存在！");
    }

    /**
     * 统计已入驻供应商总数
     * @return 供应商总数
     */
    @Override
    public int selectAccountNumber(){
        return accountDao.findAccountNumber();
    }

    /**
     * 供应商合同原件上传
     * @param multipartFile 上传文件
     * @return 返回云链接地址
     */
    public Result uploadContract(MultipartFile multipartFile,String blobName) {
    	//判断文件是否为空
        if (multipartFile == null) {
            return Result.fail("上传文件为空");
        }
    	//文件名称不为空，先删除云端文件
//    	if(!"".equals(blobName) && blobName != null) {
//    		azureBlobService.deleteFile(CommConstant.AZURE_CONTAINER.toLowerCase(), blobName.split("-_-")[1]);
//    	}
    	if(!"".equals(blobName) && blobName != null) {
    		azureBlobService.deleteFile(CommConstant.AZURE_CONTAINER.toLowerCase(), blobName);
    	}
        //获取文件名称
        String fileName = multipartFile.getOriginalFilename();
        //获取文件大小
        long fileSize = multipartFile.getSize();
        //获取文件后缀名
        String prefix=fileName.substring(fileName.lastIndexOf(".")+1);
        //判断文件类型
//        if(!("doc".equals(prefix) || "docx".equals(prefix)||"ppt".equals(prefix)||"pptx".equals(prefix)||"wps".equals(prefix)||"pdf".equals(prefix)||"txt".equals(prefix))) {
//            return Result.fail("不符合文件类型");
//        }
        if(!"pdf".equals(prefix)) {
        	return Result.fail("不符合文件类型");
        }
        //判断文件大小
        if(fileSize/1024/1024>20) {
        	return Result.fail("上传文件大于20M");
        }
      //上传成功封装为result返回页面
        List<BlobUpload> blobUploadList =azureBlobService.uploadFiles(new MultipartFile[] {multipartFile}, CommConstant.AZURE_CONTAINER.toLowerCase());
        BlobUpload blobUpload = blobUploadList.get(0);
        blobUpload.setOriginalFileName(fileName);
        return Result.success("文件上传成功",blobUpload);

    }

    /**
     * 查询出当天要结算的供应商信息列表
     * @param days  当前日期为这个月的第几天
     * @param currentDate 当前时间
     * @return
     */
    @Override
    public List<Account> findAccountList(Integer days, Date currentDate) {
        return accountDao.findAccountList(days, currentDate);
    }

    /**
     * 修改供应商状态并激活账户
     * @param accountUpdateInput
     * @return 返回修改状态
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result updateAccountStatus(AccountUpdateInput accountUpdateInput) {
        Account account = accountDao.selectById(accountUpdateInput.getAccountId());
        if (account == null) {
            return Result.fail("更新失败！");
        }
        //判断合同是否到期，到期后无法启用供应商状态。
        updateContractStatus(account);
        if(accountUpdateInput.getAccountStatus()==CommConstant.ACCOUNT_ACTIVE_STATUS && account.getContractStatus() == 2){
            return Result.fail("合同有效期已过期，请更新后启用！");
        }
        //判断启用还是禁用操作，并发送相应的短信提示
        String password =smsService.getVerCode();
        if (accountUpdateInput.getAccountStatus()==CommConstant.ACCOUNT_ACTIVE_STATUS) {
            userDao.updatePassword(account.getUserId(), new BCryptPasswordEncoder().encode(password), new Date());
        }else {
            //禁用供应商，修改商品状态
            commodityService.updateCommInvalidStatus(accountUpdateInput.getAccountId() , accountUpdateInput.getAccountStatus());
        }
        tpe.execute(new Runnable() {
            @Override
            public void run() {
                smsService.sendSms(Collections.singletonList(accountUpdateInput.getAccountTel()),
                        Arrays.asList("phone","password"),Arrays.asList(accountUpdateInput.getAccountTel(),password),
                        accountUpdateInput.getAccountStatus()==CommConstant.ACCOUNT_ACTIVE_STATUS?smsTemplateCode2:smsTemplateCode5);
            }
        });
        //修改账户状态
        accountUpdateInput.setUpdateDate(new Date());
        accountDao.updateAccountStatusById(accountUpdateInput);
        return Result.success("更新成功！");
    }

}
