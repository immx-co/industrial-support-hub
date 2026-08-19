package com.immx.industrialsupport.supportservice.security.currentuser;

import com.immx.industrialsupport.contracts.role.RoleName;

import java.util.Set;
import java.util.UUID;

public record AuthenticatedUserContext(UUID userId,
                                       UUID organizationId,
                                       UUID departmentId,
                                       String username,
                                       Set<RoleName> roles) {

    public boolean hasRole(RoleName role) {
        return roles.contains(role);
    }
}
