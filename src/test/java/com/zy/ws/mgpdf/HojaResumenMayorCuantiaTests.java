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
public class HojaResumenMayorCuantiaTests {
    @Autowired

    private PlantillaResource plantillaResource;
    private static final String rutaPdf = "C:\\Users\\jegocheaga.ZYTRUSTDC01\\Documents\\compartamos\\HojaResumenMayorCuantia.pdf";

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

    private PlantillaDto generarPlantillaDto(){
        PlantillaDto plantillaDto = new PlantillaDto();
        plantillaDto.setPlanTitulo("HOJA_RESUMEN_MAYOR_CUANTIA");
        plantillaDto.setPlanNombreArchivo("HojaResumenMayorCuantia.jasper");
        plantillaDto.setPlanRuta( "C:\\Users\\jegocheaga.ZYTRUSTDC01\\Documents\\compartamos\\HojaResumenMayorCuantiaCI\\");
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
        //clienteBean1.setCliGrupoEco(new BigDecimal("1334.00"));
        clienteBean1.setCliPatriMto(new BigDecimal("1335.00"));
        clienteBean1.setCliCodSBS(new BigDecimal("1332.00"));
        clienteBean1.setCliCodigo(new BigDecimal("1324.00"));
        clienteBean1.setCliCargo(new BigDecimal("5.00"));
        clienteBean1.setCliCLabRUC("123123312654623");
        clienteBean1.setCliNegRUC("999999999999999");

        ClienteIndBean clienteBean2 = new ClienteIndBean();
        clienteBean2.setCliNombre1("Vanessa");
        clienteBean2.setCliNombre2("Mariangela");
        clienteBean2.setCliPaterno("ddddddddddd");
        clienteBean2.setCliMaterno("ccccccccc");
        clienteBean2.setCliDocumentoNumero("2342523");
        clienteBean2.setCliDireccion("Tu casa");
        clienteBean2.setCliTelefono("686465464");
        clienteBean2.setCliCorreo("nnnnnnnnnnn@gmail.com");
        clienteBean2.setCliCargo(new BigDecimal(6.00));

        ClienteIndBean clienteBean3 = new ClienteIndBean();
        clienteBean3.setCliNombre1("Natasha");
        clienteBean3.setCliNombre2("Majo");
        clienteBean3.setCliPaterno("yyyyyyyyy");
        clienteBean3.setCliMaterno("uuuuuuuuu");
        clienteBean3.setCliDocumentoNumero("63438284532");
        clienteBean3.setCliDireccion("Esas casa");
        clienteBean3.setCliTelefono("63453545452524");
        clienteBean3.setCliCorreo("hhhhhhh@gmail.com");
        clienteBean3.setCliCargo(new BigDecimal(7.00));

        ClienteIndBean clienteBean4 = new ClienteIndBean();
        clienteBean4.setCliNombre1("Estrella");
        clienteBean4.setCliNombre2("Luisa");
        clienteBean4.setCliPaterno("Cahuas");
        clienteBean4.setCliMaterno("Vergara");
        clienteBean4.setCliDocumentoNumero("8284532");
        clienteBean4.setCliDireccion("Tusaaaaa casa");
        clienteBean4.setCliTelefono("545452524");
        clienteBean4.setCliCorreo("gggggg@gmail.com");
        clienteBean4.setCliCargo(new BigDecimal("7.00"));

        ClienteIndBean clienteBean5 = new ClienteIndBean();
        clienteBean5.setCliNombre1("Micaella");
        clienteBean5.setCliNombre2("Valeria");
        clienteBean5.setCliPaterno("aaaaaaaaaaa");
        clienteBean5.setCliMaterno("sssssssss");
        clienteBean5.setCliDocumentoNumero("1453348284532");
        clienteBean5.setCliDireccion("las casa");
        clienteBean5.setCliTelefono("123125454525124");
        clienteBean5.setCliCorreo("fffffff@gmail.com");
        clienteBean5.setCliCargo(new BigDecimal("7.00"));

        ClienteIndBean clienteBean6 = new ClienteIndBean();
        clienteBean6.setCliNombre1("Luis");
        clienteBean6.setCliNombre2("Jorge");
        clienteBean6.setCliPaterno("ddddddddddddd");
        clienteBean6.setCliMaterno("fffffffffff");
        clienteBean6.setCliDocumentoNumero("8284123532");
        clienteBean6.setCliDireccion("Tusss casa");
        clienteBean6.setCliTelefono("545452524");
        clienteBean6.setCliCorreo("jjjjjjjjj@gmail.com");
        clienteBean6.setCliCargo(new BigDecimal("7.00"));

        SeguroDesgravamenBean seguroDesgravamenBean = new SeguroDesgravamenBean();
        seguroDesgravamenBean.setSegDesgraTasa(new BigDecimal("1338.00"));
        seguroDesgravamenBean.setSegDesgraPoliza("poliza");

        clienteBean1.setSegDesgravamen(seguroDesgravamenBean);

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
        clienteBean1.setCartillaAhorros(cartillaAhorrosBean);

        CreditoIndivGlobalBean creditoGrupalBean = new CreditoIndivGlobalBean();
        creditoGrupalBean.setCodigoOperacion(new BigDecimal("1344.00"));

        CreditoIndividualBean creditoIndividualBean = new CreditoIndividualBean();
        creditoIndividualBean.setCredIndCodigo(new BigDecimal("1834.00"));
        creditoIndividualBean.setCredIndDesembolsoMonto(new BigDecimal("1934.00"));
        creditoIndividualBean.setCredIndlMonedaSimbolo("S");
        creditoIndividualBean.setCredIndDesembolsoFecha("desembolso");
        creditoIndividualBean.setCredIndTEA360(new BigDecimal("134.53737"));
        creditoIndividualBean.setCredIndTCEA(new BigDecimal("334.45354343"));
        creditoIndividualBean.setCredIndIntCompMontoTotal(new BigDecimal("133.00"));
        creditoIndividualBean.setCredIndGarMtoCod("2656165165165");
        creditoIndividualBean.setCredIndTasaMoraNominalAnual(new BigDecimal("184.00"));
        creditoIndividualBean.setCredIndFormaDesemb("formadesem");
        creditoIndividualBean.setCredIndRemiSiNo("Si");
        creditoIndividualBean.setCredIndModoEnvioECta("modoenviocta");
        creditoIndividualBean.setCredIndPagoAnticip("pago_anti");
        creditoIndividualBean.setCredIndPlazoPrestamoCantidad(new BigDecimal("12.00"));

        List<CronogramaIndividualBean> listCondicionBean = new ArrayList<>();

        CronogramaIndividualBean  itemCondicionBean;

        //Condiciones
        itemCondicionBean = new CronogramaIndividualBean();
        itemCondicionBean.setCuoIndFecha("19/07/22");
        itemCondicionBean.setCuoIndNumero(new BigDecimal("001"));
        itemCondicionBean.setCuoIndValor(new BigDecimal("1319.00"));
        itemCondicionBean.setCuoIndMontoCapital(new BigDecimal("10.00"));
        itemCondicionBean.setCuoIndMontoIntComp(new BigDecimal("0.00"));
        itemCondicionBean.setCuoIndMontoSegDesgravamen(new BigDecimal("20.00"));
        itemCondicionBean.setCuoIndCuotaEECC(new BigDecimal("30.00"));
        itemCondicionBean.setCuoIndMontoSegTodoRiesg(new BigDecimal("40.00"));
        itemCondicionBean.setCuoIndCuotaPagTot(new BigDecimal("50.00"));

        listCondicionBean.add(itemCondicionBean);
        itemCondicionBean = new CronogramaIndividualBean();
        itemCondicionBean.setCuoIndFecha("19/07/22");
        itemCondicionBean.setCuoIndNumero(new BigDecimal("002"));
        itemCondicionBean.setCuoIndValor(new BigDecimal("1334.00"));
        itemCondicionBean.setCuoIndMontoCapital(new BigDecimal("70.00"));
        itemCondicionBean.setCuoIndMontoIntComp(new BigDecimal("80.00"));
        itemCondicionBean.setCuoIndMontoSegDesgravamen(new BigDecimal("90.00"));
        itemCondicionBean.setCuoIndCuotaEECC(new BigDecimal("100.00"));
        itemCondicionBean.setCuoIndMontoSegTodoRiesg(new BigDecimal("110.00"));
        itemCondicionBean.setCuoIndCuotaPagTot(new BigDecimal("120.00"));


        listCondicionBean.add(itemCondicionBean);
        itemCondicionBean = new CronogramaIndividualBean();
        itemCondicionBean.setCuoIndFecha("19/07/22");
        itemCondicionBean.setCuoIndNumero(new BigDecimal("003"));
        itemCondicionBean.setCuoIndValor(new BigDecimal("1334.00"));
        itemCondicionBean.setCuoIndMontoCapital(new BigDecimal("10.00"));
        itemCondicionBean.setCuoIndMontoIntComp(new BigDecimal("30.00"));
        itemCondicionBean.setCuoIndMontoSegDesgravamen(new BigDecimal("20.00"));
        itemCondicionBean.setCuoIndCuotaEECC(new BigDecimal("70.00"));
        itemCondicionBean.setCuoIndMontoSegTodoRiesg(new BigDecimal("60.00"));
        itemCondicionBean.setCuoIndCuotaPagTot(new BigDecimal("50.00"));


        listCondicionBean.add(itemCondicionBean);
        creditoIndividualBean.setListaCronogramaIndividual(listCondicionBean);
        clienteBean1.setCreditoIndividual(creditoIndividualBean);

        listaCliente.add(clienteBean1);
        listaCliente.add(clienteBean2);
        listaCliente.add(clienteBean3);
        listaCliente.add(clienteBean4);
        listaCliente.add(clienteBean5);
        listaCliente.add(clienteBean6);

        creditoGrupalBean.setListaCliente(listaCliente);

        return creditoGrupalBean;
    }

}
