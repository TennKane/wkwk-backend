package com.wkwk.exception;

import com.wkwk.constant.ResponseConstant;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
public class UserNotLoginException extends CustomException {
    public UserNotLoginException() {
        super(ResponseConstant.NEED_LOGIN);
    }
}
