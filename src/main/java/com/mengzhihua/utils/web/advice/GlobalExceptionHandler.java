package com.mengzhihua.utils.web.advice;


import java.util.stream.Collectors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

import com.mengzhihua.utils.common.api.Result;
import com.mengzhihua.utils.common.api.ResultCode;
import com.mengzhihua.utils.common.exception.BizException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

/**
 * Converts known exceptions into the unified {@link Result} envelope.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BizException.class)
    public Result<Void> handleBizException(BizException ex, HttpServletRequest request) {
        log.warn("Business error {} {} : {}", request.getMethod(), request.getRequestURI(), ex.getMessage());
        return Result.fail(ex.getCode(), ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        return Result.fail(ResultCode.VALIDATION_ERROR.getCode(), firstFieldError(ex));
    }

    @ExceptionHandler(BindException.class)
    public Result<Void> handleBindException(BindException ex) {
        return Result.fail(ResultCode.VALIDATION_ERROR.getCode(), firstFieldError(ex));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public Result<Void> handleConstraintViolation(ConstraintViolationException ex) {
        String message = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.joining("; "));
        return Result.fail(ResultCode.VALIDATION_ERROR.getCode(), message);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public Result<Void> handleMissingParameter(MissingServletRequestParameterException ex) {
        return Result.fail(ResultCode.BAD_REQUEST.getCode(), "missing parameter: " + ex.getParameterName());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleNotReadable(HttpMessageNotReadableException ex) {
        return Result.fail(ResultCode.BAD_REQUEST.getCode(), "request body is invalid");
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public Result<Void> handleMethodNotSupported(HttpRequestMethodNotSupportedException ex) {
        return Result.fail(ResultCode.BAD_REQUEST.getCode(), "method not supported: " + ex.getMethod());
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public Result<Void> handleNotFound(NoResourceFoundException ex) {
        return Result.fail(ResultCode.NOT_FOUND.getCode(), "resource not found");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public Result<Void> handleIllegalArgument(IllegalArgumentException ex) {
        return Result.fail(ResultCode.BAD_REQUEST.getCode(), ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception ex, HttpServletRequest request) {
        log.error("Unhandled error {} {}", request.getMethod(), request.getRequestURI(), ex);
        return Result.fail(ResultCode.FAILED);
    }

    private static String firstFieldError(BindException ex) {
        FieldError fieldError = ex.getBindingResult().getFieldError();
        if (fieldError == null) {
            return ResultCode.VALIDATION_ERROR.getMessage();
        }
        return fieldError.getField() + " " + fieldError.getDefaultMessage();
    }
}
