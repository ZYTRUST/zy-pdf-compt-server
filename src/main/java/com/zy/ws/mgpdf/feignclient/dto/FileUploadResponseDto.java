package com.zy.ws.mgpdf.feignclient.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileUploadResponseDto implements Serializable {
    private String coError;
    private String deError;
    private String id;
    private String hash;
}
