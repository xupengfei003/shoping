package so.sao.shop.supplier.dao;

/**
 * Created by lenovo on 2017/7/7.
 */

import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import so.sao.shop.supplier.domain.User;


import java.util.Date;
import java.util.List;

/**
 * Created by lenovo on 2017/7/7.
 */
@Mapper
public interface UserDao {
    User findOne(@Param("id") Long id);
    int countPage(@Param("page") Page page);
    List findPage(@Param("page") Page page);
    User findByUsername(String username);
    int updatePassword(@Param("userId")Long userId, @Param("loginPassword")String loginPassword, @Param("lastPasswordResetDate")Date lastPasswordResetDate);
    int saveSmsCode(@Param("tel")String tel, @Param("code")String code);
    String findSmsCode(Long userId);
    int add(User user);
    int update(@Param("id")Long id, @Param("tel")String tel);
    int updateLogoutTime(@Param("userId")Long userId,@Param("logoutTime")Date logoutTime);
   
}