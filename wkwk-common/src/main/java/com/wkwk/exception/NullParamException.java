package com.wkwk.exception;

import com.wkwk.constant.ResponseConstant;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
public class NullParamException extends CustomException {

    public NullParamException() {
        super(ResponseConstant.PARAM_REQUIRE);
    }
    public NullParamException(String msg) {
        super(msg);
    }
}