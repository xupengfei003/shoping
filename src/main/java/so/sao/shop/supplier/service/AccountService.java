package so.sao.shop.supplier.service;

import com.github.pagehelper.PageInfo;
import so.sao.shop.supplier.domain.Account;
import so.sao.shop.supplier.domain.Condition;
import so.sao.shop.supplier.domain.DictItem;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.pojo.BaseResult;
import so.sao.shop.supplier.pojo.Result;
import java.util.Date;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

/**
 * Created by xujc on 2017/7/18.
 * 账户表（account）对应的Service层
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
     * 根据id更新用户信息
     * @param id
     * @param tel
     * @return 返回更新行数
     */
    int updateUser(Long id, String tel);

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
    Result getAccountBalance(Long accountId) throws Exception;

    /**
     * 分页查询供应商
     * @param condition
     * @return 分页对象
     */
	PageInfo searchAccount(Condition condition);


    /**
     * 添加用户和供应商
     * @param account
     * @return 返回用id
     */
    BaseResult saveUserAndAccount(Account account);

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

}
