package com.company.financeApp.controllers;

import com.company.financeApp.domain.dto.CategoryDto;
import com.company.financeApp.domain.dto.TransactionDto;
import com.company.financeApp.domain.dto.UserDto;
import com.company.financeApp.service.CalcService;
import com.company.financeApp.service.CategoryService;
import com.company.financeApp.service.TransactionService;
import com.company.financeApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final CategoryService categoryService;
    private final TransactionService transactionService;
    private final CalcService calcService;
    private final BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/users")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<List<UserDto>> getAll() {
        return ResponseEntity.ok(userService.findAll());
    }

    @PostMapping("/users")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<UserDto> create(@RequestBody UserDto userDto) {
        userDto.setPassword(passwordEncoder.encode(userDto.getPassword()));
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(userService.create(userDto));
    }

    @GetMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<UserDto> getById(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.findDtoById(userId));
    }

    @PutMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<UserDto> update(@PathVariable Long userId, @RequestBody UserDto userDto) {
        return ResponseEntity.ok(userService.update(userId, userDto));
    }

    @DeleteMapping("/users/{userId}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> delete(@PathVariable Long userId) {
        userService.deleteById(userId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(String.format("User[%d] was deleted", userId));
    }

    /*@GetMapping("/users/{userId}/categories")
    public ResponseEntity<List<CategoryDto>> getUserCategories(@PathVariable Long userId) {
        return ResponseEntity.ok(categoryService.findAllByUserId(userId));
    }*/

    @GetMapping("/users/{userId}/categories/custom")
    @PreAuthorize("hasAuthority('category:read')")
    public ResponseEntity<List<CategoryDto>> getUserCustomCategories(@PathVariable Long userId) {
        return ResponseEntity.ok(categoryService.findAddedByUserCategories(userId));
    }

    @PostMapping("/users/{userId}/categories")
    @PreAuthorize("hasAuthority('category:write')")
    public ResponseEntity<CategoryDto> createCategory(@PathVariable Long userId, @RequestBody CategoryDto categoryDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.create(categoryDto));
    }

    /*@PostMapping("/users/{userId}/categories/default")
    //@PreAuthorize("hasAuthority('categories:add_default')")
    ResponseEntity<CategoryDto> createDefaultCategory(@PathVariable Long userId, @RequestBody CategoryDto categoryDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(categoryService.create(categoryDto));
    }*/

    @GetMapping("/users/{userId}/categories")
    public ResponseEntity<List<CategoryDto>> getUserCategories(@PathVariable Long userId) {
        return ResponseEntity.ok(categoryService.findAllByUserId(userId));
    }

    @GetMapping("/users/{userId}/categories/{categoryId}")
    public ResponseEntity<CategoryDto> getUserCategoryById(@PathVariable Long userId, @PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.findUserCategoryById(userId, categoryId));
    }

    @PutMapping("/users/{userId}/categories/{categoryId}")
    public ResponseEntity<CategoryDto> updateCategory(@PathVariable Long userId, @PathVariable Long categoryId,
                                                           @RequestBody CategoryDto categoryDto) {
        return ResponseEntity.ok(categoryService.update(categoryId, categoryDto));
    }

    @GetMapping("/users/{userId}/transactions")
    //@PreAuthorize("hasAuthority('transactions:read')")
    public ResponseEntity<List<TransactionDto>> getUserTransactions(@PathVariable Long userId) {
        return ResponseEntity.ok(transactionService.findAllByUserId(userId));
    }

    @PostMapping("/users/{userId}/transactions")
        //@PreAuthorize("hasAuthority('transactions:write')")
    public ResponseEntity<TransactionDto> createTransaction(@PathVariable Long userId, @RequestBody TransactionDto transactionDto) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transactionService.create(transactionDto));
    }

    @GetMapping("/users/{userId}/transactions/{transactionId}")
    public ResponseEntity<TransactionDto> getUserTransactionById(@PathVariable Long userId, @PathVariable Long transactionId) {
        return ResponseEntity.ok(transactionService.getUserTransactionById(userId, transactionId));
    }

    @PutMapping("/users/{userId}/transactions/{transactionId}")
    public ResponseEntity<TransactionDto> updateTransactionById(@PathVariable Long userId, @PathVariable Long transactionId,
                                                                @RequestBody TransactionDto transactionDto) {
        return ResponseEntity.ok(transactionService.update(transactionId, transactionDto));
    }

    @DeleteMapping("/users/{userId}/transactions/{transactionId}")
    //@PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> deleteTransactionById(@PathVariable Long transactionId) {
        transactionService.deleteById(transactionId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(String.format("Transaction[%d] was deleted", transactionId));
    }

    @GetMapping("/users/{userId}/overview")
    public ResponseEntity<Map<Long, Double>> getOverview(@PathVariable Long userId) {
        return ResponseEntity.ok(calcService.calcUserTransactionsValueByCategoryFromLastMonth(userId));
    }
}
