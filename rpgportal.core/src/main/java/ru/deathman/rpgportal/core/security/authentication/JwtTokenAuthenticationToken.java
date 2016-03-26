package ru.deathman.rpgportal.core.security.authentication;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author Виктор
 */
public class JwtTokenAuthenticationToken implements Authentication {

    private String token;
    private UserDetails details;
    private boolean authenticated;

    public JwtTokenAuthenticationToken(String token) {
        this.token = token;
        this.authenticated = false;
    }

    public JwtTokenAuthenticationToken(UserDetails user) {
        details = user;
        this.authenticated = true;
    }

    public String getToken() {
        return token;
    }

    @Override
    public String getName() {
        return details.getUsername();
    }

    @Override
    @SuppressWarnings("unchecked")
    public Collection<GrantedAuthority> getAuthorities() {
        return (Collection<GrantedAuthority>) details.getAuthorities();
    }

    @Override
    public String getCredentials() {
        return details.getPassword();
    }

    @Override
    public UserDetails getDetails() {
        return details;
    }

    @Override
    public String getPrincipal() {
        return details.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        if (isAuthenticated != authenticated) {
            throw new IllegalArgumentException();
        }
        authenticated = isAuthenticated;
    }
}
