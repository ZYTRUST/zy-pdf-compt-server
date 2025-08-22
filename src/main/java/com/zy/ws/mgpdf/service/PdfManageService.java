/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zy.ws.mgpdf.service;

import com.zy.lib.common.util.Tool;
import com.zy.lib.dtos.dto.mgpdf.PdfManageDto;
import com.zy.lib.dtos.dto.mgpdf.PdfMergeDto;
import com.zy.lib.dtos.dto.mgpdf.PdfMergeItemDto;
import com.zy.lib.message.resource.ErroresEnum;
import com.zy.ws.mgpdf.conf.TrackExecutionTime;
import com.zy.ws.mgpdf.conf.ZyTException;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author jmiraval
 */
@Slf4j
@Service
public class PdfManageService {

    private final static String CONTENT_TYPE = "application/pdf";

    @TrackExecutionTime(operation = "MERGE_PDF_V2")
    public PdfManageDto mergePdfV2(PdfMergeDto pdfMergeDto) {

        PdfManageDto pdfManageDto;
        ByteArrayOutputStream mergedPDFOutputStream;
        List<InputStream> sources = new ArrayList<>();
        for (PdfMergeItemDto pdfMergeItemDto : pdfMergeDto.getPdfMergeItems()) {
            if(Tool.isNullOrEmpty(pdfMergeItemDto.getFileBuffer())){
                continue;
            }
            sources.add(new ByteArrayInputStream(pdfMergeItemDto.getFileBuffer()));
        }

        mergedPDFOutputStream = new ByteArrayOutputStream();
        try{
            PDFMergerUtility pdfMerger = new PDFMergerUtility();
            pdfMerger.addSources(sources);
            pdfMerger.setDestinationStream(mergedPDFOutputStream);

            pdfMerger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());

            /*try (PDDocument outDocument = new PDDocument()) {
                log.info("size {}",pdfMergeDto.getPdfMergeItems().size());
                for (PdfMergeItemDto pdfMergeItemDto : pdfMergeDto.getPdfMergeItems()) {
                    log.info("pdf order {}",pdfMergeItemDto.getFileOrder());
                    log.info("pdf buffer null {}", pdfMergeItemDto.getFileBuffer() == null);
                    try (PDDocument inDocument = PDDocument.load(pdfMergeItemDto.getFileBuffer())){
                        for (int i = 0; i < inDocument.getNumberOfPages(); i++) {
                            log.info("page {} de {}",i,inDocument.getNumberOfPages());
                            outDocument.addPage(inDocument.getPage(i));
                        }
                    }
                }
                outDocument.close();
                PDDocumentInformation documentInformation = new PDDocumentInformation();
                documentInformation.setTitle("");
                documentInformation.setCreator("ZyTrust S.A.");
                documentInformation.setSubject("");

                //document.setDocumentInformation(documentInformation);
                String uuid = Tool.getUuid();
                String prefix = uuid.concat(Constante.GUION_BAJO);
                Path tmpDir = Files.createTempDirectory(prefix);
                Path pahFile = tmpDir.resolve(pdfMergeDto.getFileName()).normalize();
                log.info("pathFile {}",pahFile);
                outDocument.save(pahFile.toFile());
            }*/
        }
        catch(Exception e){
            log.error("***************************",e);
            throw new ZyTException(e, ErroresEnum.FILE_EXCEPTION);
        }

        pdfManageDto = new PdfManageDto();
        pdfManageDto.setFileName(pdfMergeDto.getFileName());
        pdfManageDto.setBuffer(mergedPDFOutputStream.toByteArray());
        pdfManageDto.setSize(pdfManageDto.getBuffer().length);
        pdfManageDto.setFileType(CONTENT_TYPE);

        return pdfManageDto;
    }
    /*@TrackExecutionTime(operation = "MERGE_PDF_V2")
    public PdfManageDto mergePdfV2(PdfMergeDto pdfMergeDto) {

        log.info("<====init mergePdf v2====> ");

        PdfManageDto pdfManageDto = null;
        OutputStream out = null;
        //PdfDocument pdfDoc;
        //PdfDocument mergeItem;
        InputStream targetStream;
        //PdfMerger pdfMerger;
        Path pahFile;

        String uuid = Tool.getUuid();
        String prefix = uuid.concat(Constante.GUION_BAJO);
        Path tmpDir;
        try {
            //log.info("prefix {}",prefix);
            tmpDir = Files.createTempDirectory(prefix);
            //log.info("tmpdir {}",tmpDir);
            pahFile = tmpDir.resolve(pdfMergeDto.getFileName()).normalize();
            //log.info("pahFile {}",pahFile);
            out = Files.newOutputStream(pahFile.toFile().toPath());
            //log.info("pahFile.toFile().toPath() {}",pahFile.toFile().toPath());
            //pdfDoc = new PdfDocument(new PdfWriter(out));
            //pdfMerger = new PdfMerger(pdfDoc);
            PDFMergerUtility merger = new PDFMergerUtility();
            merger.setDestinationStream(out);

            for (PdfMergeItemDto pdfMergeItemDto : pdfMergeDto.getPdfMergeItems()) {
                if(Tool.isNullOrEmpty(pdfMergeItemDto.getFileBuffer())){
                   continue;
                }
                targetStream = new ByteArrayInputStream(pdfMergeItemDto.getFileBuffer());
                merger.addSource(targetStream);
                //mergeItem = new PdfDocument(new PdfReader(targetStream));
                //pdfMerger.merge(mergeItem, 1, mergeItem.getNumberOfPages());
                //mergeItem.close();
                targetStream.close();
            }
            merger.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
            //pdfMerger.close();
            //pdfDoc.close();
            //out.flush();
            //out.close();

            byte[] fileContent = Files.readAllBytes(pahFile);

            pdfManageDto = new PdfManageDto();
            pdfManageDto.setFileName(pdfMergeDto.getFileName());
            pdfManageDto.setBuffer(fileContent);
            pdfManageDto.setSize(fileContent.length);
            pdfManageDto.setFileType(CONTENT_TYPE);

            clearTempFiles(tmpDir);


        } catch (IOException e) {
            //LOG.error("No se puede leer  archivo {}", e);
            throw new ZyTException(e, ErroresEnum.FILE_EXCEPTION);
        } finally {
            if(out!=null){
                try{
                    out.flush();
                    out.close();
                }
                catch (Exception e){
                    log.error(e.getMessage(),e);
                }
            }
        }



        return pdfManageDto;
    }*/

}
