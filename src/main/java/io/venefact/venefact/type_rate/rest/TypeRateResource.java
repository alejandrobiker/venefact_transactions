package io.venefact.venefact.type_rate.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.venefact.venefact.type_rate.model.TypeRateDTO;
import io.venefact.venefact.type_rate.service.TypeRateService;
import jakarta.validation.Valid;
import java.util.List;
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
public class TypeRateResource {

    private final TypeRateService typeRateService;

    public TypeRateResource(final TypeRateService typeRateService) {
        this.typeRateService = typeRateService;
    }

    @GetMapping
    public ResponseEntity<List<TypeRateDTO>> getAllTypeRates() {
        return ResponseEntity.ok(typeRateService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeRateDTO> getTypeRate(@PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(typeRateService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTypeRate(@RequestBody @Valid final TypeRateDTO typeRateDTO) {
        final Long createdId = typeRateService.create(typeRateDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTypeRate(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final TypeRateDTO typeRateDTO) {
        typeRateService.update(id, typeRateDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTypeRate(@PathVariable(name = "id") final Long id) {
        typeRateService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
