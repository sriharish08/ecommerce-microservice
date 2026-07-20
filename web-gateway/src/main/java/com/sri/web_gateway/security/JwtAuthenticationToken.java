package com.sri.web_gateway.security;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

public class
JwtAuthenticationToken extends AbstractAuthenticationToken {

    private final String token;
    private final String email;
    private final Long userId;
    private final String role;

    public JwtAuthenticationToken(String token) {
        super(List.of());
        this.token = token;
        this.email = null;
        this.userId = null;
        this.role = null;
        setAuthenticated(false);
    }

    public JwtAuthenticationToken(String email, Long userId, String role) {
        super(List.of(new SimpleGrantedAuthority("ROLE_" + role)));
        this.token = null;
        this.email = email;
        this.userId = userId;
        this.role = role;
        setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return email;
    }

    public String getEmail() {
        return email;
    }

    public Long getUserId() {
        return userId;
    }

    public String getRole() {
        return role;
    }
}
