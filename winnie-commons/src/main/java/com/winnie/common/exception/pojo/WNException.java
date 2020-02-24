package com.winnie.common.exception.pojo;

import lombok.Getter;

@Getter
public class WNException extends RuntimeException {
    private Integer status;

    public WNException(Integer status,String message) {
        super(message);
        this.status = status;
    }

    public WNException(ExceptionEnum e){
        super(e.getMessage());
        this.status = e.getStatus();
    }
}
