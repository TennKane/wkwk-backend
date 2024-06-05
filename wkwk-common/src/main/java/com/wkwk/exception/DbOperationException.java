package com.wkwk.exception;

/**
 * 数据库操作异常
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
public class DbOperationException extends CustomException{
    public DbOperationException(String msg) {
        super(msg);
    }

    public DbOperationException() {
        super("数据库操作异常");
    }
}

