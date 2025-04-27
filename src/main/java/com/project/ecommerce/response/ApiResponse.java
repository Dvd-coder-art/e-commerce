package com.project.ecommerce.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus
public class ApiResponse<T> {
    private boolean success;
    private String message;
    private T data;

    public ApiResponse(){}

    public ApiResponse(boolean success, String message, T data) {
        this.success = success;
        this.message = message;
        this.data = data;
    }


}
