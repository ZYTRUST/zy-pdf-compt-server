/*
* @(#)DocumentoResponseDto.java
*
* Copyright 2019 ZyTrust SA, Todos los derechos reservados.
* ZT PROPRIETARIO/CONFIDENTIALIDAD. Su uso está sujeto a los
* términos de la licencia adquirida a ZyTrust SA.
* No se permite modificar, copiar ni difundir sin autorización
* expresa de ZyTrust SA.
*/
package com.zy.ws.mgpdf.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class DocumentoDto {
	private String docuNombre;
	private String docuCodigo;
}
