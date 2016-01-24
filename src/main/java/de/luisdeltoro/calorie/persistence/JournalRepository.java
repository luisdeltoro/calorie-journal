package de.luisdeltoro.calorie.persistence;

import de.luisdeltoro.calorie.model.Journal;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * Provides CRUD operations for the {@link Journal} entity
 */
public interface JournalRepository extends CrudRepository<Journal, Long> {

    /**
     * Finds a calorie journal matching the provided business identifier
     * @param businessId the business id of the calorie journal to be found
     * @return the desired calorie journal or <code>null</code> if it does not exist
     */
    Optional<Journal> findByBusinessId(UUID businessId);

}
