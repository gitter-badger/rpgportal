package ru.deathman.rpgportal.app.handler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.deathman.rpgportal.core.service.api.MessagesService;
import ru.deathman.rpgportal.core.vo.ApiError;
import ru.deathman.rpgportal.core.vo.ApiResponse;
import ru.deathman.rpgportal.core.vo.FieldErrorVo;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Виктор
 */
@ControllerAdvice
public class RestExceptionHandler {

    @Autowired
    private MessagesService messagesService;

    @ExceptionHandler(value = { Exception.class})
    public ResponseEntity<ApiResponse> handleException(HttpServletRequest request, Exception exception) {
        return new ResponseEntity<>(getErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, "0", localizeMessage(exception.getMessage())), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = {AuthenticationException.class, AccessDeniedException.class})
    public ResponseEntity<ApiResponse> handleAuthenticationException(HttpServletRequest request, AuthenticationException exception) {
        return new ResponseEntity<>(getErrorResponse(HttpStatus.UNAUTHORIZED, "13", localizeMessage(exception.getMessage())), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public ResponseEntity<ApiResponse> handleValidatorException(HttpServletRequest request, MethodArgumentNotValidException exception) {
        BindingResult result = exception.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        return new ResponseEntity<>(getErrorResponse(HttpStatus.BAD_REQUEST, "2", localizeMessage("error.bad.arguments"), populateFieldErrors(fieldErrors)), HttpStatus.BAD_REQUEST);
    }

    private ApiResponse getErrorResponse(HttpStatus status, String code, String message) {
        return new ApiResponse(status, null, new ApiError(code, message));
    }

    private ApiResponse getErrorResponse(HttpStatus status, String code, String message, List<FieldErrorVo> fieldErrors) {
        return new ApiResponse(status, null, new ApiError(code, message, fieldErrors));
    }

    private List<FieldErrorVo> populateFieldErrors(List<FieldError> fieldErrorList) {
        List<FieldErrorVo> fieldErrors = new ArrayList<>();
        StringBuilder errorMessage = new StringBuilder();

        for (FieldError fieldError : fieldErrorList) {
            errorMessage.append(fieldError.getCode()).append(".");
            errorMessage.append(fieldError.getObjectName()).append(".");
            errorMessage.append(fieldError.getField());

            fieldErrors.add(new FieldErrorVo(fieldError.getField(), localizeMessage(errorMessage.toString(), fieldError.getArguments())));
            errorMessage.delete(0, errorMessage.capacity());
        }
        return fieldErrors;
    }

    protected String localizeMessage(String code, Object... args) {
        return messagesService.getMessage(code, args);
    }
}