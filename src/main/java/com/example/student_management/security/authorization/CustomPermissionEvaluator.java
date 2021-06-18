package com.example.student_management.security.authorization;

import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Locale;

public class CustomPermissionEvaluator implements PermissionEvaluator {
    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        if(authentication == null || targetDomainObject == null || !(permission instanceof String)){
            return false;
        }
        String targetType = targetDomainObject.getClass().getSimpleName().toLowerCase();
        return hasPrivilege(authentication, targetType, permission.toString().toLowerCase());
    }

    private boolean hasPrivilege(Authentication authentication, String targetType, String permission) {
        for (GrantedAuthority grantedAuthority: authentication.getAuthorities()){
            String [] arr = grantedAuthority.getAuthority().split("_");

        }
        return false;

    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        if ((authentication == null) || (targetType == null) || !(permission instanceof String)) {
            return false;
        }
        return hasPrivilege(authentication, targetType.toUpperCase(),
                permission.toString().toUpperCase());
    }
}
