package com.wkwk.constant;

/**
 * @author <a href="https://github.com/TennKane">gtkkang</a>
 */
public class InteractConstant {
    /**
     * 私信内容最大长度
     */
    public static final Integer MESSAGE_MAX_LENGTH = 200;

    /**
     * 未互关朋友最多发送私信数量
     */
    public static final Integer MESSAGE_MAX_COUNT = 3;


    /**
     * 私信redis key
     */
    public static final String REDIS_PRIVATE_MESSAGE_KEY = "private_message:";

    /**
     * Redis中保存的最多私信数量
     */
    public static final Integer REDIS_PRIVATE_MESSAGE_COUNT = 30;
}
