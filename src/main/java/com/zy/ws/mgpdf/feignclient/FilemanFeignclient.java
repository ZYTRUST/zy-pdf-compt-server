package com.zy.ws.mgpdf.feignclient;

import com.zy.lib.common.util.Constante;
import com.zy.ws.mgpdf.feignclient.dto.FileUploadResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@FeignClient(name = "zyfileman-server", url = "${zyfileman.service.url}")
public interface FilemanFeignclient {

    @PostMapping(value = "/zyfileman/v1.0/fileupload", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    ResponseEntity<FileUploadResponseDto> fileupload(@RequestHeader(Constante.AUTHORIZATION) String bearerToken,
                                                     @RequestPart("file") MultipartFile file,
                                                     @RequestPart("size") String size,
                                                     @RequestPart("hash") String hash);

    @GetMapping({ "/zyfileman/v1.0/filedownload/{filename}" })
    ResponseEntity<byte[]> filedownload(@RequestHeader(Constante.AUTHORIZATION) String bearerToken,
                                        @PathVariable String filename);

    /* Descarga Documento NO firmado */
    @GetMapping({ "/zyfileman/v1.0/filedownload/in/{filename}" })
    ResponseEntity<byte[]> filedownloadin(@RequestHeader(Constante.AUTHORIZATION) String bearerToken,
                                            @PathVariable String filename);
}
