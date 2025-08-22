package com.zy.ws.mgpdf;

import com.zy.cpts.cto.lib.zy.contrato.compts.dto.ClienteBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.CreditoGrupalBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.CreditoIndividualBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.ReglasDeGrupoBean;
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
 * @version 8.2, 30/11/2022
 * @since 1.0
 */
@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class PolizaGrupoTest {

    @Autowired
    protected Utilitario utilitario;

    @Autowired
    private PlantillaResource resource;

    /*@Test
    void test_generar_pdf_PolizaGrupo() throws Exception {
        PlantillaType plantillaType = PlantillaType.POLIZA_GRUPO;

        CreditoGrupalBean obj = new CreditoGrupalBean();
        ReglasDeGrupoBean reglasDeGrupoBean = new ReglasDeGrupoBean();
        reglasDeGrupoBean.setHoraReunion("");
        reglasDeGrupoBean.setMontoMultaFaltaValor(BigDecimal.ONE);
        obj.setReglasDeGrupoBean(reglasDeGrupoBean);

        List<ClienteBean> listaCliente = new ArrayList<>();
        ClienteBean clienteBean = new ClienteBean();

        clienteBean.setCliNombre1("Juan 1");
        clienteBean.setCliNombre2("Alberto 1");
        clienteBean.setCliPaterno("Guerra");
        clienteBean.setCliMaterno("Perez");
        clienteBean.setCliDocumentoNumero("20202020");
        clienteBean.setCliDocumentoTipoDescripcion("DNI");
        clienteBean.setCliFechaNacimiento("20/05/1976");
        clienteBean.setCliSexo("M");
        clienteBean.setCliEstadoCivil("Soltero");
        clienteBean.setCliOcupacion("Ingeniero");
        clienteBean.setCliDireccion("Av las Gardelias");
        clienteBean.setCliProvincia("Lima");
        clienteBean.setCliDepartamento("Lima");
        clienteBean.setCliCelular("942942942");
        clienteBean.setCliCorreo("cguerrz@empesa.com");
        clienteBean.setCliEmiteSegDesgravamen("S");

        ClienteBean clienteBean1 = new ClienteBean();

        clienteBean1.setCliNombre1("Juan 2");
        clienteBean1.setCliNombre2("Alberto 2");
        clienteBean1.setCliPaterno("Guerra");
        clienteBean1.setCliMaterno("Perez");
        clienteBean1.setCliDocumentoNumero("20202020");
        clienteBean1.setCliDocumentoTipoDescripcion("DNI");
        clienteBean1.setCliFechaNacimiento("22/05/1976");
        clienteBean1.setCliSexo("M");
        clienteBean1.setCliEstadoCivil("Soltero");
        clienteBean1.setCliOcupacion("Ingeniero");
        clienteBean1.setCliDireccion("Av las Gardelias");
        clienteBean1.setCliProvincia("Lima");
        clienteBean1.setCliDepartamento("Lima");
        clienteBean1.setCliCelular("942942942");
        clienteBean1.setCliCorreo("cguerrz@empesa.com");
        clienteBean1.setCliEmiteSegDesgravamen("S");

        CreditoIndividualBean creditoIndividualBean = new CreditoIndividualBean();

        creditoIndividualBean.setCredIndDesembolsoFecha("26/04/2022");

        creditoIndividualBean.setCredIndlMonedaSimbolo("S");
        creditoIndividualBean.setCredIndDesembolsoMonto(new BigDecimal(3200));
        creditoIndividualBean.setCredIndPrestamoTipo("Grupal");

        creditoIndividualBean.setCredIndPrimaMinima(new BigDecimal(10200));
        creditoIndividualBean.setCredIndFormaPago("Catorcenal");
        creditoIndividualBean.setCredIndRangoDesembolso(new BigDecimal(10200));
        creditoIndividualBean.setCredIndTasaComercial(new BigDecimal(10200));
        creditoIndividualBean.setCredIndCargosComercializacionConIGV(new BigDecimal(10200));

        creditoIndividualBean.setCredIndPlazoPrestamoCantidad(new BigDecimal(4));

        clienteBean.setCreditoIndividual(creditoIndividualBean);
        listaCliente.add(clienteBean);
        listaCliente.add(clienteBean1);

        obj.setListaCliente(listaCliente);
        PdfMergeItemDto item = resource.generarPdfFomulariov2(plantillaType,obj);

        //Utilitario.saveFile("C:\\Users\\agallegos\\Documents\\Compartamos\\"+ plantillaType.name()+".pdf",item.getFileBuffer());
        Utilitario.saveFile("E:\\"+ plantillaType.name()+".pdf",item.getFileBuffer());
    }*/

}
