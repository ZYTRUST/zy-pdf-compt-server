/*
* @(#)JasperImagenFirmaDto.java
*
* Copyright 2019 ZyTrust SA, Todos los derechos reservados.
* ZT PROPRIETARIO/CONFIDENTIALIDAD. Su uso está sujeto a los
* términos de la licencia adquirida a ZyTrust SA.
* No se permite modificar, copiar ni difundir sin autorización
* expresa de ZyTrust SA.
*/
package com.zy.ws.mgpdf.controller.dto;

import javax.validation.constraints.NotBlank;

import com.zy.lib.common.util.Tool;

import lombok.Getter;
import lombok.Setter;

/**
 * Esta clase representa
 *
 * @author Gustavo Jimenez
 * @version 1.0, 14/04/2021
 * @since 1.0
 */
@Setter
@Getter
public class JasperImagenFirmaDto {

	@NotBlank
	private byte[] imgBio;
	@NotBlank
	private String imgNumDoc;
	@NotBlank
	private String imgNombres;
	@NotBlank
	private String imgCodigoBarra;
	private String imgEmail;
	private String imgFecha;
	private String imgHash;
	private String imgFirma;
	private String imgCodigo;
	@Override
	public String toString() {
		return "JasperImagenFirmaDto [imgBio=" + !Tool.isNullOrEmpty(imgBio) + ", imgNumDoc=" + imgNumDoc + ", imgNombres="
				+ imgNombres + ", imgCodigoBarra=" + imgCodigoBarra + ", imgEmail=" + imgEmail + ", imgFecha=" +
				imgFecha + ", imgHash=" + imgHash + ", imgFirma="+ imgFirma + ", imgCodigo=" + imgCodigo + "]";
	}


}
