package com.winnie.common.exception.pojo;

import lombok.Getter;
import org.joda.time.DateTime;

@Getter
public class ExceptionResult {
    private Integer status;
    private String message;
    private String timestamp;

    public ExceptionResult(WNException e) {
        this.status = e.getStatus();
        this.message = e.getMessage();
        this.timestamp = DateTime.now().toString("yyyy-MM-dd HH:mm:ss");
    }
}
