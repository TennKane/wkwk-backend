package com.wkwk.exception;

import com.wkwk.constant.ResponseConstant;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
public class UserNotExitedException extends CustomException {

    public UserNotExitedException() {
        super(ResponseConstant.USER_NOT_EXIST);
    }

    public UserNotExitedException(String message) {
        super(message);
    }
}
