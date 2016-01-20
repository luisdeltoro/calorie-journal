package de.luisdeltoro.calorie.util;

import de.luisdeltoro.calorie.model.Journal;
import de.luisdeltoro.calorie.model.Meal;
import de.luisdeltoro.calorie.ws.dto.MealDTO;
import org.springframework.http.MediaType;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Random;
import java.util.UUID;

import static de.luisdeltoro.calorie.Utils.MEDIA_SUBTYPE_V1;
import static de.luisdeltoro.calorie.Utils.MEDIA_TYPE;

/**
 * Utility class for the test suite
 */
public class TestUtils {

    /**
     * Media Type Header as {@link MediaType} object
     */
    public static final MediaType MIME_HEADER = new MediaType(MEDIA_TYPE, MEDIA_SUBTYPE_V1);

    /**
     * Extracts the last part of the uri, with should be the identifier of the resource
     * @param uri the absolute or relative URI to extract the identifier from
     * @return the identifier
     */
    public static String extractId(String uri) {
        String[] segments = uri.split("/");
        String idAsStr = segments[segments.length - 1];
        return idAsStr;
    }

    /**
     * Extracts the last part of the uri, with should be the identifier of the resource
     * @see TestUtils#extractId(String)
     * @param uri the absolute or relative URI to extract the identifier from
     * @return the identifier as a {@link UUID} object
     */
    public static UUID extractIdAsUUID(String uri) {
        return UUID.fromString(extractId(uri));
    }

    /**
     * Creates a random {@link MealDTO} for testing purposes
     * @return the random Meal Data Transfer Object
     */
    public static MealDTO createRandomMealDto() {
        Random random = new Random();
        int carbs = random.nextInt(70);
        int protein = random.nextInt(90 - carbs);
        int fats = 100 - (carbs + protein);
        return new MealDTO(createRandomDate(), createRandomCalories(), carbs, protein, fats);
    }

    /**
     * Adds 1095 random {@link Meal}s to a {@link Journal} for testing purposes
     */
    public static void addRandomMeals(Journal journal) {
        for (int i = 0; i < 1095; i++) {
            journal.addMeal(createRandomMeal());
        }
    }

    /**
     * Creates a random {@link Meal} for testing purposes
     * @return the random Meal
     */
    public static Meal createRandomMeal() {
        Random random = new Random();
        int carbs = random.nextInt(70);
        int protein = random.nextInt(90 - carbs);
        int fats = 100 - (carbs + protein);
        return new Meal(Timestamp.valueOf(createRandomDate()), createRandomCalories(), carbs, protein, fats);
    }

    /**
     * Creates a random number of calories
     * @return the random calorie number
     */
    public static int createRandomCalories() {
        Random random = new Random();
        return 350 + random.nextInt(100);
    }


    /**
     * Creates a random date and time in year 2015
     * @return the random date and time
     */
    public static LocalDateTime createRandomDate() {
        Random random = new Random();
        long minTimestamp = LocalDateTime.of(2015, 1, 1, 0, 0).toEpochSecond(ZoneOffset.UTC);
        long maxTimestamp = LocalDateTime.of(2015, 12, 31, 23, 59).toEpochSecond(ZoneOffset.UTC);
        long randomTimestamp = minTimestamp + random.nextInt((int) (maxTimestamp - minTimestamp));
        LocalDateTime randomTimestampAsDate = LocalDateTime.ofEpochSecond(randomTimestamp, 0, ZoneOffset.UTC);
        return randomTimestampAsDate;
    }

}
