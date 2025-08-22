package com.zy.ws.mgpdf.util;

public enum PlantillaType {
    CONTRATO_PRESTAMO((short) 1, "ContratoPrestamo", "ContratoPrestamoCreditoSuperMujer.jasper"),
    CARTILLA_IDENTIFICACION((short) 2, "Cartilla", "Cartilla.jasper"),
    REGLAS_DE_GRUPO((short) 3, "ReglasdeGrupo", "ReglasdeGrupo.jasper"),
    HOJA_RESUMEN((short) 4, "HojaResumen", "HojaResumen.jasper"),
    CONDICIONES_ESPECIFICAS((short) 5, "CondicionesEspecificas", "CondicionesEspecificas.jasper"),
    POLIZA_GRUPO((short) 6, "PolizaGrupo", "Poliza.jasper"),
    ESTIMADO_CLIENTE((short) 7, "EstimadoCliente", "EstimadoCliente.jasper"),
    SEGURO_OPTATIVO((short) 8, "SeguroOptativo", "SeguroOptativo.jasper"),
    CRONOGRAMA_PAGOS((short) 9, "CronogramaPagos", "CronogramaPagos.jasper"),
    TRATAMIENTO_DATOS((short) 10, "TratamientoDatosPersonales", "TratamientoDatosPersonales_1.jasper"),

    CONTRATO_CREDITO_INDIVIDUAL((short) 11, "CreditoIndividualCI", "01 - 01 Contrato Crédito Individual V1.2.jasper"),
    CERTIFICADO_SEGURO_DESGRAVAMEN_V7((short) 12, "CertificadoSeguroDesgravamenCI", "PlantillaCertificadoSeguroDesgravamenv7.jasper"),
    FICHA_DE_DATOS((short) 13, "FichaDeDatosCI", "01-03-FICHA_DE_DATOS.jasper"),
    FATCAPNV2((short) 14, "FatcaCI", "02-03-FATCAPNV2+1.jasper"),
    CERTIFICADO_SEGURO_PROTECCION_INDIVIDUAL_V4((short) 15, "CertificadoSPIndividualCI", "03-01-CertificadoSPIndividualv4.jasper"),

    AUTORIZACION_DATOS_PERSONALES((short) 15, "AutorizacionDatosPersonales", "AutorizacionUsoDatosPersonales.jasper"),
    CARTILLA_INFORMACION_AHORROS((short) 16, "CartillaInformacionAhorros", "CartillaInformacionAhorros.jasper"),

    CONTRATO_DEPOSITO_AHORROS((short) 17, "ContratoDepositoAhorros", "ContratoDepositoAhorros.jasper"),

    CERTIFICADO_SEGURO_PROTECCION_ECONOMICO_CI_V4((short) 18, "CertificadoSPEconomicoCI", "CertificadoSeguroProteccionEconomicoCIv4.jasper"),

    CRONOGRAMA_SEGURO_OPTATIVO((short) 19, "CronogramaSeguroOptativo", "CronogramaSeguroOptativo.jasper"),

    HOJA_RESUMEN_MAYOR_CUANTIA((short) 20, "HojaResumenMayorCuantiaCI", "HojaResumenMayorCuantia.jasper"),
    HOJA_RESUMEN_MENOR_CUANTIA((short) 21, "HojaResumenMenorCuantiaCI", "HojaResumenMenorCuantia.jasper"),

    DECLARACION_JURADA((short) 22, "DeclaracionJurada", "DeclaracionJurada.jasper");


    public String getJasperName() {
        return jasperName;
    }

    public String getDirName() {
        return dirName;
    }

    public short getOrder() {
        return order;
    }

    private final short order;
    private final String dirName;
    private final String jasperName;

    PlantillaType(short order, String dirName, String jasperName) {
        this.order = order;
        this.dirName = dirName;
        this.jasperName = jasperName;
    }
}
