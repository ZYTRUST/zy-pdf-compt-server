package com.zy.ws.mgpdf.feignclient;

import com.zy.lib.dtos.response.formu.RsCombinatoriaPlantillaDto;
import com.zy.lib.service.response.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

//@FeignClient(name = "zy-formulario-server", url = "http://10.3.14.121:5552/ztgateway/zy-formulario-server")
@FeignClient(name = "zy-formulario-server")
public interface ZyFormularioFeignclient {

    String AUTHOTIZATION = "Authorization";

    @GetMapping("/api/formulario/v1/consultar/combinatoria/{formIdExterno}")
    ResponseEntity<Response<List<RsCombinatoriaPlantillaDto>>> consultarCombinatoria(@PathVariable String formIdExterno,
                                                                                            @RequestHeader(name = AUTHOTIZATION) String bearerToken);
}
