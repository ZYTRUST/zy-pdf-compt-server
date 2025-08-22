package com.zy.ws.mgpdf;

import com.zy.cpts.cto.lib.zy.contrato.compts.dto.*;
import com.zy.lib.common.util.Constante;
import com.zy.lib.dtos.dto.mgpdf.PdfManageDto;
import com.zy.lib.dtos.dto.mgpdf.PdfMergeItemDto;
import com.zy.ws.mgpdf.resource.PlantillaResource;
import com.zy.ws.mgpdf.util.PlantillaType;
import com.zy.ws.mgpdf.util.Utilitario;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static com.zy.ws.mgpdf.service.PlantillaService.JASPER_PATH_COMPARTAMOS;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

//@SpringBootTest
@Slf4j
//@AutoConfigureMockMvc
class ResourceTests {


	@Autowired
	protected Utilitario utilitario;

	@Autowired
	private PlantillaResource resource;


	/*@Test
	void test_generar_pdf_consolidado() throws Exception {


		CreditoGrupalBean obj = new CreditoGrupalBean();
		ReglasDeGrupoBean reglasDeGrupoBean = new ReglasDeGrupoBean();
		reglasDeGrupoBean.setHoraReunion("");
		reglasDeGrupoBean.setMontoMultaFaltaValor(BigDecimal.ONE);
		obj.setReglasDeGrupoBean(reglasDeGrupoBean);

		List<ClienteBean> listaCliente = new ArrayList<>();
		ClienteBean clienteBean = new ClienteBean();
		clienteBean.setCliEmiteCartaSegDesgravamen("S");
		clienteBean.setCliEmiteSegDesgravamen("S");

		listaCliente.add(clienteBean);
		obj.setListaCliente(listaCliente);
		List<PdfMergeItemDto> lst = resource.obtenerListaPdfCreados(obj);
		PdfManageDto res = resource.obtenerPdfConsolidado(lst);
		Utilitario.saveFile("e:\\"+res.getFileName(),res.getBuffer());
	}*/

	/*@Test
	void test_generar_pdf_formulario() throws IOException {

		PlantillaType plantillaType = PlantillaType.CONTRATO_PRESTAMO;

		CreditoGrupalBean obj = new CreditoGrupalBean();
		ReglasDeGrupoBean reglasDeGrupoBean = new ReglasDeGrupoBean();
		reglasDeGrupoBean.setHoraReunion("");
		reglasDeGrupoBean.setMontoMultaFaltaValor(BigDecimal.ONE);
		obj.setReglasDeGrupoBean(reglasDeGrupoBean);

		List<ClienteBean> listaCliente = new ArrayList<>();
		ClienteBean clienteBean = new ClienteBean();

		clienteBean.setCliNombre1("Juan 1");
		clienteBean.setCliNombre2("Alberto 1");

		clienteBean.setCliPaterno("Perez 1");
		clienteBean.setCliMaterno("Perez 1");

		clienteBean.setCliDocumentoNumero("71342134");
		clienteBean.setCliEmiteSegDesgravamen("X");
		clienteBean.setCliEmiteCartaSegDesgravamen("");
		clienteBean.setCliFechaNacimiento("03/11/2022");

		listaCliente.add(clienteBean);

		clienteBean = new ClienteBean();
		clienteBean.setCliNombre1("Juan 2");
		clienteBean.setCliNombre2("Alberto 2");

		clienteBean.setCliPaterno("Perez 2");
		clienteBean.setCliMaterno("Perez 2");

		clienteBean.setCliDocumentoNumero("71342135");
		clienteBean.setCliEmiteSegDesgravamen("X");
		clienteBean.setCliEmiteCartaSegDesgravamen("");
		clienteBean.setCliFechaNacimiento("03/01/2022");

		listaCliente.add(clienteBean);

		clienteBean = new ClienteBean();
		clienteBean.setCliNombre1("Juan 3");
		clienteBean.setCliNombre2("Alberto 3");

		clienteBean.setCliPaterno("Perez 3");
		clienteBean.setCliMaterno("Perez 3");

		clienteBean.setCliDocumentoNumero("71342136");
		clienteBean.setCliEmiteSegDesgravamen("X");
		clienteBean.setCliEmiteCartaSegDesgravamen("");
		clienteBean.setCliFechaNacimiento("03/10/2022");

		listaCliente.add(clienteBean);

		obj.setListaCliente(listaCliente);

		PdfMergeItemDto item = resource.generarPdfFomulario(plantillaType,obj);

		Utilitario.saveFile("e:\\"+plantillaType.name()+".pdf",item.getFileBuffer());

	}*/


	/*@Value("${jasper.url}")
	private String rutaJasper;

		@Test
		void test_generar_pdf_tratamientoDatos() throws Exception {
		CreditoGrupalBean obj = new CreditoGrupalBean();
		obj.setCredGrupFechaVigencia("12/03/21");
		obj.setCredGrupalVersion("3.0");
		obj.setGrupoNombre("CORAZON DE JESUS CURA MORI 2");
		obj.setCredGrupalFecha("Lunes, 19 de Septiembre de 2022");
		obj.setGrupoNombre("Grupo de Prueba");
		obj.setGrupoCodigo(new BigDecimal(11744496));
		obj.setGrupoCiclo(new BigDecimal(2));
		obj.setCredGrupMonedaSimbolo("S/");
		obj.setCredGrupMontoDesembolso(new BigDecimal("24800.00"));
		obj.setCredGrupFechaDesembolso("19/09/2022");
		obj.setCredGrupFrecuenciaPagoCantidad(new BigDecimal(14));
		obj.setCredGrupPlazoPrestamoCantidad(new BigDecimal(8));
		obj.setCredGrupTEA360(new BigDecimal("83.698100"));
		obj.setCredGrupTCEA(new BigDecimal("98.175000"));
		obj.setCredGrupMontoAmpliacion(new BigDecimal("0.00"));
		obj.setCredGrupTasaMoraNominalAnual(new BigDecimal("11.824680"));

		//INICIALIZAR LISTACRONOGRAMAGRUPALBEAN
		List<CronogramaGrupalBean> listaCronogramaGrupal = new ArrayList<>();
		for (int i = 0; i < 17; i++) {

			CronogramaGrupalBean listaCronogramaGrupalBean = new CronogramaGrupalBean();
			listaCronogramaGrupalBean.setCuoGrupFecha(i+3+"/10/2022");
			listaCronogramaGrupalBean.setCuoGrupNumero(String.valueOf(i + 1));
			if (i == 0) {
				listaCronogramaGrupalBean.setCuoGrupMontoDesembolso(new BigDecimal(24800.000 + i));
			}
			listaCronogramaGrupalBean.setCuoGrupValor(new BigDecimal("1000.00"));
			listaCronogramaGrupalBean.setCuoGrupMontoCapital(new BigDecimal("1000.00"));
			listaCronogramaGrupalBean.setCuoGrupMontoIntComp(new BigDecimal("0.00"));
			listaCronogramaGrupalBean.setCuoGrupMontoSegDesgravamen(new BigDecimal("0.00"));
			listaCronogramaGrupalBean.setCuoGrupMontoSegTodoRiesg(new BigDecimal("0.00"));
			listaCronogramaGrupal.add(listaCronogramaGrupalBean);
		}
		obj.setListaCronogramaGrupal(listaCronogramaGrupal);
		//INICIALIZAR GASTOS
		List<CreditoGastoBean> listaCreditoGasto = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			CreditoGastoBean listaCreditoGastoBean = new CreditoGastoBean();
			listaCreditoGastoBean.setGastoMotivo("Servicio de recaudación en agentes corresponsales");
			listaCreditoGastoBean.setGastoCondiciones("Al momento de la Recaudación");
			listaCreditoGastoBean.setGastoVariables("Se traladará el importe que cobre el canal utilizado");
			listaCreditoGasto.add(listaCreditoGastoBean);
		}
		obj.setListaCreditoGasto(listaCreditoGasto);
		//INCIALIZAR COMISIONES
		List<CreditoComisionBean> listaCreditoComisiones = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			CreditoComisionBean listaCreditoComisionesBean = new CreditoComisionBean();
			listaCreditoComisionesBean.setComisionCategoria("Servicios brindados a solicitud del cliente");
			listaCreditoComisionesBean.setComisionDenominacion("Envío Físico de Estado de Cuenta");
			listaCreditoComisionesBean.setComisionTipoTransaccion("Constancia de no adeudo (a partir de la segunda emisión)");
			listaCreditoComisionesBean.setComisionMontoFijoSoles(new BigDecimal(0.05 * i));
			listaCreditoComisionesBean.setComisionMontoFijoDolares(new BigDecimal(0.50 * i));
			listaCreditoComisiones.add(listaCreditoComisionesBean);
		}
		obj.setListaCreditoComisiones(listaCreditoComisiones);
		//	Inicializa otros datos
		obj.setCredGrupFormaDesembolso(1);
//        creditoGrupalBean.setCredGrupCuentaDesembolso("2316547896514");
		obj.setCredGrupFormaContratacion(1);
		obj.setCredGrupFormaEnvioDocContractual(1);
		obj.setCredGrupFormaRemisionInformacion(1);
		obj.setCredGrupFormaPagosAnticipadosParciales(1);
		obj.setCredGrupTipoSeguro("Seguro de desgravamen");
		obj.setCredGrupMontoPrima(new BigDecimal("0.00"));
		obj.setCredGrupNombreCompaniaSeguro("MAPRE Perú Vida Compañía de Seguros y Reaseguros.");
		obj.setCredGrupRucCompania("20418896915");
		obj.setCredGrupNumPoliza("6110085");
		//INICIALIZAR FIRMANTES
		List<FirmanteBean> listaFirmantes = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			FirmanteBean listaFirmantesBean = new FirmanteBean();
			if(i == 2){
				listaFirmantesBean.setFirmanteCargo("Firma: Presidenta del Grupo");
			}else {
				listaFirmantesBean.setFirmanteCargo("Cargo " + (i + 1));
			}
			listaFirmantesBean.setFirmanteNombre1("Nombre " + (i + 1));
			listaFirmantesBean.setFirmanteNombre2("Nombre " + (i + 1));
			listaFirmantesBean.setFirmantePaterno("Paterno " + (i + 1));
			listaFirmantesBean.setFirmanteMaterno("Materno " + (i + 1));
			listaFirmantesBean.setFirmanteTipoDocumento("D.N.I.");
			listaFirmantesBean.setFirmanteNumDocumento("12345678-" + (i + 1));
			listaFirmantes.add(listaFirmantesBean);
		}
		obj.setListaFirmantes(listaFirmantes);
		ReglasDeGrupoBean reglasDeGrupoBean = new ReglasDeGrupoBean();
		reglasDeGrupoBean.setMontoMinimoAhorroValo( new BigDecimal (10.00));
		reglasDeGrupoBean.setMontoMinimoAhorroDesc("DIEZ CON 00/100 soles");
		reglasDeGrupoBean.setReunionDia("Lunes");
		reglasDeGrupoBean.setHoraReunion("15:00");
		reglasDeGrupoBean.setPersonaReunion("Fernando Rey Arévalo Díaz");
		reglasDeGrupoBean.setUbicacionReunion("URBANIZACION LAS GARDENIAS CALLE 12 - N5-3");
		reglasDeGrupoBean.setMontoMultaTardanzaValor( new BigDecimal (5.00));
		reglasDeGrupoBean.setMontoMultaTardanzaDesc("CINCO CON 00/100 soles");
		reglasDeGrupoBean.setMontoMultaFaltaValor( new BigDecimal (10.00));
		reglasDeGrupoBean.setMontoMultaFaltaDesc("DIEZ CON 00/100 soles");
		obj.setReglasDeGrupoBean(reglasDeGrupoBean);

		List<ClienteBean> listaCliente = new ArrayList<>();
		for (int i = 0; i < 17; i++) {
			//ListaClienteBean listaClienteBean = new ListaClienteBean();
			ClienteBean listaClienteBean = new ClienteBean();

			listaClienteBean.setCliNombre1("DAYANNE");
			listaClienteBean.setCliNombre2("STACY CLARISSA");
			listaClienteBean.setCliPaterno("VALENCIA");
			listaClienteBean.setCliMaterno("TORRES");
			listaClienteBean.setCliDocumentoNumero("73673099");
			listaClienteBean.setCliDireccion("CENTRO POBLADO CUCUNGARA SECTOR BARRIO MONTEVERDE CALLE SIMON BOLIVAR MANZANA MZ L LT 13");
			listaClienteBean.setCliCelular("994020435");
			listaClienteBean.setCliAutorizaDatoPersonal("S");
			listaCliente.add(listaClienteBean);
		}
		List<GerenteBean> listaGerente = new ArrayList<>();
		for (int i = 0; i < 2; i++) {
			//ListaGerentesBean listaGerentesBean = new ListaGerentesBean();
			GerenteBean listaGerentesBean = new GerenteBean();

			listaGerentesBean.setFirmanteNombre1("Nombre " + (i + 1));
			listaGerentesBean.setFirmanteNombre2("Nombre " + (i + 1));
			listaGerentesBean.setFirmantePaterno("Paterno " + (i + 1));
			listaGerentesBean.setFirmanteMaterno("Materno " + (i + 1));

			listaGerentesBean.setFirmanteFirma(getContentFile(getRutaJasper()+"HojaResumen\\" + "FirmaCompartamos"+i+".jpg"));

			listaGerente.add(listaGerentesBean);
		}
		obj.setListaGerentes(listaGerente);
		obj.setListaCliente(listaCliente);
		List<PdfMergeItemDto> lst = resource.obtenerListaPdfCreados(obj);
		PdfManageDto res = resource.obtenerPdfConsolidado(lst);
		Utilitario.saveFile("d:\\"+res.getFileName(),res.getBuffer());

	}*/

	/*@Test
	void test_generar_pdf_formulario2() throws IOException {

		PlantillaType plantillaType = PlantillaType.CARTILLA_IDENTIFICACION;

		CreditoGrupalBean obj = new CreditoGrupalBean();
		obj.setCredGrupFechaVigencia("12/03/21");
		obj.setCredGrupalVersion("3.0");
		obj.setGrupoNombre("CORAZON DE JESUS CURA MORI 2");
		obj.setCredGrupalFecha("Lunes, 19 de Septiembre de 2022");
		obj.setGrupoNombre("Grupo de Prueba");
		obj.setGrupoCodigo(new BigDecimal(11744496));
		obj.setGrupoCiclo(new BigDecimal(2));
		obj.setCredGrupMonedaSimbolo("S/");
		obj.setCredGrupMontoDesembolso(new BigDecimal("24800.00"));
		obj.setCredGrupFechaDesembolso("19/09/2022");
		obj.setCredGrupFrecuenciaPagoCantidad(new BigDecimal(14));
		obj.setCredGrupPlazoPrestamoCantidad(new BigDecimal(8));
		obj.setCredGrupTEA360(new BigDecimal("83.698100"));
		obj.setCredGrupTCEA(new BigDecimal("98.175000"));
		obj.setCredGrupMontoAmpliacion(new BigDecimal("0.00"));
		obj.setCredGrupTasaMoraNominalAnual(new BigDecimal("11.824680"));

		//INICIALIZAR LISTACRONOGRAMAGRUPALBEAN
		List<CronogramaGrupalBean> listaCronogramaGrupal = new ArrayList<>();
		for (int i = 0; i < 17; i++) {

			CronogramaGrupalBean listaCronogramaGrupalBean = new CronogramaGrupalBean();
			listaCronogramaGrupalBean.setCuoGrupFecha(i+3+"/10/2022");
			listaCronogramaGrupalBean.setCuoGrupNumero(String.valueOf(i + 1));
			if (i == 0) {
				listaCronogramaGrupalBean.setCuoGrupMontoDesembolso(new BigDecimal(24800.000 + i));
			}
			listaCronogramaGrupalBean.setCuoGrupValor(new BigDecimal("1000.00"));
			listaCronogramaGrupalBean.setCuoGrupMontoCapital(new BigDecimal("1000.00"));
			listaCronogramaGrupalBean.setCuoGrupMontoIntComp(new BigDecimal("0.00"));
			listaCronogramaGrupalBean.setCuoGrupMontoSegDesgravamen(new BigDecimal("0.00"));
			listaCronogramaGrupalBean.setCuoGrupMontoSegTodoRiesg(new BigDecimal("0.00"));
			listaCronogramaGrupal.add(listaCronogramaGrupalBean);
		}
		obj.setListaCronogramaGrupal(listaCronogramaGrupal);
		//INICIALIZAR GASTOS
		List<CreditoGastoBean> listaCreditoGasto = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			CreditoGastoBean listaCreditoGastoBean = new CreditoGastoBean();
			listaCreditoGastoBean.setGastoMotivo("Servicio de recaudación en agentes corresponsales");
			listaCreditoGastoBean.setGastoCondiciones("Al momento de la Recaudación");
			listaCreditoGastoBean.setGastoVariables("Se traladará el importe que cobre el canal utilizado");
			listaCreditoGasto.add(listaCreditoGastoBean);
		}
		obj.setListaCreditoGasto(listaCreditoGasto);
		//INCIALIZAR COMISIONES
		List<CreditoComisionBean> listaCreditoComisiones = new ArrayList<>();
		for (int i = 0; i < 8; i++) {
			CreditoComisionBean listaCreditoComisionesBean = new CreditoComisionBean();
			listaCreditoComisionesBean.setComisionCategoria("Servicios brindados a solicitud del cliente");
			listaCreditoComisionesBean.setComisionDenominacion("Envío Físico de Estado de Cuenta");
			listaCreditoComisionesBean.setComisionTipoTransaccion("Constancia de no adeudo (a partir de la segunda emisión)");
			listaCreditoComisionesBean.setComisionMontoFijoSoles(new BigDecimal(0.05 * i));
			listaCreditoComisionesBean.setComisionMontoFijoDolares(new BigDecimal(0.50 * i));
			listaCreditoComisiones.add(listaCreditoComisionesBean);
		}
		obj.setListaCreditoComisiones(listaCreditoComisiones);
		//	Inicializa otros datos
		obj.setCredGrupFormaDesembolso(1);
//        creditoGrupalBean.setCredGrupCuentaDesembolso("2316547896514");
		obj.setCredGrupFormaContratacion(1);
		obj.setCredGrupFormaEnvioDocContractual(1);
		obj.setCredGrupFormaRemisionInformacion(1);
		obj.setCredGrupFormaPagosAnticipadosParciales(1);
		obj.setCredGrupTipoSeguro("Seguro de desgravamen");
		obj.setCredGrupMontoPrima(new BigDecimal("0.00"));
		obj.setCredGrupNombreCompaniaSeguro("MAPRE Perú Vida Compañía de Seguros y Reaseguros.");
		obj.setCredGrupRucCompania("20418896915");
		obj.setCredGrupNumPoliza("6110085");
		//INICIALIZAR FIRMANTES
		List<FirmanteBean> listaFirmantes = new ArrayList<>();
		for (int i = 0; i < 3; i++) {
			FirmanteBean listaFirmantesBean = new FirmanteBean();
			listaFirmantesBean.setFirmanteCargo("Cargo " + (i + 1));
			listaFirmantesBean.setFirmanteNombre1("Nombre " + (i + 1));
			listaFirmantesBean.setFirmanteNombre2("Nombre " + (i + 1));
			listaFirmantesBean.setFirmantePaterno("Paterno " + (i + 1));
			listaFirmantesBean.setFirmanteMaterno("Materno " + (i + 1));
			listaFirmantesBean.setFirmanteTipoDocumento("D.N.I.");
			listaFirmantesBean.setFirmanteNumDocumento("12345678-" + (i + 1));
			listaFirmantes.add(listaFirmantesBean);
		}
		obj.setListaFirmantes(listaFirmantes);

		ReglasDeGrupoBean reglasDeGrupoBean = new ReglasDeGrupoBean();
		reglasDeGrupoBean.setHoraReunion("");
		reglasDeGrupoBean.setMontoMultaFaltaValor(BigDecimal.ONE);
		obj.setReglasDeGrupoBean(reglasDeGrupoBean);

		List<ClienteBean> listaClienteBean = new ArrayList<>();
		ClienteBean tratamientoDatosBean = new ClienteBean();

		tratamientoDatosBean.setCliNombre1("Juan 1");
		tratamientoDatosBean.setCliNombre2("Alberto 1");
		tratamientoDatosBean.setCliPaterno("Perez 1");
		tratamientoDatosBean.setCliMaterno("Perez 1");
		tratamientoDatosBean.setCliDocumentoNumero("12345678");
		tratamientoDatosBean.setTratamientoDatosLugarEmision("DNI");
		tratamientoDatosBean.setTratamientoDatosFechaEmision("01/01/2019");
		tratamientoDatosBean.setCliAutorizaDatoPersonal("S");

		listaClienteBean.add(tratamientoDatosBean);

		tratamientoDatosBean = new ClienteBean();
		tratamientoDatosBean.setCliNombre1("Juan 2");
		tratamientoDatosBean.setCliNombre2("Alberto 2");
		tratamientoDatosBean.setCliPaterno("Perez 2");
		tratamientoDatosBean.setCliMaterno("Perez 2");
		tratamientoDatosBean.setCliDocumentoNumero("71634293");
		tratamientoDatosBean.setTratamientoDatosLugarEmision("DNI");
		tratamientoDatosBean.setTratamientoDatosFechaEmision("02/01/2019");
		tratamientoDatosBean.setCliAutorizaDatoPersonal(" ");

		listaClienteBean.add(tratamientoDatosBean);

		obj.setListaCliente(listaClienteBean);

		List<PdfMergeItemDto> lst = resource.obtenerListaPdfCreados(obj);
		PdfManageDto res = resource.obtenerPdfConsolidado(lst);
		Utilitario.saveFile("C:\\Users\\agallegos\\Documents\\Compartamos\\"+res.getFileName(),res.getBuffer());
	}*/


	/*private String getRutaJasper(){

		return rutaJasper +
				Constante.SLASH +
				JASPER_PATH_COMPARTAMOS +
				Constante.SLASH;
	}*/
	public static byte[] getContentFile(String pathFile) throws FileNotFoundException, IOException {
		byte[] buffer;
		File fi = new File(pathFile);
		int size_file = (int) fi.length();
		buffer = new byte[size_file];
		FileInputStream fis = new FileInputStream(pathFile);
		fis.read(buffer);
		fis.close();
		return buffer;
	}
}
