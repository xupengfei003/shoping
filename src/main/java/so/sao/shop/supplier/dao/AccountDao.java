package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.pojo.input.AccountInput;
import so.sao.shop.supplier.pojo.input.AccountUpdateInput;
import so.sao.shop.supplier.pojo.output.AccountOutput;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


/**
 * Created by niewenchao on 2017/7/19.
 * 账户表（account）对应的Dao层
 */
public interface AccountDao {

    /**
     * 根据id修改供应商状态为删除
     * @param accountId 供应商id
     * @return 返回受影响行数
     */
    void deleteByPrimaryKey(Long accountId);

    /**
     * 单次添加供应商信息
     * @param record 供应商对象
     * @return 返回受影响行数
     */
    int insert(Account record);

    /**
     * 根据id查询供应商信息(省市区汉字)
     * @param accountId 供应商id
     * @return 返回供应商信息
     */
    Account selectByPrimaryKey(Long accountId);

    /**
     * 根据id查询供应商信息(省市区编码)
     * @param accountId 供应商id
     * @return 返回供应商信息
     */
    Account selectById(Long accountId);

    /**
     * 修改供应商信息
     * @param record 供应商对象
     * @return 返回受影响行数
     */
    int updateByPrimaryKeySelective(Account record);

    /**
     * 修改供应商信息
     * @param record 供应商对象
     * @return 返回受影响行数
     */
    int updateByPrimaryKey(Account record);

    /**
     * 根据用户id，查找该用户的账户信息
     * @param userId
     * @return
     */
    Account findByUserId(@Param("userId") Long userId);

    /**
     * 根据用户id,更新账户余额信息
     * @param account
     * @return
     */
    int updateAccountByUserId(Account account);

    /**
     * 根据账户表账户id，修改该表中用户的余额，返回受影响的行数；
     * @param account
     * @return
     */
    void updateUserBalance(Account account) throws Exception;


    /**
     * 根据条件查询出相应供应商的信息
     * @param accountInput 入参对象
     * @return 返回分页对象
     */
    List<Account> findPage(AccountInput accountInput);

    /**
     * 将计算的总金额,更新为该商户id下的历史总收入
     * @param account
     * @param storeId 商户id
     */
    void updateUserPrice(@Param("account") BigDecimal account, @Param("storeId") Long storeId);

    /**
     * 根据用户id查询供应商id
     * @param userId 用户id
     * @return 返回供应商id
     */
    Long findAccountIdByUserId(Long userId);

    /**
     * 统计出当前供应商数量
     * @return 供应商数量
     */
    int findAccountNumber();

    /**
     * 查询出当天要结算的供应商信息列表
     * @param days  当前日期为这个月的第几天
     * @param currentDate 当前时间
     * @return
     */
    List<Account> findAccountList(@Param("days") Integer days, @Param("currentDate") Date currentDate);

    /**
     * 批量更新上一次的结算日期
     * @param list
     * @return
     */
    int updateAccountLastSettlementDate(List<Account> list);

    /**
     * 根据用户id，查找该用户的账户信息
     * @param userId
     * @return
     */
    Account findAccountByUserId(@Param("userId") Long userId);

    /**
     * 根据账户ID查找是否存在该账户
     * @param accountId 账户ID
     * @return
     */
    int countByAccountId(Long accountId);

    /**
     * 根据供应商ID查询供应商名称及状态
     * @param accountId
     * @return
     * @throws Exception
     */
    Account findNameAndStatus (@Param("accountId") Long accountId) throws Exception;


    /**
     * 查询供应商合同截止时间一个月前的用户
     * @return
     */
    List<Account> findMonthAgo();
    /**
     * 查询合同到期的供应商
     * @return
     */
    List<Account> findContractEndDate();
    /**
     * 根据账户ID修改供应商状态
     * @return
     */
    void updateAccountStatusById(AccountUpdateInput accountUpdateInput);

    /**
     * 根据供应商ID或name查询供应商信息
     * @param accountId
     * @param providerName
     * @return 供应商信息列表
     */
    List<AccountOutput> findAccounts(@Param("accountId")Long accountId, @Param("providerName")String providerName);

    /**
     * 根据供应商ID修改供应商合同剩余30天发送短信标记
     * @param accountId
     */
    void updateAccountSmsTypeById(@Param("accountId")Long accountId);

    /**
     * 根据AccountId查询供应商的物流运费规则
     * @param accountId
     * @return
     */
    Integer findRulesById (@Param("accountId")Long accountId);

    /**
     * 根据商户ID修改当前默认运费规则
     * @param account
     * @param freightRules
     */
    void updateRulesByFreightRules(@Param("accountId") Long account, @Param("freightRules") Integer freightRules);
}

