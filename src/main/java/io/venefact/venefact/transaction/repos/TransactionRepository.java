package io.venefact.venefact.transaction.repos;

import io.venefact.venefact.transaction.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Transaction findFirstByCategoryIdId(Long id);

    Transaction findFirstByTypeTransactionIdId(Long id);

    Transaction findFirstByTypeRateIdId(Long id);

}
