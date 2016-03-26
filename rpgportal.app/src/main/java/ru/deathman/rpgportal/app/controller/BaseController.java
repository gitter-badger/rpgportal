package ru.deathman.rpgportal.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import ru.deathman.rpgportal.core.service.api.MessagesService;
import ru.deathman.rpgportal.core.vo.ApiResponse;

/**
 * @author Виктор
 */
@RestController
public class BaseController {

    @Autowired
    private MessagesService messagesService;

    protected ResponseEntity<ApiResponse> getResponse(Object data, HttpStatus status) {
        return new ResponseEntity<>(new ApiResponse(status, data), status);
    }

    protected String getLocalizedMessage(String code, Object... args) {
        return messagesService.getMessage(code, args);
    }
}
