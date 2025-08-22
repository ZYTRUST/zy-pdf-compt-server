package com.zy.ws.mgpdf.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ConsultaFaceReniecDto {
    private String tokenTransaccion;
    private String resultado;
    private String score;
    private String fecha;
    private String nombres;
    private String apPaterno;
    private String apMaterno;
    private String dni;
    private String feNac;
    private String status;
    private byte[] fotoReniec;
    private byte[] fotoSelfie;
    private String hashDocumento;
    private String hashRegistroAnterior;
}
