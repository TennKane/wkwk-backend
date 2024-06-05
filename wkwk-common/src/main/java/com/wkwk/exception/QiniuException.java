package com.wkwk.exception;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
public class QiniuException extends CustomException{
    public QiniuException(){
        super("七牛云上传文件异常");
    }
    public QiniuException(String msg){
        super(msg);
    }

}