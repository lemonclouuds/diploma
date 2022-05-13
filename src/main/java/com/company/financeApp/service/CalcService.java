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

    private List<CategoryDto> combineDefaultAndAddedByUserCategories(Long userId) {
        List<CategoryDto> categoryDtoList = categoryService.findAllByUserId(userId);
        List<CategoryDto> defaultCategories = categoryService.findDefaultCategories(userId);
        categoryDtoList.addAll(defaultCategories);
        return categoryDtoList;
    }

    public Map<Long, Double> calcUserTransactionsValueByCategoryFromLastMonth(Long userId) { //byLastMonth?
        List<CategoryDto> categories = combineDefaultAndAddedByUserCategories(userId);
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
