package com.zy.ws.mgpdf.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RqDecryptDto {
    private String  namePdf;
    private String  passwordPdf;
    @ToString.Exclude
    private byte[] bufferPdf;

}
