package ru.deathman.rpgportal.core.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.deathman.rpgportal.common.domain.Role;
import ru.deathman.rpgportal.common.domain.User;
import ru.deathman.rpgportal.common.repository.api.RoleRepository;
import ru.deathman.rpgportal.common.repository.api.UserRepository;
import ru.deathman.rpgportal.core.service.api.ConverterService;
import ru.deathman.rpgportal.core.service.api.UserService;
import ru.deathman.rpgportal.core.vo.UserVo;

/**
 * @author Виктор
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ConverterService converterService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserVo findUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        return converterService.convert(user, UserVo.class);
    }

    @Override
    public UserVo createUser(UserVo userVo) {
        if (userRepository.findByUsername(userVo.getUsername()) != null) {
            throw new IllegalArgumentException("User with username " + userVo.getUsername() + " already exists");
        }
        User user = converterService.convert(userVo, User.class);
        user.setPasswordHash(passwordEncoder.encode(userVo.getPassword()));
        user.getRoles().add(roleRepository.findOne(Role.USER));
        userRepository.save(user);
        return converterService.convert(user, UserVo.class);
    }
}
