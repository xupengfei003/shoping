package so.sao.shop.supplier.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import so.sao.shop.supplier.config.Constant;
import so.sao.shop.supplier.config.sms.SmsService;
import so.sao.shop.supplier.dao.*;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.domain.Condition;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.output.AccountBalanceOutput;
import so.sao.shop.supplier.service.AccountService;

import java.math.BigDecimal;
import java.util.*;

/**
 * Created by xujc on 2017/7/18.
 */
@Service
public class AccountServiceImpl implements AccountService {

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
     * @return 返回受影响行数
     */
    @Override
    public int delete(Long accountId) {
        return accountDao.deleteByPrimaryKey(accountId);
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
     * 增加用户信息
     *
     * @param user 用户对象
     * @return 变更条数
     */
    @Override
    public Long add(User user) {
        return userDao.add(user);
    }

    /**
     * 根据id更新用户信息
     *
     * @param id
     * @param tel
     * @return 返回更新行数
     */
    @Override
    public int updateUser(Long id, String tel) {
        return userDao.update(id, tel);
    }

    /**
     * 单次添加供应商信息
     *
     * @param account 供应商对象
     * @return 返回受影响行数
     */
    @Override
    public int insert(Account account) {
        // 创建日期
        Date date = new Date();
        account.setCreateDate(date.getTime());
        return accountDao.insertSelective(account);
    }

    /**
     * 修改供应商信息
     *
     * @param account 供应商对象
     * @return 返回受影响行数
     */
    @Override
    public int update(Account account) {
        return accountDao.updateByPrimaryKeySelective(account);
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
     * @param accountId 供应商id
     * @return 返回供应商信息
     */
    @Override
    public Account selectById(Long accountId) {
        Account account = accountDao.selectByPrimaryKey(accountId);
        if (account != null) {
            String[] accStr = account.getRegistAddress().split("-");
            String[] ConStr = account.getContractRegisterAddress().split("-");
            List<Map> list = new ArrayList<Map>();

            Map<String, Object> accMap = new HashMap<String, Object>();
            Map<String, Object> conMap = new HashMap<String, Object>();
            /**
             * 用来存放分割后的Account的注册地址
             */
            accMap.put("province", accStr[0]);
            accMap.put("city", accStr[1]);
            accMap.put("area", accStr[2]);
            /**
             * 用来存放分割后的合同上的注册地址
             */
            conMap.put("province", ConStr[0]);
            conMap.put("city", ConStr[1]);
            conMap.put("area", ConStr[2]);

            list.add(accMap);
            list.add(conMap);
            account.setAreaList(list);
            return account;
        }
        return new Account();
    }


    /**
     * 1.根据用户ID获取用户的可用余额；
     * ①、统计订单表（purchase）中该用户名下订单状态（order_status）为已完成，
     * 账户状态（account_status）为未统计的订单金额（order_price）之和
     * ②、判断统计金额是否为空，非空则将订单表（purchase）中账户状态列（account_status）
     * 中的未统计（状态码：0）改为已统计（状态码：1）
     * ③、根据账户表（account）中的用户（user_id）字段查询账户表（account）中用户的余额；
     * ④、第①步中统计的金额与第③步查询的余额相加，得到用户的总余额；
     * ⑤、根据账户表（account）中的user_id（用户id）字段，
     * 将得到的总余额同步到账户表中（account）的余额（balance）；
     * ⑥、将得到的用户总余额添加到规范规定的返回对象之中。
     * ⑦、捕获可能出现的异常
     *
     * @param userId
     * @return
     */
    @Override
    @Transactional
    public AccountBalanceOutput getAccountBalance(Long userId) {

        //定义用户余额的返回对象，包含用户余额、状态码、消息
        AccountBalanceOutput accountBalanceOutput = null;
        //若整个事务出现异常，进行捕获
        try {
            //创建output对象，包含用户余额、状态码、消息
            accountBalanceOutput = new AccountBalanceOutput();

            /**
             * ①、订单状态为已完成，账户状态为未统计的订单金额之和
             */
            BigDecimal uncountedMoney = purchaseDao.findUncountedMoney(userId);//通过Dao层的方法，统计已完成未统计的订单金额之和

            /**
             * ②、统计后将订单表（purchase）中账户状态列（account_status）
             *    中的未统计（状态码：0）改为已统计（状态码：1）
             */
            if (uncountedMoney != null && uncountedMoney.compareTo(BigDecimal.ZERO) == 1) {//判断统计出的金额是否为空，同时其要大于0
                purchaseDao.updatePurchaseAccountStatus(userId);//将订单表中账户状态列的未统计改为已统计
            } else {
                uncountedMoney = new BigDecimal("0.00");//若统计出的金额为空或为负，给其赋初始值为0.00
            }

            /**
             * ③、根据账户表（account）中的用户（user_id）字段查询账户表（account）中用户的余额；
             */
            BigDecimal oldBalance = accountDao.findUserBalance(userId);//通过Dao层的方法，根据用户ID查询用户余额
            if (oldBalance == null || oldBalance.compareTo(BigDecimal.ZERO) == 0) {//判断查询出的金额是否为空，或是否为0
                oldBalance = new BigDecimal("0.00");//若查出金额为null或0，给其赋值为0.00
            }

            /**
             * ④、第①步中统计的金额与第③步查询的余额相加，得到用户的总余额；
             */
            BigDecimal newBalance = uncountedMoney.add(oldBalance);//查询余额与统计余额相加
            newBalance = newBalance.setScale(2, BigDecimal.ROUND_HALF_UP);//给结果保留两位小数

            /**
             * ⑤、根据账户表（account）中的user_id（用户id）字段，
             *    将得到的总余额同步到账户表中（account）的余额（balance）；
             */
            Account account = new Account();//新建一个账户类的对象
            account.setUserId(userId);//将用户的ID Set进账户类对象
            account.setBalance(newBalance);//将得到的总金额 Set进账户类对象
            accountDao.updateUserBalance(account);//执行账户表Dao层方法，将余额同步到账户表中

            /**
             * ⑥、将得到的用户总余额添加到规范规定的返回对象之中。
             */
            accountBalanceOutput.setCode(Constant.CodeConfig.CODE_SUCCESS);//将成功状态码 Set到返回对象之中
            accountBalanceOutput.setMessage(Constant.MessageConfig.MSG_SUCCESS);//将成功message Set到返回对象之中
            accountBalanceOutput.setBlance(newBalance);//将得到的总金额 Set进返回对象之中
            /**
             * ⑦、捕获可能出现的异常
             */
        } catch (Exception e) {//若事务出现异常，则对异常进行捕获
            accountBalanceOutput = new AccountBalanceOutput();//创建output对象，包含用户余额、状态码、消息
            accountBalanceOutput.setCode(Constant.CodeConfig.CODE_SYSTEM_EXCEPTION);//将系统异常状态码 Set到返回对象之中
            accountBalanceOutput.setMessage(Constant.MessageConfig.MSG_SYSTEM_EXCEPTION);//将系统异常message Set到返回对象之中
        }

        return accountBalanceOutput;//返回对象
    }

    /**
     * 分页查询
     *
     * @param condition
     * @return 分页对象
     */
    @Override
    public PageInfo searchAccount(Condition condition) {
        if (condition.getBeginTime() != null) {
            condition.setBeginDate(condition.getBeginTime().getTime());
        }
        if (condition.getContractCreateTime() != null) {
            condition.setContractCreateDate(condition.getContractCreateTime().getTime());
        }
        if (condition.getPageNum() == null) {
            condition.setPageNum(1);
        }
        if (condition.getPageSize() == null) {
            condition.setPageSize(10);
        }
        Page page = PageHelper.startPage(condition.getPageNum(), condition.getPageSize());
        PageInfo pageInfo = new PageInfo(accountDao.findPage(condition));
        return pageInfo;
    }

    /**
     * 根据手机号码插入用户信息
     *
     * @param phone
     * @return 返回用户id
     */
    @Override
    public Long saveUser(String phone) {
        User user1 = userDao.findByUsername(phone);
        if (user1 == null) {
            User user = new User();
            user.setUsername(phone);
            String password = smsService.getVerCode();
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(password));
            user1.setLastPasswordResetDate(new Date().getTime());
            user.setIsAdmin("0");
            userDao.add(user);
            smsService.sendSms(Collections.singletonList(phone), password);
//                SmsUtil.sendSms(phone, password);
            return user.getId();
        } else {
            return user1.getId();
        }
    }
}
