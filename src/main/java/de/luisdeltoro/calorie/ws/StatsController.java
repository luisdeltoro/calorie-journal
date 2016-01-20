package de.luisdeltoro.calorie.ws;


import de.luisdeltoro.calorie.model.Stats;
import de.luisdeltoro.calorie.service.api.JournalService;
import de.luisdeltoro.calorie.ws.dto.StatsDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.dozer.DozerBeanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

import static de.luisdeltoro.calorie.Utils.MIME_HEADER_V1;
import static de.luisdeltoro.calorie.Utils.STATS_PATH;

/**
 * This REST controller deals with requests to the /stats endpoint
 */
@RestController
@CommonsLog
public class StatsController {

    private PassiveExpiringMap<String, Stats> calorieStats;
    @Autowired
    private DozerBeanMapper dozerBeanMapper;
    @Autowired
    private JournalService journalService;

    /**
     * Retrieves a existing calorie statistics
     * @param id the identifier of the requested calorie statistics
     * @param response the HTTP response
     * @return the desired statistics
     */
    @RequestMapping(value = STATS_PATH + "/{id}", method = RequestMethod.GET, produces = MIME_HEADER_V1)
    public StatsDTO getCalorieStats(@PathVariable String id, HttpServletResponse response) {
        log.info(String.format("Get Job Request received: id=%s ", id));
        Stats stats = journalService.getStats(UUID.fromString(id));
        return dozerBeanMapper.map(stats, StatsDTO.class);
    }
}
