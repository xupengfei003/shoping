package so.sao.shop.supplier.dao.authorized;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import so.sao.shop.supplier.domain.authorized.Permission;

import java.util.List;

/**
 * 权限dao
 *
 * @author
 * @create 2017-07-08 21:35
 **/
@Mapper
public interface PermissionDao {

	public void insert();
	
    public List<Permission> findAll();
    public List<Permission> findByUserId(@Param("userId") Long userId);
}
