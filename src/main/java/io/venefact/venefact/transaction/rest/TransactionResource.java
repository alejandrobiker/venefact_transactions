package io.venefact.venefact.transaction.rest;

import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.venefact.venefact.transaction.model.TransactionDTO;
import io.venefact.venefact.transaction.service.TransactionService;
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
@RequestMapping(value = "/api/transactions", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(
    name = "Transacción",
    description = "Módulo principal para la gestión de movimientos financieros. " +
        "Permite registrar, actualizar y consultar ingresos y gastos, " +
        "vinculándolos con categorías específicas y aplicando la tasa de cambio correspondiente."
)
public class TransactionResource {

    @Autowired
    private TransactionService transactionService;

    @GetMapping()
    public ResponseEntity<ResponseAPI<List<TransactionDTO>>> getAllTransactionsTest() {
        List<TransactionDTO> transactions = transactionService.findAllTest();
        return new ResponseEntity<>(new ResponseAPI<>("Lista de transacciones", transactions), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseAPI<TransactionDTO>> getTransaction(@PathVariable(name = "id") final Long id) {
        TransactionDTO transactionDTO = transactionService.getById(id);
        return new ResponseEntity<>(new ResponseAPI<>("Transacción", transactionDTO), HttpStatus.OK);
    }

    @PostMapping
    @ApiResponse(responseCode = "201")
    public ResponseEntity<Long> createTransaction(
            @RequestBody @Valid final TransactionDTO transactionDTO) {
        final Long createdId = transactionService.create(transactionDTO);
        return new ResponseEntity<>(createdId, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Long> updateTransaction(@PathVariable(name = "id") final Long id,
            @RequestBody @Valid final TransactionDTO transactionDTO) {
        transactionService.update(id, transactionDTO);
        return ResponseEntity.ok(id);
    }

    @DeleteMapping("/{id}")
    @ApiResponse(responseCode = "204")
    public ResponseEntity<Void> deleteTransaction(@PathVariable(name = "id") final Long id) {
        transactionService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
