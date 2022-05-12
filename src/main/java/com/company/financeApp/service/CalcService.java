package com.company.financeApp.service;

import com.company.financeApp.domain.dto.CategoryDto;
import com.company.financeApp.domain.dto.TransactionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CalcService {

    private final CategoryService categoryService;
    private final TransactionService transactionService;

    public Map<Long, Double> calculateUserExpensesByCategory(Long userId) {
        List<CategoryDto> categoryDtoList = categoryService.findAllByUserId(userId);
        List<CategoryDto> defaultCategories = categoryService.findDefaultCategories(userId);
        for (CategoryDto category : defaultCategories) {
            categoryDtoList.add(category);
        }
        return calcUserTransactionsValueByCategoryFromLastMonth(categoryDtoList, userId);
    }

    private Map<Long, Double> calcUserTransactionsValueByCategoryFromLastMonth(List<CategoryDto> categories, Long userId) { //byLastMonth?
        Map<Long, Double> res = new HashMap<>();
        for (CategoryDto category : categories) {
            List<TransactionDto> transactions = transactionService.findAllByUserIdAndCategoryIdFromLastMonth(userId, category.getId()); //and userId
            res.put(category.getId(), calcTransactionListValue(transactions));
        }
        return res;
    }

    private Double calcTransactionListValue(List<TransactionDto> transactionDtos) {
        Double res = 0.0;
        for (TransactionDto transaction : transactionDtos) {
            res += transaction.getValue();
        }
        return res;
    }



}
