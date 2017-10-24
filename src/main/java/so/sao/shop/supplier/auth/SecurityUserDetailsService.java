package so.sao.shop.supplier.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import so.sao.shop.supplier.dao.PermissionDao;
import so.sao.shop.supplier.dao.UserDao;
import so.sao.shop.supplier.domain.User;
import so.sao.shop.supplier.domain.authorized.Permission;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 根据登陆名加载用户权限
 *
 * @author
 * @create 2017-07-08 21:49
 **/
public class SecurityUserDetailsService implements UserDetailsService {

    @Autowired
    UserDao userDao;
    @Autowired
    PermissionDao permissionDao;

    public UserDetails loadUserByUsername(String username) {
        User user = userDao.findByUsername(username);
        if (user != null) {
            List<Permission> permissions = permissionDao.findByUserId(user.getId());
            List<GrantedAuthority> grantedAuthorities =
                    permissions.stream()
                            .map(t -> new SimpleGrantedAuthority(t.getName()))
                            .collect(Collectors.toList());
            user.setAuthorities(grantedAuthorities);
            return user;
        } else {
            throw new UsernameNotFoundException("用户: " + username + "不存在!");
        }
    }
}