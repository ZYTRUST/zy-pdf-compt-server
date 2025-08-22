package com.zy.ws.mgpdf.controller.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Setter
@Getter
public class BmoReporteFirma {
    private CapturaDocumentoDto capturaDocumentoDto;
    private ConsultaFaceReniecDto consultaFaceReniecDto;
    private DeteccionVidaDto deteccionVidaDto;
    private JasperImageDto jasperImageDto;

    private BigDecimal sdetId;
    private BigDecimal ctrtoId;
    private BigDecimal cliTiDoc;
    private String cliNuDoc;
    private String contratoResource;

}
