package com.zy.ws.mgpdf.controller.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReporteBiometricoDto {
private int numPagesPdf;
private byte[] bufferPdf;
private byte[] bufferImageSign;
private byte[] bufferOcr;
}
