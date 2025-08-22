/*
* @(#)JasperFormato1.java
*
* Copyright 2019 ZyTrust SA, Todos los derechos reservados.
* ZT PROPRIETARIO/CONFIDENTIALIDAD. Su uso está sujeto a los
* términos de la licencia adquirida a ZyTrust SA.
* No se permite modificar, copiar ni difundir sin autorización
* expresa de ZyTrust SA.
*/
package com.zy.ws.mgpdf.controller.dto;

import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;
import com.zy.lib.common.type.GeneroType;

import lombok.Getter;
import lombok.Setter;

/**
* Esta clase representa 
*
* @author Gustavo Jimenez
* @version 1.0, 05/04/2021
* @since 1.0
*/

@Getter
@Setter
public class JasperFormato1Dto {

	@NotNull
	private String cliNuDoc;
	@NotNull
	@NotBlank
	private String cliNomCompleto;
	@NotNull
	private GeneroType cliGenero;
	@NotNull
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_PE", timezone = "America/Lima")
	private Date cliFeNacimiento;
	@NotNull
	@NotBlank
	private String cliDireccion;
	@NotNull
	@NotBlank
	private String cliCelular;
	@NotNull
	@NotBlank
	private String cliCorreo;
	@NotNull
	@NotBlank
	private String cliFoto;

	@Override
	public String toString() {
		return "ClienteDto [cliNuDoc=" + cliNuDoc + ", cliNomCompleto=" + cliNomCompleto + ", cliGenero=" + cliGenero
				+ ", cliFeNacimiento=" + cliFeNacimiento + ", cliDireccion=" + cliDireccion + ", cliCelular="
				+ cliCelular + ", cliCorreo=" + cliCorreo + "]";
	}
}
