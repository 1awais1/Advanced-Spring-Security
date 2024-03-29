package com.example.springsecurity.demo.Security;

import com.google.common.collect.Sets;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

public enum ApplicationUserRole {
    STUDENT(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(ApplicationUserPermission.STUDENT_WRITE,
            ApplicationUserPermission.STUDENT_READ,
            ApplicationUserPermission.COURSE_READ,
            ApplicationUserPermission.COURSE_WRITE)),

    ADMIN_TRAINEE(Sets.newHashSet(ApplicationUserPermission.STUDENT_READ,
            ApplicationUserPermission.COURSE_READ));


    private final Set<ApplicationUserPermission> permissionSet;

    ApplicationUserRole(Set<ApplicationUserPermission> permissionSet) {
        this.permissionSet = permissionSet;
    }

    public Set<ApplicationUserPermission> getPermissionSet() {
        return permissionSet;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> permissions = getPermissionSet().stream().map(x -> new SimpleGrantedAuthority(x.getPermission())).collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE" + this.name()));
        return permissions;
    }

}
