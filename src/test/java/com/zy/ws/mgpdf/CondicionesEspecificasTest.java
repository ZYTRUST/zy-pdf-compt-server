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

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andre Gallegos
 * @version 8.2, 30/11/2022
 * @since 1.0
 */
@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class CondicionesEspecificasTest {

    @Autowired
    protected Utilitario utilitario;

    @Autowired
    protected PlantillaResource resource;

    /*@Test
    void test_generar_pdf_CondicionesEspecificas() throws IOException {

        PlantillaType plantillaType = PlantillaType.CONDICIONES_ESPECIFICAS;

        CreditoGrupalBean condicionesEspecificasBean = new CreditoGrupalBean();
        ReglasDeGrupoBean reglasDeGrupoBean = new ReglasDeGrupoBean();
        reglasDeGrupoBean.setHoraReunion("");
        reglasDeGrupoBean.setMontoMultaFaltaValor(BigDecimal.ONE);
        condicionesEspecificasBean.setReglasDeGrupoBean(reglasDeGrupoBean);

        condicionesEspecificasBean.setGrupoNombre("LAS VICTORIOSAS DE SANTA TERESA");
        condicionesEspecificasBean.setCodigoOperacion(new BigDecimal(11405528));

        List<ClienteBean> listaCliente = new ArrayList<>();

        ClienteBean clienteBean = new ClienteBean();
        clienteBean.setCliPaterno("NUNJAR");
        clienteBean.setCliMaterno("GIRON");
        clienteBean.setCliNombre1("MAXIMINA");
        clienteBean.setCliNombre2("LAURA");
        clienteBean.setCliCodigo(new BigDecimal(2264807));

        CreditoIndividualBean creditoIndividual = new CreditoIndividualBean();

        creditoIndividual.setCredIndCodigo(new BigDecimal(11405528));
        creditoIndividual.setCredIndlMonedaSimbolo("S/. ");
        creditoIndividual.setCredIndMonedaDescripcion("SOLES");
        creditoIndividual.setCredIndDesembolsoMonto(new BigDecimal(1334));
        creditoIndividual.setCredIndDesembolsoFecha("19/07/2022");
        creditoIndividual.setCredIndFrecuenciaPagoCantidad(new BigDecimal(14));
        creditoIndividual.setCredIndPlazoPrestamoCantidad(new BigDecimal(8));

        creditoIndividual.setCredIndTEA360(new BigDecimal(83.698098).setScale(4, RoundingMode.HALF_UP));
        creditoIndividual.setCredIndTCEA(new BigDecimal(98.330000).setScale(4, RoundingMode.HALF_UP));
        creditoIndividual.setCredIndIntCompMontoTotal(new BigDecimal(148.89).setScale(4, RoundingMode.HALF_UP));
        creditoIndividual.setCredIndCanalExternoAutorizacion(new BigDecimal(0));
        creditoIndividual.setCredIndDesembolsoCompCancelDetalleCredCancelar("N.A.");
        creditoIndividual.setCredIndDesembolsoCompCancelMontoEntregado("N.A.");
        creditoIndividual.setCredIndTasaMoraNominalAnual(new BigDecimal(11.824680));
        clienteBean.setCreditoIndividual(creditoIndividual);

        List<CronogramaIndividualBean> listaCronogramaIndividual = new ArrayList<>();
        listaCronogramaIndividual = cargaCondicion();
        creditoIndividual.setListaCronogramaIndividual(listaCronogramaIndividual);
        clienteBean.setCreditoIndividual(creditoIndividual);
        listaCliente.add(clienteBean);

        condicionesEspecificasBean.setListaCliente(listaCliente);

        PdfMergeItemDto item = resource.generarPdfFomulario(plantillaType,condicionesEspecificasBean);

        Utilitario.saveFile("C:\\Users\\agallegos\\Documents\\Compartamos\\"+plantillaType.name()+".pdf",item.getFileBuffer());
    }*/

    public static List<CronogramaIndividualBean> cargaCondicion() {
        List<CronogramaIndividualBean> listCondicionBean = new ArrayList<>();

        CronogramaIndividualBean  itemCondicionBean;

        //Condiciones
        itemCondicionBean = new CronogramaIndividualBean();
        itemCondicionBean.setCuoIndFecha("19/07/22");
        itemCondicionBean.setCuoIndNumero(new BigDecimal(001));
        itemCondicionBean.setCuoIndValor(new BigDecimal(1334.00));
        itemCondicionBean.setCuoIndMontoCapital(new BigDecimal(0.00));
        itemCondicionBean.setCuoIndMontoIntComp(new BigDecimal(0.00));
        itemCondicionBean.setCuoIndMontoSegDesgravamen(new BigDecimal(0.00));
        itemCondicionBean.setCuoIndITF(new BigDecimal(0.00));
        itemCondicionBean.setCuoIndMontoSegTodoRiesg(new BigDecimal(0.00));

        listCondicionBean.add(itemCondicionBean);

        itemCondicionBean = new CronogramaIndividualBean();
        itemCondicionBean.setCuoIndFecha("19/07/22");
        itemCondicionBean.setCuoIndNumero(new BigDecimal(001));
        itemCondicionBean.setCuoIndValor(new BigDecimal(187.00));
        itemCondicionBean.setCuoIndMontoCapital(new BigDecimal(151.08).setScale(4, RoundingMode.HALF_UP));
        itemCondicionBean.setCuoIndMontoIntComp(new BigDecimal(31.92).setScale(4, RoundingMode.HALF_UP));
        itemCondicionBean.setCuoIndMontoSegDesgravamen(new BigDecimal(4.00));
        itemCondicionBean.setCuoIndITF(new BigDecimal(0.00));
        itemCondicionBean.setCuoIndMontoSegTodoRiesg(new BigDecimal(0.00));

        listCondicionBean.add(itemCondicionBean);

        itemCondicionBean = new CronogramaIndividualBean();
        itemCondicionBean.setCuoIndFecha("19/07/22");
        itemCondicionBean.setCuoIndNumero(new BigDecimal(001));
        itemCondicionBean.setCuoIndValor(new BigDecimal(1334.00));
        itemCondicionBean.setCuoIndMontoCapital(new BigDecimal(0.00));
        itemCondicionBean.setCuoIndMontoIntComp(new BigDecimal(0.00));
        itemCondicionBean.setCuoIndMontoSegDesgravamen(new BigDecimal(0.00));
        itemCondicionBean.setCuoIndITF(new BigDecimal(0.00));
        itemCondicionBean.setCuoIndMontoSegTodoRiesg(new BigDecimal(0.00));

        listCondicionBean.add(itemCondicionBean);

        itemCondicionBean = new CronogramaIndividualBean();
        itemCondicionBean.setCuoIndFecha("19/07/22");
        itemCondicionBean.setCuoIndNumero(new BigDecimal(001));
        itemCondicionBean.setCuoIndValor(new BigDecimal(1334.00));
        itemCondicionBean.setCuoIndMontoCapital(new BigDecimal(0.00));
        itemCondicionBean.setCuoIndMontoIntComp(new BigDecimal(0.00));
        itemCondicionBean.setCuoIndMontoSegDesgravamen(new BigDecimal(0.00));
        itemCondicionBean.setCuoIndITF(new BigDecimal(0.00));
        itemCondicionBean.setCuoIndMontoSegTodoRiesg(new BigDecimal(0.00));

        listCondicionBean.add(itemCondicionBean);

        return listCondicionBean;
    }
}
