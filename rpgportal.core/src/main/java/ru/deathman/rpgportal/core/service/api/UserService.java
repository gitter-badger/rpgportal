package ru.deathman.rpgportal.core.service.api;


import ru.deathman.rpgportal.core.vo.UserVo;

/**
 * @author Виктор
 */
public interface UserService {

    UserVo findUserByUsername(String username);
    UserVo createUser(UserVo userVo);
}
