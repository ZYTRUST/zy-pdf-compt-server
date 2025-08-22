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
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Andre Gallegos
 * @version 8.2, 06/12/2022
 * @since 1.0
 */
@SpringBootTest
@Slf4j
@AutoConfigureMockMvc
public class SeguroOptativoTest {

    @Autowired
    protected Utilitario utilitario;

    @Autowired
    private PlantillaResource resource;

    /*@Test
    void test_generar_pdf_SeguroOptativo() throws Exception {
        PlantillaType plantillaType = PlantillaType.SEGURO_OPTATIVO;

        CreditoGrupalBean creditoGrupalBean = new CreditoGrupalBean();
        ReglasDeGrupoBean reglasDeGrupoBean = new ReglasDeGrupoBean();
        reglasDeGrupoBean.setHoraReunion("");
        reglasDeGrupoBean.setMontoMultaFaltaValor(BigDecimal.ONE);
        creditoGrupalBean.setReglasDeGrupoBean(reglasDeGrupoBean);

        List<ClienteBean> listaCliente = new ArrayList<>();

        ClienteBean clienteBean = new ClienteBean();

        clienteBean.setCliNombre1("Cesar 1 ");
        clienteBean.setCliNombre2("Cesar 2 ");
        clienteBean.setCliPaterno("Guerra");
        clienteBean.setCliMaterno("Perez");
        clienteBean.setCliDocumentoTipoDescripcion("DNI");
        clienteBean.setCliDocumentoNumero("20202020");
        clienteBean.setCliFechaNacimiento("20/05/1976");
        clienteBean.setCliSexo("M");
        clienteBean.setCliEstadoCivil("Soltero");
        clienteBean.setCliOcupacion("Ingeniero");
        clienteBean.setCliDireccion("Av las Gardelias");
        clienteBean.setCliProvincia("Lima");
        clienteBean.setCliDepartamento("Lima");
        clienteBean.setCliCelular("942942942");
        clienteBean.setCliCorreo("cguerrz@empesa.com");

        List<SeguroOptativoBean> listaSegOptativo = new ArrayList<>();
        SeguroOptativoBean seguroOptativoBean = new SeguroOptativoBean();
        List<BeneficiarioSeguroOptativoBean> listaBeneficiarioSegOptativo = new ArrayList<>();
        BeneficiarioSeguroOptativoBean beneficiarioSeguroOptativoBean = new BeneficiarioSeguroOptativoBean();

        beneficiarioSeguroOptativoBean.setSegOptativoBenefNombreCompleto("Juan Perez");
        beneficiarioSeguroOptativoBean.setSegOptativoBenefParentesco("Hijo");
        beneficiarioSeguroOptativoBean.setSegOptativoBenefPorcentaje(new BigDecimal(50.00).setScale(4, RoundingMode.HALF_UP));

        listaBeneficiarioSegOptativo.add(beneficiarioSeguroOptativoBean);

        beneficiarioSeguroOptativoBean = new BeneficiarioSeguroOptativoBean();
        beneficiarioSeguroOptativoBean.setSegOptativoBenefNombreCompleto("Juan Perez 2");
        beneficiarioSeguroOptativoBean.setSegOptativoBenefParentesco("Hijo");
        beneficiarioSeguroOptativoBean.setSegOptativoBenefPorcentaje(new BigDecimal(50.00).setScale(4, RoundingMode.HALF_UP));

        listaBeneficiarioSegOptativo.add(beneficiarioSeguroOptativoBean);

        seguroOptativoBean.setListaBeneficiarioSegOptativo(listaBeneficiarioSegOptativo);

        listaSegOptativo.add(seguroOptativoBean);

        clienteBean.setListaSegOptativo(listaSegOptativo);

        listaCliente.add(clienteBean);

        creditoGrupalBean.setListaCliente(listaCliente);

        PdfMergeItemDto item = resource.generarPdfFomulario(plantillaType,creditoGrupalBean);
        Utilitario.saveFile("C:\\Users\\agallegos\\Documents\\Compartamos\\"+ plantillaType.name()+".pdf",item.getFileBuffer());
    }*/
}
