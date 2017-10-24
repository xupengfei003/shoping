package so.sao.shop.supplier.service;

import org.apache.ibatis.annotations.Param;

import so.sao.shop.supplier.domain.authorized.Permission;

import java.util.List;

/**
 * Created by acer on 2017/7/11.
 */
public interface PermissionService {
	/**
	 * 查询所有权限
	 * @return
	 */
    public List<Permission> findAll();
    public List<Permission> findByUserId(@Param("userId") Long userId);
}
