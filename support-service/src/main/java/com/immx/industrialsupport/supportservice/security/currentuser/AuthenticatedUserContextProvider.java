package com.immx.industrialsupport.supportservice.security.currentuser;

import com.immx.industrialsupport.contracts.role.RoleName;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class AuthenticatedUserContextProvider {

    public AuthenticatedUserContext getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext()
                .getAuthentication();

        if(!(authentication instanceof JwtAuthenticationToken jwtAuthentication) || !authentication.isAuthenticated()) {
            throw new AuthenticationCredentialsNotFoundException("Аутентифицированный пользователь не найден.");
        }

        Jwt jwt = jwtAuthentication.getToken();

        List<String> roleClaims = jwt.getClaimAsStringList("roles");

        Set<RoleName> roles = roleClaims == null ? Set.of() : roleClaims.stream()
                .map(RoleName::valueOf)
                .collect(Collectors.toUnmodifiableSet());

        return new AuthenticatedUserContext(
                UUID.fromString(jwt.getSubject()),
                UUID.fromString(jwt.getClaimAsString("organizationId")),
                UUID.fromString(jwt.getClaimAsString("departmentId")),
                jwt.getClaimAsString("username"),
                roles);
    }
}
