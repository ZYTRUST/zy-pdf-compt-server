package com.zy.ws.mgpdf.resource;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.AhorrosBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.CartillaAhorrosBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.ClienteAhorroBean;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.zy.cpts.cto.lib.zy.contrato.compts.dto.ClienteBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.CreditoGrupalBean;
import com.zy.lib.common.type.PdfPlantillaType;
import com.zy.lib.dtos.dto.formu.PlantillaDto;
import com.zy.lib.dtos.dto.mgpdf.PdfMergeItemDto;
import com.zy.ws.mgpdf.conf.TrackExecutionTime;
import com.zy.ws.mgpdf.service.PlantillaService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PlantillaIndividualAsync {
    
    @Autowired
    private PlantillaService plantillaService;

    @Async
    @TrackExecutionTime(operation = "PLANTILLA_INDIVIDUALES_ASYNC")
    public CompletableFuture<Void> obtenerPlantilla(ClienteBean clienteBean,
                                                    CreditoGrupalBean creditoGrupalBean,
                                                    PlantillaDto plantillaDto,
                                                    List<PdfMergeItemDto> pdfMergeItemDtoList, int orden){

        List<ClienteBean> clienteBeanList = new ArrayList<>();
        clienteBeanList.add(clienteBean);

        CreditoGrupalBean obj = (CreditoGrupalBean) SerializationUtils.clone(creditoGrupalBean);
        obj.setListaCliente(clienteBeanList);

        Optional<byte[]> optional = Optional.empty();
        PdfPlantillaType pdfPlantillaType = PdfPlantillaType.get(plantillaDto.getPlanTitulo());

        //log.info("Inicio individual plantilla {}", pdfPlantillaType.name());

        switch (pdfPlantillaType) {
            case CONDICIONES_ESPECIFICAS:
                optional = plantillaService.generarCondicionesEspecificas(obj, plantillaDto);
                break;
            case POLIZA_GRUPO:
                if (clienteBean.getCliEmiteSegDesgravamen().toUpperCase().compareTo("S") == 0) {
                    optional = plantillaService.generarPolizaGrupo(obj, plantillaDto);
                }
                break;
            case ESTIMADO_CLIENTE:
                if (clienteBean.getCliEmiteCartaSegDesgravamen().toUpperCase().compareTo("S") == 0) {
                    optional = plantillaService.generarEstimadoCliente(obj, plantillaDto);
                }
                break;
            case SEGURO_OPTATIVO:
                if (clienteBean.getCliEmiteSegOptativo().toUpperCase().compareTo("S") == 0) {
                    optional = plantillaService.generarSeguroOptativo(obj, plantillaDto);
                }
                break;
            case CRONOGRAMA_PAGOS:
                if (clienteBean.getCliEmiteSegOptativo().toUpperCase().compareTo("S") == 0) {
                    optional = plantillaService.generarCronogramaPagos(obj, plantillaDto);
                }
                break;
            case TRATAMIENTO_DATOS:
                optional = plantillaService.generarTratamientoDatos(obj, plantillaDto);
                break;
            case DECLARACION_JURADA:
                if (clienteBean.getCliEmiteDJ().toUpperCase().compareTo("S") == 0) {
                    optional = plantillaService.generaDeclaracionJurada(obj, plantillaDto);
                }
                break;
            default:
                break;
        }

        if (optional.isPresent()) {
            //log.info("Ingreso cuando no es vacio : {}", plantillaDto.getPlanTitulo());
            PdfMergeItemDto pdfMergeItemDto = new PdfMergeItemDto();
            pdfMergeItemDto.setFileBuffer(optional.get());
            pdfMergeItemDto.setFileOrder((short) orden);
            //log.info("Fin individual plantilla {}", pdfPlantillaType.name());
            pdfMergeItemDtoList.add(pdfMergeItemDto);
        }
        return CompletableFuture.completedFuture(null);
    }

    @Async
    @TrackExecutionTime(operation = "PLANTILLA_INDIVIDUALES_ASYNC")
    public CompletableFuture<Void> obtenerPlantillaIndAhorros(ClienteAhorroBean clienteBean,
                                                              AhorrosBean ahorrosBean,
                                                              PlantillaDto plantillaDto,
                                                              List<PdfMergeItemDto> pdfMergeItemDtoList, int ordenAhorros){

        Optional<byte[]> optional = Optional.empty();
        PdfPlantillaType pdfPlantillaType = PdfPlantillaType.get(plantillaDto.getPlanTitulo());
        CartillaAhorrosBean cartillaAhorrosBean = null;

        Optional<ClienteAhorroBean> clienteTitularRepOp = ahorrosBean.getListaCliente().stream().
                filter(cliente -> cliente.getCliCargo().intValueExact() == 5).findFirst();
        if(clienteTitularRepOp.isPresent()) cartillaAhorrosBean = clienteTitularRepOp.get().getCartillaAhorros();

        switch (pdfPlantillaType) {
            case AHORROS_FICHA_DATOS:
                optional = plantillaService.generarAhorroFichaDatos(ahorrosBean, plantillaDto, clienteBean, cartillaAhorrosBean);
                break;
            case AHORROS_CONSENTIMIENTO_DATOS_PERSONALES:
                optional = plantillaService.generarAhorroConsentDatosPersonales(plantillaDto, clienteBean, cartillaAhorrosBean);
                break;
            default:
                break;
        }

        if (optional.isPresent()) {
            PdfMergeItemDto pdfMergeItemDto = new PdfMergeItemDto();
            pdfMergeItemDto.setFileBuffer(optional.get());
            pdfMergeItemDto.setFileOrder((short) ordenAhorros);
            pdfMergeItemDtoList.add(pdfMergeItemDto);
        }
        return CompletableFuture.completedFuture(null);
    }
}
