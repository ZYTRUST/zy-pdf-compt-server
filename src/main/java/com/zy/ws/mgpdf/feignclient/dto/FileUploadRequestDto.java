package com.zy.ws.mgpdf.feignclient.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FileUploadRequestDto implements Serializable {
    private MultipartFile file;
    private String size;
    private String hash;
    private String id;
}
