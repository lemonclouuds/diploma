package com.company.financeApp.repository;

import com.company.financeApp.domain.category.Category;
import com.company.financeApp.domain.category.CategoryType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findCategoriesByCategoryTypeAndUserId(CategoryType categoryType, Long userId);
    List<Category> findAllByUserId(Long userId);
    List<Category> findCategoriesByCategoryType(CategoryType categoryType);
}
