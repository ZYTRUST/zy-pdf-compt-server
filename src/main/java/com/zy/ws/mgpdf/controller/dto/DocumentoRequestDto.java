/*
* @(#)DocumentoRequestDto.java
*
* Copyright 2019 ZyTrust SA, Todos los derechos reservados.
* ZT PROPRIETARIO/CONFIDENTIALIDAD. Su uso está sujeto a los
* términos de la licencia adquirida a ZyTrust SA.
* No se permite modificar, copiar ni difundir sin autorización
* expresa de ZyTrust SA.
*/
package com.zy.ws.mgpdf.controller.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import com.zy.lib.common.type.TipoFormatoJasperType;

import lombok.Getter;
import lombok.Setter;

/**
 * Esta clase representa request de generacion de PDF
 *
 * @author Gustavo Jimenez
 * @version 1.0, 05/04/2021
 * @since 1.0
 */

@Setter
@Getter
public class DocumentoRequestDto {

	@NotBlank
	private TipoFormatoJasperType nombreFormato;
	
	@Valid
	private JasperFormato1Dto formatoDocumento1;
	
	@Valid
	private JasperFormato2Dto formatoDocumento2;
	
	@Valid
	private JasperImagenFirmaDto formatoImagenFirma1;
	
	@Valid
	private JasperFormato3Dto formatoDocumento3;
	
	@Valid
	private JasperFormato4Dto formatoDocumento4;
	
	@Valid
	private JasperFormato5Dto formatoDocumento5;

	@Valid
	private JasperFormato6Dto formatoDocumento6;

	@Valid
	private JasperFormato8Dto formatoDocumento8;
}
