package ru.deathman.rpgportal.core.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Виктор
 */
public class ApiError {

    private String code;
    private String message;
    private List<FieldErrorVo> fieldErrors = new ArrayList<>();

    public ApiError(String code, String message) {
        this.code = code;
        this.message = message;
    }

    public ApiError(String code, String message, List<FieldErrorVo> fieldErrors) {
        this.code = code;
        this.message = message;
        this.fieldErrors = fieldErrors;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<FieldErrorVo> getFieldErrors() {
        return fieldErrors;
    }

    public void setFieldErrors(List<FieldErrorVo> fieldErrors) {
        this.fieldErrors = fieldErrors;
    }
}
