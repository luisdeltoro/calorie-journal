package de.luisdeltoro.calorie.ws.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Data Transfer Object for calorie journals
 */
@NoArgsConstructor
@RequiredArgsConstructor
public class JournalDTO {

    @Getter @Setter @NonNull
    private String personName;
    @Getter @Setter @NonNull
    private int personAge;
    @Getter @Setter @NonNull
    private int personHeight;
    @Getter @Setter @NonNull
    private int personWeight;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JournalDTO)) return false;

        JournalDTO that = (JournalDTO) o;

        if (personAge != that.personAge) return false;
        if (personHeight != that.personHeight) return false;
        if (personWeight != that.personWeight) return false;
        return personName.equals(that.personName);

    }

    @Override
    public int hashCode() {
        int result = personName.hashCode();
        result = 31 * result + personAge;
        result = 31 * result + personHeight;
        result = 31 * result + personWeight;
        return result;
    }

    @Override
    public String toString() {
        return "JournalDTO{" +
                "personName='" + personName + '\'' +
                ", personAge=" + personAge +
                ", personHeight=" + personHeight +
                ", personWeight=" + personWeight +
                '}';
    }
}
