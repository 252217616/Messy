package com.example.demo01.exception;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@RestControllerAdvice
public class ExceptionController {

    @ExceptionHandler(NullPointerException.class)
    public String isNull (){
        return "不能为空";
    }
    @ExceptionHandler(ConstraintViolationException.class)
    public String isNu (){
        return "不能为空";
    }


}
