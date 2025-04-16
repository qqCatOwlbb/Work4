package com.catowl.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.util.Collection;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails {
    private Long id;

    private String username;

    private String password;

    private String avatar;  // 头像地址，可以为空
    private String bio;  // 个人简介，可以为空

    private Date created_at;

    private Date updated_at;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null; // 这里可以实现角色权限
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
