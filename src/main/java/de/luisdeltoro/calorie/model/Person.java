package de.luisdeltoro.calorie.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

/**
 * Domain object representing a person
 */
@Embeddable
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Person {

    @Getter @Setter @NonNull
    private String name;
    @Getter @Setter @NonNull
    private int age;
    @Getter @Setter @NonNull
    private int height;
    @Getter @Setter @NonNull
    private int weight;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Person person = (Person) o;

        if (getAge() != person.getAge()) return false;
        if (getHeight() != person.getHeight()) return false;
        if (getWeight() != person.getWeight()) return false;
        return getName().equals(person.getName());

    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getAge();
        result = 31 * result + getHeight();
        result = 31 * result + getWeight();
        return result;
    }

    @Override
    public String toString() {
        return "Person{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", height=" + height +
                ", weight=" + weight +
                '}';
    }
}
