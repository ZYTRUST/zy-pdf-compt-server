package com.zy.ws.mgpdf.service;

import com.zy.lib.dtos.response.formu.RsCombinatoriaPlantillaDto;
import com.zy.lib.message.resource.ErroresEnum;
import com.zy.lib.service.exception.ZyException;
import com.zy.lib.service.response.Response;
import com.zy.ws.mgpdf.conf.TrackExecutionTime;
import com.zy.ws.mgpdf.feignclient.ZyFormularioFeignclient;
import feign.FeignException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ZyFormularioService {

    @Autowired
    private ZyFormularioFeignclient zyFormularioFeignclient;

    @TrackExecutionTime(operation = "CONSULTAR_COMBINATORIA_FORM_SERVER")
    public List<RsCombinatoriaPlantillaDto> consultarCombinatoria(String formIdExterno, String token) throws ZyException {
        log.info("Inicia llamado a servicio consultarCombinatoria de procesoId: {}", formIdExterno);
        ResponseEntity<Response<List<RsCombinatoriaPlantillaDto>>> responseEntity;
        Response<List<RsCombinatoriaPlantillaDto>> response;

        try {
            responseEntity = zyFormularioFeignclient.consultarCombinatoria(formIdExterno, token);
        } catch (FeignException e) {
            log.error("Ocurrio un error al intentar llamar consultarCombinatoria de procesoId: {}", formIdExterno);
            log.error(e.getMessage());
            throw new ZyException(ErroresEnum.GLOBAL_FEIGNCLIENT_EXCEPTION);
        }

        if (responseEntity.getStatusCode() != HttpStatus.OK){
            log.error("ERROR AL CONSUMIR SERVICIO consultarCombinatoria - ZYFORMULARIO: {}", responseEntity.getStatusCode());
            throw new ZyException(ErroresEnum.GLOBAL_FEIGNCLIENT_ERROR);
        }

        if (!responseEntity.hasBody() || responseEntity.getBody() == null){
            log.error("ResponseEntity nulo en consultarCombinatoria - ZYFORMULARIO: {}", responseEntity.getStatusCode());
            throw new ZyException(ErroresEnum.FEIGNCLIENT_BODY_NULL);
        }

        response = responseEntity.getBody();

        if (response.getError() != ErroresEnum.OK) {
            log.error("Ocurrio un error al intentar consultarCombinatoria de procesoId: {}", formIdExterno);
            log.error("Error -> {}:{}", response.getError().codigo(), response.getError().mensaje());
            throw new ZyException(response.getError());
        }

        return response.getData();
    }
}
