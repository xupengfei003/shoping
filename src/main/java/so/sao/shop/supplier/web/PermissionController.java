package so.sao.shop.supplier.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import so.sao.shop.supplier.domain.Permission;
import so.sao.shop.supplier.service.PermissionService;

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

    @GetMapping(value = "/list")
    /**
     * 获得所有权限集合
     */
    public List<Permission> findAll(){
        return  permissionService.findAll();
    }

}
