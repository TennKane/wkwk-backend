package com.wkwk.exception;

import com.wkwk.constant.ResponseConstant;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
public class PasswordErrorException extends CustomException{

    public PasswordErrorException() {
        super(ResponseConstant.PASSWORD_ERROR);
    }

    public PasswordErrorException(String msg) {
        super(msg);
    }
}
