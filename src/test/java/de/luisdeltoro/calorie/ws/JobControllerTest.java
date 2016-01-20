package de.luisdeltoro.calorie.ws;

import de.luisdeltoro.calorie.exception.ObjectNotFoundException;
import de.luisdeltoro.calorie.ws.dto.JobDTO;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests for the {@link JobController}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class JobControllerTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();
    @Autowired
    MockHttpServletRequest request;
    @Autowired
    MockHttpServletResponse response;
    @Autowired
    private JobController jobController;

    @Test
    public void getNonExistingJob() throws Exception {
        // Given: no job in the system
        // When: retrieving a job with a non-existing id
        // Then: the job cannot be found
        thrown.expect(ObjectNotFoundException.class);
        jobController.getJob(UUID.randomUUID().toString(), response);
    }

    @Test
    public void getProcessingJob() throws Exception {
        // Given: a  job running in the system
        String jobId = jobController.scheduleJob((CompletableFuture.supplyAsync(() -> {
            try {
                Thread.currentThread().sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return UUID.randomUUID();
        })));

        // When: polling the job
        // Then: the job state is processing
        JobDTO jobDto = jobController.getJob(jobId, response);
        assertThat(jobDto.getState(), is(JobDTO.State.PROCESSING));
    }

    @Test
    public void getDoneJob() throws Exception {
        // Given: a  job running in the system
        String jobId = jobController.scheduleJob((CompletableFuture.supplyAsync(() -> {return UUID.randomUUID();})));

        // When: the job has finished
        // Then: the job state is done
        JobDTO jobDto = jobController.getJob(jobId, response);
        assertThat(jobDto.getState(), is(JobDTO.State.DONE));

    }

    @Test
    public void getFailedJob() throws Exception {
        // Given: a  job running in the system
        String jobId = jobController.scheduleJob((CompletableFuture.supplyAsync(() -> {
            throw new ObjectNotFoundException("The desired calorie journal could not be found");
        })));

        // When: the job has finished
        Thread.currentThread().sleep(500);
        // Then: the job state is failed
        JobDTO jobDto = jobController.getJob(jobId, response);
        assertThat(jobDto.getState(), is(JobDTO.State.FAILED));
    }


}