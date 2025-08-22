package com.zy.ws.mgpdf.controller.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class RsDecryptDto {
    private int numberOfPages;
    @ToString.Exclude
    private byte[] bufferPdf;
    private int sizePdf;
    private String namePdf;
}
