package com.sao.so.shop.service;

import com.sao.so.shop.domain.Permission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by acer on 2017/7/11.
 */
public interface PermissionService {

    public List<Permission> findAll();
    public List<Permission> findByUserId(@Param("userId") Long userId);
}
