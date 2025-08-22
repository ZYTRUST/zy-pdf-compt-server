package com.zy.ws.mgpdf.controller.dto;

import java.math.BigDecimal;
import java.util.Date;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JasperFormato2DetalleDto {

	private Date ddetFeFirma;
	@NotBlank
	private BigDecimal ddetNuFirmante;
	@NotBlank
	private BigDecimal ddetInFirma;
	@NotBlank
	private JasperFormato2ClienteDto cliente;

	private byte[] huella;

	private String pkHex;

}
