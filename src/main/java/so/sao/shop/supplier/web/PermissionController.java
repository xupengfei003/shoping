package so.sao.shop.supplier.web;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import so.sao.shop.supplier.domain.authorized.Permission;
import so.sao.shop.supplier.service.authorized.IPermissionService;

import java.util.List;

/**
 * @TODO 暂时未用
 * Created by acer on 2017/7/11.
 */
@RestController
@RequestMapping(value = "/permission")
public class PermissionController {
	
    @Autowired
    private IPermissionService permissionService;

    @ApiOperation("获得所有权限集合")
    @GetMapping(value = "/list")
    public List<Permission> findAll(){
        return  permissionService.findAll();
    }

}
