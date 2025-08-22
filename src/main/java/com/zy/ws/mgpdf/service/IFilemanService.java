package com.zy.ws.mgpdf.service;

import com.zy.ws.mgpdf.feignclient.dto.FileUploadRequestDto;
import com.zy.ws.mgpdf.feignclient.dto.FileUploadResponseDto;

public interface IFilemanService {
    String downloadFile(String filenameId, String bearerToken);
    FileUploadResponseDto uploadFile(FileUploadRequestDto fileUploadRequestDto, String bearerToken);
}
