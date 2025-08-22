package com.zy.ws.mgpdf.controller.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class JasperFormato8Dto{

    private List<FirmantesDto> listFirmantes;
    private boolean viewImg;
    private boolean viewBarcode;
    private boolean viewQR;
    private String idDocumento;
    private String urlQR;
    private String resource;
    private String nuCuenta;
    private String logo;
    private String textQr;
    private String codeQr;
    private String fechaQr;

    @Override
    public String toString() {
        return "JasperFormato8Dto [listFirmantes=" + listFirmantes + ", viewImg=" + viewImg + ", viewBarcode=" + viewBarcode
                + ", viewQR=" + viewQR + ", idDocumento=" + idDocumento + ", urlQR=" + urlQR + ", resource=" + resource
                + ", nuCuenta=" + nuCuenta + ", textQr=" + textQr + ", codeQr=" + codeQr + ", fechaQr=" + fechaQr + "]";
    }
}
