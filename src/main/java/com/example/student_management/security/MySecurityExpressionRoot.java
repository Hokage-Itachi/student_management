package com.example.student_management.security;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.authentication.AuthenticationTrustResolver;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class MySecurityExpressionRoot implements MethodSecurityExpressionOperations {
    protected final Authentication authentication;
    private AuthenticationTrustResolver trustResolver;
    private RoleHierarchy roleHierarchy;
    private Set<String> roles;
    private String defaultRolePrefix = "ROLE_";

    public final boolean permitAll = true;
    public final boolean denyAll = false;
    private PermissionEvaluator permissionEvaluator;

    private Object filterObject;
    private Object returnObject;

    public MySecurityExpressionRoot(Authentication authentication) {
        if (authentication == null) {
            throw new IllegalArgumentException("Authentication object cannot be null");
        }
        this.authentication = authentication;
    }

    @Override
    public final boolean hasAuthority(String authority) {
        throw new RuntimeException("method hasAuthority() not allowed");
    }

    @Override
    public final boolean hasAnyAuthority(String... authorities) {
        return hasAnyAuthorityName(authorities);
    }

    @Override
    public final boolean hasRole(String role) {
        return hasAnyRole(role);
    }

    @Override
    public final boolean hasAnyRole(String... roles) {
        return hasAnyAuthorityName(roles);
    }

    private boolean hasAnyAuthorityName(String... roles) {
        List<String> authorities = Arrays.asList(roles);
        for (GrantedAuthority grantedAuthority : authentication.getAuthorities()) {
            String authority = grantedAuthority.getAuthority();
//            System.out.println(authority);
            if (authorities.contains(authority)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public final Authentication getAuthentication() {
        return authentication;
    }

    @Override
    public final boolean permitAll() {
        return permitAll;
    }

    @Override
    public final boolean denyAll() {
        return denyAll;
    }

    @Override
    public final boolean isAnonymous() {
        return trustResolver.isAnonymous(authentication);
    }

    @Override
    public final boolean isAuthenticated() {
        return !isAnonymous();
    }

    @Override
    public final boolean isRememberMe() {
        return trustResolver.isRememberMe(authentication);
    }

    @Override
    public final boolean isFullyAuthenticated() {
        return !trustResolver.isAnonymous(authentication) && !trustResolver.isRememberMe(authentication);
    }

    public void setTrustResolver(AuthenticationTrustResolver trustResolver) {
        this.trustResolver = trustResolver;
    }

    public void setRoleHierarchy(RoleHierarchy roleHierarchy) {
        this.roleHierarchy = roleHierarchy;
    }

    @Override
    public boolean hasPermission(Object target, Object permission) {
        return permissionEvaluator.hasPermission(authentication, target, permission);
    }

    @Override
    public boolean hasPermission(Object targetId, String targetType, Object permission) {
        return permissionEvaluator.hasPermission(authentication, (Serializable) targetId, targetType, permission);
    }

    public void setPermissionEvaluator(PermissionEvaluator permissionEvaluator) {
        this.permissionEvaluator = permissionEvaluator;
    }

    @Override
    public Object getFilterObject() {
        return this.filterObject;
    }

    @Override
    public Object getReturnObject() {
        return this.returnObject;
    }

    @Override
    public Object getThis() {
        return this;
    }

    @Override
    public void setFilterObject(Object obj) {
        this.filterObject = obj;
    }

    @Override
    public void setReturnObject(Object obj) {
        this.returnObject = obj;
    }
}
