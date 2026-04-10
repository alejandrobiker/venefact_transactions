package io.venefact.venefact.type_transaction.repos;

import io.venefact.venefact.type_transaction.domain.TypeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TypeTransactionRepository extends JpaRepository<TypeTransaction, Long> {

    boolean existsByNameIgnoreCase(String name);

}
