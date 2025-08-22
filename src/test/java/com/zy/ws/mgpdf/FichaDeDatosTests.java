package com.zy.ws.mgpdf;

import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.*;
import com.zy.lib.dtos.dto.formu.PlantillaDto;
import com.zy.lib.dtos.dto.mgpdf.PdfMergeItemDto;
import com.zy.ws.mgpdf.conf.ZyTException;
import com.zy.ws.mgpdf.resource.PlantillaResource;
import com.zy.ws.mgpdf.util.Utilitario;
import com.zy.ws.mgpdf.utils.PlantillaTestsUtilitarios;
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
public class FichaDeDatosTests {
    private static final String rutaPdf = "C:\\Users\\mcarhuas.ZYTRUSTDC01" +
            "\\Documents\\ZyTrust\\Compartamos\\Jaspers\\resultado-compartamos\\CredIndFichaDeDatos\\";

    private static final String rutaJasper = "C:\\Users\\mcarhuas.ZYTRUSTDC01\\Documents\\ZyTrust\\Compartamos\\Jaspers";

    @Getter
    @AllArgsConstructor
    private static class Pair<T1, T2> {
        private T1 first;
        private T2 second;
    }
    @Test
    public void generarPdfs() throws IllegalAccessException, IOException {
        PlantillaResource plantillaResource = PlantillaTestsUtilitarios.inicializarResource(rutaJasper);
        Map<String, Pair<CreditoIndivGlobalBean, String>> casos = generarCasosDePrueba();
        PlantillaDto plantillaDto = new PlantillaDto();
        plantillaDto.setPlanTitulo("FICHA_DE_DATOS");
        plantillaDto.setPlanRuta(rutaJasper + "\\compartamos\\FichaDeDatosCI\\");
        plantillaDto.setPlanNombreArchivo("01-03-FICHA_DE_DATOS.jasper");
        plantillaDto.setPlanPorCliente(false);
        plantillaDto.setPlanOrden(BigDecimal.ONE);
        for(Map.Entry<String, Pair<CreditoIndivGlobalBean, String>> set: casos.entrySet()) {
            log.info("Nombre caso de prueba: {}", set.getKey());
            CreditoIndivGlobalBean datosDeEntrada = set.getValue().getFirst();
            log.info("Los datos de entrada del caso son: {}", datosDeEntrada);
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
            Utilitario.saveFile(rutaPdf + set.getValue().getSecond(), resultado.getFileBuffer());
        }

    }
    private Map<String, Pair<CreditoIndivGlobalBean, String>> generarCasosDePrueba() {
        Map<String, Pair<CreditoIndivGlobalBean, String>> casos = new HashMap<>();
        CreditoIndivGlobalBean caso1 = new CreditoIndivGlobalBean(), caso2 = new CreditoIndivGlobalBean(),
        caso3 = new CreditoIndivGlobalBean(), caso4 = new CreditoIndivGlobalBean(), caso5 = new CreditoIndivGlobalBean(),
        caso6 = new CreditoIndivGlobalBean();
        caso1.setListaCliente(Arrays.asList(
                generarDatosDePruebaIndependiente(5L),
                generarDatosDePruebaIndependiente(6L)));
        caso2.setListaCliente(Arrays.asList(
                generarDatosDePruebaDependiente(5L),
                generarDatosDePruebaDependiente(6L)
        ));
        caso3.setListaCliente(Arrays.asList(
                        generarDatosDePruebaIndependiente(5L),
                        generarDatosDePruebaDependiente(6L)
                )
        );
        caso4.setListaCliente(Arrays.asList(
                        generarDatosDePruebaDependiente(5L),
                        generarDatosDePruebaIndependiente(6L)
                )
        );
        caso5.setListaCliente(Collections.singletonList(
                generarDatosDePruebaIndependiente(5L)
        ));
        caso6.setListaCliente(Collections.singletonList(
                generarDatosDePruebaDependiente(5L)
        ));
        casos.put("TitularIndependiente-ConyugeIndependiente", new Pair<>(
                caso1,
                "TitularIndependiente-ConyugeIndependiente.pdf"
        ));
        casos.put("TitularDependiente-ConyugeDependiente",
                new Pair<>(caso2, "TitularDependiente-ConyugeDependiente.pdf"));
        casos.put("TitularIndependiente-ConyugeDependiente",
                new Pair<>(caso3, "TitularIndependiente-ConyugeDependiente.pdf"));
        casos.put("TitularDependiente-ConyugeIndependiente",
                new Pair<>(caso4, "TitularDependiente-ConyugeIndependiente.pdf"));
        casos.put("SoloTitularIndependiente",
                new Pair<>(caso5, "SoloTitularIndependiente.pdf"));
        casos.put("SoloTitularDependiente",
                new Pair<>(caso6, "SoloTitularDependiente.pdf"));
        return casos;
    }

    private ClienteIndBean generarDatosDePruebaIndependiente(long cargo) {
        ClienteIndBean clienteBean = new ClienteIndBean();
        clienteBean.setCliCargo(BigDecimal.valueOf(cargo));
        clienteBean.setCliDocumentoTipoDescripcion("D.N.I.");
        clienteBean.setCliDocumentoNumero("73184532");
        clienteBean.setCliPaterno("Cahuas");
        clienteBean.setCliMaterno("Vergara");
        clienteBean.setCliNombre1("Miguel");
        clienteBean.setCliNombre2("Ángel");
        clienteBean.setCliFechaNacimiento("09/04/2003");
        clienteBean.setCliUbigeoNacimiento("140126");
        clienteBean.setCliSexo("F");
        clienteBean.setCliGradoInstruccion("UNIVERSITARIO");
        clienteBean.setCliOcupacion("Ingeniero de Software");
        clienteBean.setCliCorreo("mcahuas@zytrust.com");
        clienteBean.setCliCelular("921296833");
        clienteBean.setCliPaisNacimiento("Perú");

        clienteBean.setCliEstadoCivil("Soltero");
        clienteBean.setCliRazonSocial("Alguna Razon Social");
        clienteBean.setCliFichaRRPP("8999945");
        clienteBean.setCliCargarFamiliar(BigDecimal.valueOf(6800.00));
        clienteBean.setCliDomlResideDesde("05/10/2009");
        clienteBean.setCliTelefono("55588888");
        clienteBean.setCliCondicioVivienda("Limpia y amoblada");
        clienteBean.setCliDepartamento("Lima");
        clienteBean.setCliProvincia("Callao");
        clienteBean.setCliDistrito("Callao");
        clienteBean.setCliDireccion("Direccion de mi casa");
        clienteBean.setCliTipoDom("Domicilio");
        clienteBean.setCliReferencia("Referencia de mi casa");
        clienteBean.setCliCasaNegocio("S");

        // Titular Independiente
        clienteBean.setCliNegDepartamento("Lima");
        clienteBean.setCliNegProvincia("Lima");
        clienteBean.setCliNegDistrito("San Isidro");
        clienteBean.setCliNegDireccion("Direccion de mi negocio");
        clienteBean.setCliNegTipoDireccion("Edificio");
        clienteBean.setCliNegReferencia("Una referencia");
        clienteBean.setCliNegRUC("85945954455");
        clienteBean.setCliNegSectorEconomico("Finanzas");
        clienteBean.setCliNegCIIU(BigDecimal.valueOf(6209L));
        clienteBean.setCliNegActividadEconomica("Desarrollo de software");
        clienteBean.setCliNegIngresoMensual(BigDecimal.valueOf(20000.00));
        clienteBean.setCliNegTipoEstadoEstablecimiento("Nuevo");
        clienteBean.setCliNegInicioActividades("05/10/2018");
        clienteBean.setCliNegCondicionLocal("Amoblado");

        return clienteBean;
    }

    private ClienteIndBean generarDatosDePruebaDependiente(long cargo) {
        ClienteIndBean clienteBean = new ClienteIndBean();
        clienteBean.setCliCargo(BigDecimal.valueOf(cargo));
        clienteBean.setCliDocumentoTipoDescripcion("D.N.I.");
        clienteBean.setCliDocumentoNumero("73184532");
        clienteBean.setCliPaterno("Cahuas");
        clienteBean.setCliMaterno("Vergara");
        clienteBean.setCliNombre1("Miguel");
        clienteBean.setCliNombre2("Ángel");
        clienteBean.setCliFechaNacimiento("09/04/2003");
        clienteBean.setCliUbigeoNacimiento("140126");
        clienteBean.setCliSexo("F");
        clienteBean.setCliGradoInstruccion("Secundaria");
        clienteBean.setCliOcupacion("Ingeniero de Software");
        clienteBean.setCliCorreo("mcahuas@zytrust.com");
        clienteBean.setCliPaisNacimiento("Perú");

        clienteBean.setCliEstadoCivil("Soltero");
        clienteBean.setCliRazonSocial("Alguna Razon Social");
        clienteBean.setCliFichaRRPP("8999945");
        clienteBean.setCliCargarFamiliar(BigDecimal.valueOf(6800.00));
        clienteBean.setCliDomlResideDesde("05/10/2009");
        clienteBean.setCliTelefono("55588888");
        clienteBean.setCliCondicioVivienda("Limpia y amoblada");
        clienteBean.setCliDepartamento("Lima");
        clienteBean.setCliProvincia("Callao");
        clienteBean.setCliDistrito("Callao");
        clienteBean.setCliDireccion("Direccion de mi casa");
        clienteBean.setCliTipoDom("Domicilio");
        clienteBean.setCliReferencia("Referencia de mi casa");
        clienteBean.setCliCelular("921296833");

        // Titular Dependiente
        clienteBean.setCliCLabNombreEmpresa("ZyTrust");
        clienteBean.setCliCLabCargo("Ingeniero de Software Beginner");
        clienteBean.setCliCLabCondicion("Contrato de prácticas");
        clienteBean.setCliCLabSueldo(BigDecimal.valueOf(1075.00));
        clienteBean.setCliCLabFechaIngreso("06/02/2022");
        clienteBean.setCliCLabTelefono("921296833");
        clienteBean.setCliCLabDireccion("Av. Arenales - Lince");
        clienteBean.setCliCLabRUC("20512321357");
        clienteBean.setCliCLabSectorEconomico("Desarrollo de Soluciones Tecnológicas");
        clienteBean.setCliCLabCIIU(BigDecimal.valueOf(894787));
        clienteBean.setCliCLabActividadEconomica("Desarrollo de Software");

        return clienteBean;
    }
}
