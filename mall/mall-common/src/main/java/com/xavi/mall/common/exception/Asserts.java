package com.xavi.mall.common.exception;

import com.xavi.mall.common.api.IErrorCode;

/**
 * 断言处理类，用于抛出各种API异常
 * Created by xavier
 */
public class Asserts {
    public static void fail(String message) {
        throw new ApiException(message);
    }

    public static void fail(IErrorCode errorCode) {
        throw new ApiException(errorCode);
    }

    /**
     * 断言条件为真，否则抛出异常
     *
     * @param condition 判断条件
     * @param message   异常信息
     */
    public static void isTrue(boolean condition, String message) {
        if (!condition) {
            throw new ApiException(message);
        }
    }

    /**
     * 断言对象不为null，否则抛出异常
     *
     * @param obj     被检查对象
     * @param message 异常信息
     */
    public static void notNull(Object obj, String message) {
        if (obj == null) {
            throw new ApiException(message);
        }
    }

    /**
     * 断言字符串不为空（null或空串），否则抛出异常
     *
     * @param str     被检查字符串
     * @param message 异常信息
     */
    public static void notEmpty(String str, String message) {
        if (str == null || str.isEmpty()) {
            throw new ApiException(message);
        }
    }
}
