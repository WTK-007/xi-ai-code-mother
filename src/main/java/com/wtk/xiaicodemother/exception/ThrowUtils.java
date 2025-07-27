package com.wtk.xiaicodemother.exception;

public class ThrowUtils {

    /**
     * 条件成立则抛出异常
     * @param condition
     * @param runtimeException
     */
    public static void throwIf(boolean condition,RuntimeException runtimeException) {
        if (condition) {
            throw runtimeException;
        }
    }

    /**
     * 条件成立则抛异常
     * @param condition 条件
     * @param errorCode 错误码
     */
    public static void throwIf(boolean condition, ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }

    public static void throwIf(boolean condition, ErrorCode errorCode, String message) {
        throwIf(condition, new BusinessException(errorCode, message));
    }
}
