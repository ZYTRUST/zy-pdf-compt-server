package com.zy.ws.mgpdf.conf;

import com.zy.lib.message.resource.ErroresEnum;
import com.zy.lib.service.exception.ZyException;
import com.zy.lib.service.resource.ZyResource;
import com.zy.lib.service.response.ZyResponse;
import com.zy.ws.mgpdf.util.Utilitario;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
public class RestExceptionHandler extends ZyResource<RestExceptionHandler> {

    public RestExceptionHandler(){
        super();
    }

    /**
     * Catch all for any other exceptions...
     * @return
     */
    @ExceptionHandler({ ZyException.class})
    @ResponseBody
    public ZyResponse<RestExceptionHandler> handleAnyException(ZyException ex, final WebRequest request) {
        log.error("",ex.getError());
        log.error("",Utilitario.getRootException(ex));
        return error(ex.getError());
    }

    @ExceptionHandler({ Exception.class})
    @ResponseBody
    public ZyResponse<RestExceptionHandler> handleException(Exception ex, final WebRequest request) {
        log.error("",Utilitario.getRootException(ex));
        return error(ErroresEnum.EXCEPTION);
    }

}
