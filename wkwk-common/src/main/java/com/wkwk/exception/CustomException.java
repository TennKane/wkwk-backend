package com.wkwk.exception;



import com.wkwk.enums.AppHttpCodeEnum;
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
