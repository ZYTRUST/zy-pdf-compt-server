package com.zy.ws.mgpdf.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Setter
@Getter
public class FirmaReporteDto {

    private String origenTransaccion;
    private String tokenTransaccion;
    private String fecha;
    private String status;
    private String hashDocFirmado;
    private String nombres;
    private String dni;
    private String correoOtp;
    private BigDecimal ctrtoId;
    private BigDecimal cliTiDoc;
    private BigDecimal sdetId;
    private String ctrtoIntermediacion;
    private String perfilInversion;
    private String declaracionMercado;
    private String declaracionResidencia;
    private String resource;
    private List<DocumentoDto> documentoDtoList;
    private List<DatosAdicional> datosAdicionalList;
}
