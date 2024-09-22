package com.wkwk.exception;




import lombok.Getter;

/**
 * 自定义异常
 */
@Getter
public class CustomException extends RuntimeException {

    public CustomException() {

    }

    public CustomException(String message) {
        super(message);
    }
}
