package com.company.financeApp.service;

import com.company.financeApp.domain.category.Category;
import com.company.financeApp.domain.dto.CategoryDto;
import com.company.financeApp.domain.dto.TransactionDto;
import com.company.financeApp.helper.MapperHelper;
import com.company.financeApp.repository.CategoryRepository;
import com.company.financeApp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;

import static com.company.financeApp.constants.ExceptionMessage.CATEGORY_WITH_ID_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private static ModelMapper modelMapper = new ModelMapper();

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    private final UserService userService;

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new EntityNotFoundException(String.format(CATEGORY_WITH_ID_NOT_FOUND, categoryId)));
    }

    @Transactional
    public List<TransactionDto> getCategoryTransactions(Long categoryId) {
        return MapperHelper.mapList(transactionRepository.findAllByCategory_Id(categoryId), TransactionDto.class);
    }

    @Transactional
    public List<CategoryDto> findAllByUserId(Long userId) {
        return MapperHelper.mapList(categoryRepository.findAllByUserId(userId), CategoryDto.class);
    }

}
