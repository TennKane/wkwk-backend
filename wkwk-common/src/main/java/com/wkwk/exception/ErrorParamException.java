package com.wkwk.exception;

import com.wkwk.constant.ResponseConstant;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
public class ErrorParamException extends CustomException{

    public ErrorParamException() {
        super(ResponseConstant.PARAM_INVALID);
    }

    public ErrorParamException(String msg) {
        super(msg);
    }
}