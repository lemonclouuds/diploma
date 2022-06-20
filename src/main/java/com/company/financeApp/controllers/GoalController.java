package com.company.financeApp.controllers;

import com.company.financeApp.domain.dto.GoalDto;
import com.company.financeApp.service.GoalService;
import com.company.financeApp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class GoalController {
    private final GoalService goalService;
    private final UserService userService;

    @GetMapping("/{userId}/goals")
    public ResponseEntity<List<GoalDto>> getAllUserGoals(@PathVariable Long userId) {
        return ResponseEntity.ok(goalService.findAllByUserId(userId));
    }

    @PostMapping("/{userId}/goals")
    public ResponseEntity<GoalDto> createGoal(@PathVariable Long userId,@RequestBody GoalDto goalDto) {
        goalDto.setUserId(userId);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(goalService.create(goalDto));
    }

    @PutMapping("/{userId}/goals/{goalId}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<GoalDto> update(@PathVariable Long goalId, @RequestBody GoalDto goalDto) {
        return ResponseEntity.ok(goalService.update(goalId, goalDto));
    }

    @DeleteMapping("/{userId}/goals/{goalId}")
    @PreAuthorize("hasAuthority('user:write')")
    public ResponseEntity<?> delete(@PathVariable Long goalId) {
        goalService.deleteById(goalId);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(String.format("Goal[%d] was deleted", goalId));
    }

    //add endpoint to add values to goals
}
