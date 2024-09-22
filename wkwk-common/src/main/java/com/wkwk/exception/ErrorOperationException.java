package com.wkwk.exception;

/**
 * 操作异常
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
public class ErrorOperationException extends CustomException {
    public ErrorOperationException(String message) {
        super(message);
    }

    public ErrorOperationException() {
        super("操作异常");
    }

}
