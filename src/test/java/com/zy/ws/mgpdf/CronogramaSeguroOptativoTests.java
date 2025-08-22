package com.zy.ws.mgpdf;

import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.*;
import com.zy.lib.dtos.dto.formu.PlantillaDto;
import com.zy.lib.dtos.dto.mgpdf.PdfMergeItemDto;
import com.zy.ws.mgpdf.conf.ZyTException;
import com.zy.ws.mgpdf.resource.PlantillaResource;
import com.zy.ws.mgpdf.util.PlantillaType;
import com.zy.ws.mgpdf.util.Utilitario;
import com.zy.ws.mgpdf.utils.PlantillaTestsUtilitarios;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
public class CronogramaSeguroOptativoTests {
    private static final String rutaPdf = "C:\\Users\\jsanchez\\Documents\\ZyTrust\\Jaspers\\Pruebas\\" + "CronogramaSeguroOtativo.pdf";
    private static final String rutaJasper = "C:\\Users\\jsanchez\\Documents\\ZyTrust\\Jaspers\\Compartamos";

    @Test
    void generarPdfTest() throws Exception {
        PlantillaResource plantillaResource = PlantillaTestsUtilitarios.inicializarResource(rutaJasper);
        CreditoIndivGlobalBean creditoGrupalBean = generarDatosDePrueba();
        log.info("Los datos de entrada son: {}", creditoGrupalBean);
        log.info("Plantilla Resource = {}", plantillaResource);
        PlantillaDto plantillaDto = new PlantillaDto();
        plantillaDto.setPlanTitulo("CRONOGRAMA_SEGURO_OPTATIVO");
        plantillaDto.setPlanRuta(rutaJasper + "\\compartamos\\CronogramaSeguroOptativo\\");
        plantillaDto.setPlanNombreArchivo("CronogramaSeguroOtativo.jasper");
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
        CreditoIndivGlobalBean creditoGrupalBean = new CreditoIndivGlobalBean();


        ClienteIndBean clienteBean = new ClienteIndBean();
        SeguroOptativoBean seguroOptativoBean = new SeguroOptativoBean();
        CronogramaSeguroOptativoBean cronogramaSeguroOptativoBean = new CronogramaSeguroOptativoBean();

        clienteBean.setCliNombre1("Joaquin");
        clienteBean.setCliNombre2("Ignacio");
        clienteBean.setCliPaterno("Sanchez");
        clienteBean.setCliMaterno("Chavez");
        clienteBean.setCliCodigo(new BigDecimal(2413345));

        List<SeguroOptativoBean> listaSegOptativo = new ArrayList<>();

        seguroOptativoBean.setSegOptativoCertificado(new BigDecimal(51811876));
        seguroOptativoBean.setSegOptativoTipoProducto("MAPFRE - Proteccion Grupal");
        seguroOptativoBean.setSegOptativoTipoPlan("Grupal");
        seguroOptativoBean.setSegOptativoTiempoCoberturaCantidad(new BigDecimal(4));
        seguroOptativoBean.setSegOptativoTiempoCoberturaUnidad("meses");

        List<CronogramaSeguroOptativoBean> listaCronogramaSegOptativo = new ArrayList<>();

        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaDesde("01/05/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaHasta("07/12/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoFechaVencimiento("08/12/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoPrimaMonto(new BigDecimal(54.06));

        listaCronogramaSegOptativo.add(cronogramaSeguroOptativoBean);

        cronogramaSeguroOptativoBean = new CronogramaSeguroOptativoBean();

        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaDesde("02/05/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaHasta("08/12/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoFechaVencimiento("09/12/2022");
        cronogramaSeguroOptativoBean.setCuoSegOptativoPrimaMonto(new BigDecimal(154.06));

        listaCronogramaSegOptativo.add(cronogramaSeguroOptativoBean);

        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaDesde("02/05/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaHasta("08/12/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoFechaVencimiento("09/12/2022");
        cronogramaSeguroOptativoBean.setCuoSegOptativoPrimaMonto(new BigDecimal(154.06));

        listaCronogramaSegOptativo.add(cronogramaSeguroOptativoBean);

        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaDesde("02/05/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaHasta("08/12/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoFechaVencimiento("09/12/2022");
        cronogramaSeguroOptativoBean.setCuoSegOptativoPrimaMonto(new BigDecimal(154.06));

        listaCronogramaSegOptativo.add(cronogramaSeguroOptativoBean);

        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaDesde("02/05/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaHasta("08/12/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoFechaVencimiento("09/12/2022");
        cronogramaSeguroOptativoBean.setCuoSegOptativoPrimaMonto(new BigDecimal(154.06));

        listaCronogramaSegOptativo.add(cronogramaSeguroOptativoBean);

        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaDesde("02/05/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaHasta("08/12/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoFechaVencimiento("09/12/2022");
        cronogramaSeguroOptativoBean.setCuoSegOptativoPrimaMonto(new BigDecimal(154.06));

        listaCronogramaSegOptativo.add(cronogramaSeguroOptativoBean);

        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaDesde("02/05/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaHasta("08/12/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoFechaVencimiento("09/12/2022");
        cronogramaSeguroOptativoBean.setCuoSegOptativoPrimaMonto(new BigDecimal(154.06));

        listaCronogramaSegOptativo.add(cronogramaSeguroOptativoBean);

        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaDesde("02/05/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaHasta("08/12/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoFechaVencimiento("09/12/2022");
        cronogramaSeguroOptativoBean.setCuoSegOptativoPrimaMonto(new BigDecimal(154.06));

        listaCronogramaSegOptativo.add(cronogramaSeguroOptativoBean);

        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaDesde("02/05/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaHasta("08/12/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoFechaVencimiento("09/12/2022");
        cronogramaSeguroOptativoBean.setCuoSegOptativoPrimaMonto(new BigDecimal(154.06));

        listaCronogramaSegOptativo.add(cronogramaSeguroOptativoBean);

        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaDesde("02/05/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaHasta("08/12/2023");
        cronogramaSeguroOptativoBean.setCuoSegOptativoFechaVencimiento("09/12/2022");
        cronogramaSeguroOptativoBean.setCuoSegOptativoPrimaMonto(new BigDecimal(154.06));

        listaCronogramaSegOptativo.add(cronogramaSeguroOptativoBean);

        seguroOptativoBean.setListaCronogramaSegOptativo(listaCronogramaSegOptativo);
        listaSegOptativo.add(seguroOptativoBean);

        System.out.println("listaSegOptativo: " + listaSegOptativo);
        System.out.println("=========================================/n");
        clienteBean.setListaSegOptativo(listaSegOptativo);


        creditoGrupalBean.setListaCliente(Collections.singletonList(clienteBean));
        System.out.println("Credito: " + creditoGrupalBean.getListaCliente());

        return creditoGrupalBean;
    }
}
