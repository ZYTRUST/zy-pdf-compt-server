package com.zy.ws.mgpdf.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DeteccionVidaDto {
    private String tokenTransaccion;
    private String sessionId;
    private String fecha;
    private String resultado;
    private String modeloDispositivo;
    private String versionOS;
}
