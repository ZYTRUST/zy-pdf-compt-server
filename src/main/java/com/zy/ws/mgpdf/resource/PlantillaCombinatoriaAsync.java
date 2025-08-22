package com.zy.ws.mgpdf.resource;

import com.zy.cpts.cto.lib.zy.contrato.compts.dto.RqContratoGrupalDto;
import com.zy.lib.common.util.Constante;
import com.zy.lib.common.util.Tool;
import com.zy.lib.dtos.dto.formu.PlantillaDto;
import com.zy.lib.dtos.dto.mgpdf.PdfManageDto;
import com.zy.lib.dtos.dto.mgpdf.PdfMergeDto;
import com.zy.lib.dtos.dto.mgpdf.PdfMergeItemDto;
import com.zy.lib.dtos.response.formu.RsCombinatoriaPlantillaDto;
import com.zy.ws.mgpdf.conf.TrackExecutionTime;
import com.zy.ws.mgpdf.service.PdfManageService;
import com.zy.ws.mgpdf.util.Utilitario;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PlantillaCombinatoriaAsync {

    @Autowired
    private PlantillaClienteGrupoAsync plantillaClienteGrupoAsync;

    @Autowired
    private PdfManageService pdfManageService;

    @Async
    @TrackExecutionTime(operation = "PLANTILLA_COMBINATORIA_ASYNC")
    public CompletableFuture<Void> obtenerPlantilla(RsCombinatoriaPlantillaDto rsCombinatoriaPlantillaDto,
                                                    RqContratoGrupalDto rqContratoGrupalDto,
                                                    List<PdfManageDto> pdfManageDtoList,
                                                    String nombreGrupal){

        PdfManageDto pdfManageDto;
        String nombreGrupo = rsCombinatoriaPlantillaDto.getGrupoNombre();
        if(!Tool.isNullOrEmpty(nombreGrupo) && nombreGrupo.contains(Constante.NOMBRE_GRUPO)){
            nombreGrupo = nombreGrupo.replace(Constante.NOMBRE_GRUPO, nombreGrupal);
            rsCombinatoriaPlantillaDto.setGrupoNombre(nombreGrupo);
            log.info("ENTRE A CAMBIAR EL NOMBRE_GRUPO");
        }
        
        List<PdfMergeItemDto> listPdfMerger = new ArrayList<>();
        List<CompletableFuture<Void>> listResponse= new ArrayList<>();
        for(PlantillaDto plantillaDto: rsCombinatoriaPlantillaDto.getPlantillaDtoList()){
            log.info("grupo de plantilla {} titulo {}",rsCombinatoriaPlantillaDto.getGrupoNombre(),plantillaDto.getPlanTitulo());
            listResponse.add(plantillaClienteGrupoAsync.obtenerPlantilla(plantillaDto, 
                                                                        rsCombinatoriaPlantillaDto, 
                                                                        rqContratoGrupalDto, 
                                                                        pdfManageDtoList,
                                                                        listPdfMerger));
        }
        listResponse.stream().map(CompletableFuture::join).collect(Collectors.toList());

        if(!Tool.isNullOrEmpty(rsCombinatoriaPlantillaDto.getGrupoNombre())){
            listPdfMerger = listPdfMerger.stream()
                    .sorted(Comparator.comparing(PdfMergeItemDto::getFileOrder)).collect(Collectors.toList());
            pdfManageDto = obtenerPdfConsolidado(listPdfMerger);
            pdfManageDto.setFileName(rsCombinatoriaPlantillaDto.getGrupoNombre());
            pdfManageDtoList.add(pdfManageDto);
        }
        return CompletableFuture.completedFuture(null);
    }


    public PdfManageDto obtenerPdfConsolidado(List<PdfMergeItemDto> lstPdfMerger){
        //log.info("Inicio Obtener Pdf Consolidado");
        PdfMergeDto pdfMergeDto = new PdfMergeDto();
        pdfMergeDto.setPdfMergeItems(lstPdfMerger);
        pdfMergeDto.setFileName(Tool.dateFormat(new Date(), Utilitario.FORMAT_YYMMDDHHMMSSSSS)+".pdf");

        return pdfManageService.mergePdfV2(pdfMergeDto);
    }

}
