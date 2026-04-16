package io.venefact.venefact.transaction.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import io.venefact.venefact.category.model.CategoryDTO;
import io.venefact.venefact.type_rate.model.TypeRateDTO;
import io.venefact.venefact.type_transaction.model.TypeTransactionDTO;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TransactionDTO {


    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private Long id;

    @NotNull
    @Size(max = 255)
    private String title;

    @Size(max = 255)
    private String description;

    @NotNull
    @Digits(integer = 10, fraction = 2)
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Schema(type = "string", example = "92.08")
    private BigDecimal amount;

    @NotNull
    private LocalDateTime date;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long categoryId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private CategoryDTO category;

    @NotNull
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long typeTransactionId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private TypeTransactionDTO typeTransaction;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long typeRateId;

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private TypeRateDTO typeRate;
}
