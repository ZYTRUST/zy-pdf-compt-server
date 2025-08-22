/*
* @(#)BmoReporteFirma.java
*
* Copyright 2019 ZyTrust SA, Todos los derechos reservados.
* ZT PROPRIETARIO/CONFIDENTIALIDAD. Su uso está sujeto a los
* términos de la licencia adquirida a ZyTrust SA.
* No se permite modificar, copiar ni difundir sin autorización
* expresa de ZyTrust SA.
*/
package com.zy.ws.mgpdf.controller.dto;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

/**
 * Esta clase representa
 *
 * @author Gustavo Jimenez
 * @version 1.0, 20/11/2020
 * @since 1.0
 */
@Getter
@Setter
public class ReporteFirmaRequestDto {

	private JasperFormato3CapDocDto capturaDocumentoDto;
	private JasperFormato3RNDto consultaFaceReniecDto;
	private JasperFormato3VidaDto deteccionVidaDto;
	private BigDecimal ctrtoId;
	private BigDecimal cliTiDoc;
	private BigDecimal sdetId;
	private String cliNuDoc;

	@Override
	public String toString() {
		return "BmoReporteFirma [capturaDocumentoDto=" + capturaDocumentoDto + ", consultaFaceReniecDto="
				+ consultaFaceReniecDto + ", deteccionVidaDto=" + deteccionVidaDto + ", ctrtoId=" + ctrtoId
				+ ", cliTiDoc=" + cliTiDoc + ", cliNuDoc=" + cliNuDoc + "]";
	}

}
