/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zy.ws.mgpdf.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 *
 * @author jmiraval
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
public class ZyFileNotFoundException extends RuntimeException {

    public ZyFileNotFoundException(String message) {
        super(message);
    }

    public ZyFileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
