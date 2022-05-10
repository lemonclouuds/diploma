package com.company.financeApp.service;

import com.company.financeApp.domain.category.Category;
import com.company.financeApp.domain.category.CategoryType;
import com.company.financeApp.domain.dto.CategoryDto;
import com.company.financeApp.domain.user.User;
import com.company.financeApp.domain.user.UserRole;
import com.company.financeApp.exception.EntityAlreadyExistsException;
import com.company.financeApp.helper.MapperHelper;
import com.company.financeApp.repository.CategoryRepository;
import com.company.financeApp.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static com.company.financeApp.constants.ExceptionMessage.CATEGORY_WITH_ID_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private static final ModelMapper modelMapper = new ModelMapper();

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;

    private final UserService userService;

    public List<CategoryDto> findAll() {
        return MapperHelper.mapList(categoryRepository.findAll(), CategoryDto.class);
    }

    public Category findById(Long categoryId) {
        return categoryRepository.findById(categoryId).orElseThrow(
                () -> new EntityNotFoundException(String.format(CATEGORY_WITH_ID_NOT_FOUND, categoryId)));
    }

    public CategoryDto findDtoById(Long categoryId) {
        return modelMapper.map(findById(categoryId), CategoryDto.class);
    }

    @Transactional
    public CategoryDto create(CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        if (!categoryRepository.existsById(categoryDto.getId())) {
            User user = userService.findById(categoryDto.getUserId());
            CategoryType type = CategoryType.ADDED_BY_USER;
            if (user.getUserRole().equals(UserRole.ADMIN)) {
                type = CategoryType.DEFAULT;
            }
            category.setCategoryType(type);
            category.setUser(user);
            category.setTransactions(new ArrayList<>());

            categoryRepository.save(category);
        } else {
            throw new EntityAlreadyExistsException(String.format("Category[%d] already exists",
                    categoryDto.getId()));
        }
        return categoryDto;
    }

    @Transactional
    public CategoryDto update(Long categoryId, CategoryDto categoryDto) {
        Category category = modelMapper.map(categoryDto, Category.class);
        category.setId(categoryId);
        if (categoryRepository.existsById(categoryId)) {
            User user = userService.findById(categoryDto.getUserId());
            category.setUser(user);
            categoryRepository.save(category);
        } else {
            throw new EntityNotFoundException(String.format(CATEGORY_WITH_ID_NOT_FOUND, categoryId));
        }
        return categoryDto;
    }

    @Transactional
    public void deleteById(Long id) {
        if (categoryRepository.existsById(id)) {
            categoryRepository.deleteById(id);
        }
    }

    @Transactional
    public void deleteByIdIn(Collection<Long> ids) {
        for (Long id : ids) {
            deleteById(id);
        }
    }

    @Transactional
    public List<CategoryDto> findAllByUserId(Long userId) {
        return MapperHelper.mapList(categoryRepository.findAllByUserId(userId), CategoryDto.class);
    }

    @Transactional
    public CategoryDto findUserCategoryById(Long userId, Long categoryId) {
        Category found = userService.findById(userId).getCategories()
                .stream()
                .filter(category -> categoryId.equals(category.getId()))
                .findFirst()
                .orElseThrow(
                        () -> new EntityNotFoundException(String.format(CATEGORY_WITH_ID_NOT_FOUND, categoryId)));
        return modelMapper.map(found, CategoryDto.class);
    }

    @Transactional
    public List<CategoryDto> findAddedByUserCategories(Long userId) {
        return MapperHelper.mapList(categoryRepository.findCategoriesByCategoryTypeAndAndUserId(CategoryType.ADDED_BY_USER, userId),
                CategoryDto.class);
    }

}
