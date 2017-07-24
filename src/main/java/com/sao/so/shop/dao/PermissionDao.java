package com.sao.so.shop.dao;

import com.sao.so.shop.domain.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 权限dao
 *
 * @author
 * @create 2017-07-08 21:35
 **/
@Mapper
public interface PermissionDao {
    public List<Permission> findAll();
    public List<Permission> findByUserId(@Param("userId") Long userId);
}
