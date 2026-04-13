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
                .map(this::mapToDTO)
                .toList();
    }

    public TypeTransactionDTO getById(final Long id) {
        return typeTransactionRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(NotFoundException::new);
    }

    public TypeTransactionDTO create(final TypeTransactionDTO typeTransactionDTO) {
        final TypeTransaction typeTransaction = new TypeTransaction();
        typeTransaction.setName(typeTransactionDTO.getName());
        typeTransactionRepository.save(typeTransaction);
        return mapToDTO(typeTransaction);
    }

    public TypeTransactionDTO update(final Long id, final TypeTransactionDTO typeTransactionDTO) {
        final TypeTransaction typeTransaction = typeTransactionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        typeTransaction.setName(typeTransactionDTO.getName());
        typeTransactionRepository.save(typeTransaction);
        return mapToDTO(typeTransaction);
    }

    public void delete(final Long id) {
        final TypeTransaction typeTransaction = typeTransactionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        publisher.publishEvent(new BeforeDeleteTypeTransaction(id));
        typeTransactionRepository.delete(typeTransaction);
    }

    private TypeTransactionDTO mapToDTO(final TypeTransaction typeTransaction) {
        TypeTransactionDTO typeTransactionDTO = new TypeTransactionDTO();
        typeTransactionDTO.setId(typeTransaction.getId());
        typeTransactionDTO.setName(typeTransaction.getName());
        return typeTransactionDTO;
    }

    public boolean nameExists(final String name) {
        return typeTransactionRepository.existsByNameIgnoreCase(name);
    }

}
