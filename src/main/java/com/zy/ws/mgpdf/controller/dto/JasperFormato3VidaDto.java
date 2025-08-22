package com.zy.ws.mgpdf.controller.dto;

import java.io.Serializable;

import javax.validation.constraints.NotBlank;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JasperFormato3VidaDto implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank
	private String tokenTransaccion;
	@NotBlank
	private String sessionId;
	@NotBlank
	private String fecha;
	@NotBlank
	private String resultado;
	@NotBlank
	private String modeloDispositivo;
	@NotBlank
	private String versionOS;

	@Override
	public String toString() {
		return "DeteccionVidaDto [tokenTransaccion=" + tokenTransaccion + ", sessionId=" + sessionId + ", fecha="
				+ fecha + ", resultado=" + resultado + ", modeloDispositivo=" + modeloDispositivo + ", versionOS="
				+ versionOS + "]";
	}

}
