package com.sao.so.supplier.web;

import com.sao.so.supplier.domain.Permission;
import com.sao.so.supplier.service.PermissionService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @TODO 暂时未用
 * Created by acer on 2017/7/11.
 */
@RestController
@RequestMapping(value = "/permission")
public class PermissionController {
    @Autowired
    private PermissionService permissionService;

    @ApiOperation("获取数据列表")
    @GetMapping(value = "/list")
    /**
     * 获得所有权限集合
     */
    public List<Permission> findAll(){
        return  permissionService.findAll();
    }

}
