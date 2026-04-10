package io.venefact.venefact.type_transaction.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TypeTransactionDTO {

    private Long id;

    @NotNull
    @Size(max = 255)
    @TypeTransactionNameUnique
    private String name;

}
