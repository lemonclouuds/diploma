package com.company.financeApp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GoalDto {

    private Long id;

    private String name;

    private Double goalValue;

    private Double currentValue;

    private boolean isDone;

    private Long userId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GoalDto goalDto = (GoalDto) o;
        return Objects.equals(id, goalDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
