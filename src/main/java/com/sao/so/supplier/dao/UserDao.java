package com.sao.so.supplier.dao;

/**
 * Created by lenovo on 2017/7/7.
 */
import com.github.pagehelper.Page;
import com.sao.so.supplier.domain.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

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
    int updatePassword(Long userId, String loginPassword, Long lastPasswordResetDate);
    int saveSmsCode(@Param("tel")String tel, @Param("code")String code);
    String findSmsCode(Long userId);
    Long add(User user);
    int update(Long id);
}