/*
* @(#)JasperController.java
*
* Copyright 2019 ZyTrust SA, Todos los derechos reservados.
* ZT PROPRIETARIO/CONFIDENTIALIDAD. Su uso está sujeto a los
* términos de la licencia adquirida a ZyTrust SA.
* No se permite modificar, copiar ni difundir sin autorización
* expresa de ZyTrust SA.
*/
package com.zy.ws.mgpdf.controller;

import com.zy.cpts.cto.lib.zy.contrato.compts.dto.CreditoGrupalBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.RqContratoGrupalDto;
import com.zy.lib.common.sec.sb.logs.MDCCustom;
import com.zy.lib.dtos.dto.mgpdf.PdfManageDto;
import com.zy.lib.service.response.ZyResponse;
import com.zy.ws.mgpdf.conf.TrackExecutionTime;
import com.zy.ws.mgpdf.controller.dto.DocumentoResponseDto;
import com.zy.ws.mgpdf.controller.out.PdfResponse;
import com.zy.ws.mgpdf.resource.PlantillaResource;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping({"api/documento/"})
@Api(tags = "API de Compartamos para generacion de PDF")
public class PlantillaController {

	private final PlantillaResource plantillaResource;

    public PlantillaController(PlantillaResource plantillaResource) {
        this.plantillaResource = plantillaResource;
    }

    @TrackExecutionTime(operation = "GENERAR_PLANTILLA_CONFIGURABLE_COMPARTAMOS")
    @PostMapping(value = "v3/kit/pdf/generar", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiOperation(value = "Genear KIT Configurable", notes = "Genera PDFs segun la combinatoria solicitada")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "OK", response = PdfResponse.class),
            @ApiResponse(code = 404, message = "Not Found", response = String.class),
            @ApiResponse(code = 500, message = "Internal Server Error", response = String.class)
    })
    public ZyResponse<List<PdfManageDto>> generarKitPDFv3(@RequestBody RqContratoGrupalDto rqContratoGrupalDto,
                                                          BindingResult result,
                                                          Authentication auth){
        new MDCCustom(auth);
        return plantillaResource.generarKitPDFv3(rqContratoGrupalDto, result, auth);
    }

}
