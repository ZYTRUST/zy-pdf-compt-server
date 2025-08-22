package com.zy.ws.mgpdf.resource;

import com.zy.cpts.cto.lib.zy.contrato.compts.dto.ClienteBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.RqContratoGrupalDto;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.ClienteAhorroBean;
import com.zy.lib.common.util.DecimalID;
import com.zy.lib.common.util.Tool;
import com.zy.lib.dtos.dto.formu.PlantillaDto;
import com.zy.lib.dtos.dto.mgpdf.PdfManageDto;
import com.zy.lib.dtos.dto.mgpdf.PdfMergeItemDto;
import com.zy.lib.dtos.response.formu.RsCombinatoriaPlantillaDto;
import com.zy.ws.mgpdf.conf.TrackExecutionTime;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Component
@Slf4j
public class PlantillaClienteGrupoAsync {
    
    @Autowired
    private PlantillaResource plantillaResource;

    @Autowired
    private PlantillaIndividualAsync plantillaIndividualAsync;

    @Async
    @TrackExecutionTime(operation = "PLANTILLLA_CLIENTE_GRUPO_ASYNC")
    public CompletableFuture<Void> obtenerPlantilla(PlantillaDto plantillaDto,
                                                    RsCombinatoriaPlantillaDto rsCombinatoriaPlantillaDto,
                                                    RqContratoGrupalDto rqContratoGrupalDto,
                                                    List<PdfManageDto> pdfManageDtoList,
                                                    List<PdfMergeItemDto> lstPdfMerger){

        List<PdfMergeItemDto> pdfMergeItemDtoListClientes;
        PdfManageDto pdfManageClientesDto;
        PdfManageDto pdfManageDto;
        PdfMergeItemDto pdfItemGrupoDto;
        log.info("titulo {} es por cliente? {}", plantillaDto.getPlanTitulo(), plantillaDto.getPlanPorCliente());

        if(plantillaDto.getPlanPorCliente()) {
            pdfMergeItemDtoListClientes = generarPdfsIndividuales(plantillaDto, rqContratoGrupalDto);
            if (!pdfMergeItemDtoListClientes.isEmpty()) {
                pdfManageClientesDto = plantillaResource.obtenerPdfConsolidado(pdfMergeItemDtoListClientes);

                if (Tool.isNullOrEmpty(rsCombinatoriaPlantillaDto.getGrupoNombre())) {
                    pdfManageDto = new PdfManageDto();
                    pdfManageDto.setBuffer(pdfManageClientesDto.getBuffer());
                    pdfManageDto.setFileName(plantillaDto.getPlanTitulo() + ".pdf");

                    pdfManageDtoList.add(pdfManageDto);
                } else {
                    pdfItemGrupoDto = new PdfMergeItemDto();
                    pdfItemGrupoDto.setFileBuffer(pdfManageClientesDto.getBuffer());
                    pdfItemGrupoDto.setFileOrder(plantillaDto.getPlanOrden().shortValueExact());

                    lstPdfMerger.add(pdfItemGrupoDto);
                }
            }
        }else{
            pdfItemGrupoDto = null;

            if(!Tool.isNullOrEmpty(rqContratoGrupalDto.getCreditoIndivGlobalBean())){
                pdfItemGrupoDto = plantillaResource.generarPdfGeneralIndividual(plantillaDto, rqContratoGrupalDto.getCreditoIndivGlobalBean());
            }

            if(!Tool.isNullOrEmpty(rqContratoGrupalDto.getCreditoGrupalBean())){
                pdfItemGrupoDto = plantillaResource.generarPdfGeneralGrupal(plantillaDto, rqContratoGrupalDto.getCreditoGrupalBean());
            }

            if(!Tool.isNullOrEmpty(rqContratoGrupalDto.getAhorrosBean())){
                pdfItemGrupoDto = plantillaResource.generarPdfAhorros(plantillaDto, rqContratoGrupalDto.getAhorrosBean());
            }

            if(!Tool.isNullOrEmpty(pdfItemGrupoDto)){
                if(Tool.isNullOrEmpty(rsCombinatoriaPlantillaDto.getGrupoNombre())){
                    pdfManageDto = new PdfManageDto();
                    pdfManageDto.setBuffer(pdfItemGrupoDto.getFileBuffer());
                    pdfManageDto.setFileName(plantillaDto.getPlanTitulo()+".pdf");

                    pdfManageDtoList.add(pdfManageDto);
                }else{
                    lstPdfMerger.add(pdfItemGrupoDto);
                }
            }
        }
        return CompletableFuture.completedFuture(null);
    }


    @TrackExecutionTime(operation = "PLANTILLA_INDIVIDUALES")
    public List<PdfMergeItemDto> generarPdfsIndividuales(PlantillaDto plantillaDto, RqContratoGrupalDto rqContratoGrupalDto){

        List<PdfMergeItemDto> pdfMergeItemDtoList = new ArrayList<>();

        log.info("Inicia generarPdfsIndividuales para la plantillaDto : {}", plantillaDto.getPlanTitulo());
        if(!Tool.isNullOrEmpty(rqContratoGrupalDto.getCreditoGrupalBean())) log.info("Se generara para {} clientes", rqContratoGrupalDto.getCreditoGrupalBean().getListaCliente().size());
        if(!Tool.isNullOrEmpty(rqContratoGrupalDto.getAhorrosBean())) log.info("Se generara para {} clientes", rqContratoGrupalDto.getAhorrosBean().getListaCliente().size());

        List<CompletableFuture<Void>> listResponse= new ArrayList<>();
        if(!Tool.isNullOrEmpty(rqContratoGrupalDto.getCreditoGrupalBean())){
            List<Integer> ordenList = new ArrayList<>();
            int orden = 1;
            ordenList.add(orden);
            rqContratoGrupalDto.getCreditoGrupalBean().getListaCliente().forEach(e -> log.info("Cliente ----> {}", e.getCliNombre1()));
            for(ClienteBean clienteBean: rqContratoGrupalDto.getCreditoGrupalBean().getListaCliente()) {
                log.info("clienteBean nombre: {}", clienteBean.getCliNombre1());
                if (clienteBean.getCreditoIndividual().getCredIndGeneraContractual().toUpperCase().compareTo("S") != 0) {
                    log.info("clienteBean.getCreditoIndividual().getCredIndGeneraContractual(): {}",
                            clienteBean.getCreditoIndividual().getCredIndGeneraContractual().toUpperCase());
                    continue;
                }
                generarPdfsIndividualesCliente(listResponse,clienteBean,rqContratoGrupalDto,plantillaDto,pdfMergeItemDtoList,ordenList);
            }
        }else if (!Tool.isNullOrEmpty(rqContratoGrupalDto.getAhorrosBean())){
            int ordenAhorros;
            for(ClienteAhorroBean clienteBean: rqContratoGrupalDto.getAhorrosBean().getListaCliente()) {
                log.info("clienteAhorrosBean nombre: {}", clienteBean.getCliNombre1());
                if(clienteBean.getCliCargo().compareTo(new BigDecimal("4"))==0 || clienteBean.getCliCargo().compareTo(new BigDecimal("5"))==0) {
                    ordenAhorros = clienteBean.getCliCargo().compareTo(DecimalID.CINCO) == 0 ? 1 : 2;
                    listResponse.add(plantillaIndividualAsync.obtenerPlantillaIndAhorros(
                            clienteBean,
                            rqContratoGrupalDto.getAhorrosBean(),
                            plantillaDto, pdfMergeItemDtoList, ordenAhorros));
                }
            }
        }
        listResponse.stream().map(CompletableFuture::join).collect(Collectors.toList());

        //ordenar por lista original de clientes
        pdfMergeItemDtoList = pdfMergeItemDtoList.stream()
                .sorted(Comparator.comparing(PdfMergeItemDto::getFileOrder))
                .collect(Collectors.toList());

        return pdfMergeItemDtoList;
    }

    public void generarPdfsIndividualesCliente(List<CompletableFuture<Void>> listResponse,
                                                                        ClienteBean clienteBean,
                                                                        RqContratoGrupalDto rqContratoGrupalDto,
                                                                        PlantillaDto plantillaDto,
                                                                        List<PdfMergeItemDto> pdfMergeItemDtoList,
                                                                        List<Integer> ordenList){
        log.info("clienteBean.getCreditoIndividual().getCredIndGeneraContractual(): {}",
                clienteBean.getCreditoIndividual().getCredIndGeneraContractual().toUpperCase());
        int orden = ordenList.get(0);
        listResponse.add(plantillaIndividualAsync.obtenerPlantilla(clienteBean,rqContratoGrupalDto.getCreditoGrupalBean(),plantillaDto,pdfMergeItemDtoList, orden));
        orden++;
        ordenList.remove(0);
        ordenList.add(orden);
        if (!Tool.isNullOrEmpty(plantillaDto.getPlanChildDto())) {;
            PlantillaDto plantillaChild = plantillaDto.getPlanChildDto();
            log.info("EXISTE CHILD {}",plantillaChild.getPlanId());
            log.info("CHILD ORDEN {}",orden);
            generarPdfsIndividualesCliente(listResponse,clienteBean,rqContratoGrupalDto,plantillaChild,pdfMergeItemDtoList,ordenList);
        }
    }
}
