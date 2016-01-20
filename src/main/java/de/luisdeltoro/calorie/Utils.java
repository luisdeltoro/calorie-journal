package de.luisdeltoro.calorie;

import java.util.List;

/**
 * Utility class with Constants and methods used in several places in the code
 */
public class Utils {

    /**
     * The media type
     */
    public static final String MEDIA_TYPE = "application";

    /**
     * The media subtype for version 1 of the web service api
     */
    public static final String MEDIA_SUBTYPE_V1 = "vnd.luisdeltoro.caloriejournal.v1+json";

    /**
     * Mime Type of version 1 of the web service api
     */
    public static final String MIME_HEADER_V1 = MEDIA_TYPE + "/" + MEDIA_SUBTYPE_V1;

    /**
     * The path to the journals resource
     */
    public static final String STATS_PATH = "/stats";

    /**
     * The path to the journals resource
     */
    public static final String JOURNALS_PATH = "/journals";

    /**
     * The path to the jobs resource
     */
    public static final String JOBS_PATH = "/jobs";

    /**
     * The path to the meals subresource
     */
    public static final String MEALS_PATH = "/meals";

    /**
     * The path to the calculate calorie statistics action
     */
    public static final String CALCULATE_PATH = "/action/calculate";

    /**
     * Returns the first element of the provided list or fails if its empty
     * @param list the list to extract the first element from
     * @param <T>
     * @return the first element of the list
     */
    public <T> T getOrFail(List<T> list) {
        if (!list.isEmpty()) {
            return list.get(0);
        } else {
            throw new NullPointerException("The entity does not exist");
        }
    }

}
