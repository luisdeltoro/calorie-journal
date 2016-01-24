package de.luisdeltoro.calorie.ws;

import de.luisdeltoro.calorie.exception.ObjectNotFoundException;
import de.luisdeltoro.calorie.model.Journal;
import de.luisdeltoro.calorie.model.Meal;
import de.luisdeltoro.calorie.service.api.JournalService;
import de.luisdeltoro.calorie.ws.dto.JournalDTO;
import de.luisdeltoro.calorie.ws.dto.MealDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static de.luisdeltoro.calorie.Utils.CALCULATE_PATH;
import static de.luisdeltoro.calorie.Utils.JOBS_PATH;
import static de.luisdeltoro.calorie.Utils.JOURNALS_PATH;
import static de.luisdeltoro.calorie.Utils.MEALS_PATH;
import static de.luisdeltoro.calorie.Utils.MIME_HEADER_V1;

/**
 * This REST controller deals with requests to the /journals endpoint
 */
@RestController
@CommonsLog
public class JournalController {

    @Autowired
    private DozerBeanMapper dozerBeanMapper;
    @Autowired
    private JobController jobController;
    @Autowired
    private JournalService journalService;

    /**
     * Creates a new calorie journal
     * @param journalDto DTO containing the needed input to create the journal
     * @param response the HTTP response
     */
    @RequestMapping(value = JOURNALS_PATH, method = RequestMethod.POST, consumes = MIME_HEADER_V1)
    public void createJournal(@Valid @RequestBody JournalDTO journalDto, HttpServletResponse response) {
        log.info(String.format("Create Journal Request received: %s", null));
        Journal journal = dozerBeanMapper.map(journalDto, Journal.class);
        journalService.createJournal(journal);
        response.setStatus(HttpServletResponse.SC_CREATED);
        response.addHeader(HttpHeaders.LOCATION, JOURNALS_PATH + "/" + journal.getBusinessId());
        log.info(String.format("Journal successfully created: %s", null));
    }

    /**
     * Retrieves a calorie journal
     * @param id the business identifier of the requested calorie journal
     * @return the desired document
     * @throws ObjectNotFoundException if the requested calorie journal does not exist
     */
    @RequestMapping(value = JOURNALS_PATH + "/{id}", method = RequestMethod.GET, produces = MIME_HEADER_V1)
    public JournalDTO getJournal(@PathVariable String id) {
        log.info(String.format("Get Journal Request received: businessId=%s", id));
        Optional<Journal> journal = journalService.getJournal(UUID.fromString(id));
        JournalDTO journalDto = dozerBeanMapper.map(journal.get(), JournalDTO.class);
        log.info(String.format("Journal successfully returned: %s", journalDto));
        return journalDto;
    }

    /**
     * Creates a meal and adds it to the appropriate calorie journal
     * @param id the identifier of the calorie journal to add the meal to
     * @param mealDto DTO containing the needed input to create the meal
     * @param response the HTTP response
     */
    @RequestMapping(value = JOURNALS_PATH + "/{id}" + MEALS_PATH, method = RequestMethod.POST, consumes = MIME_HEADER_V1)
    public void createMeal(@PathVariable String id, @Valid @RequestBody MealDTO mealDto, HttpServletResponse response) {
        log.info(String.format("Create Meal Request received: journalId=%s, meal=%s", id, mealDto));
        Meal meal = dozerBeanMapper.map(mealDto, Meal.class);
        journalService.addMeal(UUID.fromString(id), meal);
        log.info(String.format("Meal successfully created: %s", meal));
    }

    /**
     * Calculates statistics on the provided calorie journal
     * @param id the identifier of the calorie journal use for the statistic calculation
     * @param response the HTTP response
     */
    @RequestMapping(value = JOURNALS_PATH + "/{id}" + CALCULATE_PATH , method = RequestMethod.POST, consumes = MIME_HEADER_V1)
    public void calculateStats(@PathVariable String id, HttpServletResponse response) {
        log.info(String.format("Calculate Daily Intake Request received: journalId=%s", id));
        String jobId = jobController.scheduleJob(CompletableFuture.supplyAsync(() -> journalService.calculateStats(UUID.fromString(id))));
        response.setStatus(HttpServletResponse.SC_ACCEPTED);
        response.addHeader(HttpHeaders.LOCATION, JOBS_PATH + "/" + jobId);
        log.info(String.format("Stats job successfully created: %s", jobId));
    }
}
