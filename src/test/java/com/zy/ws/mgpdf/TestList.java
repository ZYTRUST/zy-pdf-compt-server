package com.zy.ws.mgpdf;

import com.zy.cpts.cto.lib.zy.contrato.compts.dto.ClienteBean;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.CreditoGrupalBean;
import com.zy.lib.common.util.Tool;
import com.zy.lib.dtos.dto.mgpdf.PdfManageDto;
import com.zy.lib.dtos.dto.mgpdf.PdfMergeDto;
import com.zy.lib.dtos.dto.mgpdf.PdfMergeItemDto;
import com.zy.ws.mgpdf.service.PdfManageService;
import com.zy.ws.mgpdf.util.Utilitario;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.junit.jupiter.api.Test;

import javax.rmi.CORBA.Util;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
public class TestList {

    @Test
    void mergePdfV2() throws IOException {
        List<PdfMergeItemDto> lstPdfMerger = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            PdfMergeItemDto pdf1 = new PdfMergeItemDto();
            pdf1.setFileOrder((short) i);
            //pdf1.setFileBuffer(Files.readAllBytes(Paths.get("/Users/esanchez/Downloads/subir_descargar_calificar_finanzas.pdf")));
            pdf1.setFileBuffer(Files.readAllBytes(Paths.get("/Users/esanchez/Downloads/merge.pdf")));
            lstPdfMerger.add(pdf1);
        }

        /*PdfMergeItemDto pdf2 = new PdfMergeItemDto();
        pdf2.setFileOrder((short) 10);
        pdf2.setFileBuffer(Files.readAllBytes(Paths.get("/Users/esanchez/Downloads/flujos_multiempresa_dactilar.pdf")));
        lstPdfMerger.add(pdf2);

        PdfMergeItemDto pdf3 = new PdfMergeItemDto();
        pdf3.setFileOrder((short) 11);
        pdf3.setFileBuffer(Files.readAllBytes(Paths.get("/Users/esanchez/Downloads/convertir_PDF.pdf")));
        lstPdfMerger.add(pdf3);*/

        PdfMergeDto pdfMergeDto = new PdfMergeDto();
        pdfMergeDto.setPdfMergeItems(lstPdfMerger);
        pdfMergeDto.setFileName(Tool.dateFormat(new Date(), Utilitario.FORMAT_YYMMDDHHMMSSSSS)+".pdf");

        PdfManageService manageService = new PdfManageService();
        PdfManageDto res = manageService.mergePdfV2(pdfMergeDto);
        log.info("filename {}",res.getFileName());
        log.info("fileType {}",res.getFileType());
        log.info("size {}",res.getSize());
        Utilitario.saveFile("/Users/esanchez/Downloads/"+res.getFileName(),res.getBuffer());
    }

    @Test
    void test_lista(){
        CreditoGrupalBean obj = new CreditoGrupalBean();
        List<ClienteBean> listaCliente = new ArrayList<>();
        ClienteBean clienteBean = new ClienteBean();

        clienteBean.setCliNombre1("Juan ");
        clienteBean.setCliNombre2("Alberto ");
        clienteBean.setCliEmiteSegDesgravamen("N");
        listaCliente.add(clienteBean);

        ClienteBean clienteBean1 = new ClienteBean();

        clienteBean1.setCliNombre1("Juan 1");
        clienteBean1.setCliNombre2("Alberto 1");
        clienteBean1.setCliEmiteSegDesgravamen("S");
        listaCliente.add(clienteBean1);

        ClienteBean clienteBean2 = new ClienteBean();

        clienteBean2.setCliNombre1("Juan2");
        clienteBean2.setCliNombre2("Alberto 2");
        clienteBean2.setCliEmiteSegDesgravamen("N");
        listaCliente.add(clienteBean2);

        obj.setListaCliente(listaCliente);

        log.info("listaCliente size inicial {}",obj.getListaCliente().size());
        //listaCliente.stream().filter(o -> o.getCliEmiteSegDesgravamen().equals("N"));
        obj.getListaCliente().removeIf(o -> o.getCliEmiteSegDesgravamen().equals("N"));
        /*listaCliente
                .forEach(o -> {
                    if(StringUtils.equals(o.getCliEmiteSegDesgravamen(),"N")){
                        listaCliente.remove(o);
                        log.info("-**-----");
                    }
                });*/
        log.info("listaCliente size final {}",obj.getListaCliente().size());
        listaCliente.forEach(o -> {
            log.info("nombre {}",o.getCliNombre1());
        });
    }
}
