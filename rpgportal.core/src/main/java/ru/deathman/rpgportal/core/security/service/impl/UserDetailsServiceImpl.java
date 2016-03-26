package ru.deathman.rpgportal.core.security.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.deathman.rpgportal.common.domain.Role;
import ru.deathman.rpgportal.common.domain.User;
import ru.deathman.rpgportal.common.repository.api.UserRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Виктор
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    private static final Logger logger = LoggerFactory.getLogger(UserDetailsServiceImpl.class);

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.debug("Load user with username " + username);
        final User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.warn("User with username " + username + " not found");
            throw new UsernameNotFoundException("User with username " + username + " not found");
        }
        boolean locked = user.isLocked();
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
                user.getPasswordHash(), true, true, true, !locked, getAuthorities(user));
        return userDetails;
    }

    private List<GrantedAuthority> getAuthorities(User user) {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        }
        return authorities;
    }
}
