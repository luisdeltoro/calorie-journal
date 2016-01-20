package de.luisdeltoro.calorie.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Domain object representing a calorie journal
 */
@Entity
public class Journal {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter @Setter
    private Long id;
    @Version
    private Long version;
    @Column(columnDefinition = "BINARY(16)", unique = true, nullable = false)
    @Getter @Setter
    private UUID businessId;
    @Embedded
    @Getter @Setter
    private Person person;
    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REMOVE}, orphanRemoval = true)
    @Getter
    private Set<Meal> meals = new HashSet<>();

    /*
     * For hibernate
     */
    protected Journal() {
        businessId = UUID.randomUUID();
    }

    /**
     * Builds a new calorie journal
     * @param person the person to whom this journal belongs
     */
    public Journal(Person person) {
        businessId = UUID.randomUUID();
        this.person = person;
    }

    /**
     * Adds a meal to this calorie journal
     * @param meal the meal to add
     */
    public void addMeal(Meal meal) {
        meals.add(meal);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Journal journal = (Journal) o;

        return getBusinessId().equals(journal.getBusinessId());

    }

    @Override
    public int hashCode() {
        return getBusinessId().hashCode();
    }

    @Override
    public String toString() {
        return "Journal{" +
                "id=" + id +
                ", version=" + version +
                ", businessId=" + businessId +
                ", person=" + person +
                '}';
    }
}
