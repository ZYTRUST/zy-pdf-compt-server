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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Slf4j
public class CertificadoSeguroDesgravamenv7Tests {
    private static final String rutaPdf = "C:\\Users\\mcarhuas.ZYTRUSTDC01\\Documents\\ZyTrust\\Compartamos\\Jaspers\\" +
            "resultado-compartamos\\CredIndCertificadoSeguroDesgravamen\\" +
            "PlantillaCertificadoSeguroDesgravamenv7.pdf";
    private static final String rutaJasper = "C:\\Users\\mcarhuas.ZYTRUSTDC01\\Documents\\ZyTrust\\Compartamos\\Jaspers";

    @Test
    public void generarPdfTest() throws IllegalAccessException, IOException {
        PlantillaResource plantillaResource = PlantillaTestsUtilitarios.inicializarResource(rutaJasper);
        CreditoIndivGlobalBean creditoIndivGlobalBean = generarDatosDePrueba();
        log.info("Los datos de entrada son: {}", creditoIndivGlobalBean);
        log.info("Plantilla Resource = {}", plantillaResource);
        PlantillaDto plantillaDto = new PlantillaDto();
        plantillaDto.setPlanTitulo("CERTIFICADO_SEGURO_DESGRAVAMEN_V7");
        plantillaDto.setPlanRuta(rutaJasper + "\\compartamos\\CertificadoSeguroDesgravamenCI\\");
        plantillaDto.setPlanNombreArchivo("PlantillaCertificadoSeguroDesgravamenv7.jasper");
        plantillaDto.setPlanPorCliente(false);
        plantillaDto.setPlanOrden(BigDecimal.ONE);
        PdfMergeItemDto resultado = null;
        try {
            resultado = plantillaResource.generarPdfGeneralIndividual(
                    plantillaDto,
                    creditoIndivGlobalBean
            );
        }catch (ZyTException exception) {
            Assertions.fail("Un error ocurrio al generar el pdf", exception.getCause());
        }
        log.info("El resultado de generación de PDF es PdfMergeItemDto[fileBuffer={},fileOrder={}]",
                resultado.getFileBuffer(), resultado.getFileOrder());
        Utilitario.saveFile(rutaPdf, resultado.getFileBuffer());
    }

    private CreditoIndivGlobalBean generarDatosDePrueba() {
        PrimaDesgravamenBean prima1 = new PrimaDesgravamenBean(),
                prima2 = new PrimaDesgravamenBean(), prima3 = new PrimaDesgravamenBean();
        prima1.setSegDesgraTasaComercial("0.25");
        prima1.setSegDesgraTasaComision("0.50");

        prima2.setSegDesgraTasaComercial("0.25");
        prima2.setSegDesgraTasaComision("0.50");

        prima3.setSegDesgraTasaComercial("0.25");
        prima3.setSegDesgraTasaComision("0.50");

        List<PrimaDesgravamenBean> listaPrimaDesgravamen = Arrays.asList(prima1, prima2, prima3);

        SumaAsegDesgravamenBean sumaMaxAseg1 = new SumaAsegDesgravamenBean(),
                sumaMaxAseg2 = new SumaAsegDesgravamenBean();
        sumaMaxAseg1.setSegDesgraSumMaxAseg("1000.00");
        sumaMaxAseg2.setSegDesgraSumMaxAseg("1200.00");

        SeguroDesgravamenBean seguroDesgravamenBean = new SeguroDesgravamenBean();
        seguroDesgravamenBean.setSegDesgraCertificado(new BigDecimal("57638467"));
        seguroDesgravamenBean.setSegDesgraSBS("11823");
        seguroDesgravamenBean.setSegDesgraTipoPoliza("Poliza");
        seguroDesgravamenBean.setSegDesgraPolGrup("123");
        seguroDesgravamenBean.setSegDesgraBenefi("Miguel Cahuas Beneficiario");
        seguroDesgravamenBean.setSegDesgraPolizaGrup("4480955-07");
        seguroDesgravamenBean.setSegDesgraTituloCobertura("Titulo Cobertura");
        seguroDesgravamenBean.setSegDesgraDefinicionCobertura("Definicion Cobertura");
        seguroDesgravamenBean.setSegDesgraSumaAsegurada("2000.00");
        seguroDesgravamenBean.setSegDesgraMaxima("4000.00");
        seguroDesgravamenBean.setSegDesgraMtoMaxCobert("5000.00");
        seguroDesgravamenBean.setSegDesgraPoliza("61100076 (Soles) y N° 6110077 (Dólares)");
        seguroDesgravamenBean.setSegDesgraEnlace("www.compartamos.com.pe");
        seguroDesgravamenBean.setSegDesgraPriMinDetalle("Detalle Prima Minima");
        seguroDesgravamenBean.setListaSumaAsegDesgra(Arrays.asList(sumaMaxAseg1, sumaMaxAseg2));
        seguroDesgravamenBean.setListaPrimaDesgravamen(listaPrimaDesgravamen);

        CreditoIndividualBean creditoIndividualBean = new CreditoIndividualBean();
        creditoIndividualBean.setCredIndlMonedaSimbolo("S/.");
        creditoIndividualBean.setCredIndPlazoPrestamoCantidad(BigDecimal.valueOf(7L));
        creditoIndividualBean.setCredIndDesembolsoMonto(BigDecimal.valueOf(24000.00));
        creditoIndividualBean.setCredIndPrestamoTipo("A largo Plazo");
        creditoIndividualBean.setCredIndDesembolsoFecha("16/08/2023");

        ClienteIndBean clienteBean = new ClienteIndBean();
        clienteBean.setCliNombre1("Miguel");
        clienteBean.setCliPaterno("Cahuas");
        clienteBean.setCliMaterno("Vergara");
        clienteBean.setCliDocumentoTipoDescripcion("D.N.I.");
        clienteBean.setCliDocumentoNumero("73184532");
        clienteBean.setCliFechaNacimiento("09/04/2023");
        clienteBean.setCliSexo("M");
        clienteBean.setCliEstadoCivil("Soltero");
        clienteBean.setCliOcupacion("Ingeniero de Software");
        clienteBean.setCliDireccion("Mi casa");
        clienteBean.setCliProvincia("Callao");
        clienteBean.setCliCelular("921296833");
        clienteBean.setCliDepartamento("Lima");
        clienteBean.setCliCorreo("mcahuas@zytrust.com");
        clienteBean.setSegDesgravamen(seguroDesgravamenBean);
        clienteBean.setCreditoIndividual(creditoIndividualBean);

        CreditoIndivGlobalBean creditoIndivGlobalBean = new CreditoIndivGlobalBean();
        creditoIndivGlobalBean.setListaCliente(Collections.singletonList(clienteBean));
        return creditoIndivGlobalBean;
    }
}
