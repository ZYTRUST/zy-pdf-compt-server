package com.zy.ws.mgpdf;

import com.zy.cpts.cto.lib.zy.contrato.compts.dto.ClienteBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.*;
import com.zy.lib.dtos.dto.formu.PlantillaDto;
import com.zy.lib.dtos.dto.mgpdf.PdfMergeItemDto;
import com.zy.ws.mgpdf.conf.ZyTException;
import com.zy.ws.mgpdf.resource.PlantillaResource;
import com.zy.ws.mgpdf.util.Utilitario;
import com.zy.ws.mgpdf.utils.PlantillaTestsUtilitarios;
import feign.Client;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

@Slf4j
public class HojaResumenMenorCuantiaTests {
    private static final String rutaPdf = "C:\\Users\\mcarhuas.ZYTRUSTDC01" +
            "\\Documents\\ZyTrust\\Compartamos\\Jaspers\\resultado-compartamos\\HojaResumenMenorCuantiaCI\\";
    private static final String rutaJasper = "C:\\Users\\mcarhuas.ZYTRUSTDC01\\Documents\\ZyTrust\\Compartamos\\Jaspers";
    @Getter
    @AllArgsConstructor
    public static class Pair<T1, T2> {
        private T1 first;
        private T2 second;
    }
    @Test
    public void generarPdfTest() throws IllegalAccessException, IOException {
        PlantillaResource plantillaResource = PlantillaTestsUtilitarios.inicializarResource(rutaJasper);
        log.info("Plantilla Resource = {}", plantillaResource);
        Map<String, Pair<CreditoIndivGlobalBean, String>> casosDePrueba = generarCasosDePrueba();
        for(Map.Entry<String, Pair<CreditoIndivGlobalBean, String>> entry: casosDePrueba.entrySet()) {
            log.info("Nombre del caso de prueba = {}", entry.getKey());
            CreditoIndivGlobalBean datosDeEntrada = entry.getValue().getFirst();
            log.info("Los datos de entrada del caso de prueba son = {}", datosDeEntrada);
            PlantillaDto plantillaDto = new PlantillaDto();
            plantillaDto.setPlanTitulo("HOJA_RESUMEN_MENOR_CUANTIA");
            plantillaDto.setPlanRuta(rutaJasper + "\\compartamos\\HojaResumenMenorCuantiaCI\\");
            plantillaDto.setPlanNombreArchivo("HojaResumenMenorCuantia.jasper");
            plantillaDto.setPlanPorCliente(false);
            plantillaDto.setPlanOrden(BigDecimal.ONE);
            PdfMergeItemDto resultado = null;
            try {
                resultado = plantillaResource.generarPdfGeneralIndividual(
                        plantillaDto,
                        datosDeEntrada
                );
            }catch (ZyTException exception) {
                Assertions.fail("Un error ocurrio al generar el pdf", exception.getCause());
            }
            log.info("El pdf se generó correctamente");
            Utilitario.saveFile(rutaPdf + entry.getValue().getSecond(), resultado.getFileBuffer());
        }
    }
   private Map<String, Pair<CreditoIndivGlobalBean, String>> generarCasosDePrueba() {
        Map<String, Pair<CreditoIndivGlobalBean, String>> casosDePrueba = new HashMap<>();
        CreditoIndivGlobalBean caso1 = new CreditoIndivGlobalBean(), caso2 = new CreditoIndivGlobalBean(),
                caso3 = new CreditoIndivGlobalBean(), caso4 = new CreditoIndivGlobalBean(),
                caso5 = new CreditoIndivGlobalBean(), caso6 = new CreditoIndivGlobalBean();
        caso1.setListaCliente(Collections.singletonList(generarDatosTitular()));
        caso2.setListaCliente(Arrays.asList(generarDatosTitular(), generarDatosConyuge()));
        caso3.setListaCliente(Arrays.asList(generarDatosTitular(), generarDatosConyuge(),
                generarDatosFiador()));
        caso4.setListaCliente(Arrays.asList(generarDatosTitular(), generarDatosConyuge(),
                generarDatosFiador(), generarDatosFiador()));
        caso5.setListaCliente(Arrays.asList(generarDatosTitular(), generarDatosConyuge(),
                generarDatosFiador(), generarDatosFiador(), generarDatosFiador()));
        caso6.setListaCliente(Arrays.asList(generarDatosTitular(), generarDatosConyuge(),
                generarDatosFiador(), generarDatosFiador(), generarDatosFiador(), generarDatosFiador()));
        casosDePrueba.put("Titular", new Pair<>(caso1, "HojaResumenMenorCuantiaTitular.pdf"));
        casosDePrueba.put("Titular-Conyuge", new Pair<>(caso2, "HojaResumenMenorCuantiaTitularConyuge.pdf"));
        casosDePrueba.put("Titular-Conyuge-Fiador",
                new Pair<>(caso3, "HojaResumenMenorCuantiaTitularConyuge1Fiador.pdf"));
        casosDePrueba.put("Titular-Conyuge-Fiador-Fiador",
                new Pair<>(caso4, "HojaResumenMenorCuantiaTitularConyuge2Fiador.pdf"));
        casosDePrueba.put("Titular-Conyuge-Fiador-Fiador-Fiador",
                new Pair<>(caso5, "HojaResumenMenorCuantiaTitularConyuge3Fiador.pdf"));
        casosDePrueba.put("Titular-Conyuge-Fiador-Fiador-Fiador-Fiador",
                new Pair<>(caso6, "HojaResumenMenorCuantiaTitularConyuge4Fiador.pdf"));
        return casosDePrueba;
    }

    private ClienteIndBean generarDatosTitular() {
        SeguroDesgravamenBean seguroDesgravamenBean = new SeguroDesgravamenBean();
        seguroDesgravamenBean.setSegDesgraDetallePoliza("Detalle Poliza");
        seguroDesgravamenBean.setSegDesgraTasa(BigDecimal.valueOf(17.5));

        CronogramaIndividualBean cronogramaIndividualBean = new CronogramaIndividualBean();
        cronogramaIndividualBean.setCuoIndFecha("11/08/22");
        cronogramaIndividualBean.setCuoIndNumero(BigDecimal.ONE);
        cronogramaIndividualBean.setCuoIndValor(BigDecimal.valueOf(4000.00));
        cronogramaIndividualBean.setCuoIndMontoCapital(BigDecimal.valueOf(120.00));
        cronogramaIndividualBean.setCuoIndMontoIntComp(BigDecimal.valueOf(3.92));
        cronogramaIndividualBean.setCuoIndMontoSegDesgravamen(BigDecimal.valueOf(3.92));
//        cronogramaIndividualBean.setCuoIndITF(BigDecimal.ZERO);
        cronogramaIndividualBean.setCuoIndMontoSegTodoRiesg(BigDecimal.ZERO);
        cronogramaIndividualBean.setCuoIndCuotaEECC(BigDecimal.valueOf(750.00));
        cronogramaIndividualBean.setCuoIndCuotaPagTot(BigDecimal.valueOf(980.00));

        CreditoIndividualBean creditoIndividualBean = new CreditoIndividualBean();
        creditoIndividualBean.setCredIndlMonedaSimbolo("S/.");
        creditoIndividualBean.setCredIndDesembolsoMonto(BigDecimal.valueOf(30000.00));
        creditoIndividualBean.setCredIndDesembolsoFecha("24/08/2023");
        creditoIndividualBean.setCredIndTEA360(BigDecimal.valueOf(2.75));
        creditoIndividualBean.setCredIndTCEA(BigDecimal.valueOf(7.00));
        creditoIndividualBean.setCredIndIntCompMontoTotal(BigDecimal.valueOf(7000));
        creditoIndividualBean.setCredIndTasaMoraNominalAnual(BigDecimal.valueOf(5.50));
        creditoIndividualBean.setCredIndFormaDesemb("Efectivo");
        creditoIndividualBean.setCredIndRemiSiNo("Si");
        creditoIndividualBean.setCredIndModoEnvioECta("Modo envío");
        creditoIndividualBean.setCredIndPagoAnticip("Pagos Quincenales");
        creditoIndividualBean.setCredIndCodigo(BigDecimal.valueOf(5678));
        creditoIndividualBean.setListaCronogramaIndividual(
                Arrays.asList(
                        cronogramaIndividualBean,
                        cronogramaIndividualBean,
                        cronogramaIndividualBean
                )
        );

        ClienteIndBean clienteBean = new ClienteIndBean();
        clienteBean.setCliCargo(BigDecimal.valueOf(5L));
        clienteBean.setCliNombre1("Miguel");
        clienteBean.setCliNombre2("Ángel");
        clienteBean.setCliPaterno("Cahuas");
        clienteBean.setCliMaterno("Vergara");
        clienteBean.setCliCodigo(BigDecimal.valueOf(594895945));
        clienteBean.setCliRazonSocial("Razon Social");
        clienteBean.setCliDireccion("Direccion de mi casa");
        clienteBean.setCliDistrito("Callao");
        clienteBean.setCliProvincia("Callao");
        clienteBean.setCliDepartamento("Lima");
        clienteBean.setCliNegRUC("26578497584");
        clienteBean.setCliDocumentoNumero("73184532");
        clienteBean.setCliCodSBS(BigDecimal.valueOf(49487));
        //clienteBean.setCliGrupoEco(BigDecimal.valueOf(5935094));
        clienteBean.setCliPatriMto(BigDecimal.valueOf(20000.00));
        clienteBean.setCliActivNom("Desarrollo de Software");
        clienteBean.setCliNegInicioActividades("12/09/2022");
        clienteBean.setCliCodCIIU(BigDecimal.valueOf(859485));
        clienteBean.setCliActividadesOtras("Desarrollo de Videojuegos");
        clienteBean.setCliCelular("921296833");
        clienteBean.setCliCorreo("mcahuas@zytrust.com");
        clienteBean.setSegDesgravamen(seguroDesgravamenBean);
        clienteBean.setCreditoIndividual(creditoIndividualBean);
        return clienteBean;
    }
    private ClienteIndBean generarDatosFiador() {
        ClienteIndBean fiadorBean = new ClienteIndBean();
        fiadorBean.setCliCargo(BigDecimal.valueOf(7L));
        fiadorBean.setCliNombre1("Juan");
        fiadorBean.setCliNombre2("Daniel");
        fiadorBean.setCliPaterno("Rojas");
        fiadorBean.setCliMaterno("Sanchez");
        fiadorBean.setCliDocumentoNumero("77748874");
        fiadorBean.setCliCelular("947465046");
        fiadorBean.setCliCorreo("fiador@compartamos.com");
        fiadorBean.setCliDireccion("Direccion Fiador 1");
        return fiadorBean;
    }
    private ClienteIndBean generarDatosConyuge() {
        ClienteIndBean conyugeBean = new ClienteIndBean();
        conyugeBean.setCliCargo(BigDecimal.valueOf(6L));
        conyugeBean.setCliNombre1("Maria");
        conyugeBean.setCliNombre2("Fe");
        conyugeBean.setCliPaterno("Martinez");
        conyugeBean.setCliMaterno("Rosas");
        conyugeBean.setCliPatriMtoPer(BigDecimal.valueOf(5000.00));
        conyugeBean.setCliDireccion("Direccion de Maria");
        conyugeBean.setCliCelular("947857638");
        conyugeBean.setCliCorreo("MariaMartinez@hotmail.com");
        conyugeBean.setCliDocumentoNumero("78497525");
        return conyugeBean;
    }
}
