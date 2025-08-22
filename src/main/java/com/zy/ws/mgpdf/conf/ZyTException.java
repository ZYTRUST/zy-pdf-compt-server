package com.zy.ws.mgpdf.conf;

import com.zy.lib.message.resource.ErroresEnum;

public class ZyTException extends RuntimeException {

    private final ErroresEnum error;

    public ZyTException(ErroresEnum error) {
        this.error = error;
    }

    public ZyTException(String message, Throwable cause, ErroresEnum error) {
        super(message, cause);
        this.error = error;
    }

    public ZyTException(String message, ErroresEnum error) {
        super(message);
        this.error = error;
    }

    public ZyTException(Throwable cause, ErroresEnum error) {
        super(cause);
        this.error = error;
    }

    public ZyTException() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public ErroresEnum getError() {
        return this.error;
    }
}