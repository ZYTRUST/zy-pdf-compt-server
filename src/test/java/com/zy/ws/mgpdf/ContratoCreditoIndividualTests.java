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
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ContratoCreditoIndividualTests {
    @Autowired

    PlantillaResource plantillaResource;
    private static final String rutaPdf = "C:\\Users\\jegocheaga.ZYTRUSTDC01\\Documents\\compartamos\\"+ "ContratoCreditoIndividual.pdf";
    @Test
    public void generarPdfTest() throws IOException, IllegalAccessException {

        PlantillaResource plantillaResource = PlantillaTestsUtilitarios.inicializarResource("C:\\Users\\jegocheaga.ZYTRUSTDC01\\Documents\\compartamos\\CreditoIndividualCI\\" );
        PdfMergeItemDto resultado = null;
        log.info("Plantilla Resource = {}",plantillaResource);
        try {
            resultado = plantillaResource.generarPdfGeneralIndividual(
                    generarPlantillaDto(),
                    generarDatosDePrueba()
            );
        }catch (ZyTException exception) {
            Assertions.fail("Un error ocurrio al generar el pdf", exception.getCause());
        }
        log.info("El resultado de generación de PDF es PdfMergeItemDto[fileBuffer={},fileOrder={}]",
                resultado.getFileBuffer(), resultado.getFileOrder());
        Utilitario.saveFile(rutaPdf, resultado.getFileBuffer());
    }

    private PlantillaDto generarPlantillaDto(){
        PlantillaDto plantillaDto = new PlantillaDto();
        plantillaDto.setPlanTitulo("CONTRATO_CREDITO_INDIVIDUAL");
        plantillaDto.setPlanNombreArchivo("01 - 01 Contrato Crédito Individual V1.2.jasper");
        plantillaDto.setPlanRuta( "C:\\Users\\jegocheaga.ZYTRUSTDC01\\Documents\\compartamos\\CreditoIndividualCI\\");
        plantillaDto.setPlanOrden(new BigDecimal("14"));
        plantillaDto.setPlanPorCliente(false);
        return  plantillaDto;
    }
    private CreditoIndivGlobalBean generarDatosDePrueba() {

        List<ClienteIndBean> listaCliente = new ArrayList<>();
        ClienteIndBean clienteBean1 = new ClienteIndBean();
        clienteBean1.setCliNombre1("Joaquin");
        clienteBean1.setCliNombre2("Miguel");
        clienteBean1.setCliPaterno("Cahuas");
        clienteBean1.setCliMaterno("Vergara");
        clienteBean1.setCliDocumentoNumero("73184532");
        clienteBean1.setCliDireccion("Mi casa");
        clienteBean1.setCliTelefono("965944532");
        clienteBean1.setCliCorreo("asdasd@gmail.com");
        clienteBean1.setCliDocumentoTipoDescripcion("D.N.I.");
        clienteBean1.setCliRazonSocial("tecnología");
        clienteBean1.setCliDistrito("Mi DISTRITO");
        clienteBean1.setCliProvincia("Callao");
        clienteBean1.setCliDepartamento("Lima");
        //clienteBean1.setCliAutorizaDetalle("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed dictum sem non dolor dignissim gravida. Cras sapien mi, aliquet vitae dapibus hendrerit, tincidunt et nisi. Vestibulum augue eros, ultrices at nisi eget, varius mollis enim. Fusce maximus, mauris id cursus porttitor, sem orci luctus nibh, et ornare ex diam at metus. Mauris eget condimentum mi. Nunc pellentesque lacus ultrices enim interdum cursus. Aenean interdum molestie lorem, a pulvinar mauris. Nullam quis quam feugiat, eleifend erat sed, volutpat mi. Donec vehicula, nisl eget accumsan ultricies, lorem massa tempor neque, et sollicitudin lorem dui egestas odio. Sed efficitur mi non sem rhoncus laoreet. Sed ex est, rhoncus eget rutrum sed, lacinia non dolor.");
        clienteBean1.setCliPatriMtoPer(new BigDecimal("1337.00"));
        clienteBean1.setCliActivNom("activo");

        clienteBean1.setCliFecInicio("22/10/1999");
        clienteBean1.setCliCodCIIU(new BigDecimal("1336.00"));
        clienteBean1.setCliActividadesOtras("otros activos");
       // clienteBean1.setCliGrupoEco(new BigDecimal("1334.00"));
        clienteBean1.setCliPatriMto(new BigDecimal("1335.00"));
        clienteBean1.setCliCodSBS(new BigDecimal("1332.00"));
        clienteBean1.setCliCodigo(new BigDecimal("1324.00"));
        clienteBean1.setCliCargo(new BigDecimal("5.00"));
        clienteBean1.setCliCLabRUC("123123312654623");
        clienteBean1.setCliNegRUC("999999999999999");

        listaCliente.add(clienteBean1);
        CreditoIndivGlobalBean creditoGrupalBean = new CreditoIndivGlobalBean();
        creditoGrupalBean.setListaCliente(listaCliente);

        return creditoGrupalBean;
    }


}
