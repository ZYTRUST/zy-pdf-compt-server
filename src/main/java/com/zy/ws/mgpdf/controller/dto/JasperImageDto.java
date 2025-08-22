package com.zy.ws.mgpdf.controller.dto;

import com.zy.ws.mgpdf.util.IndicadorType;
import lombok.Data;

@Data
public class JasperImageDto {

    private byte[] image;
    private String numDoc;
    private String nombres;
    private String jasper;
    private String email;
    private String fecha;
    private String hash;
    private String firma;
    private String codigo;
    private IndicadorType indicador;
}

