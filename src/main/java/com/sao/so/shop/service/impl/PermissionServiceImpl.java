package com.sao.so.shop.service.impl;

import com.sao.so.shop.dao.PermissionDao;
import com.sao.so.shop.domain.Permission;
import com.sao.so.shop.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by acer on 2017/7/11.
 */
@Service
public class PermissionServiceImpl implements PermissionService {
    @Autowired
    PermissionDao permissionDao;
    @Override
    /**
     * 查询所有权限
     */
    public List<Permission> findAll() {
        return permissionDao.findAll();
    }

    @Override
    public List<Permission> findByUserId(Long userId) {
        return permissionDao.findByUserId(userId);
    }
}
