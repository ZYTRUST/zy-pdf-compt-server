package com.zy.ws.mgpdf.service;

import com.zy.cpts.cto.lib.zy.contrato.compts.dto.*;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.SeguroOptativoBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.*;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.CreditoIndividualBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.SeguroDesgravamenBean;
import com.zy.lib.common.util.DecimalID;
import com.zy.lib.common.util.Tool;
import com.zy.lib.dtos.dto.formu.PlantillaDto;
import com.zy.lib.message.resource.ErroresEnum;
import com.zy.ws.mgpdf.conf.TrackExecutionTime;
import com.zy.ws.mgpdf.conf.ZyTException;
import com.zy.ws.mgpdf.util.Utilitario;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlantillaService {

    public static final String JASPER_PATH_COMPARTAMOS = "compartamos";

    @Getter
    @Setter
    @Value("${jasper.url}")
    private String rutaJasper;

    public Optional<byte[]> generarJasper(HashMap<String, Object> parametros, PlantillaDto plantillaDto){

        byte[] reportePdf;
        JasperPrint print;
        StringBuilder builder = new StringBuilder();
        builder.append(plantillaDto.getPlanRuta());
        builder.append(plantillaDto.getPlanNombreArchivo());

        try {
            log.info("====> " + plantillaDto.getPlanNombreArchivo());
            long endtime = System.currentTimeMillis();
            print = JasperFillManager.fillReport(builder.toString(), parametros, new JREmptyDataSource());
            long endtime1 = System.currentTimeMillis();
            log.info(plantillaDto.getPlanNombreArchivo() + " ==> " + "generarJasper.fillReport ruta:{},duration:{}",builder,(endtime1-endtime)/1000f);
            log.info(plantillaDto.getPlanNombreArchivo() + " ==> " + print.toString());
            reportePdf = JasperExportManager.exportReportToPdf(print);
            long endtime2 = System.currentTimeMillis();
            log.info(plantillaDto.getPlanNombreArchivo() + " ==> " + "generarJasper.exportReportToPdf ruta:{},duration:{}",builder,(endtime2-endtime1)/1000f);
            return Optional.of(reportePdf);
        } catch (Exception e) {
            log.info(plantillaDto.getPlanNombreArchivo() + " ===> " + "Error:{}",e.getMessage());
            throw new ZyTException(e, ErroresEnum.CTRTO_EXCEPTION_CONTRATO_PDF);
        }
    }

    /********************************* CREDITO GRUPAL ************************************************************/

    @TrackExecutionTime(operation = "CARTILLA_IDENTIFICACION")
    public Optional<byte[]> generarCartillaIdentificacion(CreditoGrupalBean creditoGrupalBean, PlantillaDto plantillaDto){
//                                                    PlantillaType plantillaType) {

//        String ruta = getRutaJasper(plantillaType);
        String ruta = plantillaDto.getPlanRuta();

        HashMap<String, Object> parametros = new HashMap<>();

        if (CollectionUtils.isNotEmpty(creditoGrupalBean.getListaGerentes())) {
            creditoGrupalBean.getListaGerentes()
                    .forEach(o -> {
                        if (o.getFirmanteFirma() != null) {
                            o.setFirmanteFirmaJasper(new ByteArrayInputStream(o.getFirmanteFirma()));
                        }
                    });
        }

        parametros.put("p_ruta", ruta);
        parametros.put("p_credGrupFechaVigencia", creditoGrupalBean.getCredGrupFechaVigencia());
        parametros.put("p_credGrupalVersion", creditoGrupalBean.getCredGrupalVersion());

        parametros.put("p_grupoNombre", creditoGrupalBean.getGrupoNombre());
        // String fecha = Utilitario.formatoFecha(creditoGrupalBean.getCredGrupFechaVigencia());
        String fecha = Utilitario.formatoFecha(creditoGrupalBean.getCredGrupFechaDesembolso());
        parametros.put("p_credGrupalFecha", fecha);

        parametros.put("l_listaCliente", creditoGrupalBean.getListaCliente());
        parametros.put("l_listaGerentes", creditoGrupalBean.getListaGerentes());

        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "ESTIMADO_CLIENTE")
    public Optional<byte[]> generarEstimadoCliente(CreditoGrupalBean creditoGrupalBean, PlantillaDto plantillaDto){
//                                                    PlantillaType plantillaType) {
//        String ruta = getRutaJasper(plantillaType);
        String ruta = plantillaDto.getPlanRuta();

        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("p_ruta", ruta);

        /*if(CollectionUtils.isNotEmpty(creditoGrupalBean.getListaCliente())){
            creditoGrupalBean.getListaCliente().removeIf(o -> o.getCliEmiteCartaSegDesgravamen().equals("N"));
        }*/
        parametros.put("p_credGrupalFecha", creditoGrupalBean.getCredGrupFechaDesembolso());

        parametros.put("l_listaCliente", creditoGrupalBean.getListaCliente());

        try {
            parametros.put("p_firma_tdp", Utilitario.getByteInputStream(ruta + "firma_tdp.jpg"));
            parametros.put("p_logo_movistar", Utilitario.getByteInputStream(ruta + "CompartamosLogo.png"));
            parametros.put("p_fecha", "15/01/2020");
        } catch (IOException e) {
            throw new ZyTException(e, ErroresEnum.FILE_SERVER_NO_FILE);
        }

        return generarJasper(parametros, plantillaDto);

    }

    @TrackExecutionTime(operation = "CONTRATO_PRESTAMO")
    public Optional<byte[]> generarcontratoPrestamo(CreditoGrupalBean creditoGrupalBean, PlantillaDto plantillaDto){
//                                                    PlantillaType plantillaType) {

//        String ruta = getRutaJasper(plantillaType);
        String ruta = plantillaDto.getPlanRuta();

        HashMap<String, Object> parametros;
        parametros = new HashMap<>();

        parametros.put("p_ruta", ruta);
        parametros.put("p_enlace", "www.compartamos.com.pe");
        parametros.put("p_credGrupFechaVigencia", creditoGrupalBean.getCredGrupFechaVigencia());
        parametros.put("p_credGrupalVersion", creditoGrupalBean.getCredGrupalVersion());

        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "REGLAS_DE_GRUPO")
    public Optional<byte[]> generarReglasdeGrupo(CreditoGrupalBean creditoGrupalBean,PlantillaDto plantillaDto){
//                                                    PlantillaType plantillaType) {
//        String ruta = getRutaJasper(plantillaType);
        String ruta = plantillaDto.getPlanRuta();

        HashMap<String, Object> parametros;
        parametros = new HashMap<>();

        parametros.put("p_ruta", ruta);
        parametros.put("p_MontoMinimoAhorroValor", String.format("%.2f", creditoGrupalBean.getCredGrupAhorroMinimoMonto()));
        parametros.put("p_MontoMinimoAhorroDesc", creditoGrupalBean.getCredGrupAhorroMinimoLetras());
        parametros.put("p_ReunionDia", creditoGrupalBean.getCredGrupReunionDia());
        parametros.put("p_HoraReunion", creditoGrupalBean.getCredGrupReunionHora());
        parametros.put("p_PersonaReunion", creditoGrupalBean.getCredGrupReunioncliCasa());
        parametros.put("p_UbicacionReunion", creditoGrupalBean.getCredGrupReunionDireccion());
        parametros.put("p_MontoMultaTardanzaValor", String.format("%.2f", creditoGrupalBean.getCredGrupReunionMultaTardanzaMonto()));
        parametros.put("p_MontoMultaFaltaValor", String.format("%.2f", creditoGrupalBean.getCredGrupReunionMultaFaltaMonto()));
        parametros.put("p_MontoMultaTardanzaDesc", creditoGrupalBean.getCredGrupReunionMultaTardanzaLetras());
        parametros.put("p_MontoMultaFaltaDesc", creditoGrupalBean.getCredGrupReunionMultaFaltaLetras());

        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "HOJA_RESUMEN")
    public Optional<byte[]> generarHojaResumen(CreditoGrupalBean creditoGrupalBean,PlantillaDto plantillaDto){
//                                                    PlantillaType plantillaType) {
//        String ruta = getRutaJasper(plantillaType);
        String ruta = plantillaDto.getPlanRuta();

        HashMap<String, Object> parametros = new HashMap<>();

        if (CollectionUtils.isNotEmpty(creditoGrupalBean.getListaGerentes())) {
            creditoGrupalBean.getListaGerentes()
                    .forEach(o -> {
                        if (o.getFirmanteFirma() != null) {
                            o.setFirmanteFirmaJasper(new ByteArrayInputStream(o.getFirmanteFirma()));
                        }
                    });
        }

        parametros.put("p_ruta", ruta);
        parametros.put("credGrupFechaVigencia", creditoGrupalBean.getCredGrupFechaVigencia());
        parametros.put("credGrupalVersion", creditoGrupalBean.getCredGrupalVersion());
        parametros.put("grupoCodigo", creditoGrupalBean.getCodigoOperacion());
        parametros.put("grupoNombre", creditoGrupalBean.getGrupoNombre());
        parametros.put("grupoCiclo", creditoGrupalBean.getGrupoCiclo());
        parametros.put("credGrupMonedaSimbolo", creditoGrupalBean.getCredGrupMonedaSimbolo());
        parametros.put("credGrupMontoDesembolso", creditoGrupalBean.getCredGrupMontoDesembolso());
        parametros.put("credGrupFechaDesembolso", creditoGrupalBean.getCredGrupFechaDesembolso());
        parametros.put("credGrupFrecuenciaPagoCantidad", creditoGrupalBean.getCredGrupFrecuenciaPagoCantidad());
        parametros.put("credGrupPlazoPrestamoCantidad", creditoGrupalBean.getCredGrupPlazoPrestamoCantidad());
        parametros.put("credGrupTEA360", creditoGrupalBean.getCredGrupTEA360());
        parametros.put("credGrupTCEA", creditoGrupalBean.getCredGrupTCEA());
        parametros.put("credGrupMontoAmpliacion", creditoGrupalBean.getCredGrupMontoAmpliacion());
        parametros.put("credGrupTasaMoraNominalAnual", creditoGrupalBean.getCredGrupTasaMoraNominalAnual());

        parametros.put("listaCronogramaGrupal", creditoGrupalBean.getListaCronogramaGrupal());
        BigDecimal montoTotal = BigDecimal.ZERO;
        for (int i = 1; i < creditoGrupalBean.getListaCronogramaGrupal().size(); i++) {
            montoTotal = montoTotal.add(creditoGrupalBean.getListaCronogramaGrupal().get(i).getCuoGrupMontoCapital());
        }
        parametros.put("p_listaCronogramaCapital", montoTotal);
        parametros.put("listaCreditoGasto", creditoGrupalBean.getListaCreditoGasto());
        parametros.put("listaCreditoComisiones", creditoGrupalBean.getListaCreditoComisiones());

        parametros.put("credGrupFormaDesembolso", creditoGrupalBean.getCredGrupFormaDesembolso());
        parametros.put("credGrupCuentaDesembolso", creditoGrupalBean.getCredGrupCuentaDesembolso());
        parametros.put("credGrupFormaContratacion", creditoGrupalBean.getCredGrupFormaContratacion());
        parametros.put("credGrupFormaEnvioDocContractual", creditoGrupalBean.getCredGrupFormaEnvioDocContractual());
        parametros.put("credGrupFormaRemisionInformacion", creditoGrupalBean.getCredGrupFormaRemisionInformacion());
        parametros.put("credGrupFormaPagosAnticipadosParciales", creditoGrupalBean.getCredGrupFormaPagosAnticipadosParciales());

        parametros.put("credGrupTipoSeguro", creditoGrupalBean.getCredGrupTipoSeguro());
        parametros.put("credGrupMontoPrima", creditoGrupalBean.getCredGrupMontoPrima());
        parametros.put("credGrupRucCompania", creditoGrupalBean.getCredGrupRucCompania());
        parametros.put("credGrupNombreCompaniaSeguro", creditoGrupalBean.getCredGrupNombreCompaniaSeguro());
        parametros.put("credGrupNumPoliza", creditoGrupalBean.getCredGrupNumPoliza());

        parametros.put("listaFirmantes", creditoGrupalBean.getListaFirmantes());
        parametros.put("listaGerentes", creditoGrupalBean.getListaGerentes());

        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "TRATAMIENTO_DATOS")
    public Optional<byte[]> generarTratamientoDatos(CreditoGrupalBean creditoGrupalBean, PlantillaDto plantillaDto){
//                                                    PlantillaType plantillaType) {

//        String ruta = getRutaJasper(plantillaType);
        String ruta = plantillaDto.getPlanRuta();

        HashMap<String, Object> parametros = new HashMap<>();

        parametros.put("p_ruta", ruta);

        parametros.put("l_listaCliente", creditoGrupalBean.getListaCliente());

        parametros.put("p_fechaDesembolso", creditoGrupalBean.getCredGrupFechaDesembolso());

        try {
            //parametros.put("p_firma_tdp", Utilitario.getByteInputStream(ruta + "firma_tdp.jpg"));
            parametros.put("p_logo_movistar", Utilitario.getByteInputStream(ruta + "CompartamosLogo.png"));
        } catch (IOException e) {
            throw new ZyTException(e, ErroresEnum.FILE_SERVER_NO_FILE);
        }

        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "POLIZA_GRUPO")
    public Optional<byte[]> generarPolizaGrupo(CreditoGrupalBean creditoGrupalBean, PlantillaDto plantillaDto){
//                                                    PlantillaType plantillaType) {

//        String ruta = getRutaJasper(plantillaType);
        //log.info("1.inicia operacion {} {}","POLIZA GRUPO",plantillaDto.getPlanRuta());
        String ruta = plantillaDto.getPlanRuta();
        HashMap<String, Object> parametros;
        parametros = new HashMap<>();

        //Config para el formateo de los numeros (pattern: #,##0.00)
        final NumberFormat nf = NumberFormat.getNumberInstance(Locale.ENGLISH);
        nf.setMaximumFractionDigits(2);
        nf.setMinimumFractionDigits(2);
        nf.setGroupingUsed(true);

        DecimalFormat df = new DecimalFormat("#,##0.00");
        BigDecimal bd = new BigDecimal(10200);
        System.out.println(df.format(bd));
//        Date date = new Date();
//        SimpleDateFormat DateFor = new SimpleDateFormat("dd/MM/yyyy");
//        String stringDate= DateFor.format(date);

        parametros.put("p_ruta", ruta);
        //parametros.put("p_fecha", stringDate);
        parametros.put("p_fecha", creditoGrupalBean.getCredGrupFechaDesembolso());

        if (CollectionUtils.isNotEmpty(creditoGrupalBean.getListaCliente())) {
            creditoGrupalBean.getListaCliente().removeIf(o -> o.getCliEmiteSegDesgravamen().equals("N"));

            for (int i = 0; i < creditoGrupalBean.getListaCliente().size(); i++) {
                //String formattedNumber = nf.format(creditoGrupalBean.getListaCliente().get(i).getCreditoIndividual().getCredIndDesembolsoMonto());
                String formattedNumber = nf.format(creditoGrupalBean.getListaCliente().get(i).getCreditoIndividual().getCredIndPrestamoSinSegMonto());
                String formattedNumber2 = nf.format(creditoGrupalBean.getListaCliente().get(i).getCreditoIndividual().getCredIndRangoDesembolso());
                creditoGrupalBean.getListaCliente().get(i).getCreditoIndividual().setCredIndPrestamoSinSegMontoFormatted(formattedNumber);
                creditoGrupalBean.getListaCliente().get(i).getCreditoIndividual().setCredIndRangoDesembolsoFormatted(formattedNumber2);
            }
        }
        //log.info("2.inicia operacion {} {}","POLIZA GRUPO",plantillaDto.getPlanRuta());

        parametros.put("l_listaCliente", creditoGrupalBean.getListaCliente());

        try {
            parametros.put("p_firma_testigo", Utilitario.getByteInputStream(ruta + "firma_tdp_1.jpg"));
            parametros.put("p_firma_asegurado", Utilitario.getByteInputStream(ruta + "firma_tdp.jpg"));
            parametros.put("p_firma_director", Utilitario.getByteInputStream(ruta + "firma_director.png"));
            parametros.put("p_logo_movistar", Utilitario.getByteInputStream(ruta + "mapfre_logo.png"));
        } catch (IOException e) {
            throw new ZyTException(e, ErroresEnum.FILE_SERVER_NO_FILE);
        }
        //log.info("3.inicia operacion {} {}","POLIZA GRUPO",plantillaDto.getPlanRuta());

        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "CONDICIONES_ESPECIFICAS")
    public Optional<byte[]> generarCondicionesEspecificas(CreditoGrupalBean creditoGrupalBean, PlantillaDto plantillaDto){
//                                                    PlantillaType plantillaType) {
//        String ruta = getRutaJasper(plantillaType);
        String ruta = plantillaDto.getPlanRuta();

        HashMap<String, Object> parametros = new HashMap<>();

        parametros.put("p_ruta", ruta);

        parametros.put("p_grupoNombre", creditoGrupalBean.getGrupoNombre());
        parametros.put("p_codigoOperacion", creditoGrupalBean.getCodigoOperacion());
        parametros.put("l_listaCliente", creditoGrupalBean.getListaCliente());

        try {
            parametros.put("p_logo_movistar", Utilitario.getByteInputStream(ruta + "LogoCompartamos.jpg"));
        } catch (IOException e) {
            throw new ZyTException(e, ErroresEnum.FILE_SERVER_NO_FILE);
        }

        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "SEGURO_OPTATIVO")
    public Optional<byte[]> generarSeguroOptativo(CreditoGrupalBean creditoGrupalBean, PlantillaDto plantillaDto){
//                                                    PlantillaType plantillaType) {
//        String ruta = getRutaJasper(plantillaType);
        String ruta = plantillaDto.getPlanRuta();
        HashMap<String, Object> parametros;
        parametros = new HashMap<>();
        final ClienteBean clienteBean = creditoGrupalBean.getListaCliente().get(0);
        final com.zy.cpts.cto.lib.zy.contrato.compts.dto.SeguroDesgravamenBean seguroDesgravamenBean = clienteBean.getSegDesgravamen();

        final List<SeguroOptativoBean>  listaSeguroOptativo = clienteBean.getListaSegOptativo();
        if(listaSeguroOptativo.size() != 1) {
            log.error("Se esperaba que la lista seguro optativo tuviera 1 elemento, se encontró {}", listaSeguroOptativo.size());
            return Optional.empty();
        }

        final List<com.zy.cpts.cto.lib.zy.contrato.compts.dto.BeneficiarioSeguroOptativoBean> listaBeneficiarios =  listaSeguroOptativo.get(0).getListaBeneficiarioSegOptativo();

        parametros.put("p_ruta", ruta);
        parametros.put("p_fecha", "15/01/2020");


        parametros.put("p_Nombres", clienteBean.getCliNombre1() + (clienteBean.getCliNombre2() == null ? "": " " + clienteBean.getCliNombre2()));
        parametros.put("p_Apellido_Paterno", clienteBean.getCliPaterno());
        parametros.put("p_Apellido_Materno", clienteBean.getCliMaterno());
        parametros.put("p_NumDocIdentidad", clienteBean.getCliDocumentoNumero());
        parametros.put("p_FechaNacimiento", clienteBean.getCliFechaNacimiento());
        parametros.put("p_Sexo", clienteBean.getCliSexo());
        parametros.put("p_Direccion", clienteBean.getCliDireccion());
        parametros.put("p_Provincia", clienteBean.getCliProvincia());
        parametros.put("p_Departamento", clienteBean.getCliDepartamento());
        parametros.put("p_Telf_Cel", clienteBean.getCliCelular());
        parametros.put("p_Correo", clienteBean.getCliCorreo());

        final SeguroOptativoBean  seguroOptativoBean = listaSeguroOptativo.get(0);

        parametros.put("p_Plan_Seguro", seguroOptativoBean.getSegOptativoPlanComercialPrima());
        parametros.put("p_N_Certificado", !Tool.isNullOrEmpty(seguroOptativoBean.getSegOptativoCertificado())?seguroOptativoBean.getSegOptativoCertificado().toString() : " " );

        parametros.put("p_listaBeneficiarios", listaBeneficiarios);

        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "DECLARACION_JURADA")
    public Optional<byte[]> generaDeclaracionJurada(CreditoGrupalBean creditoGrupalBean, PlantillaDto plantillaDto){

        final com.zy.cpts.cto.lib.zy.contrato.compts.dto.ClienteBean clienteBean = creditoGrupalBean.getListaCliente().get(0);
        final String rutaJasper = plantillaDto.getPlanRuta();
//        final com.zy.cpts.cto.lib.zy.contrato.compts.dto.SeguroDesgravamenBean seguroDesgravamenBean = clienteBean.getSegDesgravamen();

        final List<SeguroOptativoBean>  listaSeguroOptativo = clienteBean.getListaSegOptativo();
        if(listaSeguroOptativo.size() != 1) {
            log.error("Se esperaba que la lista seguro optativo tuviera 1 elemento, se encontró {}", listaSeguroOptativo.size());
            return Optional.empty();
        }

        final HashMap<String, Object> parametros = new HashMap<>();

        parametros.put("p_ruta", rutaJasper);
        parametros.put("p_fecha", "15/01/2020");

        parametros.put("p_Nombres", clienteBean.getCliNombre1() + (clienteBean.getCliNombre2() == null ? "": " " + clienteBean.getCliNombre2()));
        parametros.put("p_Apellido_Paterno", clienteBean.getCliPaterno());
        parametros.put("p_Apellido_Materno", clienteBean.getCliMaterno());
        parametros.put("p_Tipo_Doc", clienteBean.getCliDocumentoTipoDescripcion());
        parametros.put("p_NumDocIdentidad", clienteBean.getCliDocumentoNumero());
        parametros.put("p_Direccion", clienteBean.getCliDireccion());
        parametros.put("p_deNExterior", clienteBean.getSegOptativoDomNExterior());
        parametros.put("p_deUrb", clienteBean.getSegOptativoDomUrb());
        parametros.put("p_deDistrito", clienteBean.getCliDistrito());
        parametros.put("p_Provincia", clienteBean.getCliProvincia());
        parametros.put("p_Departamento", clienteBean.getCliDepartamento());

        parametros.put("p_deTipoDeUso", clienteBean.getSegOptativoDomTipoUso());
        parametros.put("p_deGiro", clienteBean.getCliActivNom());
        parametros.put("p_deOcupacion", clienteBean.getCliOcupacion());

        parametros.put("p_deAnioConstruccion",!Tool.isNullOrEmpty(clienteBean.getSegOptativoDomAnoConst())? clienteBean.getSegOptativoDomAnoConst().toString() : " ");
        parametros.put("p_deNroPisos",!Tool.isNullOrEmpty(clienteBean.getSegOptativoDomPisos())? clienteBean.getSegOptativoDomPisos().toString() : " ");
        parametros.put("p_deNroSotanos",!Tool.isNullOrEmpty(clienteBean.getSegOptativoDomSotanos())? clienteBean.getSegOptativoDomSotanos().toString() : " ");

        parametros.put("p_deTipoMaterialConstruccion", clienteBean.getSegOptativoDomMaterial());

        parametros.put("p_listaContenido", listaSeguroOptativo.get(0).getListaContenido());

        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "CRONOGRAMA_PAGOS")
    public Optional<byte[]> generarCronogramaPagos(CreditoGrupalBean creditoGrupalBean, PlantillaDto plantillaDto){
//                                                    PlantillaType plantillaType) {

//        String ruta = getRutaJasper(plantillaType);
        String ruta = plantillaDto.getPlanRuta();

        HashMap<String, Object> parametros;
        parametros = new HashMap<>();

        parametros.put("p_ruta", ruta);
        parametros.put("p_fecha", "15/01/2020");

        parametros.put("grupoCodigo", creditoGrupalBean.getGrupoCodigo());

        parametros.put("l_listaCliente", creditoGrupalBean.getListaCliente());

        return generarJasper(parametros, plantillaDto);

    }

    /********************************* CREDITO INDIVIDUAL ************************************************************/

    @TrackExecutionTime(operation = "SEGURO_DESGRAVAMEN_CON_DEVOLUCION_CI")
    public Optional<byte[]> generarPlantillaCertificadoSeguroDesgravamenDevolucion(CreditoIndivGlobalBean creditoIndivGlobalBean, PlantillaDto plantillaDto){
        final ClienteIndBean clienteBean = creditoIndivGlobalBean.getListaCliente().get(0);
        final CreditoIndividualBean creditoIndividualBean = clienteBean.getCreditoIndividual();
        final SeguroDesgravamenBean seguroDesgravamenBean = clienteBean.getSegDesgravamen();
        final String rutaJasper = plantillaDto.getPlanRuta();
        final HashMap<String, Object> parametros = new HashMap<>();

        final List<ClienteIndBean> resultadoBusquedaConyuge = creditoIndivGlobalBean.getListaCliente()
                .stream().filter(cliente -> cliente.getCliCargo().intValueExact() == 6)
                .collect(Collectors.toList());
        if(resultadoBusquedaConyuge.size() > 1) {
            log.error("No se encontraron conyuges en la lista de clientes, se esperaban solo 1 conyuge o ninguno, se encontró: {}", resultadoBusquedaConyuge.size());
            return Optional.empty();
        }

        final ClienteIndBean conyugeBean = resultadoBusquedaConyuge.isEmpty() ? null : resultadoBusquedaConyuge.get(0);

        parametros.put("p_N_Certificado", !Tool.isNullOrEmpty(seguroDesgravamenBean.getSegDesgraCertificado())?seguroDesgravamenBean.getSegDesgraCertificado().toString() : " " );
        parametros.put("p_Poliza", seguroDesgravamenBean.getSegDesgraPolizaGrup());
        parametros.put("p_Nombres", clienteBean.getCliNombre1() + (clienteBean.getCliNombre2() == null ? "": " " + clienteBean.getCliNombre2()));
        parametros.put("p_Apellido_Paterno", clienteBean.getCliPaterno());
        parametros.put("p_Apellido_Materno", clienteBean.getCliMaterno());
        parametros.put("p_NumDocIdentidad", clienteBean.getCliDocumentoNumero());
        parametros.put("p_FechaNacimiento", clienteBean.getCliFechaNacimiento());
        parametros.put("p_Sexo", clienteBean.getCliSexo());
        parametros.put("p_Direccion", clienteBean.getCliDireccion());
        parametros.put("p_Provincia", clienteBean.getCliProvincia());
        parametros.put("p_Departamento", clienteBean.getCliDepartamento());
        parametros.put("p_Telf_Cel", clienteBean.getCliCelular());
        parametros.put("p_Correo", clienteBean.getCliCorreo());
        parametros.put("p_Ocupacion", clienteBean.getCliOcupacion());

        parametros.put("p_Moneda", creditoIndividualBean.getCredIndMonedaDescripcion());
        parametros.put("p_Meses_Prestamo", !Tool.isNullOrEmpty(creditoIndividualBean.getCredIndPlazoPrestamoCantidad()) ? creditoIndividualBean.getCredIndPlazoPrestamoCantidad().toString() : "");
        parametros.put("p_Importe_Prestamo", !Tool.isNullOrEmpty(creditoIndividualBean.getCredIndDesembolsoMonto()) ? creditoIndividualBean.getCredIndDesembolsoMonto().toString() : "");
        parametros.put("p_Tipo_Prestamo", creditoIndividualBean.getCredIndPrestamoTipo());

        List<PrimaDesgravamenBean> listaPrimaDesgravamen = clienteBean.getSegDesgravamen().getListaPrimaDesgravamen();
        if(Tool.isNullOrEmpty(listaPrimaDesgravamen)) {
            listaPrimaDesgravamen = Collections.emptyList();
        }
        if(listaPrimaDesgravamen.size() > 6) {
            log.error("Se esperaba que listaPrimaDesgravamen tuviera 6 elementos o menos, se encontró {}", listaPrimaDesgravamen.size());
            return Optional.empty();
        }
        int indicePorcentajes = 1;
        int indiceCargos = 1;
        int valorAumentado = 2;
        for (PrimaDesgravamenBean primaDesgravamenBean : listaPrimaDesgravamen) {
            if (indicePorcentajes == 7)  {
                indicePorcentajes = 9;
                valorAumentado = 1;
            }
            parametros.put("p_Cargos" + indiceCargos, primaDesgravamenBean.getSegDesgraTasaComision());
            parametros.put("p_Porcentaje" + indicePorcentajes, primaDesgravamenBean.getSegDesgraTasaComercial());
            indicePorcentajes += valorAumentado;
            ++indiceCargos;
        }

        parametros.put("p_Viven", seguroDesgravamenBean.getSegDesgraTituloCobertura());
        parametros.put("p_Sobre", seguroDesgravamenBean.getSegDesgraDefinicionCobertura());
        parametros.put("p_Devolucion", seguroDesgravamenBean.getSegDesgraSumaAsegurada());

        List<SumaAsegDesgravamenBean> sumasMaxAseg = clienteBean.getSegDesgravamen().getListaSumaAsegDesgra();

        if(sumasMaxAseg.size() > 2) {
            log.error("La lista segDesgraSumMaxAseg debe tener a lo mucho 2 valores, la ingresada tiene {}", sumasMaxAseg.size());
            return Optional.empty();
        }

        parametros.put("p_Suma_Asegurada_Max_Por_Asegurado_1", sumasMaxAseg.size() >= 1 ? sumasMaxAseg.get(0).getSegDesgraSumMaxAseg() : "");
        parametros.put("p_Suma_Asegurada_Max_Por_Asegurado_2", sumasMaxAseg.size() == 2 ? sumasMaxAseg.get(1).getSegDesgraSumMaxAseg() : "");
        parametros.put("p_Fecha_Solicitud", creditoIndividualBean.getCredIndDesembolsoFecha());

        if(Tool.isNullOrEmpty(conyugeBean)) {
            return generarJasper(parametros,plantillaDto);
        }
        // Conyuge
        //parametros.put("p_TipoDocIdentidad_Conyuge", tipoDocIdentidadConyuge);
        parametros.put("p_NumDocIdentidad_Conyuge", conyugeBean.getCliDocumentoNumero());
        parametros.put("p_RUC_Conyuge", "");
        parametros.put("p_ApellidoPaterno_Conyuge", conyugeBean.getCliPaterno());
        parametros.put("p_ApellidoMaterno_Conyuge", conyugeBean.getCliMaterno());
        parametros.put("p_Nombres_Conyuge", conyugeBean.getCliNombre1() + " " + (conyugeBean.getCliNombre2() != null? conyugeBean.getCliNombre2(): ""));
        parametros.put("p_FechaNacimiento_Conyuge", conyugeBean.getCliFechaNacimiento());
        parametros.put("p_Sexo_Conyuge", conyugeBean.getCliSexo());
        parametros.put("p_Ocupacion_Conyuge", conyugeBean.getCliOcupacion());
        parametros.put("p_Email_Conyuge", conyugeBean.getCliCorreo());
        parametros.put("p_Departamento_Conyuge", conyugeBean.getCliDepartamento());
        parametros.put("p_Provincia_Conyuge", conyugeBean.getCliProvincia());
        parametros.put("p_Direccion_Conyuge", conyugeBean.getCliDireccion());
        parametros.put("p_Telefono_Conyuge", conyugeBean.getCliCelular());


        log.trace("Se genera el jasper de CERTIFICADO_SEGURO_DESGRAVAMEN_DEVOLUCION");
        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "SEGURO_DESGRAVAMEN_MENSUAL_CI")
    public Optional<byte[]> generarPlantillaCertificadoSeguroDesgravamenMensual(CreditoIndivGlobalBean creditoIndivGlobalBean, PlantillaDto plantillaDto){
        final ClienteIndBean clienteBean = creditoIndivGlobalBean.getListaCliente().get(0);
        final CreditoIndividualBean creditoIndividualBean = clienteBean.getCreditoIndividual();
        final SeguroDesgravamenBean seguroDesgravamenBean = clienteBean.getSegDesgravamen();
        final String rutaJasper = plantillaDto.getPlanRuta();
        final HashMap<String, Object> parametros = new HashMap<>();

        parametros.put("p_segDesgraCertificado", !Tool.isNullOrEmpty(seguroDesgravamenBean.getSegDesgraCertificado())?seguroDesgravamenBean.getSegDesgraCertificado().toString() : " " );
        parametros.put("p_cliPoliza", seguroDesgravamenBean.getSegDesgraPolizaGrup());
        parametros.put("p_cliNombres", clienteBean.getCliNombre1() + (clienteBean.getCliNombre2() == null ? "": " " + clienteBean.getCliNombre2()));
        parametros.put("p_cliPaterno", clienteBean.getCliPaterno());
        parametros.put("p_cliMaterno", clienteBean.getCliMaterno());
        parametros.put("p_cliDocumentoNumero", clienteBean.getCliDocumentoNumero());
        parametros.put("p_cliFechaNacimiento", !Tool.isNullOrEmpty(clienteBean.getCliFechaNacimiento()) ? clienteBean.getCliFechaNacimiento() : "");
        parametros.put("p_cliSexo", !Tool.isNullOrEmpty(clienteBean.getCliSexo()) ? clienteBean.getCliSexo() : "");
        parametros.put("p_cliDireccion", clienteBean.getCliDireccion());
        parametros.put("p_cliProvincia", !Tool.isNullOrEmpty(clienteBean.getCliProvincia()) ? clienteBean.getCliProvincia() : "");
        parametros.put("p_cliDepartamento", !Tool.isNullOrEmpty(clienteBean.getCliDepartamento()) ? clienteBean.getCliDepartamento() : "");
        parametros.put("p_cliCelular", clienteBean.getCliCelular());
        parametros.put("p_cliCorreo", !Tool.isNullOrEmpty(clienteBean.getCliCorreo()) ? clienteBean.getCliCorreo() : "");
        parametros.put("p_cliOcupacion", !Tool.isNullOrEmpty(clienteBean.getCliOcupacion()) ? clienteBean.getCliOcupacion() : "");
        parametros.put("p_seguroDesgravamen", seguroDesgravamenBean);
        parametros.put("p_creditoIndividual", creditoIndividualBean);
        parametros.put("p_ruta", rutaJasper);
        parametros.put("p_credIndMonedaSimbolo", !Tool.isNullOrEmpty(creditoIndividualBean.getCredIndMonedaDescripcion()) ? creditoIndividualBean.getCredIndMonedaDescripcion() : "");

        //parametros.put("p_segDesgraTiempoCoberturaCantidad", !Tool.isNullOrEmpty(seguroDesgravamenBean.getSegDesgraTiempoCoberturaCantidad()) ? seguroDesgravamenBean.getSegDesgraTiempoCoberturaCantidad() : "");
        //no existe en la trama, es de credito grupal
        parametros.put("p_segDesgraTiempoCoberturaCantidad", !Tool.isNullOrEmpty(creditoIndividualBean.getCredIndPlazoPrestamoCantidad()) ? creditoIndividualBean.getCredIndPlazoPrestamoCantidad().toString() : "");

        //se uso el mas parecido
        parametros.put("p_credIndPrestamoSinSegMontoFormatted", !Tool.isNullOrEmpty(creditoIndividualBean.getCredIndDesembolsoMonto()) ? creditoIndividualBean.getCredIndDesembolsoMonto().toString() : "");
        parametros.put("p_credIndPrestamoTipo", !Tool.isNullOrEmpty(creditoIndividualBean.getCredIndPrestamoTipo()) ? creditoIndividualBean.getCredIndPrestamoTipo() : "");
//        parametros.put("p_segDesgraTasaComercial", !Tool.isNullOrEmpty(seguroDesgravamenBean.getSegDesgraTasaComercial()) ? seguroDesgravamenBean.getSegDesgraTasaComercial() : "");
//        parametros.put("p_segDesgraTasaComision", !Tool.isNullOrEmpty(seguroDesgravamenBean.getSegDesgraTasaComision()) ? seguroDesgravamenBean.getSegDesgraTasaComision() : "");
        parametros.put("p_credIndDesembolsoFecha", !Tool.isNullOrEmpty(creditoIndividualBean.getCredIndDesembolsoFecha()) ? creditoIndividualBean.getCredIndDesembolsoFecha() : "");

        List<PrimaDesgravamenBean> listaPrimaDesgravamen = clienteBean.getSegDesgravamen().getListaPrimaDesgravamen();
        parametros.put("p_segDesgraTasaComercial", listaPrimaDesgravamen.size() >= 1 ? listaPrimaDesgravamen.get(0).getSegDesgraTasaComercial() : "");
        parametros.put("p_segDesgraTasaComision", listaPrimaDesgravamen.size() >= 1 ? listaPrimaDesgravamen.get(0).getSegDesgraTasaComision() : "");


        List<SumaAsegDesgravamenBean> sumasMaxAseg = clienteBean.getSegDesgravamen().getListaSumaAsegDesgra();

        if(sumasMaxAseg.size() > 2) {
            log.error("La lista segDesgraSumMaxAseg debe tener a lo mucho 2 valores, la ingresada tiene {}", sumasMaxAseg.size());
            return Optional.empty();
        }

        parametros.put("p_segDesgraSumMaxAseg", sumasMaxAseg.size() >= 1 ? sumasMaxAseg.get(0).getSegDesgraSumMaxAseg() : "");
        parametros.put("p_credIndDesembolsoFecha", !Tool.isNullOrEmpty(creditoIndividualBean.getCredIndDesembolsoFecha()) ? creditoIndividualBean.getCredIndDesembolsoFecha() : "");

        log.trace("Se genera el jasper de CERTIFICADO_SEGURO_DESGRAVAMEN_MENSUAL");
        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "CONTRATO_DE_PRESTAMO")
    public Optional<byte[]> generarPlantillaContraLineaCreditoPrestamo(CreditoIndivGlobalBean creditoIndivGlobalBean, PlantillaDto plantillaDto){
        final HashMap<String, Object> parametros = new HashMap<>();
        log.trace("Se genera el jasper de CONTRATO_DE_PRESTAMO");
        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "HOJA_RESUMEN_LINEA_CREDITO")
    public Optional<byte[]> generarPlantillaHojaResumenLineaCredito(CreditoIndivGlobalBean creditoIndivGlobalBean, PlantillaDto plantillaDto){
        final HashMap<String, Object> parametros = new HashMap<>();
        final ClienteIndBean clienteBean = creditoIndivGlobalBean.getListaCliente().get(0);
        final CreditoIndividualBean creditoIndividualBean = clienteBean.getCreditoIndividual();

        final List<ClienteIndBean> resultadoBusquedaTitular = creditoIndivGlobalBean.getListaCliente()
                .stream().filter(cliente -> cliente.getCliCargo().intValueExact() == 5)
                .collect(Collectors.toList());

        final List<ClienteIndBean> resultadoBusquedaConyuge = creditoIndivGlobalBean.getListaCliente()
                .stream().filter(cliente -> cliente.getCliCargo().intValueExact() == 6)
                .collect(Collectors.toList());

        final List<ClienteIndBean> resultadoBusquedaFiadores = creditoIndivGlobalBean.getListaCliente()
                .stream().filter(cliente -> cliente.getCliCargo().intValueExact() == 7)
                .collect(Collectors.toList());

        if(resultadoBusquedaTitular.size() != 1) {
            log.error("No se encontraron conyuges en la lista de clientes, se esperaban solo 1 cónyuge, se encontró: {}", resultadoBusquedaConyuge.size());
            return Optional.empty();
        }

        if(resultadoBusquedaConyuge.size() > 1) {
            log.error("No se encontraron conyuges en la lista de clientes, se esperaban solo 1 titular, se encontró: {}", resultadoBusquedaTitular.size());
            return Optional.empty();
        }

        final ClienteIndBean titularBean = resultadoBusquedaTitular.get(0);
        final ClienteIndBean conyugeBean = resultadoBusquedaConyuge.isEmpty() ? null : resultadoBusquedaConyuge.get(0);


        parametros.put("p_NombrePrestatario",  titularBean.getCliPaterno() + (Tool.isNullOrEmpty(titularBean.getCliMaterno())?", "  : " "+ titularBean.getCliMaterno() + ", ") + titularBean.getCliNombre1() + (titularBean.getCliNombre2() == null ? "": " " + titularBean.getCliNombre2()));

        if(!Tool.isNullOrEmpty(conyugeBean)){
            parametros.put("p_NombrePrestatarioConyuge",  conyugeBean.getCliPaterno() + (Tool.isNullOrEmpty(conyugeBean.getCliMaterno())?", "  : " "+ conyugeBean.getCliMaterno() + ", ") + conyugeBean.getCliNombre1() + (conyugeBean.getCliNombre2() == null ? "": " " + conyugeBean.getCliNombre2()));
            parametros.put("p_FirmanteNombres2",  conyugeBean.getCliPaterno() + (Tool.isNullOrEmpty(conyugeBean.getCliMaterno())?", "  : " "+ conyugeBean.getCliMaterno() + ", ") + conyugeBean.getCliNombre1() + (conyugeBean.getCliNombre2() == null ? "": " " + conyugeBean.getCliNombre2()));
            parametros.put("p_FirmanteDoi2",  conyugeBean.getCliDocumentoNumero());
            parametros.put("p_FirmanteDireccion2",  conyugeBean.getCliDireccion());
            parametros.put("p_FirmanteTelefono2",  conyugeBean.getCliCelular());
            parametros.put("p_FirmanteEmail2",  conyugeBean.getCliCorreo());
        }else{
            parametros.put("p_NombrePrestatarioConyuge", " ");
            parametros.put("p_FirmanteNombres2",  " ");
            parametros.put("p_FirmanteDoi2",  " ");
            parametros.put("p_FirmanteDireccion2",  " ");
            parametros.put("p_FirmanteTelefono2",  " ");
            parametros.put("p_FirmanteEmail2",  " ");
        }

        parametros.put("p_CliCodigo", clienteBean.getCliCodigo());
        parametros.put("p_MonedaMonto", creditoIndividualBean.getCredIndlMonedaSimbolo() + (creditoIndividualBean.getCredIndDesembolsoMonto() == null ? "": " " + creditoIndividualBean.getCredIndDesembolsoMonto().toString()));
        parametros.put("p_FechaContratacion", creditoIndividualBean.getCredIndDesembolsoFecha());
        parametros.put("p_CantCuotas", creditoIndividualBean.getCredIndPlazoPrestamoCantidad());
        parametros.put("p_TeaTasa", creditoIndividualBean.getCredIndTEA360());
        parametros.put("p_MoraTasa", creditoIndividualBean.getCredIndTasaMoraNominalAnual());
        parametros.put("p_TCEAMin", creditoIndividualBean.getCredIndTCEA());
        parametros.put("p_deGarmtoCod", creditoIndividualBean.getCredIndGarMtoCod());
        parametros.put("p_deFormaDesemb", creditoIndividualBean.getCredIndFormaDesemb());
        parametros.put("p_deRemisino", creditoIndividualBean.getCredIndRemiSiNo());
        parametros.put("p_deModoEnvioCta", creditoIndividualBean.getCredIndModoEnvioECta());
        parametros.put("p_deAFecPagoAnticip", creditoIndividualBean.getCredIndPagoAnticip());
        parametros.put("p_FirmanteNombres1",  titularBean.getCliPaterno() + (Tool.isNullOrEmpty(titularBean.getCliMaterno())?", "  : " "+ titularBean.getCliMaterno() + ", ") + titularBean.getCliNombre1() + (titularBean.getCliNombre2() == null ? "": " " + titularBean.getCliNombre2()));
        parametros.put("p_FirmanteDoi1",  titularBean.getCliDocumentoNumero());
        parametros.put("p_FirmanteDireccion1",  titularBean.getCliDireccion());
        parametros.put("p_FirmanteTelefono1",  titularBean.getCliCelular());
        parametros.put("p_FirmanteEmail1",  titularBean.getCliCorreo());

        for(int i=0; i<2;i++){
            int a = i+1;
            String nombre = "p_FiadorNombres" + a;
            if(i>=resultadoBusquedaFiadores.size()){
                continue;
            }
            ClienteIndBean fiador =  resultadoBusquedaFiadores.get(i);
            parametros.put(nombre,  fiador.getCliPaterno() + (Tool.isNullOrEmpty(fiador.getCliMaterno())?", "  : " "+ fiador.getCliMaterno() + ", ") + fiador.getCliNombre1() + (fiador.getCliNombre2() == null ? "": " " + fiador.getCliNombre2()));
            nombre = "p_FiadorDoi" + a;
            parametros.put(nombre,  fiador.getCliDocumentoNumero());
            nombre = "p_FiadorDireccion" + a;
            parametros.put(nombre,  fiador.getCliDireccion());
            nombre = "p_FiadorTelefono" + a;
            parametros.put(nombre,  fiador.getCliCelular());
            nombre = "p_FiadorEmail" + a;
            parametros.put(nombre,  fiador.getCliCorreo());
        }

        //parametros.put("p_FirmanteNombreRL",  titularBean.getCliCorreo());
        //parametros.put("p_FirmanteDoiRl",  titularBean.getCliCorreo());
        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "CRONOGRAMA_PAGOS_LINEA_CREDITOS")
    public Optional<byte[]> generarPlantillaCronogramaLineaCreditos(CreditoIndivGlobalBean creditoIndivGlobalBean, PlantillaDto plantillaDto){
        final HashMap<String, Object> parametros = new HashMap<>();
        final ClienteIndBean clienteBean = creditoIndivGlobalBean.getListaCliente().get(0);
        final CreditoIndividualBean creditoIndividualBean = clienteBean.getCreditoIndividual();
        SeguroDesgravamenBean seguroDesgravamenBean =  creditoIndivGlobalBean.getListaCliente().get(0).getSegDesgravamen();

        parametros.put("p_deOperNro", creditoIndividualBean.getCredIndCodigo());

        parametros.put("p_nombre",  clienteBean.getCliPaterno() + (!Tool.isNullOrEmpty(clienteBean.getCliMaterno())? " "+ clienteBean.getCliMaterno() + ", " : ", ")  + clienteBean.getCliNombre1() + (clienteBean.getCliNombre2() == null ? "": " " + clienteBean.getCliNombre2()));
        parametros.put("p_codCliente", clienteBean.getCliCodigo());
        parametros.put("p_SubTipoContrato", creditoIndivGlobalBean.getContratoSubtipo());
        parametros.put("p_deTea_Tasa", creditoIndividualBean.getCredIndTEA360());
        parametros.put("p_deTasaMora", creditoIndividualBean.getCredIndTasaMoraNominalAnual());
        parametros.put("p_deTasaCosto", creditoIndividualBean.getCredIndTCEA());
        parametros.put("p_deSegTasa", seguroDesgravamenBean.getSegDesgraTasa());
        parametros.put("p_deRemisino", creditoIndividualBean.getCredIndRemiSiNo());
        parametros.put("p_deAFecPagoAnticip", creditoIndividualBean.getCredIndPagoAnticip());

        parametros.put("p_listaCronograma", creditoIndividualBean.getListaCronogramaIndividual());
        parametros.put("p_cliAutorizaDetalle", !Tool.isNullOrEmpty(clienteBean.getCliAutorizaDetalle()) ?
                clienteBean.getCliAutorizaDetalle().toUpperCase() : clienteBean.getCliAutorizaDetalle());

        Date date = new Date();
        String dia= new SimpleDateFormat("dd-").format(date);
        String mes= new SimpleDateFormat("MMM").format(date);
        String year= new SimpleDateFormat("-y").format(date);
        String hora = new SimpleDateFormat("H:").format(date);
        String minutos = new SimpleDateFormat("m:").format(date);
        String seg = new SimpleDateFormat("s").format(date);
        parametros.put("p_Fecha", dia+mes.toUpperCase()+year);
        parametros.put("p_Hora", (hora.length()==2?("0"+hora):hora)+(minutos.length()==2?("0"+minutos):minutos)+(seg.length()==1?("0"+seg):seg));

        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "CERTIFICADO_SEGURO_DESGRAVAMEN_V7")
    public Optional<byte[]> generarPlantillaCertificadoSeguroDesgravamenv7(CreditoIndivGlobalBean creditoIndivGlobalBean, PlantillaDto plantillaDto){


        final List<ClienteIndBean> resultadoBusquedaConyuge = creditoIndivGlobalBean.getListaCliente()
                .stream().filter(cliente -> cliente.getCliCargo().intValueExact() == 6)
                .collect(Collectors.toList());

        if(resultadoBusquedaConyuge.size() > 1) {
            log.error("No se encontraron conyuges en la lista de clientes, se esperaban solo 1 conyuge o ninguno, se encontró: {}", resultadoBusquedaConyuge.size());
            return Optional.empty();
        }

        final ClienteIndBean conyugeBean = resultadoBusquedaConyuge.isEmpty() ? null : resultadoBusquedaConyuge.get(0);
        final ClienteIndBean clienteBean = creditoIndivGlobalBean.getListaCliente().get(0);
        final CreditoIndividualBean creditoIndividualBean = clienteBean.getCreditoIndividual();
        final SeguroDesgravamenBean seguroDesgravamenBean = clienteBean.getSegDesgravamen();
        final String rutaJasper = plantillaDto.getPlanRuta();
        final HashMap<String, Object> parametros = new HashMap<>();

        parametros.put("p_N_Certificado", seguroDesgravamenBean.getSegDesgraCertificado());
        parametros.put("p_SBS", seguroDesgravamenBean.getSegDesgraSBS());
        parametros.put("p_Seguro_Desgravamen", seguroDesgravamenBean.getSegDesgraTipoPoliza());
        parametros.put("p_Poliza", seguroDesgravamenBean.getSegDesgraPolizaGrup());
        parametros.put("p_Nombres", clienteBean.getCliNombre1() + (clienteBean.getCliNombre2() == null ? "": " " + clienteBean.getCliNombre2()));
        parametros.put("p_Apellido_Paterno", clienteBean.getCliPaterno());
        parametros.put("p_Apellido_Materno", clienteBean.getCliMaterno());
        parametros.put("p_Tipo_Doc", clienteBean.getCliDocumentoTipoDescripcion());
        parametros.put("p_Num_Doc", clienteBean.getCliDocumentoNumero());
        parametros.put("p_Fecha_Nac", clienteBean.getCliFechaNacimiento());
        parametros.put("p_Sexo", clienteBean.getCliSexo());
        parametros.put("p_Estado_Civil", clienteBean.getCliEstadoCivil());
        parametros.put("p_Ocupacion", clienteBean.getCliOcupacion());
        parametros.put("p_Direccion", clienteBean.getCliDireccion());
        parametros.put("p_Provincia", clienteBean.getCliProvincia());
        parametros.put("p_Telf_Cel", clienteBean.getCliCelular());
        parametros.put("p_Departamento", clienteBean.getCliDepartamento());
        parametros.put("p_Correo", clienteBean.getCliCorreo());
        parametros.put("p_Beneficiario", seguroDesgravamenBean.getSegDesgraBenefi());
        parametros.put("p_Soles", creditoIndividualBean.getCredIndlMonedaSimbolo().equals("S/.")?"X":"");
        parametros.put("p_Dolares", creditoIndividualBean.getCredIndlMonedaSimbolo().equals("$")?"X":"");
        parametros.put("p_Meses_Prestamo", creditoIndividualBean.getCredIndPlazoPrestamoCantidad());
        parametros.put("p_Importe_Prestamo", creditoIndividualBean.getCredIndDesembolsoMonto());
        parametros.put("p_Tipo_Prestamo", creditoIndividualBean.getCredIndPrestamoTipo());
        parametros.put("p_Soldo1", seguroDesgravamenBean.getSegDesgraPolGrup());parametros.put("p_Porcentaje1", seguroDesgravamenBean.getSegDesgraTasaComercial());

        List<PrimaDesgravamenBean> listaPrimaDesgravamen = clienteBean.getSegDesgravamen().getListaPrimaDesgravamen();
        if(Tool.isNullOrEmpty(listaPrimaDesgravamen)) {
           listaPrimaDesgravamen = Collections.emptyList();
        }

        if(listaPrimaDesgravamen.size() > 6) {
            log.error("Se esperaba que listaPrimaDesgravamen tuviera 6 elementos o menos, se encontró {}", listaPrimaDesgravamen.size());
            return Optional.empty();
        }

        int indicePorcentajes = 1;
        int indiceCargos = 1;
        int valorAumentado = 2;
        for (PrimaDesgravamenBean primaDesgravamenBean : listaPrimaDesgravamen) {
            if (indicePorcentajes == 7)  {
                indicePorcentajes = 9;
                valorAumentado = 1;
            }
            parametros.put("p_Cargos" + indiceCargos, primaDesgravamenBean.getSegDesgraTasaComision());
            parametros.put("p_Porcentaje" + indicePorcentajes, primaDesgravamenBean.getSegDesgraTasaComercial());
            indicePorcentajes += valorAumentado;
            ++indiceCargos;
        }

        parametros.put("p_Porcentaje7", seguroDesgravamenBean.getSegDesgraPriMinDetalle());

        parametros.put("p_Viven", seguroDesgravamenBean.getSegDesgraTituloCobertura());
        parametros.put("p_Sobre", seguroDesgravamenBean.getSegDesgraDefinicionCobertura());
        parametros.put("p_Devolucion", seguroDesgravamenBean.getSegDesgraSumaAsegurada());
        parametros.put("p_Sum_Asegurada_Max", seguroDesgravamenBean.getSegDesgraMaxima());

        List<SumaAsegDesgravamenBean> sumasMaxAseg = clienteBean.getSegDesgravamen().getListaSumaAsegDesgra();

        if(sumasMaxAseg.size() > 2) {
            log.error("La lista segDesgraSumMaxAseg debe tener a lo mucho 2 valores, la ingresada tiene {}", sumasMaxAseg.size());
            return Optional.empty();
        }

        parametros.put("p_Suma_Asegurada_Max_Por_Asegurado_1", sumasMaxAseg.size() >= 1 ? sumasMaxAseg.get(0).getSegDesgraSumMaxAseg() : "");
        parametros.put("p_Suma_Asegurada_Max_Por_Asegurado_2", sumasMaxAseg.size() == 2 ? sumasMaxAseg.get(1).getSegDesgraSumMaxAseg() : "");
        parametros.put("p_Monto", seguroDesgravamenBean.getSegDesgraMtoMaxCobert());
        parametros.put("p_Fecha_Solicitud", creditoIndividualBean.getCredIndDesembolsoFecha());
        parametros.put("p_Soldo2", seguroDesgravamenBean.getSegDesgraPoliza());
        parametros.put("p_Enlace", seguroDesgravamenBean.getSegDesgraEnlace());
        parametros.put("p_FirmaDirectorUnidadDeVida", rutaJasper + "firmaDirectorUnidadDeVida.jpg");
        parametros.put("p_Logo_Mapfre", rutaJasper + "LogoMapfre.png");


        if(Tool.isNullOrEmpty(conyugeBean)) {
            return generarJasper(parametros,plantillaDto);
        }

        // Conyuge
        //parametros.put("p_TipoDocIdentidad_Conyuge", tipoDocIdentidadConyuge);
        parametros.put("p_NumDocIdentidad_Conyuge", conyugeBean.getCliDocumentoNumero());
        parametros.put("p_RUC_Conyuge", "");
        parametros.put("p_ApellidoPaterno_Conyuge", conyugeBean.getCliPaterno());
        parametros.put("p_ApellidoMaterno_Conyuge", conyugeBean.getCliMaterno());
        parametros.put("p_Nombres_Conyuge", conyugeBean.getCliNombre1() + " " + (conyugeBean.getCliNombre2() != null? conyugeBean.getCliNombre2(): ""));
        parametros.put("p_FechaNacimiento_Conyuge", conyugeBean.getCliFechaNacimiento());
        parametros.put("p_Sexo_Conyuge", conyugeBean.getCliSexo());
        parametros.put("p_Ocupacion_Conyuge", conyugeBean.getCliOcupacion());
        parametros.put("p_Email_Conyuge", conyugeBean.getCliCorreo());
        parametros.put("p_Departamento_Conyuge", conyugeBean.getCliDepartamento());
        parametros.put("p_Provincia_Conyuge", conyugeBean.getCliProvincia());
        parametros.put("p_Direccion_Conyuge", conyugeBean.getCliDireccion());
        parametros.put("p_Telefono_Conyuge", conyugeBean.getCliCelular());




        log.trace("Se genera el jasper de CERTIFICADO_SEGURO_DESGRAVAMEN_V7");
        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "FICHA_DE_DATOS")
    public Optional<byte[]> generarFichaDeDatos(CreditoIndivGlobalBean creditoIndivGlobalBean, PlantillaDto plantillaDto) {
        final List<ClienteIndBean> resultadoBusquedaTitular = creditoIndivGlobalBean.getListaCliente()
                .stream().filter(cliente -> cliente.getCliCargo().intValueExact() == 5)
                .collect(Collectors.toList());

        final List<ClienteIndBean> resultadoBusquedaConyuge = creditoIndivGlobalBean.getListaCliente()
                .stream().filter(cliente -> cliente.getCliCargo().intValueExact() == 6)
                .collect(Collectors.toList());

        if(resultadoBusquedaTitular.size() != 1) {
            log.error("No se encontraron conyuges en la lista de clientes, se esperaban solo 1 cónyuge, se encontró: {}", resultadoBusquedaConyuge.size());
            return Optional.empty();
        }

        if(resultadoBusquedaConyuge.size() > 1) {
            log.error("No se encontraron conyuges en la lista de clientes, se esperaban solo 1 titular, se encontró: {}", resultadoBusquedaTitular.size());
            return Optional.empty();
        }

        final ClienteIndBean titularBean = resultadoBusquedaTitular.get(0);
        final ClienteIndBean conyugeBean = resultadoBusquedaConyuge.isEmpty() ? null : resultadoBusquedaConyuge.get(0);
        final String rutaJasper = plantillaDto.getPlanRuta();

        final String tipoDocIdentidad = titularBean.getCliDocumentoTipoDescripcion().equals("D.N.I.") ? "DNI":
                titularBean.getCliDocumentoTipoDescripcion().equals("C.E.") ? "CE" : "";

        final HashMap<String, Object> parametros = new HashMap<>();

        // Titular
        parametros.put("p_tipoDocIdentidad", tipoDocIdentidad);
        parametros.put("p_NumDocIdentidad", titularBean.getCliDocumentoNumero());
        parametros.put("p_Ruc", "");
        parametros.put("p_ApellidoPaterno", titularBean.getCliPaterno());
        parametros.put("p_ApellidoMaterno", titularBean.getCliMaterno());
        parametros.put("p_Nombres", titularBean.getCliNombre1() + " " + (titularBean.getCliNombre2() != null? titularBean.getCliNombre2(): ""));
        parametros.put("p_FechaNacimiento", titularBean.getCliFechaNacimiento());
        parametros.put("p_Ubigeo", titularBean.getCliUbigeoNacimiento());
        parametros.put("p_PaisDeNacimiento", titularBean.getCliPaisNacimiento());
        parametros.put("p_Sexo", titularBean.getCliSexo());
        parametros.put("p_GradoDeInstruccion", titularBean.getCliGradoInstruccion());
        parametros.put("p_Ocupacion", titularBean.getCliOcupacion());
        parametros.put("p_emailTitular", titularBean.getCliCorreo());
        parametros.put("p_Departamento_TitularIndependiente", titularBean.getCliNegDepartamento());
        parametros.put("p_Provincia_TitularIndependiente", titularBean.getCliNegProvincia());
        parametros.put("p_Distrito_TitularIndependiente", titularBean.getCliNegDistrito());
        parametros.put("p_Direccion_TitularIndependiente", titularBean.getCliNegDireccion());
        parametros.put("p_TipoDireccion_TitularIndependiente", titularBean.getCliNegTipoDireccion());
        parametros.put("p_Referencia_TitularIndependiente", titularBean.getCliNegReferencia());
        parametros.put("p_Ruc_TitularIndependiente", titularBean.getCliNegRUC());
        parametros.put("p_SectorEconomico_TitularIndependiente", titularBean.getCliNegSectorEconomico());
        parametros.put("p_CIIU_TitularIndependiente", titularBean.getCliNegCIIU());
        parametros.put("p_ActividadEconomica_TitularIndependiente", titularBean.getCliNegActividadEconomica());
        parametros.put("p_IngresoMensual_TitularIndependiente", titularBean.getCliNegIngresoMensual());
        parametros.put("p_TipoEstablecimiento_TitularIndependiente", titularBean.getCliNegTipoEstadoEstablecimiento());
        parametros.put("p_InicioActividades_TitularIndependiente", titularBean.getCliNegInicioActividades());
        parametros.put("p_CondicionLocal_TitularIndependiente", titularBean.getCliNegCondicionLocal());
        parametros.put("p_NombreEmpresa_TitularDependiente", titularBean.getCliCLabNombreEmpresa());
        parametros.put("p_Cargo_TitularDependiente", titularBean.getCliCLabCargo());
        parametros.put("p_CondicionLaboral_TitularDependiente", titularBean.getCliCLabCondicion());
        parametros.put("p_Sueldo_TitularDependiente", titularBean.getCliCLabSueldo());
        parametros.put("p_FechaIngreso_TitularDependiente", titularBean.getCliCLabFechaIngreso());

        // Parametros que solo tiene el titular
        String estadoCivil = titularBean.getCliEstadoCivil().contains("Casado")? "CASADO":
                titularBean.getCliEstadoCivil().contains("Soltero")? "SOLTERO":
                        titularBean.getCliEstadoCivil().contains("Viudo")? "VIUDO":
                                titularBean.getCliEstadoCivil().contains("Divorciado")? "DIVORCIADO":
                                        titularBean.getCliEstadoCivil().contains("Conviviente")? "CONVIVIENTE": "";

        parametros.put("p_EstadoCivil", estadoCivil);
        parametros.put("p_RazonSocial", titularBean.getCliRazonSocial());
        parametros.put("p_FichaRRPP", titularBean.getCliFichaRRPP());
        parametros.put("p_CargaFamiliar", titularBean.getCliCargarFamiliar());
        parametros.put("p_TitularResideDesde", titularBean.getCliDomlResideDesde());
        parametros.put("p_Telefono1Titular", titularBean.getCliCelular());
        parametros.put("p_Telefono2Titular", titularBean.getCliTelefono());
        parametros.put("p_CondicionViviendaTitular", titularBean.getCliCondicioVivienda());
        parametros.put("p_EsCasaYNegocio_Titular", titularBean.getCliCasaNegocio());
        parametros.put("p_DepartamentoTitular", titularBean.getCliDepartamento());
        parametros.put("p_ProvinciaTitular", titularBean.getCliProvincia());
        parametros.put("p_DistritoTitular", titularBean.getCliDistrito());
        parametros.put("p_DireccionTitular", titularBean.getCliDireccion());
        parametros.put("p_TipoDireccionTitular", titularBean.getCliTipoDom());
        parametros.put("p_ReferenciaTitular", titularBean.getCliReferencia());
        parametros.put("p_Telefono_TitularDependiente", titularBean.getCliCLabTelefono());
        parametros.put("p_DireccionLaboral_TitularDependiente", titularBean.getCliCLabDireccion());
        parametros.put("p_RUC_TitularDependiente", titularBean.getCliCLabRUC());
        parametros.put("p_SectorEconomico_TitularDependiente", titularBean.getCliCLabSectorEconomico());
        parametros.put("p_CIIU_TitularDependiente", titularBean.getCliCLabCIIU());
        parametros.put("p_ActividadEconomica_TitularDependiente", titularBean.getCliCLabActividadEconomica());

        try {
            parametros.put("p_LogoCompartamos", Utilitario.getByteInputStream(rutaJasper + "LogoCompartamos.png"));
        } catch (IOException e) {
            throw new ZyTException(e, ErroresEnum.FILE_SERVER_NO_FILE);
        }

        if(Tool.isNullOrEmpty(conyugeBean)) {
            return generarJasper(parametros,plantillaDto);
        }

        final String tipoDocIdentidadConyuge = conyugeBean.getCliDocumentoTipoDescripcion().equals("D.N.I.")? "DNI":
                conyugeBean.getCliDocumentoTipoDescripcion().equals("C.E.")?"CE":"";
        // Conyuge
        parametros.put("p_TipoDocIdentidad_Conyuge", tipoDocIdentidadConyuge);
        parametros.put("p_NumDocIdentidad_Conyuge", conyugeBean.getCliDocumentoNumero());
        parametros.put("p_RUC_Conyuge", "");
        parametros.put("p_ApellidoPaterno_Conyuge", conyugeBean.getCliPaterno());
        parametros.put("p_ApellidoMaterno_Conyuge", conyugeBean.getCliMaterno());
        parametros.put("p_Nombres_Conyuge", conyugeBean.getCliNombre1() + " " + (conyugeBean.getCliNombre2() != null? conyugeBean.getCliNombre2(): ""));
        parametros.put("p_FechaNacimiento_Conyuge", conyugeBean.getCliFechaNacimiento());
        parametros.put("p_Ubigeo_Conyuge", conyugeBean.getCliUbigeoNacimiento());
        parametros.put("p_PaisNacimiento_Conyuge", conyugeBean.getCliPaisNacimiento());
        parametros.put("p_Sexo_Conyuge", conyugeBean.getCliSexo());
        parametros.put("p_GradoInstruccion_Conyuge", conyugeBean.getCliGradoInstruccion());
        parametros.put("p_Ocupacion_Conyuge", conyugeBean.getCliOcupacion());
        parametros.put("p_Email_Conyuge", conyugeBean.getCliCorreo());
        parametros.put("p_Departamento_ConyugeIndependiente", conyugeBean.getCliNegDepartamento());
        parametros.put("p_Provincia_ConyugeIndependiente", conyugeBean.getCliNegProvincia());
        parametros.put("p_Distrito_ConyugeIndependiente", conyugeBean.getCliNegDistrito());
        parametros.put("p_Direccion_ConyugeIndependiente", conyugeBean.getCliNegDireccion());
        parametros.put("p_TipoDireccion_ConyugeIndependiente", conyugeBean.getCliNegTipoDireccion());
        parametros.put("p_Referencia_ConyugeIndependiente", conyugeBean.getCliNegReferencia());
        parametros.put("p_RUC_ConyugeIndependiente", conyugeBean.getCliNegRUC());
        parametros.put("p_SectorEconomico_ConyugeIndependiente", conyugeBean.getCliNegSectorEconomico());
        parametros.put("p_CIIU_ConyugeIndependiente", conyugeBean.getCliNegCIIU());
        parametros.put("p_ActividadEconomica_ConyugeIndependiente", conyugeBean.getCliNegActividadEconomica());
        parametros.put("p_IngresoMensual_ConyugeIndependiente", conyugeBean.getCliNegIngresoMensual());
        parametros.put("p_TipoEstablecimiento_ConyugeIndependiente", conyugeBean.getCliNegTipoEstadoEstablecimiento());
        parametros.put("p_InicioActividades_ConyugeIndependiente", conyugeBean.getCliNegInicioActividades());
        parametros.put("p_CondicionLaboral_ConyugeIndependiente", conyugeBean.getCliNegCondicionLocal());
        parametros.put("p_NombreDeLaEmpresa_ConyugeDependiente", conyugeBean.getCliCLabNombreEmpresa());
        parametros.put("p_RUC_ConyugueDependiente", conyugeBean.getCliCLabRUC());
        parametros.put("p_CargoQueOcupa_ConyugeDependiente", conyugeBean.getCliCLabCargo());
        parametros.put("p_CondicionLaboral_ConyugeDependiente", conyugeBean.getCliCLabCondicion());
        parametros.put("p_Sueldo_ConyugeDependiente", conyugeBean.getCliCLabSueldo());
        parametros.put("p_FechaIngreso_ConyugeDependiente", conyugeBean.getCliCLabFechaIngreso());

        return generarJasper(parametros,plantillaDto);
    }

    @TrackExecutionTime(operation = "FATCAPNV2")
    public Optional<byte[]> generarFatca(CreditoIndivGlobalBean creditoIndivGlobalBean, PlantillaDto plantillaDto) {

        final ClienteIndBean clienteBean = creditoIndivGlobalBean.getListaCliente().get(0);
        final String rutaJasper = plantillaDto.getPlanRuta();
        final FatBean fatca = clienteBean.getFatca();
        final String nombresCompletos = clienteBean.getCliNombre1() + " " +
                (clienteBean.getCliNombre2() != null?clienteBean.getCliNombre2():"") + " " +
                clienteBean.getCliPaterno() + " " + clienteBean.getCliMaterno();

        final HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("p_NombresCompletos", nombresCompletos);
        parametros.put("p_NumeroDocumentoIdentidad", clienteBean.getCliDocumentoNumero());
        parametros.put("p_NombresCompletos_TitularCuenta", nombresCompletos);
        parametros.put("p_NumeroDocumento_TitularCuenta", clienteBean.getCliDocumentoNumero());
        parametros.put("p_FechaNacimiento_TitularCuenta", clienteBean.getCliFechaNacimiento());
        parametros.put("p_PaisNacimiento_TitularCuenta", clienteBean.getCliPaisNacimiento());
        parametros.put("p_TieneObligacionesFiscalesEnEEUU", "Si".equals(fatca.getFatObligacionFiscalEEUU()));
        parametros.put("p_NumeroIndentificacionTributaria", fatca.getFatNumIdenTrib());
        parametros.put("p_TieneResidenciaFiscalOtrosPaises", "S".equals(fatca.getFatResidenciaFiscalExtranjero()));
        parametros.put("p_listaFatca", fatca.getListaFatca());
        parametros.put("p_CompartamosLogo", rutaJasper + "LogoCompartamos.png");
        parametros.put("p_Ciudad", fatca.getFatCiudadEmision());
        parametros.put("p_Fecha", clienteBean.getCreditoIndividual().getCredIndDesembolsoFecha());

        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "CERTIFICADO_SEGURO_PROTECCION_INDIVIDUALV4")
    public Optional<byte[]> generarCertificadoSeguroProteccionIndividualV4(CreditoIndivGlobalBean creditoIndivGlobalBean, PlantillaDto plantillaDto){
        final ClienteIndBean clienteBean = creditoIndivGlobalBean.getListaCliente().get(0);
        final String rutaJasper = plantillaDto.getPlanRuta();
        final SeguroDesgravamenBean seguroDesgravamenBean = clienteBean.getSegDesgravamen();

        final @Valid List<com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.SeguroOptativoBean> listaSeguroOptativo = clienteBean.getListaSegOptativo();
        if(listaSeguroOptativo.size() != 1) {
            log.error("Se esperaba que la lista seguro optativo tuviera 1 elemento, se encontró {}", listaSeguroOptativo.size());
            return Optional.empty();
        }
        final com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.SeguroOptativoBean  seguroOptativoBean = listaSeguroOptativo.get(0);
        final List<com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.BeneficiarioSeguroOptativoBean> listaBeneficiarios =  listaSeguroOptativo.get(0).getListaBeneficiarioSegOptativo();

        final HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("p_NumeroCertificado", listaSeguroOptativo.get(0).getSegOptativoCertificado().toString());
        parametros.put("p_Nombres_AseguradoTitular", clienteBean.getCliNombre1() + " " + (clienteBean.getCliNombre2() != null ?clienteBean.getCliNombre2():""));
        parametros.put("p_ApellidoPaterno_AseguradoTitular", clienteBean.getCliPaterno());
        parametros.put("p_ApellidoMaterno_AseguradoTitular", clienteBean.getCliMaterno());
        parametros.put("p_TipoDocumento_AseguradoTitular", clienteBean.getCliDocumentoTipoDescripcion());
        parametros.put("p_N_Documento_AseguradoTitular", clienteBean.getCliDocumentoNumero());
        parametros.put("p_FechaNacimiento_AseguradoTitular", clienteBean.getCliFechaNacimiento());
        parametros.put("p_Sexo_AseguradoTitular", clienteBean.getCliSexo());
        parametros.put("p_EstadoCivil_AseguradoTitular", clienteBean.getCliEstadoCivil());
        parametros.put("p_Ocupacion_AseguradoTitular", clienteBean.getCliOcupacion());
        parametros.put("p_Direccion_AseguradoTitular", clienteBean.getCliDireccion());
        parametros.put("p_Provincia_AseguradoTitular", clienteBean.getCliProvincia());
        parametros.put("p_Departamento_AseguradoTitular", clienteBean.getCliDepartamento());
        parametros.put("p_TelefonoCelular_AseguradoTitular", clienteBean.getCliCelular());
        parametros.put("p_CorreoElectronico_AseguradoTitular", clienteBean.getCliCorreo());
        parametros.put("p_listaBeneficiarios", listaBeneficiarios);
        parametros.put("p_Plan_Seguro", seguroOptativoBean.getSegOptativoPlanComercialPrima());
        parametros.put("p_deDistrito", clienteBean.getCliDistrito());
        parametros.put("p_deNExterior", clienteBean.getCliDomNExterior());
        parametros.put("p_deUrb", clienteBean.getCliDomUrb());
        parametros.put("p_deTipoDeUso", clienteBean.getCliDomTipoUso());
        //
        parametros.put("p_deGiro", clienteBean.getCliActivNom());
        parametros.put("p_deOcupacion", clienteBean.getCliOcupacion());
        //
        parametros.put("p_deAnioConstruccion", clienteBean.getCliDomAnoConst());
        parametros.put("p_deNroPisos", clienteBean.getCliDomPisos());
        parametros.put("p_deNroSotanos", clienteBean.getCliDomSotanos());
        parametros.put("p_deTipoMaterialConstruccion", clienteBean.getCliDomMaterial());
        //
        parametros.put("p_listaContenido", listaSeguroOptativo.get(0).getListaContenido());
        //

        parametros.put("p_FirmaMapfreSeguros", rutaJasper + "FirmaMapfre.png");
        try {
            parametros.put("p_LogoMapfre", Utilitario.getByteInputStream(rutaJasper + "LogoMapfre.png"));
            parametros.put("p_FirmaDirectorUnidadDeVida", Utilitario.getByteInputStream(rutaJasper + "FirmaDirectorUnidadDeVida.jpg"));
            parametros.put("p_FirmaDirectorUnidadRiesgosGenerales", Utilitario.getByteInputStream( rutaJasper + "FirmaDirectorUnidadRiesgosGenerales.png"));
            //parametros.put("p_FirmaMapfreSeguros", Utilitario.getByteInputStream( rutaJasper + "FirmaMapfre.png"));
        } catch (IOException e) {
            throw new ZyTException(e, ErroresEnum.FILE_SERVER_NO_FILE);
        }

        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "CONTRATO_CREDITO_INDIVIDUALV4")
    public Optional<byte[]> generarContratoCreditoIndividual(PlantillaDto plantillaDto) {
        String rutaJasper = plantillaDto.getPlanRuta();
        final HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("p_deLogo", rutaJasper + "LogoCompartamos.png");
        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "AUTORIZACION_DE_DATOS_PERSONALES")
    public Optional<byte[]> generarAutorizacionDeDatosPersonales(CreditoIndivGlobalBean creditoIndivGlobalBean, PlantillaDto plantillaDto){

        final ClienteIndBean clienteBean = creditoIndivGlobalBean.getListaCliente().get(0);

        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("p_deNombres", clienteBean.getCliNombre1()+" "+(clienteBean.getCliNombre2()!=null?clienteBean.getCliNombre2():""));
        parametros.put("p_deApePaterno", clienteBean.getCliPaterno());
        parametros.put("p_deApeMaterno", clienteBean.getCliMaterno());
        parametros.put("p_deNroDocumentoFirmante", clienteBean.getCliDocumentoNumero());
        parametros.put("p_deEstadoCivil", clienteBean.getCliEstadoCivil());
        parametros.put("p_deDomicilio",clienteBean.getCliDireccion());
        parametros.put("p_deACasada",clienteBean.getCliCasada());
        parametros.put("l_deAutorizaciones",clienteBean.getCliAutorizaDetalle());

        return generarJasper(parametros,plantillaDto);
    }

    @TrackExecutionTime(operation = "CARTILLA_INFORMACION_AHORROS")
    public Optional<byte[]> generarCartillaInformacionAhorros(CreditoIndivGlobalBean creditoIndivGlobalBean, PlantillaDto plantillaDto){

        final ClienteIndBean clienteBean = creditoIndivGlobalBean.getListaCliente().get(0);
        String rutaJasper = plantillaDto.getPlanRuta();

        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("p_deNombres", clienteBean.getCliNombre1()+" "+(clienteBean.getCliNombre2()!=null?clienteBean.getCliNombre2():""));
        parametros.put("p_deApePaterno", clienteBean.getCliPaterno());
        parametros.put("p_deApeMaterno", clienteBean.getCliMaterno());
        parametros.put("p_deDireccion",clienteBean.getCliDireccion());
        parametros.put("p_deEstadoCivil", clienteBean.getCliCelular());
        parametros.put("p_deCorreo", clienteBean.getCliCorreo());
        parametros.put("p_deNroDocumentoFirmante", clienteBean.getCliDocumentoNumero());
        parametros.put("p_deAgencia",clienteBean.getCartillaAhorros().getPasAgencia());
        parametros.put("p_deTipoCuenta",clienteBean.getCartillaAhorros().getPasTipoCuenta());
        parametros.put("p_deProducto",clienteBean.getCartillaAhorros().getPasProducto());
        parametros.put("p_deNroCuenta",clienteBean.getCartillaAhorros().getPasNroCuenta());
        parametros.put("p_deCelular",clienteBean.getCliCelular());
        parametros.put("p_deMoneda",clienteBean.getCartillaAhorros().getPasMoneda());
        parametros.put("p_deCtaInter",clienteBean.getCartillaAhorros().getPasNroCCI());
        parametros.put("p_deFechaApe",clienteBean.getCartillaAhorros().getPasFechaApertura());
        parametros.put("p_deTEAP",clienteBean.getCartillaAhorros().getPasTeaTasa());
        parametros.put("p_deTREAP",clienteBean.getCartillaAhorros().getPasTreaTasa());
        parametros.put("p_deSALDOMINP",clienteBean.getCartillaAhorros().getPasSalMinimo());
        parametros.put("p_deMONTOMINP",clienteBean.getCartillaAhorros().getPasMtoMinApertura());
        parametros.put("p_deTEAD",clienteBean.getCartillaAhorros().getPasTeaTasa2());
        parametros.put("p_deTREAD",clienteBean.getCartillaAhorros().getPasTreaTasa2());
        parametros.put("p_deSALDOMIND",clienteBean.getCartillaAhorros().getPasSalMinimo2());
        parametros.put("p_deMONTOMIND",clienteBean.getCartillaAhorros().getPasMtoMinApertura2());
        parametros.put("p_deLogo", rutaJasper + "LogoCompartamos.png");
        parametros.put("p_deFirma1", rutaJasper + "FIRMA1.jpg");
        parametros.put("p_deFirma2", rutaJasper + "FIRMA2.jpg");

        return generarJasper(parametros,plantillaDto);
    }

    @TrackExecutionTime(operation = "CONTRATO_DEPOSITO_AHORROS")
    public Optional<byte[]> generarContratoDepositoAhorros(PlantillaDto plantillaDto){
        String rutaJasper = plantillaDto.getPlanRuta();
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("p_deLogo", rutaJasper + "LogoCompartamos.png");
        return generarJasper(parametros,plantillaDto);
    }

    @TrackExecutionTime(operation = "CERTIFICADO_SEGURO_PROTECCION_ECONOMICO_CIV4")
    public Optional<byte[]> generarCertificadoSeguroProteccionEconomicoCIV4( CreditoIndivGlobalBean creditoIndivGlobalBean, PlantillaDto plantillaDto) {
        final ClienteIndBean clienteBean = creditoIndivGlobalBean.getListaCliente().get(0);
        final String rutaJasper = plantillaDto.getPlanRuta();
        final SeguroDesgravamenBean seguroDesgravamenBean = clienteBean.getSegDesgravamen();

        final @Valid List<com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.SeguroOptativoBean> listaSeguroOptativo = clienteBean.getListaSegOptativo();
        if(listaSeguroOptativo.size() != 1) {
            log.error("Se esperaba que la lista seguro optativo tuviera 1 elemento, se encontró {}", listaSeguroOptativo.size());
            return Optional.empty();
        }

        final HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("p_NumCertificado", listaSeguroOptativo.get(0).getSegOptativoCertificado().toString());
        parametros.put("p_AsegNombre", clienteBean.getCliNombre1() + " " + (clienteBean.getCliNombre2() != null ?clienteBean.getCliNombre2():""));
        parametros.put("p_AsegApPaterno", clienteBean.getCliPaterno());
        parametros.put("p_AsegApMaterno", clienteBean.getCliMaterno());
        parametros.put("p_AsegTipoDoc", clienteBean.getCliDocumentoTipoDescripcion());
        parametros.put("p_AsegNuDoc", clienteBean.getCliDocumentoNumero());
        parametros.put("p_AsegFeNacimiento", clienteBean.getCliFechaNacimiento());
        parametros.put("p_AsegSexo", clienteBean.getCliSexo());
        parametros.put("p_AsegEstCivil", clienteBean.getCliEstadoCivil());
        parametros.put("p_AsegOcupacion", clienteBean.getCliOcupacion());
        parametros.put("p_AsegDireccion", clienteBean.getCliDireccion());
        parametros.put("p_AsegProvincia", clienteBean.getCliProvincia());
        parametros.put("p_AsegDepartamento", clienteBean.getCliDepartamento());
        parametros.put("p_AsegTelfCel", clienteBean.getCliCelular());
        parametros.put("p_AsegCorreo", clienteBean.getCliCorreo());

        if (!Tool.isNullOrEmpty(clienteBean.getListaSegOptativo()) && !Tool.isNullOrEmpty(clienteBean.getListaSegOptativo().get(0)) && !Tool.isNullOrEmpty(clienteBean.getListaSegOptativo().get(0).getListaBeneficiarioSegOptativo())){
            List<BeneficiarioSeguroOptativoBean> listaBeneficiarioSeguroOptativoBean = clienteBean.getListaSegOptativo().get(0).
                    getListaBeneficiarioSegOptativo();
            parametros.put("p_deListaBeneficiariosSeguroOptativo", listaBeneficiarioSeguroOptativoBean);
        }

        try {
            parametros.put("p_LogoMapfre", Utilitario.getByteInputStream(rutaJasper + "logoMapfre.jpg"));
            parametros.put("p_FirmaDirVida", Utilitario.getByteInputStream( rutaJasper + "FirmaDirectorUnidadDeVida.png"));
        } catch (IOException e) {
            throw new ZyTException(e, ErroresEnum.FILE_SERVER_NO_FILE);
        }

        return generarJasper(parametros,plantillaDto);
    }


    @TrackExecutionTime(operation = "CRONOGRAMA_SEGURO_OPTATIVO")
    public Optional<byte[]> generarCronogramaSeguroOptativo( CreditoIndivGlobalBean creditoIndivGlobalBean, PlantillaDto plantillaDto ) {
        final ClienteIndBean clienteBean = creditoIndivGlobalBean.getListaCliente().get(0);
        final HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("p_cliNombre", clienteBean.getCliPaterno() + " " + clienteBean.getCliMaterno() + " " + clienteBean.getCliNombre1() + " " + (clienteBean.getCliNombre2() != null ? clienteBean.getCliNombre2() : ""));
        parametros.put("p_codCliente", clienteBean.getCliCodigo());

        if (!Tool.isNullOrEmpty(clienteBean.getListaSegOptativo()) && !Tool.isNullOrEmpty(clienteBean.getListaSegOptativo().get(0))){
            com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.SeguroOptativoBean seguroOptativoBean = creditoIndivGlobalBean.getListaCliente().get(0).getListaSegOptativo().get(0);
            parametros.put("p_codSeguro", seguroOptativoBean.getSegOptativoCertificado());
            parametros.put("p_prodTipo", seguroOptativoBean.getSegOptativoTipoProducto());
            parametros.put("p_tipoFirma", seguroOptativoBean.getSegOptativoTipoPlan());
            parametros.put("p_tiempoCobertura", seguroOptativoBean.getSegOptativoTiempoCoberturaCantidad() + " " + seguroOptativoBean.getSegOptativoTiempoCoberturaUnidad());
            parametros.put("l_croSegOptativo", seguroOptativoBean.getListaCronogramaSegOptativo());
        }else{
            parametros.put("p_codSeguro", "");
            parametros.put("p_prodTipo", "");
            parametros.put("p_tipoFirma", "");
            parametros.put("p_tiempoCobertura", "");
            parametros.put("l_croSegOptativo", "");
        }
        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "HOJA_RESUMEN_MAYOR_CUANTIA")
    public Optional<byte[]> generarHojaResumenMayorCuantia(
            CreditoIndivGlobalBean creditoGrupalBean,
            PlantillaDto plantillaDto
    ) {
        List<ClienteIndBean> listaclienteTitularBean =  creditoGrupalBean.getListaCliente();
        List<ClienteIndBean> listaClientesBeanReport =  new ArrayList<>();
        List<ClienteIndBean> listaClientesFiadoresBeanReport = new ArrayList<>();
        ClienteIndBean clienteTitularBean = null;
        ClienteIndBean clienteConyugeBean = null;


        for (ClienteIndBean c : listaclienteTitularBean) {
            switch (c.getCliCargo().intValue()) {
                case 5:
                    clienteTitularBean = c;
                    break;
                case 6:
                    clienteConyugeBean = c;
                    break;
                case 7:
                    listaClientesFiadoresBeanReport.add(c);
                    break;
            }
        }
        listaClientesBeanReport.add(clienteTitularBean);
        listaClientesBeanReport.add(clienteConyugeBean);


        final String rutaJasper = plantillaDto.getPlanRuta();

        CreditoIndividualBean creditoIndividualBean = listaClientesBeanReport.get(0).getCreditoIndividual();
        SeguroDesgravamenBean seguroDesgravamenBean =  listaClientesBeanReport.get(0).getSegDesgravamen();
        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("p_deOperNro", listaClientesBeanReport.get(0).getCreditoIndividual().getCredIndCodigo());

        if (clienteTitularBean!=null) {
            parametros.put("p_deFirC1Nombre",  listaClientesBeanReport.get(0).getCliNombre1() + " " + ( listaClientesBeanReport.get(0).getCliNombre2() != null ? clienteTitularBean.getCliNombre2() : "") + " " + (clienteTitularBean.getCliPaterno() != null ? clienteTitularBean.getCliPaterno() : "") + " " + (clienteTitularBean.getCliMaterno() != null ? clienteTitularBean.getCliMaterno() : ""));
            parametros.put("p_deFirC1Nombres",  listaClientesBeanReport.get(0).getCliNombre1() + " " + ( listaClientesBeanReport.get(0).getCliNombre2() != null ? clienteTitularBean.getCliNombre2() : ""));
            parametros.put("p_deFirC1ApePaterno", ( listaClientesBeanReport.get(0).getCliPaterno() != null ?  listaClientesBeanReport.get(0).getCliPaterno() : ""));
            parametros.put("p_deFirC1ApeMaterno",  ( listaClientesBeanReport.get(0).getCliMaterno() != null ?  listaClientesBeanReport.get(0).getCliMaterno() : ""));

            parametros.put("p_deRazSocial",  listaClientesBeanReport.get(0).getCliRazonSocial());
            parametros.put("p_deFirC1Dir",  listaClientesBeanReport.get(0).getCliDireccion());
            parametros.put("p_deCliDirDistri",  listaClientesBeanReport.get(0).getCliDistrito());
            parametros.put("p_deCliDirProvi",  listaClientesBeanReport.get(0).getCliProvincia());
            parametros.put("p_deCliDirDepto",  listaClientesBeanReport.get(0).getCliDepartamento());
            parametros.put("p_deRuc",  listaClientesBeanReport.get(0).getCliCLabRUC());
            parametros.put("p_deRucNegocio",  listaClientesBeanReport.get(0).getCliNegRUC());
            parametros.put("p_deFirC1Tel",  listaClientesBeanReport.get(0).getCliTelefono());
            parametros.put("p_deFirC1Email",  listaClientesBeanReport.get(0).getCliCorreo());
            parametros.put("p_deFirC1Doi",  listaClientesBeanReport.get(0).getCliDocumentoNumero());
            parametros.put("p_deCodSbs",  listaClientesBeanReport.get(0).getCliCodSBS());
            parametros.put("p_deGrupoEco",  listaClientesBeanReport.get(0).getCliGrupoEco());
            parametros.put("p_dePatrimto",  listaClientesBeanReport.get(0).getCliPatriMto());
            parametros.put("p_deCtaPres",  listaClientesBeanReport.get(0).getCliCodigo());
            //parametros.put("p_deTelefono",  listaClientesBeanReport.get(0).getCliTelefono());
            parametros.put("p_deTelefono", listaClientesBeanReport.get(0).getCliCelular());
            parametros.put("p_dePatrimtoPer",  listaClientesBeanReport.get(0).getCliPatriMtoPer());
            parametros.put("p_deActivNom",  listaClientesBeanReport.get(0).getCliActivNom());
            parametros.put("p_deFecInicio",  listaClientesBeanReport.get(0).getCliFecInicio());
            parametros.put("p_deCodCiiu",  listaClientesBeanReport.get(0).getCliCodCIIU());
            parametros.put("p_deActivoTras",  listaClientesBeanReport.get(0).getCliActividadesOtras());
        }
        parametros.put("p_deMdaImpoper", creditoIndividualBean.getCredIndDesembolsoMonto() + " " + creditoIndividualBean.getCredIndlMonedaSimbolo());
        parametros.put("p_deFecDesemb", creditoIndividualBean.getCredIndDesembolsoFecha());
        parametros.put("p_deTea_Tasa", creditoIndividualBean.getCredIndTEA360());
        parametros.put("p_deTasaCosto", creditoIndividualBean.getCredIndTCEA());
        parametros.put("p_deIntComp", creditoIndividualBean.getCredIndIntCompMontoTotal());
        parametros.put("p_deGarmtoCod", creditoIndividualBean.getCredIndGarMtoCod());
        parametros.put("p_deTasaMora", creditoIndividualBean.getCredIndTasaMoraNominalAnual());
        parametros.put("p_deFormaDesemb", creditoIndividualBean.getCredIndFormaDesemb());
        parametros.put("p_deRemisino", creditoIndividualBean.getCredIndRemiSiNo());
        parametros.put("p_deModoEnvioCta", creditoIndividualBean.getCredIndModoEnvioECta());
        parametros.put("p_deAFecPagoAnticip", creditoIndividualBean.getCredIndPagoAnticip());
        parametros.put("p_deCredIndPlazoPrestamoCantidad", creditoIndividualBean.getCredIndPlazoPrestamoCantidad());



        parametros.put("p_deDesgrav", seguroDesgravamenBean.getSegDesgraDetallePoliza());
        parametros.put("p_deSegTasa", seguroDesgravamenBean.getSegDesgraTasa());

        if (!Tool.isNullOrEmpty(clienteTitularBean)) parametros.put("l_listaPagos", clienteTitularBean.getCreditoIndividual().getListaCronogramaIndividual());

        parametros.put("p_deListaClientes", listaClientesBeanReport);
        parametros.put("p_deListaFiadores", listaClientesFiadoresBeanReport);

        parametros.put("p_deLogo", rutaJasper + "LogoCompartamos.png");
        parametros.put("p_deFirma1", rutaJasper + "FIRMA1.png");
        parametros.put("p_deFirma2", rutaJasper + "FIRMA2.png");
        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "HOJA_RESUMEN_MENOR_CUANTIA")
    public Optional<byte[]> generarHojaResumenMenorCuantia( CreditoIndivGlobalBean creditoIndivGlobalBean, PlantillaDto plantillaDto ) {
        final List<ClienteIndBean> resultadoBusquedaTitular = creditoIndivGlobalBean.getListaCliente().stream().
                filter(cliente -> cliente.getCliCargo().intValueExact() == 5).collect(Collectors.toList());

        if(resultadoBusquedaTitular.size() != 1) {
            log.error("Debe haber solo un Integrante (cliCargo=5) en la lista de clientes");
            return Optional.empty();
        }

        final ClienteIndBean clienteBean = resultadoBusquedaTitular.get(0);

        final List<ClienteIndBean> resultadoBusquedaConyuge = creditoIndivGlobalBean.getListaCliente()
                .stream().filter( cliente -> cliente.getCliCargo().intValueExact() == 6)
                .collect(Collectors.toList());
        log.info("resultadoBusquedaConyuge size: {}",resultadoBusquedaConyuge.size());

        if(resultadoBusquedaConyuge.size() > 1) {
            log.error("Se esperaba uno o ningún cónyuge, se encontraron {}", resultadoBusquedaConyuge.size());
            return Optional.empty();
        }

        final ClienteIndBean conyugeBean = (resultadoBusquedaConyuge.isEmpty())?null:resultadoBusquedaConyuge.get(0);

        final List<ClienteIndBean> fiadores = creditoIndivGlobalBean.getListaCliente()
                .stream().filter(cliente -> cliente.getCliCargo().intValueExact() == 7)
                .collect(Collectors.toList());
        log.info("fiadores size: {}",fiadores.size());

        if(fiadores.size() > 4) {
            log.error("Deben haber como máximo 4 fiadores (cliCargo = 7) en la lista de clientes, se encontró {}"
                    , fiadores.size());
            return Optional.empty();
        }

        final com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.CreditoIndividualBean creditoIndividualBean = clienteBean.getCreditoIndividual();
        final com.zy.cpts.cto.lib.zy.contrato.compts.dto.individual.SeguroDesgravamenBean seguroDesgravamenBean = clienteBean.getSegDesgravamen();
        final String rutaJasper = plantillaDto.getPlanRuta();
        final HashMap<String, Object> parametros = new HashMap<>();

        final Function<ClienteIndBean, String> obtenerNombresCompletos = (cliente) -> cliente.getCliNombre1() + " " +
                (cliente.getCliNombre2() != null?cliente.getCliNombre2():"") + " " +
                cliente.getCliPaterno() + " " + cliente.getCliMaterno();


        parametros.put("p_deNombreTit", clienteBean.getCliPaterno() + (clienteBean.getCliMaterno()!=null? (" " + clienteBean.getCliMaterno()+ ", ") :", ")+ clienteBean.getCliNombre1() + (clienteBean.getCliNombre2()!=null? (" " + clienteBean.getCliNombre2()) :""));
        // Datos Conyuge
        parametros.put("p_deNombreCony", (conyugeBean != null)? (conyugeBean.getCliPaterno() + (conyugeBean.getCliMaterno()!=null? (" " + conyugeBean.getCliMaterno()+ ", ") :", ")+ conyugeBean.getCliNombre1() + (conyugeBean.getCliNombre2()!=null? (" " + conyugeBean.getCliNombre2()) :"")) :"");

        parametros.put("p_deCtaPres", clienteBean.getCliCodigo().toString());
        parametros.put("p_deMdaImpoper", creditoIndividualBean.getCredIndlMonedaSimbolo() + creditoIndividualBean.getCredIndDesembolsoMonto().toString());
        parametros.put("p_deFecDesemb", creditoIndividualBean.getCredIndDesembolsoFecha());
        parametros.put("p_deTea_Tasa", creditoIndividualBean.getCredIndTEA360());
        parametros.put("p_deTasaCosto", creditoIndividualBean.getCredIndTCEA());
        parametros.put("p_deIntComp", creditoIndividualBean.getCredIndIntCompMontoTotal());
        parametros.put("p_deTasaMora", creditoIndividualBean.getCredIndTasaMoraNominalAnual());
        parametros.put("p_deFormaDesemb", creditoIndividualBean.getCredIndFormaDesemb());
        parametros.put("p_deRemisino", creditoIndividualBean.getCredIndRemiSiNo());
        parametros.put("p_deModoEnvioCta", creditoIndividualBean.getCredIndModoEnvioECta());
        parametros.put("p_deAFecPagoAnticip", creditoIndividualBean.getCredIndPagoAnticip());
        parametros.put("p_deCredIndPlazoPrestamoCantidad", creditoIndividualBean.getCredIndPlazoPrestamoCantidad());
        parametros.put("p_deDesgrav", seguroDesgravamenBean.getSegDesgraDetallePoliza());
        parametros.put("p_deRazSocial", obtenerNombresCompletos.apply(clienteBean));
        parametros.put("p_deDirFiscal", clienteBean.getCliDireccion());
        parametros.put("p_deCliDirDistri", clienteBean.getCliDistrito());
        parametros.put("p_deCliDirProvi", clienteBean.getCliProvincia());
        parametros.put("p_deCliDirDepto", clienteBean.getCliDepartamento());
        parametros.put("p_deRuc", " ");
        parametros.put("p_deDNI", clienteBean.getCliDocumentoNumero());
        parametros.put("p_deCodSbs", clienteBean.getCliCodSBS());
        parametros.put("p_deGrupoEco", clienteBean.getCliGrupoEco());
        parametros.put("p_dePatrimto", clienteBean.getCliPatriMto());
        // Datos Conyuge
        parametros.put("p_deNomPresCony", (conyugeBean != null)? (conyugeBean.getCliPaterno() + (conyugeBean.getCliMaterno()!=null? (" " + conyugeBean.getCliMaterno()+ ", ") :", ")+ conyugeBean.getCliNombre1() + (conyugeBean.getCliNombre2()!=null? (" " + conyugeBean.getCliNombre2()) :"")) :"");
        parametros.put("p_dePatrimtoPer", (conyugeBean != null)?conyugeBean.getCliPatriMtoPer():"");

        parametros.put("p_deActivNom", clienteBean.getCliActivNom());
        parametros.put("p_deFecInicio", clienteBean.getCliFecInicio());
        parametros.put("p_deCodCiiu", clienteBean.getCliCodCIIU());
        parametros.put("p_deActivoTras", clienteBean.getCliActividadesOtras());
        parametros.put("p_deOperNro", creditoIndividualBean.getCredIndCodigo());
        parametros.put("l_listaPagos", creditoIndividualBean.getListaCronogramaIndividual());
        parametros.put("p_deSegTasa", seguroDesgravamenBean.getSegDesgraTasa());
        parametros.put("p_deFirC1Nombre", obtenerNombresCompletos.apply(clienteBean));
        parametros.put("p_deFirC1Doi", clienteBean.getCliDocumentoNumero());
        parametros.put("p_deFirC1Dir", clienteBean.getCliDireccion());
        parametros.put("p_deFirC1Tel", clienteBean.getCliCelular());
        parametros.put("p_deFirC1Email", clienteBean.getCliCorreo());
        parametros.put("p_deFirC2Nombre", (conyugeBean != null)? obtenerNombresCompletos.apply(conyugeBean): null);
        parametros.put("p_deFirC2Doi", (conyugeBean != null)? conyugeBean.getCliDocumentoNumero(): null);
        parametros.put("p_deFirC2Dir", (conyugeBean != null)? conyugeBean.getCliDireccion(): null);
        parametros.put("p_deFirC2Tel", (conyugeBean != null)? conyugeBean.getCliCelular(): null);
        parametros.put("p_deFirC2Email", (conyugeBean != null)? conyugeBean.getCliCorreo(): null);

        for(int i = 0; i < fiadores.size(); ++i) {
            ClienteIndBean fiador = fiadores.get(i);
            parametros.put("p_deFirF"+ (i + 1) + "Nombre", obtenerNombresCompletos.apply(fiador));
            parametros.put("p_deFirF"+ (i + 1) + "Doi", fiador.getCliDocumentoNumero());
            parametros.put("p_deFirF"+ (i + 1) + "Dir", fiador.getCliDireccion());
            parametros.put("p_deFirF"+ (i + 1) + "Tel", fiador.getCliCelular());
            parametros.put("p_deFirF"+ (i + 1) + "Email", fiador.getCliCorreo());
        }

        parametros.put("p_deLogo", rutaJasper + "LogoCompartamos.png");
        parametros.put("l_listaFirmantesClientes", (conyugeBean != null)? Arrays.asList(clienteBean, conyugeBean): Collections.singletonList(clienteBean));
        parametros.put("l_listaFirmantesFiadores", (fiadores.isEmpty())? Collections.emptyList(): fiadores);

        try {
            parametros.put("p_deFirma1", Utilitario.getByteInputStream(rutaJasper + "Firma1.png"));
            parametros.put("p_deFirma2", Utilitario.getByteInputStream(rutaJasper + "Firma2.jpg"));
        }catch (IOException exception) {
            throw new ZyTException(exception, ErroresEnum.FILE_SERVER_NO_FILE);
        }

        return generarJasper(parametros, plantillaDto);
    }

    /********************************* AHORROS ************************************************************/
    @TrackExecutionTime(operation = "AHORROS_CONSENTIMIENTO_DATOS_PERSONALES")
    public Optional<byte[]> generarAhorroConsentDatosPersonales(PlantillaDto plantillaDto, ClienteAhorroBean clienteBean,
                                                                CartillaAhorrosBean cartillaAhorrosBean){

        HashMap<String, Object> parametros = new HashMap<>();
        parametros.put("p_cliAutorizaDetalle", !Tool.isNullOrEmpty(clienteBean.getCliAutorizaDetalle()) ?
                clienteBean.getCliAutorizaDetalle().toUpperCase() : clienteBean.getCliAutorizaDetalle());
        parametros.put("p_cliPaterno", clienteBean.getCliPaterno());
        parametros.put("p_cliMaterno", clienteBean.getCliMaterno());
        parametros.put("p_cliNombre1", clienteBean.getCliNombre1());
        parametros.put("p_cliNombre2", clienteBean.getCliNombre2());
        parametros.put("p_cliDocumentoTipoDescripcion", clienteBean.getCliDocumentoTipoDescripcion());
        parametros.put("p_cliDocumentoNumero", clienteBean.getCliDocumentoNumero());
        parametros.put("p_pasFechaApertura", cartillaAhorrosBean.getPasFechaApertura());
        parametros.put("p_cliTratamientoDatosLugarEmision", clienteBean.getCliTratamientoDatosLugarEmision());

        log.info(Tool.toJson(parametros));
        return generarJasper(parametros, plantillaDto);
    }

        @TrackExecutionTime(operation = "AHORRO_FICHA_DATOS")
    public Optional<byte[]> generarAhorroFichaDatos(AhorrosBean ahorrosBeanAhorrosBean, PlantillaDto plantillaDto,
                                                    ClienteAhorroBean clienteBean, CartillaAhorrosBean cartillaAhorrosBean){
        log.info("---> Inicia: generarAhorroFichaDatos");
        HashMap<String, Object> parametros;
        List<ClienteAhorroBean> resultadoBusquedaConyuge = new ArrayList<>();
        ClienteAhorroBean conyugeBean = null;

        if(!Tool.isNullOrEmpty(clienteBean.getCliDocConyugeNumero()) && !Tool.isNullOrEmpty(clienteBean.getCliDocConyugeTipoCodigo())){
            resultadoBusquedaConyuge = ahorrosBeanAhorrosBean.getListaCliente()
                    .stream()
                    .filter(e -> (e.getCliCargo().compareTo(DecimalID.SEIS)==0 &&
                            !Tool.isNullOrEmpty(e.getCliDocConyugeTipoCodigo()) &&
                            e.getCliDocumentoTipoCodigo().compareTo(clienteBean.getCliDocConyugeTipoCodigo())==0 &&
                            e.getCliDocumentoNumero().compareTo(clienteBean.getCliDocConyugeNumero())==0))
                    .collect(Collectors.toList());
        }

        log.info(clienteBean.getCliDocumentoNumero() + " ===> " + Tool.toJson(resultadoBusquedaConyuge));
        if(resultadoBusquedaConyuge.size() > 1) {
            log.error("Se encontro mas de 1 conyuge, se esperaban solo 1 conyuge, se encontraron {}.", resultadoBusquedaConyuge.size());
            return Optional.empty();
        }else if(resultadoBusquedaConyuge.size()==1){
            conyugeBean = resultadoBusquedaConyuge.get(0);
        }

        parametros = new HashMap<>();
        parametros.put("p_pasFechaApertura", cartillaAhorrosBean.getPasFechaApertura());
        parametros.put("p_cliCodigo", clienteBean.getCliCodigo().toString());
        parametros.put("p_cliDocumentoTipoDescripcion", clienteBean.getCliDocumentoTipoDescripcion());
        parametros.put("p_cliPaterno", clienteBean.getCliPaterno());
        parametros.put("p_cliMaterno", clienteBean.getCliMaterno());
        parametros.put("p_cliNombre1", clienteBean.getCliNombre1());
        parametros.put("p_cliNombre2", clienteBean.getCliNombre2());
        parametros.put("p_cliDocumentoNumero", clienteBean.getCliDocumentoNumero());
        parametros.put("p_cliCorreo", clienteBean.getCliCorreo());
        parametros.put("p_cliRazonSocial", clienteBean.getCliRazonSocial());
        parametros.put("p_cliFechaRRPP", clienteBean.getCliFechaRRPP());
        parametros.put("p_cliGradoInstruccion", clienteBean.getCliGradoInstruccion());
        parametros.put("p_cliOcupacion", clienteBean.getCliOcupacion());
        parametros.put("p_cliServidorPublico", clienteBean.getCliServidorPublico());
        parametros.put("p_cliEstadoCivil", clienteBean.getCliEstadoCivil());
        parametros.put("p_cliSexo", clienteBean.getCliSexo());
        parametros.put("p_cliPaisNacimiento", clienteBean.getCliPaisNacimiento());
        parametros.put("p_cliFechaNacimiento", clienteBean.getCliFechaNacimiento());
        parametros.put("p_cliPaisNacionalidad", clienteBean.getCliPaisNacionalidad());
        parametros.put("p_cliPaisResidencia", clienteBean.getCliPaisResidencia());
        parametros.put("p_cliDireccion", clienteBean.getCliDireccion());
        parametros.put("p_cliDepartamento", clienteBean.getCliDepartamento());
        parametros.put("p_cliProvincia", clienteBean.getCliProvincia());
        parametros.put("p_cliDistrito", clienteBean.getCliDistrito());
        parametros.put("p_cliZona", clienteBean.getCliZona());
        parametros.put("p_cliReferencia", clienteBean.getCliReferencia());
        parametros.put("p_cliDomIResideDesde", clienteBean.getCliDomlResideDesde());
        parametros.put("p_cliTipoDom", clienteBean.getCliTipoDom());
        parametros.put("p_cliCondicioVivienda", clienteBean.getCliCondicioVivienda());
        parametros.put("p_cliTelefonos", clienteBean.getCliTelefonos());
        parametros.put("p_cliCargaFamiliar", clienteBean.getCliCargaFamiliar().toString());
        parametros.put("p_cliNegRUC", clienteBean.getCliNegRUC());
        parametros.put("p_cliNegSectorEconomico", clienteBean.getCliNegSectorEconomico());
        parametros.put("p_cliNegCIIU", clienteBean.getCliNegCIIU().toString());
        parametros.put("p_cliNegCondicionLocal", clienteBean.getCliNegCondicionLocal());
        parametros.put("p_cliNegInicioActividades", clienteBean.getCliNegInicioActividades());
        parametros.put("p_cliNegTipoEstadoEstablecimiento", clienteBean.getCliNegTipoEstadoEstablecimiento());
        parametros.put("p_cliNegNroLocales", clienteBean.getCliNegNroLocales());
        parametros.put("p_cliNegDireccion", clienteBean.getCliNegDireccion());
        parametros.put("p_cliNegZona", clienteBean.getCliNegZona());
        parametros.put("p_cliNegDepartamento", clienteBean.getCliNegDepartamento());
        parametros.put("p_cliNegProvincia", clienteBean.getCliNegProvincia());
        parametros.put("p_cliNegDistrito", clienteBean.getCliNegDistrito());
        parametros.put("p_cliCLabFechaIngreso", clienteBean.getCliCLabFechaIngreso());
        parametros.put("p_cliCLabCargo", clienteBean.getCliCLabCargo());
        parametros.put("p_cliCLabSueldo", clienteBean.getCliCLabSueldo().toString());
        parametros.put("p_cliCLabNombreEmpresa", clienteBean.getCliCLabNombreEmpresa());
        parametros.put("p_cliCLabDireccion", clienteBean.getCliCLabDireccion());
        parametros.put("p_cliCLabTelefono", clienteBean.getCliCLabTelefono());
        parametros.put("p_cliCLabAnexo", clienteBean.getCliCLabAnexo());
        parametros.put("p_cliCLabCondicion", clienteBean.getCliCLabCondicion());
        parametros.put("p_cliDocumentoNumeroConyugue", clienteBean.getCliDocConyugeNumero());
        parametros.put("p_cliCLabRuc", clienteBean.getCliCLabRUC());
        if(!Tool.isNullOrEmpty(conyugeBean)){
            parametros.put("p_cliPaternoConyugue", conyugeBean.getCliPaterno());
            parametros.put("p_cliMaternoConyugue", conyugeBean.getCliMaterno());
            parametros.put("p_cliNombre1Conyugue", conyugeBean.getCliNombre1());
            parametros.put("p_cliNombre2Conyugue", conyugeBean.getCliNombre2());
            parametros.put("p_cliCorreoConyugue", conyugeBean.getCliCorreo());
            parametros.put("p_cliGradoInstruccionConyugue", conyugeBean.getCliGradoInstruccion());
            parametros.put("p_cliOcupacionConyugue", conyugeBean.getCliOcupacion());
            parametros.put("p_cliServidorPublicoConyugue", conyugeBean.getCliServidorPublico());
        }
        log.info("---> Termina: generarAhorroFichaDatos");
        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "AHORROS_CARTILLA_DE_INFORMACION")
    public Optional<byte[]> generarAhorrosCartillaInfo(AhorrosBean ahorrosBean, PlantillaDto plantillaDto,
                                                       CartillaAhorrosBean cartillaAhorrosBean) {
        List<ClienteAhorroBean> resultadoBusquedaTitularRep;
        ClienteAhorroBean titularRepBean;
        List<ClienteAhorroBean> resultadoBusquedaTitularMan;
        ClienteAhorroBean titularManBean = null;
        HashMap<String, Object> parametros = new HashMap<>();

        resultadoBusquedaTitularRep = ahorrosBean.getListaCliente()
                .stream().filter(cliente -> (cliente.getCliCargo().compareTo(new BigDecimal("5"))==0))
                .collect(Collectors.toList());
        if(resultadoBusquedaTitularRep.isEmpty()){
            log.error("No se encontraron titulares representativos...");
            return Optional.empty();
        }
        if(resultadoBusquedaTitularRep.size() > 1) {
            log.error("Se encontraron {} titulares representativos, se esperaba solo 1.", resultadoBusquedaTitularRep.size());
            return Optional.empty();
        }else {
            titularRepBean = resultadoBusquedaTitularRep.get(0);
        }

        resultadoBusquedaTitularMan = ahorrosBean.getListaCliente()
                .stream().filter(cliente -> (cliente.getCliCargo().compareTo(new BigDecimal("4"))==0))
                .collect(Collectors.toList());
        if(resultadoBusquedaTitularMan.size() > 1) {
            log.error("Se encontraron {} titulares mancomunados, se esperaba solo 1.", resultadoBusquedaTitularMan.size());
            return Optional.empty();
        }else if(resultadoBusquedaTitularMan.size()==1){
            titularManBean = resultadoBusquedaTitularMan.get(0);
        }

        if(!Tool.isNullOrEmpty(cartillaAhorrosBean)){
            parametros.put("p_pasAgencia", cartillaAhorrosBean.getPasAgencia());
            parametros.put("p_pasTipoCuenta", cartillaAhorrosBean.getPasTipoCuenta());
            parametros.put("p_pasProducto", cartillaAhorrosBean.getPasProducto());
            parametros.put("p_pasNroCuenta", cartillaAhorrosBean.getPasNroCuenta());
            parametros.put("p_pasMoneda", cartillaAhorrosBean.getPasMoneda());
            parametros.put("p_pasNroCCI", cartillaAhorrosBean.getPasNroCCI());
            parametros.put("p_pasFechaApertura", cartillaAhorrosBean.getPasFechaApertura());
            parametros.put("p_pasTeaTasa", cartillaAhorrosBean.getPasTeaTasa());
            parametros.put("p_pasTeaTasa2", cartillaAhorrosBean.getPasTeaTasa2());
            parametros.put("p_pasTreaTasa", cartillaAhorrosBean.getPasTreaTasa());
            parametros.put("p_pasTreaTasa2", cartillaAhorrosBean.getPasTreaTasa2());
            parametros.put("p_pasSalMinimo", cartillaAhorrosBean.getPasSalMinimo());
            parametros.put("p_pasSalMinimo2", cartillaAhorrosBean.getPasSalMinimo2());
            parametros.put("p_pasMtoMinApertura", cartillaAhorrosBean.getPasMtoMinApertura());
            parametros.put("p_pasMtoMinApertura2", cartillaAhorrosBean.getPasMtoMinApertura2());
        }
        parametros.put("p_cliNombre1Rep", titularRepBean.getCliNombre1());
        parametros.put("p_cliNombre2Rep", titularRepBean.getCliNombre2());
        parametros.put("p_cliPaternoRep", titularRepBean.getCliPaterno());
        parametros.put("p_cliMaternoRep", titularRepBean.getCliMaterno());
        parametros.put("p_cliDireccion", titularRepBean.getCliDireccion());
        parametros.put("p_cliCorreo", titularRepBean.getCliCorreo());
        parametros.put("p_cliCelular", titularRepBean.getCliCelular());
        parametros.put("p_cliDocumentoNumeroRep", titularRepBean.getCliDocumentoNumero());
        if(!Tool.isNullOrEmpty(titularManBean)){
            parametros.put("p_cliNombre1Manc", titularManBean.getCliNombre1());
            parametros.put("p_cliNombre2Manc", titularManBean.getCliNombre2());
            parametros.put("p_cliPaternoManc", titularManBean.getCliPaterno());
            parametros.put("p_cliMaternoManc", titularManBean.getCliMaterno());
            parametros.put("p_cliDocumentoNumeroManc", titularManBean.getCliDocumentoNumero());
        }
        return generarJasper(parametros, plantillaDto);
    }

    @TrackExecutionTime(operation = "AHORROS_CONTRATO_APERTURA")
    public Optional<byte[]> generarAhorrosContratoApertura(PlantillaDto plantillaDto){
        log.info("---> Inicia: generarAhorrosContratoApertura");
        HashMap<String, Object> parametros = new HashMap<>();
        log.info("---> Termina: generarAhorrosContratoApertura");
        return generarJasper(parametros, plantillaDto);
    }

}
