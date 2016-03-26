package ru.deathman.rpgportal.core.converter;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.deathman.rpgportal.common.domain.Role;
import ru.deathman.rpgportal.common.domain.User;
import ru.deathman.rpgportal.core.vo.UserVo;

/**
 * @author Виктор
 */
@Component
@Qualifier("vo")
public class UserVoConverter extends AbstractTwoWayConverter<User, UserVo> {

    @Override
    protected UserVo convert(User source) {
        UserVo target = new UserVo();
        target.setId(source.getId());
        target.setUsername(source.getUsername());
        target.setFirstname(source.getFirstname());
        target.setLastname(source.getLastname());
        target.setEmail(source.getEmail());
        target.setBirthdate(source.getBirthdate());
        target.setFailedAttempts(source.getFailedAttempts());
        target.setLocked(source.isLocked());
        target.setLockEnd(source.getLockEnd());
        for (Role role : source.getRoles()) {
            target.getRoles().add(role.getName());
        }
        return target;
    }

    @Override
    protected User convertBack(UserVo source) {
        User target = new User();
        target.setUsername(source.getUsername());
        target.setFirstname(source.getFirstname());
        target.setLastname(source.getLastname());
        target.setEmail(source.getEmail());
        target.setBirthdate(source.getBirthdate());
        return target;
    }
}
