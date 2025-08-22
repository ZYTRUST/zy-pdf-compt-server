package com.zy.ws.mgpdf.controller.dto;

public class ContratoRequestDto {
    private JasperFormato2Dto jasperFormato2Dto;
    private String rutaContrato;
    private boolean viewHuella;
    private boolean viewBarcode;

    public ContratoRequestDto() {
    }

    public ContratoRequestDto(JasperFormato2Dto jasperFormato2Dto, String rutaContrato, boolean viewHuella, boolean viewBarcode) {
        this.jasperFormato2Dto = jasperFormato2Dto;
        this.rutaContrato = rutaContrato;
        this.viewHuella = viewHuella;
        this.viewBarcode = viewBarcode;
    }

    public JasperFormato2Dto getContratoDto() {
        return jasperFormato2Dto;
    }

    public void setContratoDto(JasperFormato2Dto jasperFormato2Dto) {
        this.jasperFormato2Dto = jasperFormato2Dto;
    }

    public String getRutaContrato() {
        return rutaContrato;
    }

    public void setRutaContrato(String rutaContrato) {
        this.rutaContrato = rutaContrato;
    }

    public boolean isViewHuella() {
        return viewHuella;
    }

    public void setViewHuella(boolean viewHuella) {
        this.viewHuella = viewHuella;
    }

    public boolean isViewBarcode() {
        return viewBarcode;
    }

    public void setViewBarcode(boolean viewBarcode) {
        this.viewBarcode = viewBarcode;
    }
}
