package com.zy.ws.mgpdf.controller.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JasperFormato4Dto {

	@NotBlank
	private String tokenTransaccion;
	@NotBlank
	private String fecha;
	@NotBlank
	private String status;
	@NotBlank
	private String nombres;
	@NotBlank
	private String dni;
	@NotBlank
	private String correoOtp;
	@NotBlank
	private String ctrtoIntermediacion;
	@NotBlank
	private String perfilInversion;
	@NotBlank
	private String declaracionMercado;
	@NotBlank
	private String declaracionResidencia;
	
	@Override
	public String toString() {
		return "JasperFormato4Dto [tokenTransaccion=" + tokenTransaccion
				+ ", fecha=" + fecha + ", status=" + status + ", nombres=" + nombres + ", dni=" + dni + ", correoOtp="
				+ correoOtp + ", ctrtoIntermediacion=" + ctrtoIntermediacion + ", perfilInversion=" + perfilInversion
				+ ", declaracionMercado=" + declaracionMercado + ", declaracionResidencia=" + declaracionResidencia
				+ "]";
	}

	

}