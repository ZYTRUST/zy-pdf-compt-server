package com.zy.ws.mgpdf.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

@Getter
@Setter
@ToString
public class JasperFormato6Dto implements Serializable {

    @NotBlank
    private byte[] fotoValidacion;
    @NotBlank
    private String dni;
    @NotBlank
    private String apellidoMaterno;
    @NotBlank
    private String apellidoPaterno;
    @NotBlank
    private String nombres;
    @NotBlank
    private String resultadoVali;
    @NotBlank
    private String descripcionResult;
    @NotBlank
    private String nombreOpera;
    @NotBlank
    private String fechaTxn;
    @NotBlank
    private byte[] codigoQR;
    @NotBlank
    private String nombreEmpresa;
    @NotBlank
    private String dniOpera;
    @NotBlank
    private String ruc;
    @NotBlank
    private String nuConsulta;
    @NotBlank
    private String fechaPie;
    @NotBlank
    private byte[] codigoBarra;

}
