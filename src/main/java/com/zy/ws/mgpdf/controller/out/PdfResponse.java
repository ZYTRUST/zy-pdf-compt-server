package com.zy.ws.mgpdf.controller.out;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PdfResponse implements Serializable {

    private String id;
}
