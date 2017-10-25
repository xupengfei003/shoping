package so.sao.shop.supplier.service.authorized;

import java.util.List;

import so.sao.shop.supplier.domain.authorized.Permission;

public interface IPermissionService {

	public void insert(Permission permission);
	
	public void startInsert(Permission permission);
	
	public List<Permission> findAll();
	
}
