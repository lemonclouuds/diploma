package com.company.financeApp.service;

import com.company.financeApp.domain.Goal;
import com.company.financeApp.domain.dto.CategoryDto;
import com.company.financeApp.domain.dto.GoalDto;
import com.company.financeApp.domain.user.User;
import com.company.financeApp.exception.EntityAlreadyExistsException;
import com.company.financeApp.helper.MapperHelper;
import com.company.financeApp.repository.GoalRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.Collection;
import java.util.List;

import static com.company.financeApp.constants.ExceptionMessage.GOAL_WITH_ID_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class GoalService {
    private static final ModelMapper modelMapper = new ModelMapper();

    private final GoalRepository goalRepository;
    private final UserService userService;

    public List<CategoryDto> findAll() {
        return MapperHelper.mapList(goalRepository.findAll(), CategoryDto.class);
    }

    public Goal findById(Long goalId) {
        return goalRepository.findById(goalId).orElseThrow(
                () -> new EntityNotFoundException(String.format(GOAL_WITH_ID_NOT_FOUND, goalId)));
    }

    public List<GoalDto> findAllByUserId(Long userId) {
        return MapperHelper.mapList(goalRepository.findAllByUserId(userId), GoalDto.class);
    }

    public GoalDto findDtoById(Long goalId) {
        return modelMapper.map(findById(goalId), GoalDto.class);
    }

    @Transactional
    public GoalDto create(GoalDto goalDto) {
        Goal goal = modelMapper.map(goalDto, Goal.class);
        if (!goalRepository.existsById(goalDto.getId())) {
            User user = userService.findById(goalDto.getUserId());
            goal.setUser(user);
            goal.setDone(false);
            goal.setCurrentValue(0.0);

            goalRepository.save(goal);
        } else {
            throw new EntityAlreadyExistsException(String.format(GOAL_WITH_ID_NOT_FOUND,
                    goalDto.getId()));
        }
        return goalDto;
    }

    @Transactional
    public GoalDto update(Long goalId, GoalDto goalDto) {
        Goal goal = modelMapper.map(goalDto, Goal.class);
        goal.setId(goalId);
        if (goalRepository.existsById(goalId)) {
            if (goalDto.getCurrentValue() <= goalDto.getGoalValue()) {
                goal.setDone(false);
            } else {
                goal.setDone(true);
            }
            User user = userService.findById(goalDto.getUserId());
            goal.setUser(user);
            goalRepository.save(goal);
        } else {
            throw new EntityNotFoundException(String.format(GOAL_WITH_ID_NOT_FOUND, goalId));
        }
        return goalDto;
    }

    @Transactional
    public void deleteById(Long id) {
        if (goalRepository.existsById(id)) {
            goalRepository.deleteById(id);
        }
    }

    @Transactional
    public void deleteByIdIn(Collection<Long> ids) {
        for (Long id : ids) {
            deleteById(id);
        }
    }

    @Transactional
    public GoalDto addToGoal(Long goalId, Double addingValue) {
        Goal goal = findById(goalId);
        if (addingValue <= 0) {
            throw new IllegalArgumentException("Zero or negative value");
        } else {
            goal.setCurrentValue(goal.getCurrentValue() + addingValue);
            if (goal.getCurrentValue() >= goal.getGoalValue()) {
                goal.setDone(true);
            } else {
                goal.setDone(false);
            }
        }
        return modelMapper.map(goal, GoalDto.class);
    }

}
