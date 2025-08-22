package com.zy.ws.mgpdf.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CapturaDocumentoDto {

    private String tokenTransaccion;
    private String resultadoBio;
    private String scoreBio;
    private String fecha;
    private String nuDocumento;
    private String apPaterno;
    private String apMaterno;
    private String nombres;
    private String nacionalidad;
    private String feNac;
    private String status;
    private byte[] anverso;
    private byte[] reverso;
}
