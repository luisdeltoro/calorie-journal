package de.luisdeltoro.calorie.ws.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Data Transfer Object for jobs
 */
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class JobDTO {

    /**
     * Describes the state of this job
     */
    public enum State {
        @JsonProperty("processing")
        PROCESSING,
        @JsonProperty("done")
        DONE,
        @JsonProperty("failed")
        FAILED
    }

    /**
     * Builds a complete instance
     * @param state the state of the job
     */
    public JobDTO(State state) {
        this.state = state;
    }

    private State state;
    private String error;

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "JobDTO{" +
                "state=" + state +
                ", error='" + error + '\'' +
                '}';
    }
}
