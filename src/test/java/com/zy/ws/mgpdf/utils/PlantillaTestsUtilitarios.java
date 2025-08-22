package com.zy.ws.mgpdf.utils;

import com.zy.ws.mgpdf.resource.PlantillaResource;
import com.zy.ws.mgpdf.service.FilemanService;
import com.zy.ws.mgpdf.service.PdfManageService;
import com.zy.ws.mgpdf.service.PlantillaService;
import com.zy.ws.mgpdf.service.ZyFormularioService;

import java.lang.reflect.Field;

public class PlantillaTestsUtilitarios {
    public static PlantillaResource inicializarResource(String rutaJasper) throws IllegalAccessException {
        PlantillaService plantillaService = new PlantillaService();
        plantillaService.setRutaJasper(rutaJasper);
        PdfManageService pdfManageService = new PdfManageService();
        FilemanService filemanService = new FilemanService(null);
        ZyFormularioService zyFormularioService = new ZyFormularioService();
        PlantillaResource plantillaResource = new PlantillaResource();
        for(Field field: plantillaResource.getClass().getDeclaredFields()) {
            String fieldName = field.getName();
            if("log".equals(fieldName)) {
                continue;
            }
            field.setAccessible(true);
            switch(fieldName) {
                case "pdfManageService":
                    field.set(plantillaResource, pdfManageService);
                    break;
                case "filemanService":
                    field.set(plantillaResource, filemanService);
                    break;
                case "plantillaService":
                    field.set(plantillaResource, plantillaService);
                    break;
                case "zyFormularioService":
                    field.set(plantillaResource, zyFormularioService);
                    break;
            }
            field.setAccessible(false);
        }
        return plantillaResource;
    }
}
