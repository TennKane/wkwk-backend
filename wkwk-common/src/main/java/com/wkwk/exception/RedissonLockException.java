package com.wkwk.exception;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
public class RedissonLockException extends CustomException{
    public RedissonLockException(){
        super("redisson加锁失败");
    }
    public RedissonLockException(String msg){
        super(msg);
    }
}
