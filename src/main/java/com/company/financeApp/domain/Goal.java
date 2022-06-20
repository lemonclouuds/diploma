package com.company.financeApp.domain;

import com.company.financeApp.domain.user.User;
import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Objects;

@Data
@NoArgsConstructor
@Entity
@Table(name = "goals")
public class Goal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    //TODO BigDecimal value;
    @Column(name = "goal_value", nullable = false)
    private Double goalValue;

    //TODO BigDecimal value;
    @Column(name = "current_value", nullable = false)
    private Double currentValue;

    @Column(name = "is_done")
    private boolean isDone;

    @ManyToOne(fetch = FetchType.EAGER, cascade = {CascadeType.MERGE})
    @JoinColumn(name = "user_id")
    @JsonBackReference("user")
    private User user;

    public Goal(String name, Double goalValue, User user) {
        this.name = name;
        this.goalValue = goalValue;
        this.user = user;
        this.currentValue = 0.0;
        this.isDone = false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Goal goal = (Goal) o;
        return Objects.equals(id, goal.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
