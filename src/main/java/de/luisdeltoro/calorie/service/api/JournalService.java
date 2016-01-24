package de.luisdeltoro.calorie.service.api;

import de.luisdeltoro.calorie.model.Stats;
import de.luisdeltoro.calorie.model.Journal;
import de.luisdeltoro.calorie.model.Meal;

import java.util.Optional;
import java.util.UUID;

/**
 * This service deals with the operations related to {@link Journal}s, {@link Meal}s and {@link Stats}.
 */
public interface JournalService {

    /**
     * Creates a new calorie journal by saving it to persistent storage
     * @param journal the journal to be stored
     * @return the journal with assigned subrogate identifiers
     */
    Journal createJournal(Journal journal);

    /**
     * Retrieved an carlorie journal from persistent storage
     * @param businessId the business identifier of the desired journal
     * @return the desired calorie journal
     */
    Optional<Journal> getJournal(UUID businessId);

    /**
     * Add a meal to an existing calorie journla
     * @param journalId the business identifier of the calorie journal
     * @param meal the meal to be added to the calorie journal
     */
    void addMeal(UUID journalId, Meal meal);

    /**
     * Calculates statistics on all the meals contain the provided calorie journal
     * @param journalId the business identifier of the calorie journal
     * @return the identifier of the statistics generated
     */
    UUID calculateStats(UUID journalId);

    /**
     * Retrieved a temporary Statistics calculation from in-memory storage
     * @param statsId the identifier of the statistics to be retrieved
     * @return the desired statistics
     */
    Optional<Stats> getStats(UUID statsId);

}
