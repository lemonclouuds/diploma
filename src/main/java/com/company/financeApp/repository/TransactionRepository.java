package com.company.financeApp.repository;

import com.company.financeApp.domain.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findAllByCategory_Id(Long categoryId);
    List<Transaction> findAllByUserId(Long userId);
    List<Transaction> findAllByUserIdAndDateGreaterThanEqualAndDateLessThanEqualAndCategory_Id(Long userId, LocalDate startDate,
                                                                                               LocalDate endDate, Long categoryId);

}
