package com.zy.ws.mgpdf;

import com.zy.ws.mgpdf.util.PlantillaType;
import lombok.extern.slf4j.Slf4j;
import lombok.var;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Slf4j
public class TestDemo {

    public static void main(String[] args){
        log.info(PlantillaType.CARTILLA_IDENTIFICACION.name());

        long start = System.currentTimeMillis();
        Executor executor = Executors.newFixedThreadPool(20);
        var categories = Stream.of(PlantillaType.values())
                                            .map(t ->  CompletableFuture.supplyAsync(
                                                    ()->prueba(t.name()),executor)
                                            )
                                            .collect(toList());

        List<String> lst = categories.stream().map(CompletableFuture::join).collect(Collectors.toList());
        long end = System.currentTimeMillis();

        System.out.printf("The operation took %s ms%n", end - start);
        //System.out.println("Categories are: " + categories);
        System.out.println("Categories are: " + lst);
    }
    public static String prueba(String value){
        log.info("value {}",value);
        try {
            Thread.sleep(1000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "hi "+value;
    }
}
