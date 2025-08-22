package com.zy.ws.mgpdf.controller.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JasperFormato3RNDto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank
	private String tokenTransaccion;
	@NotBlank
	private String resultado;
	@NotBlank
	private String score;
	@NotBlank
	private String fecha;
	@NotBlank
	private String nombres;
	@NotBlank
	private String apPaterno;
	@NotBlank
	private String apMaterno;
	@NotBlank
	private String dni;
	@NotBlank
	private String feNac;
	@NotBlank
	private String status;
	@NotBlank
	private byte[] fotoReniec;
	@NotBlank
	private byte[] fotoSelfie;
	@NotBlank
	private String hashDocumento;
	@NotBlank
	private String hashRegistroAnterior;

	@Override
	public String toString() {
		return "ConsultaFaceReniecDto [tokenTransaccion=" + tokenTransaccion + ", resultado=" + resultado + ", score="
				+ score + ", fecha=" + fecha + ", nombres=" + nombres + ", apPaterno=" + apPaterno + ", apMaterno="
				+ apMaterno + ", dni=" + dni + ", feNac=" + feNac + ", status=" + status + ", hashDocumento="
				+ hashDocumento + ", hashRegistroAnterior=" + hashRegistroAnterior + "]";
	}

}
