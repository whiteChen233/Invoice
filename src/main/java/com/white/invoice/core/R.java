package com.white.invoice.core;

import java.io.Serializable;

import com.alibaba.fastjson.JSONObject;

import lombok.Data;

@Data
public class R implements Serializable {

    /**
     * The constant SUCCESS.
     */
    private static final int SUCCESS = 0;

    /**
     * The constant FAIL.
     */
    private static final int FAIL = 1;

    /**
     * The Data.
     */
    private JSONObject data;

    /**
     * The Status.
     */
    private int status = SUCCESS;

    /**
     * The Msg.
     */
    private String msg = "";

    /**
     * Data r.
     *
     * @param data the data
     * @return the r
     */
    public R data(JSONObject data) {
        setData(data);
        return this;
    }

    /**
     * Status r.
     *
     * @param status the status
     * @return the r
     */
    public R status(int status) {
        setStatus(status);
        return this;
    }

    /**
     * Message r.
     *
     * @param msg the msg
     * @return the r
     */
    public R message(String msg) {
        setMsg(msg);
        return this;
    }

    /**
     * Error r.
     *
     * @param e the e
     * @return the r
     */
    public R error(Throwable e) {
        return status(FAIL).message(e.toString());
    }

    /**
     * Ok r.
     *
     * @return the r
     */
    public static R ok() {
        return new R();
    }

    /**
     * Fail r.
     *
     * @return the r
     */
    public static R error() {
        return ok().status(FAIL);
    }
}

