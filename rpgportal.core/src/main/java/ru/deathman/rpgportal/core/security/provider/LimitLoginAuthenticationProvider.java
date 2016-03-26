package ru.deathman.rpgportal.core.security.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.deathman.rpgportal.common.domain.User;
import ru.deathman.rpgportal.common.repository.api.UserRepository;

import java.util.Calendar;
import java.util.Date;

/**
 * @author Виктор
 */
@Component(value = "limitLoginAuthenticationProvider")
public class LimitLoginAuthenticationProvider extends DaoAuthenticationProvider {

    @Autowired
    private UserRepository userRepository;
    @Value("${user.login.max_attempts}")
    private int maxFailAttempts;
    @Value("${user.login.lockTime}")
    private int lockTime;

    @Autowired
    @Override
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }

    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        super.setPasswordEncoder(passwordEncoder);
    }

    @Override
    @Transactional(noRollbackFor = AuthenticationException.class)
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        try {
            Authentication auth = super.authenticate(authentication);
            userRepository.resetFailAttempts(auth.getName());
            return auth;
        } catch (BadCredentialsException e) {
            User user = userRepository.findByUsername(authentication.getName());

            if (user.getFailedAttempts() < maxFailAttempts) {
                user.setFailedAttempts(user.getFailedAttempts() + 1);
                userRepository.save(user);
                throw e;
            }
            user.setLocked(true);
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.MINUTE, lockTime);
            user.setLockEnd(calendar.getTime());
            userRepository.save(user);

            String message = "User " + user.getUsername() + " is locked until " + user.getLockEnd();
            throw new LockedException(message);
        } catch (LockedException e) {
            User user = userRepository.findByUsername(authentication.getName());

            if (new Date().after(user.getLockEnd())) {
                user.setFailedAttempts(0);
                user.setLocked(false);
                user.setLockEnd(null);
                userRepository.save(user);

                this.authenticate(authentication);
            }

            String message = "User " + user.getUsername() + " is locked until " + user.getLockEnd();
            throw new LockedException(message);
        }
    }
}
