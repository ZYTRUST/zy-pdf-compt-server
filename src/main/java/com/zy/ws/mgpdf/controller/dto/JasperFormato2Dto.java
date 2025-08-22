package com.zy.ws.mgpdf.controller.dto;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JasperFormato2Dto {

	@NotBlank
	private Date docuFeRegistro;
	@NotBlank
	private String docuNuCuenta;
	@NotBlank
	private String docuAgencia;
	@NotBlank
	private String docuTipoDocumento;
	@NotBlank
	private String docuSubTipoDocumento;
	@NotBlank
	private String docuMonto;
	@NotBlank
	private String docuTipoMoneda;
	@NotBlank
	private BigDecimal docuTasaVigente;
	@Valid
	@NotBlank
	private List<JasperFormato2DetalleDto> docuDetalle;
	
	@Valid
	@NotBlank
	private JasperParametrosDto parametros;
	
	
	@Override
	public String toString() {
		return "JasperFormato2Dto [docuFeCrea=" + docuFeRegistro + ", docuNuCuenta=" + docuNuCuenta + ", docuAgencia="
				+ docuAgencia + ", docuTipoDocumento=" + docuTipoDocumento + ", docuSubTipoDocumento="
				+ docuSubTipoDocumento + ", docuMonto=" + docuMonto + ", docuTipoMoneda=" + docuTipoMoneda
				+ ", docuTasaVigente=" + docuTasaVigente + ", docuDetalle=" + docuDetalle + "]";
	}

}