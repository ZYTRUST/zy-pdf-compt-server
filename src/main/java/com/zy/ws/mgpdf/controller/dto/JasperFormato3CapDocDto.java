package com.zy.ws.mgpdf.controller.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JasperFormato3CapDocDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank
	private String tokenTransaccion;
	@NotBlank
	private String resultadoBio;
	@NotBlank
	private String scoreBio;
	@NotBlank
	private String fecha;
	@NotBlank
	private String nuDocumento;
	@NotBlank
	private String apPaterno;
	@NotBlank
	private String apMaterno;
	@NotBlank
	private String nombres;
	@NotBlank
	private String nacionalidad;
	@NotBlank
	private String feNac;
	@NotBlank
	private String status;
	@NotBlank
	private byte[] anverso;
	@NotBlank
	private byte[] reverso;

	@Override
	public String toString() {
		return "CapturaDocumentoDto [tokenTransaccion=" + tokenTransaccion + ", resultadoBio=" + resultadoBio
				+ ", scoreBio=" + scoreBio + ", fecha=" + fecha + ", nuDocumento=" + nuDocumento + ", apPaterno="
				+ apPaterno + ", apMaterno=" + apMaterno + ", nombres=" + nombres + ", nacionalidad=" + nacionalidad
				+ ", feNac=" + feNac + ", status=" + status + "]";
	}

}
