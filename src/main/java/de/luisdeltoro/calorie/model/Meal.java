package de.luisdeltoro.calorie.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.sql.Timestamp;

/**
 * Domain object representing a meal
 */
@Entity
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Meal {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    @Getter @Setter
    private Long id;
    @Getter @Setter @NonNull
    private Timestamp time;
    @Getter @Setter @NonNull
    private int calories;
    @Getter @Setter @NonNull
    private int carbs;
    @Getter @Setter @NonNull
    private int protein;
    @Getter @Setter @NonNull
    private int fats;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Meal meal = (Meal) o;

        if (getId() != null) {
            return getId().equals(meal.getId());
        } else if (getTime() != null) {
            return getTime().equals(meal.getTime());
        } else {
            return false;
        }

    }

    @Override
    public int hashCode() {
        if (getId() != null) {
            return getId().hashCode();
        } else if (getTime() != null) {
            return getTime().hashCode();
        } else {
            return 31;
        }
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", time=" + time +
                ", calories=" + calories +
                ", carbs=" + carbs +
                ", protein=" + protein +
                ", fats=" + fats +
                '}';
    }
}
