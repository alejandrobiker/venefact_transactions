package io.venefact.venefact.type_rate.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.venefact.venefact.type_rate.model.TypeRateDTO;
import io.venefact.venefact.type_rate.service.TypeRateService;
import io.venefact.venefact.util.ResponseAPI;
import jakarta.validation.Valid;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping(value = "/api/typeRates", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Tasa", description = "Tasa de conversión monetaria")
public class TypeRateResource {

    @Autowired
    private  TypeRateService typeRateService;

    @GetMapping
    public ResponseEntity<ResponseAPI<List<TypeRateDTO>>> getAllTypeRates() {
        List<TypeRateDTO> typeRates = typeRateService.findAll();
        return new ResponseEntity<>(new ResponseAPI<>("Lista de tipos de tasas", typeRates), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseAPI<TypeRateDTO>> getTypeRate(@PathVariable(name = "id") final Long id) {
        TypeRateDTO typeRateDTO = typeRateService.getById(id);
        return new ResponseEntity<>(new ResponseAPI<>("Tipo de tasa", typeRateDTO), HttpStatus.OK);
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<ResponseAPI<TypeRateDTO>> createTypeRate(@RequestBody @Valid final TypeRateDTO typeRateDTO) {
        TypeRateDTO typeRateCreated = typeRateService.create(typeRateDTO);
        return new ResponseEntity<>(new ResponseAPI<>("Tipo de tasa creada", typeRateCreated), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseAPI<TypeRateDTO>> updateTypeRate(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final TypeRateDTO typeRateDTO) {
        TypeRateDTO typeRateUpdate = typeRateService.update(id, typeRateDTO);
        return new ResponseEntity<>(new ResponseAPI<>("Tipo de tasa actualizada", typeRateUpdate), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<ResponseAPI<Boolean>> deleteTypeRate(@PathVariable(name = "id") final Long id) {
        typeRateService.delete(id);
        return new ResponseEntity<>(new ResponseAPI<>("Tipo de tasa eliminada", true), HttpStatus.OK);
    }

}
