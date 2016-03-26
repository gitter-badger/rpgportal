package ru.deathman.rpgportal.core.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;
import ru.deathman.rpgportal.core.security.authentication.JwtTokenAuthenticationToken;
import ru.deathman.rpgportal.core.security.service.api.TokenAuthenticationService;

/**
 * @author Виктор
 */
@Component(value = "jwtTokenAuthenticationProvider")
public class JwtTokenAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        if (!supports(authentication.getClass())){
            return null;
        }
        return tokenAuthenticationService.verifyToken(authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtTokenAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
