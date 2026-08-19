package com.immx.industrialsupport.webui.session;

import com.immx.industrialsupport.contracts.authorization.LoginResponse;
import com.immx.industrialsupport.contracts.role.RoleName;
import com.vaadin.flow.spring.annotation.VaadinSessionScope;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.UUID;

@Component
@VaadinSessionScope
public class UserSession {

    private LoginResponse loginResponse;

    public void authenticate(LoginResponse loginResponse) {
        this.loginResponse = loginResponse;
    }

    public boolean isAuthenticated() {
        return loginResponse != null && loginResponse.accessToken() != null && !loginResponse.accessToken()
                .isBlank();
    }

    public String getAccessToken() {
        if(!isAuthenticated())
            return null;

        return loginResponse.accessToken();
    }

    public String getAuthorizationHeader() {
        if(!isAuthenticated())
            return null;

        return loginResponse.tokenType() + " " + loginResponse.accessToken();
    }

    public String getUsername() {
        if(!isAuthenticated())
            return null;

        return loginResponse.username();
    }

    public UUID getUserId() {
        if(!isAuthenticated())
            return null;

        return loginResponse.userId();
    }

    public UUID getOrganizationId() {
        if(!isAuthenticated())
            return null;

        return loginResponse.organizationId();
    }

    public UUID getDepartmentId() {
        if(!isAuthenticated())
            return null;

        return loginResponse.departmentId();
    }

    public Set<RoleName> getRoles() {
        if(!isAuthenticated() || loginResponse.roles() == null)
            return Set.of();

        return Set.copyOf(loginResponse.roles());
    }

    public boolean hasRole(RoleName role) {
        return getRoles().contains(role);
    }

    public void logout() {
        loginResponse = null;
    }
}
