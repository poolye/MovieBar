package com.tec666.moviebar.config.exception;

import lombok.Data;

/**
 * @author longge93
 */
@Data
public class BaseException extends RuntimeException{

    private String message;

    public BaseException(String message) {
        super(message);
        this.message = message;
    }
}
