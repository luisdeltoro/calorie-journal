package de.luisdeltoro.calorie.ws.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for calorie statistics
 */
@NoArgsConstructor
@RequiredArgsConstructor
public class StatsDTO {

    @Getter @Setter @NonNull
    private Double averageCalories;
    @Getter @Setter @NonNull
    private Double averageCarbs;
    @Getter @Setter @NonNull
    private Double averageProtein;
    @Getter @Setter @NonNull
    private Double averageFats;

    @Override
    public String toString() {
        return "StatsDTO{" +
                "averageCalories=" + averageCalories +
                ", averageCarbs=" + averageCarbs +
                ", averageProtein=" + averageProtein +
                ", averageFats=" + averageFats +
                '}';
    }
}
