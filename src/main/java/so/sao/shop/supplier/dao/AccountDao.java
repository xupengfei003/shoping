package so.sao.shop.supplier.dao;

import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.domain.Condition;

import java.math.BigDecimal;
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
    int deleteByPrimaryKey(Long accountId);

    /**
     * 单次添加供应商信息
     * @param record 供应商对象
     * @return 返回受影响行数
     */
    int insert(Account record);

    /**
     * 单次添加供应商信息
     * @param record 供应商对象
     * @return 返回受影响行数
     */
    int insertSelective(Account record);

    /**
     * 根据id查询供应商信息
     * @param accountId 供应商id
     * @return 返回供应商信息
     */
    Account selectByPrimaryKey(Long accountId);

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
     * 批量插入供应商信息
     * @param accounts
     * @return 返回受影响行数
     */
    int saveBatch(List<Account> accounts);

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
     * 1.根据账户表（account）中的用户（user_id）字段查询该表中用户的余额（balance）,返回账户中余额；
     * @param userId
     * @return
     */
    BigDecimal findUserBalance(@Param("userId") Long userId);

    /**
     * 根据账户表（account）中的user_id（用户id）字段修改该表中用户的余额（balance），返回受影响的行数；
     * @param account
     * @return
     */
    int updateUserBalance(@Param("account") Account account);

    
    /**
     * 根据条件查询出相应供应商的信息
     * @param condition 分页对象
     * @return 返回分页对象
     */
    List<Account> findPage(Condition condition);

	/**
     * 将计算的总金额,更新为该商户id下的历史总收入
     * @param account
     * @param storeId 商户id
     */
    void updateUserPrice(@Param("account") BigDecimal account, @Param("storeId") Long storeId);
}
