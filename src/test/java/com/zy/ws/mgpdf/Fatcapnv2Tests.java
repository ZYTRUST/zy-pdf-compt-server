package com.zy.ws.mgpdf;

import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.*;
import com.zy.lib.dtos.dto.formu.PlantillaDto;
import com.zy.lib.dtos.dto.mgpdf.PdfMergeItemDto;
import com.zy.ws.mgpdf.conf.ZyTException;
import com.zy.ws.mgpdf.resource.PlantillaResource;
import com.zy.ws.mgpdf.util.Utilitario;
import com.zy.ws.mgpdf.utils.PlantillaTestsUtilitarios;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

@Slf4j
public class Fatcapnv2Tests {
    private static final String rutaPdf = "C:\\Users\\mcarhuas.ZYTRUSTDC01" +
            "\\Documents\\ZyTrust\\Compartamos\\Jaspers\\resultado-compartamos\\CredIndFATCAPNV2\\"
            + "FATCAPNV2.pdf";
    private static final String rutaJasper = "C:\\Users\\mcarhuas.ZYTRUSTDC01\\Documents\\ZyTrust\\Compartamos\\Jaspers";
    @Test
    public void generarPdfTestFatca() throws IOException, IllegalAccessException {
        PlantillaResource plantillaResource = PlantillaTestsUtilitarios.inicializarResource(rutaJasper);
        CreditoIndivGlobalBean creditoGrupalBean = generarDatosFatca();
        log.info("Los datos de entrada son: {}", creditoGrupalBean);
        log.info("Plantilla Resource = {}",plantillaResource);
        PlantillaDto plantillaDto = new PlantillaDto();
        plantillaDto.setPlanTitulo("FATCAPNV2");
        plantillaDto.setPlanRuta(rutaJasper + "\\compartamos\\FatcaCI\\");
        plantillaDto.setPlanNombreArchivo("02-03-FATCAPNV2+1.jasper");
        plantillaDto.setPlanPorCliente(false);
        plantillaDto.setPlanOrden(BigDecimal.ONE);
        PdfMergeItemDto resultado = null;
        try {
            resultado = plantillaResource.generarPdfGeneralIndividual(
                    plantillaDto,
                    creditoGrupalBean
            );
        }catch (ZyTException exception) {
            Assertions.fail("Un error ocurrio al generar el pdf", exception.getCause());
        }
        log.info("El resultado de generación de PDF es PdfMergeItemDto[fileBuffer={},fileOrder={}]",
                resultado.getFileBuffer(), resultado.getFileOrder());
        Utilitario.saveFile(rutaPdf, resultado.getFileBuffer());
    }

    private CreditoIndivGlobalBean generarDatosFatca() {
        FatcaBean fatcaBean1 = new FatcaBean(), fatcaBean2 = new FatcaBean(), fatcaBean3 = new FatcaBean();
        fatcaBean1.setFatcaPais("EEUU"); fatcaBean1.setFatcaNIT("65456556");
        fatcaBean2.setFatcaPais("Colombia"); fatcaBean2.setFatcaNIT("65456556");
        fatcaBean3.setFatcaPais("Chile"); fatcaBean3.setFatcaDNIT("No se ha tramitado debido a diversas razones asociadas a nuevas políticas contractuales en la empresa.");

        FatBean fatBean = new FatBean();
        fatBean.setFatObligacionFiscalEEUU("SI");
        fatBean.setFatNumIdenTrib("58985734837");
        fatBean.setFatResidenciaFiscalExtranjero("SI");
        fatBean.setListaFatca(Arrays.asList(fatcaBean1, fatcaBean2, fatcaBean3));
        fatBean.setFatCiudadEmision("Lima");

        CreditoIndividualBean creditoIndividualBean = new CreditoIndividualBean();
        creditoIndividualBean.setCredIndDesembolsoFecha("20/09/2023");

        ClienteIndBean clienteBean = new ClienteIndBean();
        clienteBean.setCliNombre1("Miguel");
        clienteBean.setCliNombre2("Ángel");
        clienteBean.setCliPaterno("Cahuas");
        clienteBean.setCliMaterno("Vergara");
        clienteBean.setCliDocumentoNumero("73184532");
        clienteBean.setCliFechaNacimiento("09/04/2003");
        clienteBean.setCliPaisNacimiento("Perú");
        clienteBean.setFatca(fatBean);
        clienteBean.setCreditoIndividual(creditoIndividualBean);

        CreditoIndivGlobalBean creditoGrupalBean = new CreditoIndivGlobalBean();
        creditoGrupalBean.setListaCliente(Collections.singletonList(clienteBean));
        return creditoGrupalBean;
    }
}
