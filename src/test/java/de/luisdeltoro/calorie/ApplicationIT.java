package de.luisdeltoro.calorie;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import de.luisdeltoro.calorie.ws.dto.JournalDTO;
import de.luisdeltoro.calorie.ws.dto.MealDTO;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URI;

import static com.jayway.jsonpath.matchers.JsonPathMatchers.hasJsonPath;
import static com.jayway.jsonpath.matchers.JsonPathMatchers.isJson;
import static de.luisdeltoro.calorie.Utils.CALCULATE_PATH;
import static de.luisdeltoro.calorie.Utils.JOBS_PATH;
import static de.luisdeltoro.calorie.Utils.JOURNALS_PATH;
import static de.luisdeltoro.calorie.util.TestUtils.MIME_HEADER;
import static de.luisdeltoro.calorie.util.TestUtils.createRandomMealDto;
import static de.luisdeltoro.calorie.util.TestUtils.extractId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertEquals;


/**
 * Integration Tests for the whole application
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApplicationIT {

    private static HttpHeaders headers;

    @BeforeClass
    public static void buildTestData() throws JsonProcessingException {
        headers = new HttpHeaders();
        headers.setContentType(MIME_HEADER);
    }

    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void calculateStats() throws InterruptedException {
        // Given: There is a calorie journal containing a set of meals
        String journalId = createSampleJournalAndMeals();

        // When: A request to calculate the calorie statistics for that journal is sent
        HttpEntity<Void> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(JOURNALS_PATH + "/" + journalId + CALCULATE_PATH, HttpMethod.POST, requestEntity, Void.class);

        // Then: We receive the location of the job
        URI jobLocation = responseEntity.getHeaders().getLocation();

        // And: After the calculation has finished the statistics are provided
        ResponseEntity<String> entity = pollJobUntilDone(extractId(jobLocation.toString()));
        assertThat(entity.getStatusCode(), is(HttpStatus.OK));
        String body = entity.getBody();
        System.out.print(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>" + entity.getBody());

        assertThat(body, isJson());
        assertThat(body, hasJsonPath("$.averageCalories", not(equalTo(0.0d))));
        assertThat(body, hasJsonPath("$.averageCarbs", not(equalTo(0.0d))));
        assertThat(body, hasJsonPath("$.averageProtein", not(equalTo(0.0d))));
        assertThat(body, hasJsonPath("$.averageFats", not(equalTo(0.0d))));
    }

    private String createSampleJournalAndMeals() {
        final JournalDTO journalDto = new JournalDTO("Bruce Lee", 33, 170, 78);

        HttpEntity<JournalDTO> requestEntity = new HttpEntity<>(journalDto, headers);
        ResponseEntity<Void> responseEntity = restTemplate.exchange(JOURNALS_PATH, HttpMethod.POST, requestEntity, Void.class);
        URI journalLocation = responseEntity.getHeaders().getLocation();
        String journalId = extractId(journalLocation.toString());
        for (int i = 0; i < 10; i++) {
            HttpEntity<MealDTO> mealRequestEntity = new HttpEntity<>(createRandomMealDto(), headers);
            restTemplate.exchange(JOURNALS_PATH + "/" + journalId + "/meals", HttpMethod.POST, mealRequestEntity, Void.class);
        }
        return journalId;
    }

    private ResponseEntity<String> pollJobUntilDone(String jobId) throws InterruptedException {
        int maxRetries = 20;
        ResponseEntity<String> entity;
        do {
            Thread.sleep(500);
            entity = restTemplate.getForEntity(JOBS_PATH + "/" + jobId, String.class);
            assertEquals(HttpStatus.OK, entity.getStatusCode());
            maxRetries--;
        } while (entity.getBody().contains("state") && maxRetries != 0);
        return entity;
    }
}
