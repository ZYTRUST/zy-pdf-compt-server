package com.zy.ws.mgpdf.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zy.cpts.cto.lib.zy.contrato.compts.dto.CreditoGrupalBean;
import com.zy.lib.message.resource.ErroresEnum;
import com.zy.lib.service.exception.ZyException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.SerializationUtils;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.Locale;

@Component
@Slf4j
public class Utilitario {

    public static final String FORMAT_DDMMYYYYHHMMSS = "dd/MM/yyyy HH:mm:ss";
    public static final String FORMAT_YYMMDDHHMMSSSSS = "yyMMddHHmmssSSS";

    public byte[] convertObjectToJsonBytes(Object object) throws IOException {

        return createJsonMapper().writeValueAsBytes(object);
    }

    public String convertObjectToJsonString(Object object) throws IOException {

        return createJsonMapper().writeValueAsString(object);
    }

    public ObjectMapper createJsonMapper()  {

        ObjectMapper mapper = new ObjectMapper();
        mapper.setSerializationInclusion(com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        return mapper;
    }

   public static Throwable getRootException(Throwable e) {

        Throwable lt = e;
        Throwable t = e.getCause();

        while (t != null) {
            lt = t;
            t = t.getCause();
        }
        return lt; /**t == null ? lt : t;*/
    }

    public static Object getObjectFromString( String s ) throws Exception {
        byte [] bData = Base64.getDecoder().decode( s );
        ObjectInputStream oStream = new ObjectInputStream( new ByteArrayInputStream(bData) );
        Object object  = oStream.readObject();
        oStream.close();
        return object;
    }

    public static String getStringFromObject( Serializable object ) throws Exception {
        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
        ObjectOutputStream oStream = new ObjectOutputStream( bStream );
        oStream.writeObject( object );
        oStream.close();
        return Base64.getEncoder().encodeToString(bStream.toByteArray());
    }

    public static boolean checkIfExpired(Date feCrea, long tiempoExpira) throws ZyException {

        if (feCrea == null) {
            throw new ZyException(ErroresEnum.BMO_SOLICITUD_FECREA_NULL);
        }

        long diffInSeconds = (new Date().getTime() - feCrea.getTime()) / 1000;

        return diffInSeconds > tiempoExpira;
    }

    public static byte[] getByteArray(String path) throws IOException {

        return Files.readAllBytes(Paths.get(path));
    }

    public static ByteArrayInputStream getByteInputStream(String path) throws IOException {
        byte[] in = Files.readAllBytes(Paths.get(path));
        return new ByteArrayInputStream(in);
    }

    public static void saveFile(String ruta, byte[] bytes) throws IOException {
        Path path = Paths.get(ruta);
        Files.write(path, bytes);
    }

    public static String formatoFecha(String fecha) {

        try {
            String pattern = "EEEEE',' dd 'de' MMMMM 'de' yyyy";
            Date date1 = new SimpleDateFormat("dd/MM/yy").parse(fecha);
            Locale locale = new Locale("es", "ES");
            DateFormatSymbols dateFormatSymbols = new DateFormatSymbols(locale);
            dateFormatSymbols.setWeekdays(new String[]{
                    "Unused",
                    "Domingo",
                    "Lunes",
                    "Martes",
                    "Miercoles",
                    "Jueves",
                    "Viernes",
                    "Sabado",
            });
            dateFormatSymbols.setMonths(new String[]{
                    "Enero",
                    "Febrero",
                    "Marzo",
                    "Abril",
                    "Mayo",
                    "Junio",
                    "Julio",
                    "Agosto",
                    "Septiembre",
                    "Octubre",
                    "Noviembre",
                    "Diciembre",
            });
            SimpleDateFormat simpleDateFormat =
                    new SimpleDateFormat(pattern, dateFormatSymbols);

            String date = simpleDateFormat.format(date1);

            return date;
        } catch (ParseException e) {
            return "";
        }

    }

}
