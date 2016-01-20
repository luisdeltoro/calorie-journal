package de.luisdeltoro.calorie.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Domain object representing the statistic calculation of average nutritional facts of several meals together
 */
@RequiredArgsConstructor
public class Stats {

    @Getter @Setter @NonNull
    private double averageCalories;
    @Getter @Setter @NonNull
    private double averageCarbs;
    @Getter @Setter @NonNull
    private double averageProtein;
    @Getter @Setter @NonNull
    private double averageFats;

    @Override
    public String toString() {
        return "Stats{" +
                "averageCalories=" + averageCalories +
                ", averageCarbs=" + averageCarbs +
                ", averageProtein=" + averageProtein +
                ", averageFats=" + averageFats +
                '}';
    }
}
