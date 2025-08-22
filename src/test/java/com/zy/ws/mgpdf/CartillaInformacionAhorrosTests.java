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
import java.util.Collections;

@Slf4j

public class CartillaInformacionAhorrosTests {
    private static final String rutaPdf = "C:\\Users\\jegocheaga.ZYTRUSTDC01\\Documents\\compartamos\\" +
            "CartillaInformacionAhorros.pdf";
    @Test
    public void generarPdfTest() throws IOException, IllegalAccessException {
        PlantillaResource plantillaResource = PlantillaTestsUtilitarios.inicializarResource("C:\\Users\\jegocheaga.ZYTRUSTDC01\\Documents\\compartamos\\" );
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

    //    private void inicializarResource() {
//        PlantillaService plantillaService = new PlantillaService();
//        plantillaService.setRutaJasper("C:\\Users\\jegocheaga.ZYTRUSTDC01\\Documents");
//        PdfManageService pdfManageService = new PdfManageService();
//        FilemanService filemanService = new FilemanService(null);
//        plantillaResource = new PlantillaResource(
//                plantillaService,
//                pdfManageService,
//                filemanService
//        );
//    }
    private PlantillaDto generarPlantillaDto(){
        PlantillaDto plantillaDto = new PlantillaDto();
        plantillaDto.setPlanTitulo("CARTILLA_INFORMACION_AHORROS");
        plantillaDto.setPlanNombreArchivo("CartillaInformacionAhorros.jasper");
        plantillaDto.setPlanRuta( "C:\\Users\\jegocheaga.ZYTRUSTDC01\\Documents\\compartamos\\CartillaInformacionAhorros\\");
        plantillaDto.setPlanOrden(new BigDecimal("14"));
        plantillaDto.setPlanPorCliente(false);
        return  plantillaDto;
    }
    private CreditoIndivGlobalBean generarDatosDePrueba() {

        ClienteIndBean clienteBean = new ClienteIndBean();
        clienteBean.setCliNombre1("Joaquin");
        clienteBean.setCliNombre2("Miguel");
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
        //clienteBean.setCliCasada("Costa");
        //clienteBean.setCliAutorizaDetalle("Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed dictum sem non dolor dignissim gravida. Cras sapien mi, aliquet vitae dapibus hendrerit, tincidunt et nisi. Vestibulum augue eros, ultrices at nisi eget, varius mollis enim. Fusce maximus, mauris id cursus porttitor, sem orci luctus nibh, et ornare ex diam at metus. Mauris eget condimentum mi. Nunc pellentesque lacus ultrices enim interdum cursus. Aenean interdum molestie lorem, a pulvinar mauris. Nullam quis quam feugiat, eleifend erat sed, volutpat mi. Donec vehicula, nisl eget accumsan ultricies, lorem massa tempor neque, et sollicitudin lorem dui egestas odio. Sed efficitur mi non sem rhoncus laoreet. Sed ex est, rhoncus eget rutrum sed, lacinia non dolor.");

        CartillaAhorrosBean cartillaAhorrosBean = new CartillaAhorrosBean();
        cartillaAhorrosBean.setPasAgencia("Agencia");
        cartillaAhorrosBean.setPasTipoCuenta("Tipo cuenta");
        cartillaAhorrosBean.setPasProducto("Producto");
        cartillaAhorrosBean.setPasNroCuenta("12321321312312");
        cartillaAhorrosBean.setPasMoneda("S/");
        cartillaAhorrosBean.setPasNroCCI("2131231231231323123");
        cartillaAhorrosBean.setPasFechaApertura("22/10/1999");
        cartillaAhorrosBean.setPasTeaTasa("tasa1");
        cartillaAhorrosBean.setPasTreaTasa("trea1");
        cartillaAhorrosBean.setPasSalMinimo("12321");
        cartillaAhorrosBean.setPasMtoMinApertura("apertura");
        cartillaAhorrosBean.setPasTeaTasa2("tasa2");
        cartillaAhorrosBean.setPasTreaTasa2("trea2");
        cartillaAhorrosBean.setPasSalMinimo2("sal2");
        cartillaAhorrosBean.setPasMtoMinApertura2("min2");
        //clienteBean.setCartillaAhorros(cartillaAhorrosBean);

        CreditoIndivGlobalBean creditoIndivGlobalBean = new CreditoIndivGlobalBean();
        creditoIndivGlobalBean.setListaCliente(Collections.singletonList(clienteBean));
        return creditoIndivGlobalBean;
    }
}
