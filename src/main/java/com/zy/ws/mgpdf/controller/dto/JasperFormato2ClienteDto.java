package com.zy.ws.mgpdf.controller.dto;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JasperFormato2ClienteDto {
	@NotBlank
    private String cliNuDoc;
	@NotBlank
	private String cliNombres;
	@NotBlank
    private String cliApPaterno;
	@NotBlank
    private String cliApMaterno;
	@NotBlank
    private String cliDireccion;
	@NotBlank
    private String cliNombreCompleto;
	@NotBlank
    private String cliTiDoc;
}
