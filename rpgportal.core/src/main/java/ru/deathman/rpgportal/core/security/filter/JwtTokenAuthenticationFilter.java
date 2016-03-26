package ru.deathman.rpgportal.core.security.filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.event.InteractiveAuthenticationSuccessEvent;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.filter.GenericFilterBean;
import ru.deathman.rpgportal.core.security.service.api.TokenAuthenticationService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * @author Виктор
 */
@Component
public class JwtTokenAuthenticationFilter extends GenericFilterBean {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;
    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        logger.debug("Processing url " + request.getRequestURI());
        if (!(request.getRequestURI().contains("/login") || request.getRequestURI().contains("/signin") || request.getRequestURI().contains("/health"))) {
            if (SecurityContextHolder.getContext().getAuthentication() == null) {
                Authentication tokenAuth = tokenAuthenticationService.autoLogin(request);

                if (tokenAuth != null) {
                        tokenAuth = authenticationManager.authenticate(tokenAuth);

                        SecurityContextHolder.getContext().setAuthentication(tokenAuth);

                        if (logger.isDebugEnabled()) {
                            logger.debug("SecurityContextHolder populated with jwt token: '"
                                    + SecurityContextHolder.getContext().getAuthentication()
                                    + "'");
                        }

                        if (this.eventPublisher != null) {
                            eventPublisher
                                    .publishEvent(new InteractiveAuthenticationSuccessEvent(
                                            SecurityContextHolder.getContext()
                                                    .getAuthentication(), this.getClass()));
                        }
                }
            } else {
                if (logger.isDebugEnabled()) {
                    logger.debug("SecurityContextHolder not populated with jwt, as it already contained: '"
                            + SecurityContextHolder.getContext().getAuthentication() + "'");
                }
            }
        }
        chain.doFilter(req, res);
    }

    @Override
    public void afterPropertiesSet() throws ServletException {
        Assert.notNull(authenticationManager, "authenticationManager must be specified");
        Assert.notNull(tokenAuthenticationService, "tokenAuthenticationService must be specified");
    }
}
