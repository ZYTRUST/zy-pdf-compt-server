package com.zy.ws.mgpdf;

import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.*;
import com.zy.lib.dtos.dto.formu.PlantillaDto;
import com.zy.lib.dtos.dto.mgpdf.PdfMergeItemDto;
import com.zy.ws.mgpdf.conf.ZyTException;
import com.zy.ws.mgpdf.resource.PlantillaResource;
import com.zy.ws.mgpdf.utils.PlantillaTestsUtilitarios;
import com.zy.ws.mgpdf.util.Utilitario;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Collections;

@Slf4j
public class CertificadoSeguroProteccionIndividualv4Tests {
    private static final String rutaPdf = "C:\\Users\\mcarhuas.ZYTRUSTDC01" +
            "\\Documents\\ZyTrust\\Compartamos\\Jaspers\\resultado-compartamos\\CredIndCertificadoSPIndividualv4\\"
            + "03-01-CertificadoSPIndividualv4.pdf";
    private static final String rutaJasper = "C:\\Users\\mcarhuas.ZYTRUSTDC01\\Documents\\ZyTrust\\Compartamos\\Jaspers";
    @Test
    public void generarPdfTest() throws IOException, IllegalAccessException {
        PlantillaResource plantillaResource = PlantillaTestsUtilitarios.inicializarResource(rutaJasper);
        CreditoIndivGlobalBean creditoGrupalBean = generarDatosDePrueba();
        log.info("Los datos de entrada son: {}", creditoGrupalBean);
        log.info("Plantilla Resource = {}",plantillaResource);
        PlantillaDto plantillaDto = new PlantillaDto();
        plantillaDto.setPlanTitulo("CERTIFICADO_SEGURO_PROTECCION_INDIVIDUAL_V4");
        plantillaDto.setPlanRuta(rutaJasper + "\\compartamos\\CertificadoSPIndividualCI\\");
        plantillaDto.setPlanNombreArchivo("03-01-CertificadoSPIndividualv4.jasper");
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

    private CreditoIndivGlobalBean generarDatosDePrueba() {
        BeneficiarioSeguroOptativoBean beneficiarioSeguroOptativoBean = new BeneficiarioSeguroOptativoBean();
        beneficiarioSeguroOptativoBean.setSegOptativoBenefNombreCompleto("Miguel Cahuas");
        beneficiarioSeguroOptativoBean.setSegOptativoBenefParentesco("Hermano");
        beneficiarioSeguroOptativoBean.setSegOptativoBenefPorcentaje(BigDecimal.valueOf(10.0));

        SeguroOptativoBean seguroOptativoBean = new SeguroOptativoBean();
        seguroOptativoBean.setListaBeneficiarioSegOptativo(Collections.singletonList(beneficiarioSeguroOptativoBean));

        SeguroDesgravamenBean seguroDesgravamenBean = new SeguroDesgravamenBean();
        seguroDesgravamenBean.setSegDesgraCertificado(BigDecimal.valueOf(77484898L));

        ClienteIndBean clienteBean = new ClienteIndBean();
        clienteBean.setCliNombre1("Miguel");
        clienteBean.setCliNombre2("Ángel");
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
        clienteBean.setCliCelular("921296833");
        clienteBean.setSegDesgravamen(seguroDesgravamenBean);
        clienteBean.setListaSegOptativo(Collections.singletonList(seguroOptativoBean));

        CreditoIndivGlobalBean creditoGrupalBean = new CreditoIndivGlobalBean();
        creditoGrupalBean.setListaCliente(Collections.singletonList(clienteBean));
        return creditoGrupalBean;
    }
}
