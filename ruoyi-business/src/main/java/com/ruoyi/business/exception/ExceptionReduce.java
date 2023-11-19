package com.ruoyi.business.exception;

import com.ruoyi.common.core.domain.AjaxResult;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ExceptionReduce {
    @ExceptionHandler(CustomerException.class)
    public AjaxResult reduce(CustomerException e) {
        return AjaxResult.error(e.getMessage());
    }
}
