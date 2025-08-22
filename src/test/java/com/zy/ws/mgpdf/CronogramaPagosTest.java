package com.zy.ws.mgpdf;

import com.zy.cpts.cto.lib.zy.contrato.compts.dto.*;
import com.zy.lib.dtos.dto.mgpdf.PdfMergeItemDto;
import com.zy.ws.mgpdf.resource.PlantillaResource;
import com.zy.ws.mgpdf.util.PlantillaType;
import com.zy.ws.mgpdf.util.Utilitario;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andre Gallegos
 * @version 8.2, 07/12/2022
 * @since 1.0
 */
@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class CronogramaPagosTest {

    @Autowired
    protected Utilitario utilitario;

    @Autowired
    private PlantillaResource resource;

    /*@Test
    void test_generar_pdf_CronogramaPagos() throws Exception {
        PlantillaType plantillaType = PlantillaType.CRONOGRAMA_PAGOS;

        CreditoGrupalBean creditoGrupalBean = new CreditoGrupalBean();
        ReglasDeGrupoBean reglasDeGrupoBean = new ReglasDeGrupoBean();
        reglasDeGrupoBean.setHoraReunion("");
        reglasDeGrupoBean.setMontoMultaFaltaValor(BigDecimal.ONE);
        creditoGrupalBean.setReglasDeGrupoBean(reglasDeGrupoBean);

        List<ClienteBean> listaCliente = new ArrayList<>();

        ClienteBean clienteBean = new ClienteBean();

        clienteBean.setCliNombre1("Juan");
        clienteBean.setCliNombre2("Alberto");
        clienteBean.setCliPaterno("Guerra");
        clienteBean.setCliMaterno("Perez");
        clienteBean.setCliCodigo(new BigDecimal(123456));

        List<SeguroOptativoBean> listaSegOptativo = new ArrayList<>();
        SeguroOptativoBean seguroOptativoBean = new SeguroOptativoBean();
        seguroOptativoBean.setSegOptativoCertificado(new BigDecimal(51811876));
        seguroOptativoBean.setSegOptativoTipoProducto("MAPFRE - Proteccion Grupal");
        seguroOptativoBean.setSegOptativoTipoPlan("Grupal");
        seguroOptativoBean.setSegOptativoTiempoCoberturaCantidad(new BigDecimal(4));
        seguroOptativoBean.setSegOptativoTiempoCoberturaUnidad("meses");

        List<CronogramaSeguroOptativoBean> listaCronogramaSegOptativo = new ArrayList<>();
        CronogramaSeguroOptativoBean cronogramaSeguroOptativoBean = new CronogramaSeguroOptativoBean();

        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaDesde("01/05/2022");
        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaHasta("07/12/2022");
        cronogramaSeguroOptativoBean.setCuoSegOptativoFechaVencimiento("08/12/2022");
        cronogramaSeguroOptativoBean.setCuoSegOptativoPrimaMonto(new BigDecimal(54.06));

        listaCronogramaSegOptativo.add(cronogramaSeguroOptativoBean);

        cronogramaSeguroOptativoBean = new CronogramaSeguroOptativoBean();

        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaDesde("02/05/2022");
        cronogramaSeguroOptativoBean.setCuoSegOptativoCoberturaHasta("08/12/2022");
        cronogramaSeguroOptativoBean.setCuoSegOptativoFechaVencimiento("09/12/2022");
        cronogramaSeguroOptativoBean.setCuoSegOptativoPrimaMonto(new BigDecimal(154.06));

        listaCronogramaSegOptativo.add(cronogramaSeguroOptativoBean);

        seguroOptativoBean.setListaCronogramaSegOptativo(listaCronogramaSegOptativo);
        listaSegOptativo.add(seguroOptativoBean);

        System.out.println("listaSegOptativo: " + listaSegOptativo);
        System.out.println("=========================================/n");
        clienteBean.setListaSegOptativo(listaSegOptativo);

        listaCliente.add(clienteBean);

        creditoGrupalBean.setGrupoCodigo(new BigDecimal(561243));
        creditoGrupalBean.setListaCliente(listaCliente);
        System.out.println("Credito: " + creditoGrupalBean.getListaCliente());

        PdfMergeItemDto item = resource.generarPdfFomulario(plantillaType,creditoGrupalBean);

        Utilitario.saveFile("C:\\Users\\agallegos\\Documents\\Compartamos\\"+ plantillaType.name()+".pdf",item.getFileBuffer());
    }*/

}
