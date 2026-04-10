package io.venefact.venefact.transaction.service;

import io.venefact.venefact.category.domain.Category;
import io.venefact.venefact.category.repos.CategoryRepository;
import io.venefact.venefact.events.BeforeDeleteCategory;
import io.venefact.venefact.events.BeforeDeleteTypeRate;
import io.venefact.venefact.events.BeforeDeleteTypeTransaction;
import io.venefact.venefact.transaction.domain.Transaction;
import io.venefact.venefact.transaction.model.TransactionDTO;
import io.venefact.venefact.transaction.repos.TransactionRepository;
import io.venefact.venefact.type_rate.domain.TypeRate;
import io.venefact.venefact.type_rate.repos.TypeRateRepository;
import io.venefact.venefact.type_transaction.domain.TypeTransaction;
import io.venefact.venefact.type_transaction.repos.TypeTransactionRepository;
import io.venefact.venefact.util.NotFoundException;
import io.venefact.venefact.util.ReferencedException;
import java.util.List;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final CategoryRepository categoryRepository;
    private final TypeTransactionRepository typeTransactionRepository;
    private final TypeRateRepository typeRateRepository;

    public TransactionService(final TransactionRepository transactionRepository,
            final CategoryRepository categoryRepository,
            final TypeTransactionRepository typeTransactionRepository,
            final TypeRateRepository typeRateRepository) {
        this.transactionRepository = transactionRepository;
        this.categoryRepository = categoryRepository;
        this.typeTransactionRepository = typeTransactionRepository;
        this.typeRateRepository = typeRateRepository;
    }

    public List<TransactionDTO> findAll() {
        final List<Transaction> transactions = transactionRepository.findAll(Sort.by("id"));
        return transactions.stream()
                .map(transaction -> mapToDTO(transaction, new TransactionDTO()))
                .toList();
    }

    public TransactionDTO get(final Long id) {
        return transactionRepository.findById(id)
                .map(transaction -> mapToDTO(transaction, new TransactionDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public Long create(final TransactionDTO transactionDTO) {
        final Transaction transaction = new Transaction();
        mapToEntity(transactionDTO, transaction);
        return transactionRepository.save(transaction).getId();
    }

    public void update(final Long id, final TransactionDTO transactionDTO) {
        final Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        mapToEntity(transactionDTO, transaction);
        transactionRepository.save(transaction);
    }

    public void delete(final Long id) {
        final Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(NotFoundException::new);
        transactionRepository.delete(transaction);
    }

    private TransactionDTO mapToDTO(final Transaction transaction,
            final TransactionDTO transactionDTO) {
        transactionDTO.setId(transaction.getId());
        transactionDTO.setTitle(transaction.getTitle());
        transactionDTO.setDescription(transaction.getDescription());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setDate(transaction.getDate());
        transactionDTO.setCategoryId(transaction.getCategoryId() == null ? null : transaction.getCategoryId().getId());
        transactionDTO.setTypeTransactionId(transaction.getTypeTransactionId() == null ? null : transaction.getTypeTransactionId().getId());
        transactionDTO.setTypeRateId(transaction.getTypeRateId() == null ? null : transaction.getTypeRateId().getId());
        return transactionDTO;
    }

    private Transaction mapToEntity(final TransactionDTO transactionDTO,
            final Transaction transaction) {
        transaction.setTitle(transactionDTO.getTitle());
        transaction.setDescription(transactionDTO.getDescription());
        transaction.setAmount(transactionDTO.getAmount());
        transaction.setDate(transactionDTO.getDate());
        final Category categoryId = transactionDTO.getCategoryId() == null ? null : categoryRepository.findById(transactionDTO.getCategoryId())
                .orElseThrow(() -> new NotFoundException("categoryId not found"));
        transaction.setCategoryId(categoryId);
        final TypeTransaction typeTransactionId = transactionDTO.getTypeTransactionId() == null ? null : typeTransactionRepository.findById(transactionDTO.getTypeTransactionId())
                .orElseThrow(() -> new NotFoundException("typeTransactionId not found"));
        transaction.setTypeTransactionId(typeTransactionId);
        final TypeRate typeRateId = transactionDTO.getTypeRateId() == null ? null : typeRateRepository.findById(transactionDTO.getTypeRateId())
                .orElseThrow(() -> new NotFoundException("typeRateId not found"));
        transaction.setTypeRateId(typeRateId);
        return transaction;
    }

    @EventListener(BeforeDeleteCategory.class)
    public void on(final BeforeDeleteCategory event) {
        final ReferencedException referencedException = new ReferencedException();
        final Transaction categoryIdTransaction = transactionRepository.findFirstByCategoryIdId(event.getId());
        if (categoryIdTransaction != null) {
            referencedException.setKey("category.transaction.categoryId.referenced");
            referencedException.addParam(categoryIdTransaction.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteTypeTransaction.class)
    public void on(final BeforeDeleteTypeTransaction event) {
        final ReferencedException referencedException = new ReferencedException();
        final Transaction typeTransactionIdTransaction = transactionRepository.findFirstByTypeTransactionIdId(event.getId());
        if (typeTransactionIdTransaction != null) {
            referencedException.setKey("typeTransaction.transaction.typeTransactionId.referenced");
            referencedException.addParam(typeTransactionIdTransaction.getId());
            throw referencedException;
        }
    }

    @EventListener(BeforeDeleteTypeRate.class)
    public void on(final BeforeDeleteTypeRate event) {
        final ReferencedException referencedException = new ReferencedException();
        final Transaction typeRateIdTransaction = transactionRepository.findFirstByTypeRateIdId(event.getId());
        if (typeRateIdTransaction != null) {
            referencedException.setKey("typeRate.transaction.typeRateId.referenced");
            referencedException.addParam(typeRateIdTransaction.getId());
            throw referencedException;
        }
    }

}
