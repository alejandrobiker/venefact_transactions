package io.venefact.venefact.transaction.service;

import io.venefact.venefact.category.domain.Category;
import io.venefact.venefact.category.model.CategoryDTO;
import io.venefact.venefact.category.repos.CategoryRepository;
import io.venefact.venefact.events.BeforeDeleteCategory;
import io.venefact.venefact.events.BeforeDeleteTypeRate;
import io.venefact.venefact.events.BeforeDeleteTypeTransaction;
import io.venefact.venefact.transaction.domain.Transaction;
import io.venefact.venefact.transaction.model.TransactionDTO;
import io.venefact.venefact.transaction.repos.TransactionRepository;
import io.venefact.venefact.type_rate.domain.TypeRate;
import io.venefact.venefact.type_rate.model.TypeRateDTO;
import io.venefact.venefact.type_rate.repos.TypeRateRepository;
import io.venefact.venefact.type_transaction.domain.TypeTransaction;
import io.venefact.venefact.type_transaction.model.TypeTransactionDTO;
import io.venefact.venefact.type_transaction.repos.TypeTransactionRepository;
import io.venefact.venefact.util.NotFoundException;
import io.venefact.venefact.util.ReferencedException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private TypeTransactionRepository typeTransactionRepository;

    @Autowired
    private TypeRateRepository typeRateRepository;

    public List<TransactionDTO> findAllTest() {
        final List<Transaction> transactions = transactionRepository.findAll(Sort.by("id").descending());
        return transactions.stream()
                .map(this::mapToDTO)
                .toList();
    }

    public TransactionDTO getById(final Long id) {
        return transactionRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(NotFoundException::new);
    }

    public TransactionDTO create(final TransactionDTO transactionDTO) {
        final Transaction transaction = new Transaction();
        Transaction transactionMap = mapToEntity(transactionDTO, transaction);
        return mapToDTO(transactionRepository.save(transactionMap));
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

    private TransactionDTO mapToDTO(final Transaction transaction) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setId(transaction.getId());
        transactionDTO.setTitle(transaction.getTitle());
        transactionDTO.setDescription(transaction.getDescription());
        transactionDTO.setAmount(transaction.getAmount());
        transactionDTO.setDate(transaction.getDate());

        final Optional<Category> category = categoryRepository.findById(transaction.getCategoryId().getId());
        if (category.isPresent()) {
            CategoryDTO categoryDTO = new CategoryDTO();
            categoryDTO.setId(category.get().getId());
            categoryDTO.setName(category.get().getName());
            transactionDTO.setCategory(categoryDTO);
        }

        final Optional<TypeTransaction> typeTransaction = typeTransactionRepository.findById(transaction.getTypeTransactionId().getId());
        if (typeTransaction.isPresent()) {
            TypeTransactionDTO typeTransactionDTO = new TypeTransactionDTO();
            typeTransactionDTO.setId(typeTransaction.get().getId());
            typeTransactionDTO.setName(typeTransaction.get().getName());
            transactionDTO.setTypeTransaction(typeTransactionDTO);
        }

        if (transaction.getTypeRateId() != null) {
            final Optional<TypeRate> typeRate = typeRateRepository.findById(transaction.getTypeRateId().getId());
            if (typeRate.isPresent()) {
                TypeRateDTO typeRateDTO = new TypeRateDTO();
                typeRateDTO.setId(typeRate.get().getId());
                typeRateDTO.setName(typeRate.get().getName());
                transactionDTO.setTypeRate(typeRateDTO);
            }
        }

        return transactionDTO;
    }

    private Transaction mapToEntity(final TransactionDTO transactionDTO, final Transaction transaction) {
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
