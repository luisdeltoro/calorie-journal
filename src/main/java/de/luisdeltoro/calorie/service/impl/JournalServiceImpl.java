package de.luisdeltoro.calorie.service.impl;

import de.luisdeltoro.calorie.model.Journal;
import de.luisdeltoro.calorie.model.Meal;
import de.luisdeltoro.calorie.model.Stats;
import de.luisdeltoro.calorie.persistence.JournalRepository;
import de.luisdeltoro.calorie.service.api.JournalService;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * A simple implementation of {@link JournalService} that uses Spring's JPA repositories for persistence
 */
@Service
public class JournalServiceImpl implements JournalService {

    private PassiveExpiringMap<UUID, Stats> stats;
    @Value("${stats.expirationTime}")
    private int expirationTime;
    @Autowired
    private JournalRepository journalRepository;

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = false)
    @Override
    public Journal createJournal(Journal journal) {
        journalRepository.save(journal);
        return journal;
    }

    /**
     * {@inheritDoc}
     */
    @Transactional
    @Override
    public Optional<Journal> getJournal(UUID journalId) {
        return journalRepository.findByBusinessId(journalId);
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = false)
    @Override
    public void addMeal(UUID journalId, Meal meal) {
        Optional<Journal> journal = journalRepository.findByBusinessId(journalId);
        journal.get().addMeal(meal);
        journalRepository.save(journal.get());
    }

    /**
     * {@inheritDoc}
     */
    @Transactional(readOnly = false)
    @Override
    public UUID calculateStats(UUID journalId) {
        Optional<Journal> journal = journalRepository.findByBusinessId(journalId);
        double averageCalories = journal.get().getMeals().stream().mapToInt(meal -> meal.getCalories()).average().getAsDouble();
        double averageCarbs = journal.get().getMeals().stream().mapToInt(meal -> meal.getCarbs()).average().getAsDouble();
        double averageProtein = journal.get().getMeals().stream().mapToInt(meal -> meal.getProtein()).average().getAsDouble();
        double averageFats = journal.get().getMeals().stream().mapToInt(meal -> meal.getFats()).average().getAsDouble();
        Stats stats =  new Stats(averageCalories, averageCarbs, averageProtein, averageFats);
        UUID statsId = UUID.randomUUID();
        this.stats.put(statsId, stats);
        return statsId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Optional<Stats> getStats(UUID statsId) {
        Stats stats = this.stats.get(statsId);
        return Optional.ofNullable(stats);
    }

    @PostConstruct
    private void initialize() {
        stats = new PassiveExpiringMap<>(expirationTime, TimeUnit.MINUTES);
    }

}
