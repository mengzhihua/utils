package com.mengzhihua.utils.common.exception;


import java.io.Serial;

import com.mengzhihua.utils.common.api.ResultCode;

/**
 * Business exception that is translated into a unified {@code Result} by the global handler.
 */
public class BizException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final int code;

    public BizException(String message) {
        this(ResultCode.FAILED.getCode(), message);
    }

    public BizException(ResultCode resultCode) {
        this(resultCode.getCode(), resultCode.getMessage());
    }

    public BizException(ResultCode resultCode, String message) {
        this(resultCode.getCode(), message);
    }

    public BizException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
