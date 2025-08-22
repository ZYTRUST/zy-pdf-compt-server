package com.zy.ws.mgpdf;



import com.zy.cpts.cto.lib.zy.contrato.compts.dto.CreditoGrupalBean;
//import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.ClienteBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.ClienteIndBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.CreditoIndivGlobalBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.SeguroDesgravamenBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.SeguroOptativoBean;
import com.zy.lib.dtos.dto.formu.PlantillaDto;
import com.zy.lib.dtos.dto.mgpdf.PdfMergeItemDto;
import com.zy.ws.mgpdf.conf.ZyTException;
import com.zy.ws.mgpdf.resource.PlantillaResource;
import com.zy.ws.mgpdf.util.Utilitario;
import com.zy.ws.mgpdf.utils.PlantillaTestsUtilitarios;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class CertificadoSeguroProteccionEconomicoCIv4Tests {
    @Autowired
    private PlantillaResource plantillaResource;
    //
//    private static final String rutaPdf = "C:\\Users\\jsanchez\\" + "Documents\\ZyTrust\\Jaspers\\Pruebas\\" + "CertificadoSeguroProteccionEconomicoCIv4.pdf";
    private static final String rutaPdf = "C:\\Users\\jegocheaga.ZYTRUSTDC01\\Documents\\compartamos\\CertificadoSPEconomicoCI.pdf";

    private static final String rutaJasper = "C:\\Users\\jegocheaga.ZYTRUSTDC01\\Documents\\compartamos\\";
    @Test
    public void generarPdfTest() throws IOException, IllegalAccessException {
        PlantillaResource plantillaResource = PlantillaTestsUtilitarios.inicializarResource(rutaJasper);
        CreditoIndivGlobalBean creditoGrupalBean = generarDatosDePrueba();
        log.info("Los datos de entrada son: {}", creditoGrupalBean);
        log.info("Plantilla Resource = {}",plantillaResource);
        PlantillaDto plantillaDto = new PlantillaDto();
        plantillaDto.setPlanTitulo("CERTIFICADO_SEGURO_PROTECCION_ECONOMICO_CI_V4");
        plantillaDto.setPlanRuta(rutaJasper +"CertificadoSPEconomicoCI\\");
        plantillaDto.setPlanNombreArchivo("CertificadoSeguroProteccionEconomicoCIv4.jasper");
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
        List<com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean> listaBeneficiarioSeguroOptativoBean = new ArrayList<com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean>();

        com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean beneficiarioSeguroOptativoBean = new com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean();
        beneficiarioSeguroOptativoBean.setSegOptativoBenefNombreCompleto("Miguel Cahuas");
        beneficiarioSeguroOptativoBean.setSegOptativoBenefParentesco("Hermano");
        beneficiarioSeguroOptativoBean.setSegOptativoBenefPorcentaje(BigDecimal.valueOf(10.0));

        com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean beneficiarioSeguroOptativoBean2 = new com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean();
        beneficiarioSeguroOptativoBean2.setSegOptativoBenefNombreCompleto("Ana Comun");
        beneficiarioSeguroOptativoBean2.setSegOptativoBenefParentesco("Hermana");
        beneficiarioSeguroOptativoBean2.setSegOptativoBenefPorcentaje(BigDecimal.valueOf(10.0));

        com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean beneficiarioSeguroOptativoBean3 = new com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean();
        beneficiarioSeguroOptativoBean3.setSegOptativoBenefNombreCompleto("Jorge Kawamura");
        beneficiarioSeguroOptativoBean3.setSegOptativoBenefParentesco("Hermano");
        beneficiarioSeguroOptativoBean3.setSegOptativoBenefPorcentaje(BigDecimal.valueOf(10.0));

        com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean beneficiarioSeguroOptativoBean4 = new com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean();
        beneficiarioSeguroOptativoBean4.setSegOptativoBenefNombreCompleto("Jorge Kawamura");
        beneficiarioSeguroOptativoBean4.setSegOptativoBenefParentesco("Hermano");
        beneficiarioSeguroOptativoBean4.setSegOptativoBenefPorcentaje(BigDecimal.valueOf(10.0));

        com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean beneficiarioSeguroOptativoBean5 = new com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean();
        beneficiarioSeguroOptativoBean5.setSegOptativoBenefNombreCompleto("Jorge Kawamura");
        beneficiarioSeguroOptativoBean5.setSegOptativoBenefParentesco("Hermano");
        beneficiarioSeguroOptativoBean5.setSegOptativoBenefPorcentaje(BigDecimal.valueOf(10.0));

        com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean beneficiarioSeguroOptativoBean6 = new com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean();
        beneficiarioSeguroOptativoBean6.setSegOptativoBenefNombreCompleto("Jorge Kawamura");
        beneficiarioSeguroOptativoBean6.setSegOptativoBenefParentesco("Hermano");
        beneficiarioSeguroOptativoBean6.setSegOptativoBenefPorcentaje(BigDecimal.valueOf(10.0));

        com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean beneficiarioSeguroOptativoBean7 = new com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean();
        beneficiarioSeguroOptativoBean7.setSegOptativoBenefNombreCompleto("Jorge Kawamura");
        beneficiarioSeguroOptativoBean7.setSegOptativoBenefParentesco("Hermano");
        beneficiarioSeguroOptativoBean7.setSegOptativoBenefPorcentaje(BigDecimal.valueOf(10.0));

        com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean beneficiarioSeguroOptativoBean8 = new com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean();
        beneficiarioSeguroOptativoBean8.setSegOptativoBenefNombreCompleto("Jorge Kawamura");
        beneficiarioSeguroOptativoBean8.setSegOptativoBenefParentesco("Hermano");
        beneficiarioSeguroOptativoBean8.setSegOptativoBenefPorcentaje(BigDecimal.valueOf(10.0));

        com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean beneficiarioSeguroOptativoBean9 = new com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean();
        beneficiarioSeguroOptativoBean9.setSegOptativoBenefNombreCompleto("Jorge Kawamura");
        beneficiarioSeguroOptativoBean9.setSegOptativoBenefParentesco("Hermano");
        beneficiarioSeguroOptativoBean9.setSegOptativoBenefPorcentaje(BigDecimal.valueOf(10.0));

        com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean beneficiarioSeguroOptativoBean10 = new com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean();
        beneficiarioSeguroOptativoBean10.setSegOptativoBenefNombreCompleto("Jorge Kawamura");
        beneficiarioSeguroOptativoBean10.setSegOptativoBenefParentesco("Hermano");
        beneficiarioSeguroOptativoBean10.setSegOptativoBenefPorcentaje(BigDecimal.valueOf(10.0));

        listaBeneficiarioSeguroOptativoBean.add(beneficiarioSeguroOptativoBean);
        listaBeneficiarioSeguroOptativoBean.add(beneficiarioSeguroOptativoBean2);
        listaBeneficiarioSeguroOptativoBean.add(beneficiarioSeguroOptativoBean3);
        listaBeneficiarioSeguroOptativoBean.add(beneficiarioSeguroOptativoBean4);
        listaBeneficiarioSeguroOptativoBean.add(beneficiarioSeguroOptativoBean5);
        listaBeneficiarioSeguroOptativoBean.add(beneficiarioSeguroOptativoBean6);
        listaBeneficiarioSeguroOptativoBean.add(beneficiarioSeguroOptativoBean7);
        listaBeneficiarioSeguroOptativoBean.add(beneficiarioSeguroOptativoBean8);
        listaBeneficiarioSeguroOptativoBean.add(beneficiarioSeguroOptativoBean9);
        listaBeneficiarioSeguroOptativoBean.add(beneficiarioSeguroOptativoBean10);

        SeguroDesgravamenBean seguroDesgravamenBean = new SeguroDesgravamenBean();
        seguroDesgravamenBean.setSegDesgraCertificado(BigDecimal.valueOf(77484898L));

        SeguroOptativoBean seguroOptativoBean = new SeguroOptativoBean();
        seguroOptativoBean.setListaBeneficiarioSegOptativo(listaBeneficiarioSeguroOptativoBean);

        ClienteIndBean clienteBean = new ClienteIndBean();
        clienteBean.setCliNombre1("Joaquin");
        clienteBean.setCliNombre2("Ignacio");
        clienteBean.setCliPaterno("Sanchez");
        clienteBean.setCliMaterno("Chavez");
        clienteBean.setCliDocumentoTipoDescripcion("DNI");
        clienteBean.setCliDocumentoNumero("74533819");
        clienteBean.setCliFechaNacimiento("23/08/2023");
        clienteBean.setCliSexo("Masculino");
        clienteBean.setCliEstadoCivil("Soltero");
        clienteBean.setCliOcupacion("Ingeniero de Software");
        clienteBean.setCliDireccion("Mi casa");
        clienteBean.setCliProvincia("Surquillo");
        clienteBean.setCliCelular("996898402");
        clienteBean.setCliDepartamento("Lima");
        clienteBean.setCliCorreo("jsanchez@zytrust.com");
        clienteBean.setCliCelular("996889402");
        clienteBean.setSegDesgravamen(seguroDesgravamenBean);
        clienteBean.setListaSegOptativo(Collections.singletonList(seguroOptativoBean));

        CreditoIndivGlobalBean creditoGrupalBean = new CreditoIndivGlobalBean();
        creditoGrupalBean.setListaCliente(Collections.singletonList(clienteBean));
        return creditoGrupalBean;
    }
}
