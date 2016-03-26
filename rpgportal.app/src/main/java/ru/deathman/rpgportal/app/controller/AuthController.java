package ru.deathman.rpgportal.app.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.deathman.rpgportal.core.security.service.api.TokenAuthenticationService;
import ru.deathman.rpgportal.core.service.api.UserService;
import ru.deathman.rpgportal.core.vo.ApiResponse;
import ru.deathman.rpgportal.core.vo.UserVo;

import javax.validation.Valid;

/**
 * @author Виктор
 */
@RestController
@RequestMapping(value = "/api/auth",
                consumes = MediaType.APPLICATION_JSON_VALUE,
                produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthController extends BaseController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenAuthenticationService tokenAuthenticationService;

    private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ApiResponse> login(@RequestBody @Valid UserVo user) {
        logger.debug("User with username " + user.getUsername() + " try to login");
        UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
        Authentication authentication = authenticationManager.authenticate(loginToken);

        String token = tokenAuthenticationService.createToken(authentication.getName());

        logger.info("User with username " + user.getUsername() + " successfully login");
        return getResponse(token, HttpStatus.OK);
    }

    @RequestMapping(value = "signin", method = RequestMethod.POST)
    @ResponseBody
    public ResponseEntity<ApiResponse> singin(@RequestBody @Valid UserVo user) {
        UserVo resultUser = userService.createUser(user);
        return getResponse(resultUser, HttpStatus.OK);
    }

    @RequestMapping(value = "/user/current", method = RequestMethod.GET)
    @ResponseBody
    public ResponseEntity<ApiResponse> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserVo userVo = userService.findUserByUsername(authentication.getName());
        return getResponse(userVo, HttpStatus.OK);
    }
}
