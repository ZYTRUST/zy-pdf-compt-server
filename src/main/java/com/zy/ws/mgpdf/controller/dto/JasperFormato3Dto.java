package com.zy.ws.mgpdf.controller.dto;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class JasperFormato3Dto {

	@Valid
	@NotBlank
	private JasperFormato3CapDocDto capDocDto;
	@Valid
	@NotBlank
	private JasperFormato3RNDto rNDto;
	@Valid
	@NotBlank
	private JasperFormato3VidaDto vidaDto;

	@Override
	public String toString() {
		return "JasperFormato3Dto [capDocDto=" + capDocDto + ", rNDto=" + rNDto + ", vidaDto=" + vidaDto + "]";
	}

}