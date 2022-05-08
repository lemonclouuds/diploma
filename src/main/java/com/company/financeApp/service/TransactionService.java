package com.company.financeApp.service;

import com.company.financeApp.domain.Transaction;
import com.company.financeApp.domain.dto.TransactionDto;
import com.company.financeApp.helper.MapperHelper;
import com.company.financeApp.repository.CategoryRepository;
import com.company.financeApp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;

import static com.company.financeApp.constants.ExceptionMessage.TRANSACTION_WITH_ID_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private static ModelMapper modelMapper = new ModelMapper();

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    private final UserService userService;

    public List<TransactionDto> findAll() {
        return MapperHelper.mapList(transactionRepository.findAll(), TransactionDto.class);
    }

    public Transaction findById(Long transactionId) {
        return transactionRepository.findById(transactionId).orElseThrow(
                () -> new EntityNotFoundException(String.format(TRANSACTION_WITH_ID_NOT_FOUND, transactionId)));
    }

    public TransactionDto findDtoById(Long transactionId) {
        return modelMapper.map(findById(transactionId), TransactionDto.class);
    }

    @Transactional
    public List<Transaction> findAllByCategoryId(Long categoryId) {
        return transactionRepository.findAllByCategory_Id(categoryId);
    }

    @Transactional
    public List<TransactionDto> findTransactionsDtoByCategoryId(Long categoryId) {
        return MapperHelper.mapList(transactionRepository.findAllByCategory_Id(categoryId), TransactionDto.class);
    }

    @Transactional
    public List<TransactionDto> findAllByUserId(Long userId) {
        return MapperHelper.mapList(transactionRepository.findAllByUserId(userId), TransactionDto.class);
    }

    @Transactional
    public TransactionDto getUserTransactionById(Long userId, Long transactionId) {
        Transaction found = userService.findById(userId).getTransactions()
                .stream()
                .filter(transaction -> transactionId.equals(transaction.getId()))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format(TRANSACTION_WITH_ID_NOT_FOUND, transactionId)));
        return modelMapper.map(found, TransactionDto.class);
    }

    @Transactional
    public void deleteById(Long id) {
        if (transactionRepository.existsById(id)) {
            transactionRepository.deleteById(id);
        }
    }

    @Transactional
    public void deleteByIdIn(Collection<Long> ids) {
        for (Long id : ids) {
            deleteById(id);
        }
    }

}
