package com.winnie.common.exception.controller;

import com.winnie.common.exception.pojo.ExceptionResult;
import com.winnie.common.exception.pojo.WNException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerWNExceptionController {
    @ExceptionHandler(WNException.class)
    public ResponseEntity<ExceptionResult> HandlerWNException(WNException e){
        return ResponseEntity.status(e.getStatus()).body(new ExceptionResult(e));
    }
}
