package de.luisdeltoro.calorie.ws;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.luisdeltoro.calorie.model.Journal;
import de.luisdeltoro.calorie.model.Person;
import de.luisdeltoro.calorie.persistence.JournalRepository;
import de.luisdeltoro.calorie.ws.dto.JournalDTO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Optional;
import java.util.UUID;

import static de.luisdeltoro.calorie.Utils.CALCULATE_PATH;
import static de.luisdeltoro.calorie.Utils.JOBS_PATH;
import static de.luisdeltoro.calorie.Utils.JOURNALS_PATH;
import static de.luisdeltoro.calorie.util.TestUtils.MIME_HEADER;
import static de.luisdeltoro.calorie.util.TestUtils.addRandomMeals;
import static de.luisdeltoro.calorie.util.TestUtils.extractIdAsUUID;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Tests for the {@link JournalController}
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class JournalControllerTest {

    @Autowired
    private MockMvc mvc;
    @Autowired
    private JournalRepository journalRepository;
    @Autowired
    private ObjectMapper objectMapper;


    @Test
    public void createJournal() throws Exception {
        final JournalDTO journalDto = new JournalDTO("Bruce Lee", 33, 170, 78);
        // Given: no calorie in the system
        // When: creating a calorie journal
        String journalLocation = mvc.perform(MockMvcRequestBuilders.post(JOURNALS_PATH)
                .contentType(MIME_HEADER)
                .content(objectMapper.writeValueAsString(journalDto))

        )
                .andExpect(status().isCreated())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(JOURNALS_PATH)))
                .andReturn().getResponse().getHeader(HttpHeaders.LOCATION);

        // Then: the calorie journal is stored in the system
        Optional<Journal> journal = journalRepository.findByBusinessId(extractIdAsUUID(journalLocation));
        assertThat(journal.isPresent(), is(true));
        assertThat(journal.get().getPerson().getName(), is(equalTo(journalDto.getPersonName())));
        assertThat(journal.get().getPerson().getAge(), is(equalTo(journalDto.getPersonAge())));
        assertThat(journal.get().getPerson().getHeight(), is(equalTo(journalDto.getPersonHeight())));
        assertThat(journal.get().getPerson().getWeight(), is(equalTo(journalDto.getPersonWeight())));
    }

    @Test
    public void getExistingDocument() throws Exception {
        // Given: an existing calorie journal in the system
        Journal journal = new Journal(new Person("Bruce Lee", 33, 170, 78));
        journalRepository.save(journal);

        // When: retrieving the calorie journal
        // Then: the right calorie journal is retrieved
        mvc.perform(MockMvcRequestBuilders.get(JOURNALS_PATH + "/" + journal.getBusinessId()).accept(MIME_HEADER)
                .contentType(MIME_HEADER)

        )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MIME_HEADER + ";charset=UTF-8"))
                .andExpect(jsonPath("$.personName", is("Bruce Lee")))
                .andExpect(jsonPath("$.personAge", is(33)))
                .andExpect(jsonPath("$.personHeight", is(170)))
                .andExpect(jsonPath("$.personWeight", is(78)));
    }

    @Test
    public void getNonExistingDocument() throws Exception {
        // Given: no calorie journal in the system

        // When: retrieving a calorie journal with a non-existing id
        // Then: the calorie journal cannot be found
        mvc.perform(MockMvcRequestBuilders.get(JOURNALS_PATH + "/" + UUID.randomUUID()).accept(MIME_HEADER)
                .contentType(MIME_HEADER)

        )
                .andExpect(status().isNotFound());
    }

    @Test
    public void calculateStats() throws Exception {
        Person person = new Person("Bruce Lee", 33, 170, 78);
        Journal journal = new Journal(person);
        addRandomMeals(journal);
        journalRepository.save(journal);

        mvc.perform(MockMvcRequestBuilders.post(JOURNALS_PATH + "/" + journal.getBusinessId() + CALCULATE_PATH)
                .contentType(MIME_HEADER)
        )
                .andExpect(status().isAccepted())
                .andExpect(header().string(HttpHeaders.LOCATION, containsString(JOBS_PATH)));
    }

}