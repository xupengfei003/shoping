package so.sao.shop.supplier.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import so.sao.shop.supplier.dao.authorized.PermissionDao;
import so.sao.shop.supplier.domain.authorized.Permission;
import so.sao.shop.supplier.service.PermissionService;

import java.util.List;

/**
 * Created by acer on 2017/7/11.
 */
@Service
public class PermissionServiceImpl implements PermissionService {
	
	@Autowired
	private PermissionDao permissionDao;

	@Override
	public List<Permission> findAll() {
		return permissionDao.findAll();
	}

	@Override
	public List<Permission> findByUserId(Long userId) {
		return permissionDao.findByUserId(userId);
	}
}
