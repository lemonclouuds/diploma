package com.company.financeApp.service;

import com.company.financeApp.domain.Transaction;
import com.company.financeApp.domain.category.Category;
import com.company.financeApp.domain.dto.TransactionDto;
import com.company.financeApp.domain.user.User;
import com.company.financeApp.exception.EntityAlreadyExistsException;
import com.company.financeApp.helper.MapperHelper;
import com.company.financeApp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import static com.company.financeApp.constants.ExceptionMessage.TRANSACTION_WITH_ID_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class TransactionService {
    private static ModelMapper modelMapper = new ModelMapper();

    private final TransactionRepository transactionRepository;
    private final CategoryService categoryService;

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
    public List<TransactionDto> findAllByCategoryId(Long categoryId) {
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
    public TransactionDto create(TransactionDto transactionDto) {
        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);
        if (!transactionRepository.existsById(transactionDto.getId())) {
            User user = userService.findById(transactionDto.getUserId());
            Category category = categoryService.findById(transactionDto.getCategoryId());
            transaction.setUser(user);
            transaction.setCategory(category);
            transactionRepository.save(transaction);
        } else {
            throw new EntityAlreadyExistsException(String.format("Transaction with id[%d] already exists",
                    transactionDto.getId()));
        }
        return transactionDto;
    }

    @Transactional
    public TransactionDto update(Long transactionId, TransactionDto transactionDto) {
        Transaction transaction = modelMapper.map(transactionDto, Transaction.class);
        transaction.setId(transactionId);
        if (transactionRepository.existsById(transactionId)) {
            User user = userService.findById(transactionDto.getUserId());
            Category category = categoryService.findById(transactionDto.getCategoryId());
            transaction.setUser(user);
            transaction.setCategory(category);
            transactionRepository.save(transaction);
        } else {
            throw new EntityNotFoundException(String.format(TRANSACTION_WITH_ID_NOT_FOUND, transactionId));
        }
        return transactionDto;
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

    @Transactional
    public List<TransactionDto> findAllByUserIdAndCategoryIdFromLastMonth(Long userId, Long categoryId) {
        LocalDate currentDate = LocalDate.now();
        int year = currentDate.getYear();
        int month = currentDate.minusMonths(1L).getMonthValue();
        int day = currentDate.getDayOfMonth();
        List<Transaction> found = transactionRepository
                .findAllByUserIdAndDateGreaterThanEqualAndDateLessThanEqualAndCategory_Id(userId, LocalDate.of(year, month, day), currentDate, categoryId);
        return MapperHelper.mapList(found, TransactionDto.class);
    }


}
