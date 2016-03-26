package ru.deathman.rpgportal.core.security.service.impl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.CredentialsExpiredException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.deathman.rpgportal.core.security.authentication.JwtTokenAuthenticationToken;
import ru.deathman.rpgportal.core.security.service.api.TokenAuthenticationService;
import ru.deathman.rpgportal.core.security.util.JwtUtil;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Виктор
 */
@Service
public class JwtTokenAuthenticationServiceImpl implements TokenAuthenticationService {

    private final Logger logger = LoggerFactory.getLogger(JwtTokenAuthenticationServiceImpl.class);

    private static final String AUTH_HEADER_NAME = "Authorization";
    private static final long TWO_WEEKS = 14 * 24 * 60 * 60 * 1000;

    @Value("${swt.token.secret}")
    private String secretKey;
    @Autowired
    private UserDetailsService userDetailsService;
    private AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

    @Override
    public Authentication autoLogin(HttpServletRequest request) {
        String token = request.getHeader(AUTH_HEADER_NAME);
        if (StringUtils.isEmpty(token) || !token.startsWith("Bearer")) {
            logger.warn("Empty or wrong authorization header");
            throw new AuthenticationCredentialsNotFoundException("Empty or wrong authorization header");
        }
        return new JwtTokenAuthenticationToken(token.substring(7));
    }

    @Override
    public Authentication verifyToken(Authentication authentication) {
        if (!(authentication instanceof JwtTokenAuthenticationToken)) {
            throw new IllegalArgumentException("authentication must be JwtTokenAuthenticationToken");
        }
        String token = ((JwtTokenAuthenticationToken) authentication).getToken();
        String username = null;
        try {
            username = JwtUtil.verifyToken(token, secretKey);
        } catch (ExpiredJwtException e) {
            logger.warn("Jwt token expired");
            throw new CredentialsExpiredException("Provided token expired", e);
        } catch (SignatureException e) {
            logger.warn("Jwt token has wrong signature", e);
            throw new BadCredentialsException("Provided token has wrong signature");
        }
        UserDetails user = userDetailsService.loadUserByUsername(username);
        detailsChecker.check(user);
        return new JwtTokenAuthenticationToken(user);
    }

    @Override
    public String createToken(String username) {
        UserDetails user = userDetailsService.loadUserByUsername(username);
        return JwtUtil.createToken(user, TWO_WEEKS, secretKey);
    }
}
