package de.luisdeltoro.calorie.ws.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for meals
 */
@NoArgsConstructor
@RequiredArgsConstructor
public class MealDTO {

    @Getter @Setter @NonNull
    private LocalDateTime time;
    @Getter @Setter @NonNull
    private Integer calories;
    @Getter @Setter @NonNull
    private Integer carbs;
    @Getter @Setter @NonNull
    private Integer protein;
    @Getter @Setter @NonNull
    private Integer fats;

    @Override
    public String toString() {
        return "MealDTO{" +
                "time=" + time +
                ", calories=" + calories +
                ", carbs=" + carbs +
                ", protein=" + protein +
                ", fats=" + fats +
                '}';
    }
}
