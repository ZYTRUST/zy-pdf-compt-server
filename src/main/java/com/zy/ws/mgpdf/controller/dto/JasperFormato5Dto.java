package com.zy.ws.mgpdf.controller.dto;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotBlank;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sun.istack.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JasperFormato5Dto implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@NotBlank
	private String txnServ;
	@NotNull
	@Temporal(TemporalType.DATE)
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy", locale = "es_PE", timezone = "America/Lima")
	private Date feCrea;	
	@NotBlank
	private String nuDoc;
	@NotBlank
	private String nombres;
	@NotBlank
	private String apPaterno;
	@NotBlank
	private String apMaterno;
	@NotBlank
	private String desResVb;
	@NotBlank
	private String score;
	@NotBlank
	private byte[] imgBio;
	
	@Override
	public String toString() {
		return "JasperFormato5Dto [txnServ=" + txnServ + ", feCrea=" + feCrea + ", nuDoc=" + nuDoc + ", nombres="
				+ nombres + ", apPaterno=" + apPaterno + ", apMaterno=" + apMaterno + ", desResVb=" + desResVb
				+ ", score=" + score + ", imgBio=" + Arrays.toString(imgBio) + "]";
	}
}
