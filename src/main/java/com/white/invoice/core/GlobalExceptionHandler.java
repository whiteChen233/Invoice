package com.white.invoice.core;

import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public R handleException(Model model, Exception e) {
        R r = R.error();
        String msg = "系统异常:" + e.getMessage();
        if (e instanceof MethodArgumentNotValidException) {
            MethodArgumentNotValidException manve = (MethodArgumentNotValidException) e;
            FieldError fieldError = manve.getBindingResult().getFieldError();
            msg = "格式错误(" + fieldError.getField() + "): " + fieldError.getDefaultMessage();
        }
        if (e instanceof BindException) {
            BindException be = (BindException) e;
            FieldError fieldError = be.getBindingResult().getFieldError();
            msg = "格式错误(" + fieldError.getField() + "): " + fieldError.getDefaultMessage();
        }
        r.setMsg(msg);
        log.error("GlobalExceptionHandler: {}", msg, e);
        model.addAttribute("response", r);
        return r;
    }
}
