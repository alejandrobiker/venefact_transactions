package io.venefact.venefact.type_transaction.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.venefact.venefact.type_transaction.model.TypeTransactionDTO;
import io.venefact.venefact.type_transaction.service.TypeTransactionService;
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
@RequestMapping(value = "/api/typeTransactions", produces = MediaType.APPLICATION_JSON_VALUE)
public class TypeTransactionResource {

    private final TypeTransactionService typeTransactionService;

    public TypeTransactionResource(final TypeTransactionService typeTransactionService) {
        this.typeTransactionService = typeTransactionService;
    }

    @GetMapping
    public ResponseEntity<List<TypeTransactionDTO>> getAllTypeTransactions() {
        return ResponseEntity.ok(typeTransactionService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TypeTransactionDTO> getTypeTransaction(
            @PathVariable(name = "id") final Long id) {
        return ResponseEntity.ok(typeTransactionService.get(id));
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTypeTransaction(
            @RequestBody @Valid final TypeTransactionDTO typeTransactionDTO) {
        final Long createdId = typeTransactionService.create(typeTransactionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTypeTransaction(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final TypeTransactionDTO typeTransactionDTO) {
        typeTransactionService.update(id, typeTransactionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTypeTransaction(@PathVariable(name = "id") final Long id) {
        typeTransactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
