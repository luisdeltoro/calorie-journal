package de.luisdeltoro.calorie.ws;


import de.luisdeltoro.calorie.exception.ObjectNotFoundException;
import de.luisdeltoro.calorie.ws.dto.JobDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.collections4.map.PassiveExpiringMap;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static de.luisdeltoro.calorie.Utils.JOBS_PATH;
import static de.luisdeltoro.calorie.Utils.MIME_HEADER_V1;
import static de.luisdeltoro.calorie.Utils.STATS_PATH;
import static de.luisdeltoro.calorie.ws.dto.JobDTO.State.DONE;
import static de.luisdeltoro.calorie.ws.dto.JobDTO.State.FAILED;
import static de.luisdeltoro.calorie.ws.dto.JobDTO.State.PROCESSING;

/**
 * This REST controller deals with requests to the /jobs endpoint
 */
@RestController
@CommonsLog
public class JobController {

    private PassiveExpiringMap<String, Future<UUID>> jobs;
    @Value("${job.expirationTime}")
    private int expirationTime;

    /**
     * Retrieves a job
     * @param businessId the business identifier of the requested job
     * @param response the HTTP response
     * @return the desired job
     * @throws InterruptedException if the thread executing the job was interrupted while waiting
     */
    @RequestMapping(value = JOBS_PATH + "/{businessId}", method = RequestMethod.GET, produces = MIME_HEADER_V1)
    public JobDTO getJob(@PathVariable String businessId, HttpServletResponse response) throws InterruptedException {
        log.info(String.format("Get Job Request received: businessId=%s ", businessId));
        Future<UUID> jobFuture = getJobFuture(businessId);
        JobDTO jobDto;
        if (!jobFuture.isDone()) {
            jobDto = new JobDTO(PROCESSING);
            log.info(String.format("Job '%s' is still processing ", businessId));
        } else {
            UUID statsId;
            try {
                statsId = jobFuture.get();
                jobDto = new JobDTO(DONE);
                response.setStatus(HttpServletResponse.SC_SEE_OTHER);
                response.addHeader(HttpHeaders.LOCATION, STATS_PATH + "/" + statsId.toString());
                log.info(String.format("Job '%s' is done: id=%s", businessId, statsId.toString()));
            } catch (ExecutionException e) {
                jobDto = buildJobFailedDto(e);
                log.info(String.format("Job '%s' failed.", businessId), e);
            }
        }
        return jobDto;
    }

    /**
     * Schedules a job by adding the provided future to in-memory storage
     * @param future the future containing the long running task
     * @return the identifier of the scheduled job
     */
    public String scheduleJob(Future<UUID> future) {
        String jobId = UUID.randomUUID().toString();
        jobs.put(jobId, future);
        return jobId;
    }

    private Future<UUID> getJobFuture(String businessId) {
        Future<UUID> future = jobs.get(businessId);
        if (future == null) throw new ObjectNotFoundException(String.format("Job with id '%s' does not exists ", businessId));
        return future;
    }

    private JobDTO buildJobFailedDto(ExecutionException e) {
        JobDTO jobDto;
        jobDto = new JobDTO(FAILED);
        if (e.getCause() != null) {
            jobDto.setError(e.getCause().getLocalizedMessage());
        } else {
            jobDto.setError(e.getLocalizedMessage());
        }
        return jobDto;
    }

    @PostConstruct
    public void initialize() {
        jobs = new PassiveExpiringMap<>(expirationTime, TimeUnit.MINUTES);
    }
}
