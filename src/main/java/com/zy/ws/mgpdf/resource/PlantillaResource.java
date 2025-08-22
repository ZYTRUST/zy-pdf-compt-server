/*
 * @(#)JasperResource.java
 *
 * Copyright 2019 ZyTrust SA, Todos los derechos reservados.
 * ZT PROPRIETARIO/CONFIDENTIALIDAD. Su uso está sujeto a los
 * términos de la licencia adquirida a ZyTrust SA.
 * No se permite modificar, copiar ni difundir sin autorización
 * expresa de ZyTrust SA.
 */
package com.zy.ws.mgpdf.resource;

import com.zy.cpts.cto.lib.zy.contrato.compts.dto.CreditoGrupalBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.RqContratoGrupalDto;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.AhorrosBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.CartillaAhorrosBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.ClienteAhorroBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.CreditoIndivGlobalBean;
import com.zy.lib.common.type.PdfPlantillaType;
import com.zy.lib.common.util.Tool;
import com.zy.lib.dtos.dto.formu.PlantillaDto;
import com.zy.lib.dtos.dto.mgpdf.PdfManageDto;
import com.zy.lib.dtos.dto.mgpdf.PdfMergeDto;
import com.zy.lib.dtos.dto.mgpdf.PdfMergeItemDto;
import com.zy.lib.dtos.response.formu.RsCombinatoriaPlantillaDto;
import com.zy.lib.message.resource.ErroresEnum;
import com.zy.lib.service.exception.ZyException;
import com.zy.lib.service.resource.TokenData;
import com.zy.lib.service.resource.ZyResource;
import com.zy.lib.service.response.ZyResponse;
import com.zy.ws.mgpdf.conf.ZyTException;
import com.zy.ws.mgpdf.controller.out.PdfResponse;
import com.zy.ws.mgpdf.service.PdfManageService;
import com.zy.ws.mgpdf.service.PlantillaService;
import com.zy.ws.mgpdf.service.ZyFormularioService;
import com.zy.ws.mgpdf.util.Utilitario;
import org.apache.commons.lang.SerializationUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
public class PlantillaResource extends ZyResource<PdfResponse> {
    private static final Logger log = LogManager.getLogger(PlantillaResource.class);

    @Autowired
    private PdfManageService pdfManageService;

    @Autowired
    private PlantillaService plantillaService;

    @Autowired
    private ZyFormularioService zyFormularioService;

    @Autowired
    private PlantillaCombinatoriaAsync plantillaCombinatoriaAsync;

    public ZyResponse<List<PdfManageDto>> generarKitPDFv3(@RequestBody RqContratoGrupalDto rqContratoGrupalDto,
                                                          BindingResult result,
                                                          Authentication auth) {
        TokenData tokenData;
        List<PdfManageDto> pdfManageDtoList = new ArrayList<>();
        List<RsCombinatoriaPlantillaDto> rsCombinatoriaPlantillaDtoList;
        String nombreGrupal;

        if (result.hasErrors()) {
            log.error("Datos invalidos:{}",Tool.errorList(result.getFieldErrors()));
            throw new ZyTException(ErroresEnum.DATA_INVALIDA);
        }

        if(auth == null) throw new ZyTException(ErroresEnum.GW_TOKEN_UNKNOW);
        tokenData = readToken(auth);

        try{
            log.info("Inicia obtención de combinatoria");
            rsCombinatoriaPlantillaDtoList = zyFormularioService.consultarCombinatoria(
                    rqContratoGrupalDto.getProcesoId().toString(), tokenData.getToken());
            log.info("Se obtuvo en total de grupos: {}",rsCombinatoriaPlantillaDtoList.size());
        } catch (ZyException e) {
            log.error("Error en consultarCombinatoria()");
            throw new ZyTException(e.getError());
        }

        nombreGrupal = obtenerNombreGrupo(rqContratoGrupalDto);
        if(Tool.isNullOrEmpty(nombreGrupal)) throw new ZyTException(ErroresEnum.DATA_INVALIDA);

        if(!Tool.isNullOrEmpty(rqContratoGrupalDto.getAhorrosBean()) && !rqContratoGrupalDto.getAhorrosBean().getListaCliente().isEmpty()){
            List<ClienteAhorroBean> listaClienteOrd = rqContratoGrupalDto.getAhorrosBean().getListaCliente().stream()
                    .sorted(Comparator.comparing(ClienteAhorroBean::getCliCargo).reversed())
                    .collect(Collectors.toList());
            rqContratoGrupalDto.getAhorrosBean().setListaCliente(listaClienteOrd);
        }

        //log.info("Inicia generación de pdfs");
        List<CompletableFuture<Void>> listResponsePlantilla= new ArrayList<>();
        for(RsCombinatoriaPlantillaDto rsCombinatoriaPlantillaDto: rsCombinatoriaPlantillaDtoList){
            listResponsePlantilla.add(plantillaCombinatoriaAsync.obtenerPlantilla(rsCombinatoriaPlantillaDto,
                   rqContratoGrupalDto, pdfManageDtoList, nombreGrupal));
        }

        listResponsePlantilla.stream().map(CompletableFuture::join).collect(Collectors.toList());

        log.info("Termina generación de pdfs - total: {}", pdfManageDtoList.size());
        return new ZyResponse<>(pdfManageDtoList);
    }

    private String obtenerNombreGrupo(RqContratoGrupalDto rqContratoGrupalDto){
        if(!Tool.isNullOrEmpty(rqContratoGrupalDto.getCreditoGrupalBean())){
            return rqContratoGrupalDto.getCreditoGrupalBean().getGrupoNombre();
        }

        if(!Tool.isNullOrEmpty(rqContratoGrupalDto.getCreditoIndivGlobalBean())){
            return rqContratoGrupalDto.getCreditoIndivGlobalBean().getGrupoNombre();
        }

        if(!Tool.isNullOrEmpty(rqContratoGrupalDto.getAhorrosBean())){
            return rqContratoGrupalDto.getAhorrosBean().getGrupoNombre();
        }
        return null;
    }

    public PdfManageDto obtenerPdfConsolidado(List<PdfMergeItemDto> lstPdfMerger){
        log.info("Inicio Obtener Pdf Consolidado");
        PdfMergeDto pdfMergeDto = new PdfMergeDto();
        pdfMergeDto.setPdfMergeItems(lstPdfMerger);
        pdfMergeDto.setFileName(Tool.dateFormat(new Date(), Utilitario.FORMAT_YYMMDDHHMMSSSSS)+".pdf");

        return pdfManageService.mergePdfV2(pdfMergeDto);
    }

    public PdfMergeItemDto generarPdfGeneralGrupal(PlantillaDto plantillaDto, CreditoGrupalBean creditoGrupalBean){

        PdfMergeItemDto pdfMergeItemDto = new PdfMergeItemDto();
        Optional<byte[]>optional = Optional.empty();

        log.info("Inicia generarPdfGeneral para la plantillaDto : {}", plantillaDto.getPlanTitulo());

        CreditoGrupalBean obj = (CreditoGrupalBean) SerializationUtils.clone(creditoGrupalBean);
        PdfPlantillaType pdfPlantillaType =  PdfPlantillaType.get(plantillaDto.getPlanTitulo());

        switch (pdfPlantillaType){
            case CONTRATO_PRESTAMO:
                optional = plantillaService.generarcontratoPrestamo(obj,plantillaDto);

                if (!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());

                break;
            case CARTILLA_IDENTIFICACION:
                optional = plantillaService.generarCartillaIdentificacion(obj,plantillaDto);

                if (!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());

                break;
            case HOJA_RESUMEN:
                optional = plantillaService.generarHojaResumen(obj,plantillaDto);

                if (!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());

                break;
            case REGLAS_DE_GRUPO:
                optional = plantillaService.generarReglasdeGrupo(obj,plantillaDto);

                if (!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());

                break;
            default:
                break;
        }

        log.info("Termina generarPdfGeneral para la plantillaDto : {}", plantillaDto.getPlanTitulo());
        return pdfMergeItemDto;
    }

    public PdfMergeItemDto generarPdfGeneralIndividual(PlantillaDto plantillaDto, CreditoIndivGlobalBean creditoIndivGlobalBean){

        PdfMergeItemDto pdfMergeItemDto = new PdfMergeItemDto();
        Optional<byte[]>optional = Optional.empty();

        log.info("Inicia generarPdfGeneral para la plantillaDto : {}", plantillaDto.getPlanTitulo());

        CreditoIndivGlobalBean obj = (CreditoIndivGlobalBean) SerializationUtils.clone(creditoIndivGlobalBean);
        PdfPlantillaType pdfPlantillaType =  PdfPlantillaType.get(plantillaDto.getPlanTitulo());

        switch (pdfPlantillaType){
            case CONTRATO_CREDITO_INDIVIDUAL:
                optional = plantillaService.generarContratoCreditoIndividual(plantillaDto);

                if (!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());

                break;
            case HOJA_RESUMEN_MAYOR_CUANTIA:
                optional = plantillaService.generarHojaResumenMayorCuantia(obj,plantillaDto);

                if (!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());

                break;
            case HOJA_RESUMEN_MENOR_CUANTIA:
                optional = plantillaService.generarHojaResumenMenorCuantia(obj,plantillaDto);

                if (!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());
                break;
            case CERTIFICADO_SEGURO_DESGRAVAMEN_V7:
                optional = plantillaService.generarPlantillaCertificadoSeguroDesgravamenv7(obj,plantillaDto);

                if (!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());

                break;
            case FICHA_DE_DATOS:
                optional = plantillaService.generarFichaDeDatos(obj,plantillaDto);

                if (!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());

                break;
            case AUTORIZACION_DATOS_PERSONALES:
                optional = plantillaService.generarAutorizacionDeDatosPersonales(obj,plantillaDto);

                if (!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());

                break;

            case CONTRATO_DEPOSITO_AHORROS:
                optional = plantillaService.generarContratoDepositoAhorros(plantillaDto);

                if (!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());

                break;
            case CARTILLA_INFORMACION_AHORROS:
                optional = plantillaService.generarCartillaInformacionAhorros(obj,plantillaDto);

                if (!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());

                break;
            case FATCAPNV2:
                optional = plantillaService.generarFatca(obj,plantillaDto);

                if (!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());

                break;

            case CERTIFICADO_SEGURO_PROTECCION_INDIVIDUAL_V4:
                optional = plantillaService.generarCertificadoSeguroProteccionIndividualV4(obj,plantillaDto);

                if (!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());

                break;
            case CERTIFICADO_SEGURO_PROTECCION_ECONOMICO_CI_V4:
                optional = plantillaService.generarCertificadoSeguroProteccionEconomicoCIV4(obj,plantillaDto);

                if (!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());

                break;
            case CRONOGRAMA_SEGURO_OPTATIVO:
                optional = plantillaService.generarCronogramaSeguroOptativo(obj, plantillaDto);

                if(!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());
                break;
            case SEGURO_DESGRAVAMEN_CON_DEVOLUCION_CI:
                optional = plantillaService.generarPlantillaCertificadoSeguroDesgravamenDevolucion(obj, plantillaDto);

                if(!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());
                break;

            case SEGURO_DESGRAVAMEN_MENSUAL_CI:
                optional = plantillaService.generarPlantillaCertificadoSeguroDesgravamenMensual(obj, plantillaDto);

                if(!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());
                break;

            case CONTRATO_DE_PRESTAMO:
                optional = plantillaService.generarPlantillaContraLineaCreditoPrestamo(obj, plantillaDto);

                if(!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());
                break;

            case HOJA_RESUMEN_LINEA_CREDITO:
                optional = plantillaService.generarPlantillaHojaResumenLineaCredito(obj, plantillaDto);

                if(!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());
                break;

            case CRONOGRAMA_PAGOS_LINEA_CREDITOS:
                optional = plantillaService.generarPlantillaCronogramaLineaCreditos(obj, plantillaDto);

                if(!optional.isPresent()) {
                    log.info("plantillaType: {}",plantillaDto.getPlanTitulo());
                    throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
                }

                pdfMergeItemDto.setFileBuffer(optional.get());
                pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());
                break;
            default:
                break;
        }

        log.info("Termina generarPdfGeneral para la plantillaDto : {}", plantillaDto.getPlanTitulo());
        return pdfMergeItemDto;
    }

    public PdfMergeItemDto generarPdfAhorros(PlantillaDto plantillaDto, AhorrosBean ahorrosBean){

        PdfMergeItemDto pdfMergeItemDto = new PdfMergeItemDto();
        Optional<byte[]>optional = Optional.empty();
        CartillaAhorrosBean cartillaAhorrosBean = null;

        log.info("Inicia generarPdfAhorros para la plantillaDto : {}", plantillaDto.getPlanTitulo());

        AhorrosBean obj = (AhorrosBean) SerializationUtils.clone(ahorrosBean);
        PdfPlantillaType pdfPlantillaType =  PdfPlantillaType.get(plantillaDto.getPlanTitulo());

        Optional<ClienteAhorroBean> clienteTitularRepOp = ahorrosBean.getListaCliente().stream().
                filter(cliente -> cliente.getCliCargo().intValueExact() == 5).findFirst();
        if(clienteTitularRepOp.isPresent()) cartillaAhorrosBean = clienteTitularRepOp.get().getCartillaAhorros();

        switch (pdfPlantillaType){
            case AHORROS_CARTILLA_DE_INFORMACION:
                optional = plantillaService.generarAhorrosCartillaInfo(obj, plantillaDto, cartillaAhorrosBean);
                break;
            case AHORROS_CONTRATO_APERTURA:
                optional = plantillaService.generarAhorrosContratoApertura(plantillaDto);
                break;
            default:
                break;
        }

        log.info("Respuesta de la plantilla : {}", plantillaDto.getPlanTitulo());
        if (!optional.isPresent()) {
            log.info("plantillaType nopresent: {}",plantillaDto.getPlanTitulo());
            throw new ZyTException(ErroresEnum.GW_TEMPLATE_IS_NULL);
        }

        pdfMergeItemDto.setFileBuffer(optional.get());
        pdfMergeItemDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());

        log.info("Termina generarPdfAhorros para la plantillaDto : {}", plantillaDto.getPlanTitulo());
        return pdfMergeItemDto;
    }


}
