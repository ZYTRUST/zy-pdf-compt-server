/*
* @(#)JasperParametrosDto.java
*
* Copyright 2019 ZyTrust SA, Todos los derechos reservados.
* ZT PROPRIETARIO/CONFIDENTIALIDAD. Su uso está sujeto a los
* términos de la licencia adquirida a ZyTrust SA.
* No se permite modificar, copiar ni difundir sin autorización
* expresa de ZyTrust SA.
*/
package com.zy.ws.mgpdf.controller.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

/**
* Esta clase representa 
*
* @author Gustavo Jimenez
* @version 1.0, 07/04/2021
* @since 1.0
*/

@Setter
@Getter
public class JasperParametrosDto {
	
	@NotBlank
	private boolean viewHuella;
	@NotBlank
	private boolean  viewBarcode;
	
	
	@Override
	public String toString() {
		return "JasperParametrosDto [viewHuella=" + viewHuella + ", viewBarcode=" + viewBarcode + "]";
	}
	
	
}

