package ru.deathman.rpgportal.core.security.service.api;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Виктор
 */
public interface TokenAuthenticationService {

    String createToken(String username);
    Authentication verifyToken(Authentication authentication);
    Authentication autoLogin(HttpServletRequest request);

}
