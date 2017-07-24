package com.sao.so.supplier.service;

import com.github.pagehelper.PageInfo;
import com.sao.so.supplier.domain.Account;
import com.sao.so.supplier.domain.Condition;
import com.sao.so.supplier.domain.User;
import com.sao.so.supplier.pojo.output.AccountBalanceOutput;

import java.util.List;

/**
 * Created by xujc on 2017/7/18.
 * 账户表（t_account）对应的Service层
 */
public interface AccountService {

    /**
     * 根据id修改供应商状态为删除
     * @param accountId 供应商id
     * @return 返回受影响行数
     */
    int delete(Long accountId);

    /**
     * 单次添加供应商信息
     * @param account 供应商对象
     * @return 返回受影响行数
     */
    int insert(Account account);

    /**
     * 修改供应商信息
     * @param account 供应商对象
     * @return 返回受影响行数
     */
    int update(Account account);

    /**
     * 根据id查询供应商信息
     * @param accountId 供应商id
     * @return 返回供应商信息
     */
    Account selectById(Long accountId);

    /**
     * 根据userId查供应商信息
     * @param userId
     * @return
     */
    Account selectByUserId(Long userId);

    /**
     * 初始化银行信息
     * @return 返回银行列表
     */
    List<String> selectBank();

    /**
     * 增加用户信息
     * @param user 用户对象
     * @return 变更条数
     */
    Long add(User user);

    /**
     * 根据id更新用户信息
     * @param id
     * @return 返回更新行数
     */
    int updateUser(Long id);

    /**
     * 初始化行业信息
     * @return 行业信息列表
     */
    List<String> selectHangYe();

    /**
     * 1.根据用户ID获取用户的可用余额（余额分为上次提现剩余金额和订单增量余额）
     * @param userId
     * @return
     */
    AccountBalanceOutput getAccountBalance(Long userId);

    /**
     * 分页查询供应商
     * @param condition
     * @return 分页对象
     */
	PageInfo searchAccount(Condition condition);


    /**
     * 添加用户
     * @param phone
     * @return 返回用id
     */
	Long saveUser(String phone);

}
