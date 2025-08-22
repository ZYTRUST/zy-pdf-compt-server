package com.zy.ws.mgpdf.service;

import com.zy.lib.message.resource.ErroresEnum;
import com.zy.ws.mgpdf.conf.TrackExecutionTime;
import com.zy.ws.mgpdf.conf.ZyTException;
import com.zy.ws.mgpdf.feignclient.FilemanFeignclient;
import com.zy.ws.mgpdf.feignclient.dto.FileUploadRequestDto;
import com.zy.ws.mgpdf.feignclient.dto.FileUploadResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class FilemanService implements IFilemanService {

    private final FilemanFeignclient filemanFeignclient;

    public FilemanService(FilemanFeignclient filemanFeignclient) {
        this.filemanFeignclient = filemanFeignclient;
    }

    public String downloadFile(String filenameId, String bearerToken)  {

        ResponseEntity<byte[]> responseEntity;
        byte[] response;

        try{
            responseEntity = filemanFeignclient.filedownloadin(bearerToken, filenameId);
        }
        catch (Exception e){
            throw new ZyTException(e,ErroresEnum.GLOBAL_FEIGNCLIENT_EXCEPTION);
        }

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new ZyTException(ErroresEnum.GLOBAL_FEIGNCLIENT_ERROR);
        }

        response = responseEntity.getBody();
        if(response == null){
            throw new ZyTException(ErroresEnum.FEIGNCLIENT_BODY_NULL);
        }
        return Base64.encodeBase64String(response);
    }

    @TrackExecutionTime(operation = "SUBIR_PDF_FILEMAN")
    public FileUploadResponseDto uploadFile(FileUploadRequestDto fileUploadRequestDto,
                                            String bearerToken) {

        ResponseEntity<FileUploadResponseDto> responseEntity;
        FileUploadResponseDto response;

        try{
            responseEntity = filemanFeignclient.fileupload(bearerToken,
                                                           fileUploadRequestDto.getFile(),
                                                           fileUploadRequestDto.getSize(),
                                                           fileUploadRequestDto.getHash());
        }
        catch (Exception e){
            throw new ZyTException(e,ErroresEnum.GLOBAL_FEIGNCLIENT_EXCEPTION);
        }

        if (responseEntity.getStatusCode() != HttpStatus.OK) {
            throw new ZyTException(ErroresEnum.GLOBAL_FEIGNCLIENT_ERROR);
        }

        response = responseEntity.getBody();
        if(response == null){
            throw new ZyTException(ErroresEnum.FEIGNCLIENT_BODY_NULL);
        }
        return response;
    }
}
