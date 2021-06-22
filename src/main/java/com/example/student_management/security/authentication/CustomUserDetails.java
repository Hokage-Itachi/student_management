package com.example.student_management.security.authentication;

import com.example.student_management.domain.Permission;
import com.example.student_management.domain.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomUserDetails implements UserDetails {

    private User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Set<Permission> user_permissions = user.getPermissions();
        Set<Permission> role_permissions = user.getRole().getPermissions();
        Set<String> permissions = new HashSet<>();
        if (user_permissions != null || !user_permissions.isEmpty()) {
            permissions = user_permissions.stream().map(Permission::getPerName).collect(Collectors.toSet());
        }
        if(role_permissions != null || !role_permissions.isEmpty()){
            permissions.addAll(role_permissions.stream().map(Permission::getPerName).collect(Collectors.toSet()));
        }
        return permissions.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());

    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
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

    @Override
    public boolean isEnabled() {
        return true;
    }
}
