package com.sao.so.shop.auth;

import com.sao.so.shop.dao.PermissionDao;
import com.sao.so.shop.dao.UserDao;
import com.sao.so.shop.domain.Permission;
import com.sao.so.shop.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Date;
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
            return new User(user.getId(), user.getUsername(), user.getPassword(), grantedAuthorities,user.getLastPasswordResetDate());
        } else {
            throw new UsernameNotFoundException("用户: " + username + "不存在!");
        }
    }
}