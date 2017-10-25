package so.sao.shop.supplier.service.authorized.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import so.sao.shop.supplier.dao.authorized.PermissionDao;
import so.sao.shop.supplier.domain.authorized.Permission;
import so.sao.shop.supplier.service.authorized.IPermissionService;

@Service
public class PermissionService implements IPermissionService {

	@Autowired
	private PermissionDao permissionDao;

	@Override
	public void insert(Permission permission) {
		permissionDao.insert(permission);
	}

	@Override
	public void startInsert(Permission permission) {
		List<Permission> permissions = this.findAll();
		/*if(permissions.size() <= 0) {
			this.insert(permission);
		}*/
		for(Permission per:permissions) {
			if (permission.getUrl().equals(per.getUrl())) {
				return ;
			}
		}		
		this.insert(permission);
	}
	
	@Override
	public List<Permission> findAll() {
		return permissionDao.findAll();
	}
	
}
