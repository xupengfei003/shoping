package so.sao.shop.supplier.service;

import com.github.pagehelper.PageInfo;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.domain.DictItem;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;
import so.sao.shop.supplier.pojo.input.AccountInput;
import so.sao.shop.supplier.pojo.input.AccountUpdateInput;

/**
 * Created by xujc on 2017/7/18.
 * 账户表（account）对应的Service层
 */
public interface AccountService {

    /**
     * 根据id修改供应商状态为删除
     * @param accountId 供应商id
     * @return 返回结果
     */
	Result delete(Long accountId) throws Exception;

    /**
     * 修改供应商信息和用户信息
     * @param account 供应商对象
     * @return 返回修改结果
     */
    Result updateAccountAndUser(Account account) throws Exception;

    /**
     * 根据id查询供应商信息(省市区汉字)
     * @param accountId 供应商id
     * @return 返回供应商信息
     */
    Account selectById(Long accountId);

    /**
     * 根据id查询供应商信息(省市区编码)
     * @param accountId 供应商id
     * @return 返回供应商信息
     */
    Account selectById0(Long accountId);

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
    int add(User user);

    /**
     * 初始化行业信息
     * @return 行业信息列表
     */
    List<String> selectHangYe();

    /**
     * 初始化行业信息(包含code)
     * @return 行业信息列表
     */
    List<DictItem> selectHangYeDict();


    /**
     * 根据账户ID获取用户的可用余额
     * @param accountId
     * @return
     */
    Map<String,String> getAccountBalance(Long accountId) throws Exception;

    /**
     * 分页查询供应商
     * @param accountInput
     * @return 分页对象
     */
    List<Account> searchAccount(AccountInput accountInput);


    /**
     * 添加用户和供应商
     * @param account
     * @return 返回用id
     */
    Result saveUserAndAccount(Account account) throws Exception;

    /**
     * 统计已入驻供应商总数
     * @return 已入驻供应商数量
     */
    int selectAccountNumber();

    /**
     * 供应商合同原件上传
     * @param multipartFile 上传文件
     * @return 返回云链接地址
     */
    Result uploadContract(MultipartFile multipartFile,String blobName);

    /**
     * 查询出当天要结算的供应商信息列表
     * @param days  当前日期为这个月的第几天
     * @param currentDate 当前时间
     * @return
     */
    List<Account> findAccountList(Integer days, Date currentDate);

    /**
     * 修改供应商状态并激活账户
     * @param accountUpdateInput
     */
    Result updateAccountStatus(AccountUpdateInput accountUpdateInput);

    /**
     * 根据合同状态设置用户登录状态
     * @param username
     * @return
     */
    Result getLoginUserStatus(Long username);
}
