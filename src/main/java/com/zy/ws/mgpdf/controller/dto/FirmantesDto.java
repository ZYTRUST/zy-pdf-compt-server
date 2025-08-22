package com.zy.ws.mgpdf.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class FirmantesDto {
    private String nuDocumento;
    private byte[] huella;
    private byte[] foto;
    private String nombres;
    private String deTipoDoc;
    private String codigoBarra;
    private String domicilio;
    
}
