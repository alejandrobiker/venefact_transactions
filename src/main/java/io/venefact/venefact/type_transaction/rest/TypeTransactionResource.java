package io.venefact.venefact.type_transaction.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.venefact.venefact.category.model.CategoryDTO;
import io.venefact.venefact.type_transaction.model.TypeTransactionDTO;
import io.venefact.venefact.type_transaction.service.TypeTransactionService;
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
@RequestMapping(value = "/api/typeTransactions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(
    name = "Tipo de Transacción",
    description = "Permite clasificar los movimientos como 'Ingreso' o 'Gasto'"
)
public class TypeTransactionResource {

    @Autowired
    private TypeTransactionService typeTransactionService;

    @GetMapping
    public ResponseEntity<ResponseAPI<List<TypeTransactionDTO>>> getAllTypeTransactions() {
        List<TypeTransactionDTO> typeTransactions = typeTransactionService.findAll();
        return new ResponseEntity<>(new ResponseAPI<>("Lista de tipos de transacciones", typeTransactions), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseAPI<TypeTransactionDTO>> getTypeTransaction(
            @PathVariable(name = "id") final Long id) {
        TypeTransactionDTO tipoTransactionDto = typeTransactionService.getById(id);
        return new ResponseEntity<>(new ResponseAPI<>("Tipo de transacción", tipoTransactionDto), HttpStatus.OK);
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<ResponseAPI<TypeTransactionDTO>> createTypeTransaction(
            @RequestBody @Valid final TypeTransactionDTO typeTransactionDTO) {
        TypeTransactionDTO typeTransactionCreated = typeTransactionService.create(typeTransactionDTO);
        return new ResponseEntity<>(new ResponseAPI<>("Tipo de transacción creada", typeTransactionCreated), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ResponseAPI<TypeTransactionDTO>> updateTypeTransaction(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final TypeTransactionDTO typeTransactionDTO) {
        TypeTransactionDTO typeTransactionUpdate = typeTransactionService.update(id, typeTransactionDTO);
        return new ResponseEntity<>(new ResponseAPI<>("Tipo de transacción actualizada", typeTransactionUpdate), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<ResponseAPI<Boolean>> deleteTypeTransaction(@PathVariable(name = "id") final Long id) {
        typeTransactionService.delete(id);
        return new ResponseEntity<>(new ResponseAPI<>("Tipo de transacción eliminada", true), HttpStatus.OK);
    }

}
