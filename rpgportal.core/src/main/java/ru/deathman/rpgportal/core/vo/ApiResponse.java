package ru.deathman.rpgportal.core.vo;

import org.springframework.http.HttpStatus;

/**
 * @author Виктор
 */
public class ApiResponse {

    private String status;
    private Object data;
    private ApiError error;

    public ApiResponse(HttpStatus status, Object data) {
        this.status = status.toString();
        this.data = data;
    }

    public ApiResponse(HttpStatus status, Object data, ApiError error) {
        this(status, data);
        this.error = error;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ApiError getError() {
        return error;
    }

    public void setError(ApiError error) {
        this.error = error;
    }
}
