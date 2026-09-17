package com.mengzhihua.utils.util;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Exception message and stack-trace helpers.
 */
public final class ExceptionUtil {

    private ExceptionUtil() {
    }

    public static String getMessage(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        String message = throwable.getMessage();
        return message == null ? throwable.getClass().getSimpleName() : message;
    }

    public static Throwable rootCause(Throwable throwable) {
        Throwable current = throwable;
        while (current != null && current.getCause() != null && current.getCause() != current) {
            current = current.getCause();
        }
        return current;
    }

    public static String stacktrace(Throwable throwable) {
        if (throwable == null) {
            return "";
        }
        StringWriter writer = new StringWriter();
        throwable.printStackTrace(new PrintWriter(writer));
        return writer.toString();
    }
}
