package io.venefact.venefact.type_transaction.service;

import io.venefact.venefact.events.BeforeDeleteTypeTransaction;
import io.venefact.venefact.type_transaction.domain.TypeTransaction;
import io.venefact.venefact.type_transaction.model.TypeTransactionDTO;
import io.venefact.venefact.type_transaction.repos.TypeTransactionRepository;
import io.venefact.venefact.util.NotFoundException;
import java.util.List;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TypeTransactionService {

    private final TypeTransactionRepository typeTransactionRepository;
    private final ApplicationEventPublisher publisher;

    public TypeTransactionService(final TypeTransactionRepository typeTransactionRepository,
            final ApplicationEventPublisher publisher) {
        this.typeTransactionRepository = typeTransactionRepository;
        this.publisher = publisher;
    }

    public List<TypeTransactionDTO> findAll() {
        final List<TypeTransaction> typeTransactions = typeTransactionRepository.findAll(Sort.by("id"));
        return typeTransactions.stream()
                .map(typeTransaction -> mapToDTO(typeTransaction, new TypeTransactionDTO()))
                .toList();
    }

    public TypeTransactionDTO get(final Long id) {
        return typeTransactionRepository.findById(id)
                .map(typeTransaction -> mapToDTO(typeTransaction, new TypeTransactionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TypeTransactionDTO typeTransactionDTO) {
        final TypeTransaction typeTransaction = new TypeTransaction();
        mapToEntity(typeTransactionDTO, typeTransaction);
        return typeTransactionRepository.save(typeTransaction).getId();
    }

    public void update(final Long id, final TypeTransactionDTO typeTransactionDTO) {
        final TypeTransaction typeTransaction = typeTransactionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(typeTransactionDTO, typeTransaction);
        typeTransactionRepository.save(typeTransaction);
    }

    public void delete(final Long id) {
        final TypeTransaction typeTransaction = typeTransactionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteTypeTransaction(id));
        typeTransactionRepository.delete(typeTransaction);
    }

    private TypeTransactionDTO mapToDTO(final TypeTransaction typeTransaction,
            final TypeTransactionDTO typeTransactionDTO) {
        typeTransactionDTO.setId(typeTransaction.getId());
        typeTransactionDTO.setName(typeTransaction.getName());
        return typeTransactionDTO;
    }

    private TypeTransaction mapToEntity(final TypeTransactionDTO typeTransactionDTO,
            final TypeTransaction typeTransaction) {
        typeTransaction.setName(typeTransactionDTO.getName());
        return typeTransaction;
    }

    public boolean nameExists(final String name) {
        return typeTransactionRepository.existsByNameIgnoreCase(name);
    }

}
