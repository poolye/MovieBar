package com.tec666.moviebar.config.response;


import lombok.Data;

import java.io.Serializable;

/**
 * @author longge93
 */
@Data
public class AjaxResponse<T> implements Serializable {

    private boolean status;

    private int code;

    private String msg;

    private T data;

    public AjaxResponse() {
        this.status = true;
        this.code = 200;
        this.msg = "success";
    }

    public AjaxResponse(boolean status, int code, String msg) {
        this.status = status;
        this.code = code;
        this.msg = msg;
    }

    public AjaxResponse(boolean status, int code, String msg, T data) {
        this.status = status;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

}
