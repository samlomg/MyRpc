package com.dglbc.dbtools.exception;

public class TipsShow extends RuntimeException {
    public TipsShow(String msg) {
        super(msg);
    }

    public static void alert(String msg) {
        throw new TipsShow(msg);
    }
}
