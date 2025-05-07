package app1.presentation.endpoint;

import app1.presentation.endpoint.events.EventDTO;
import app1.presentation.endpoint.relations.RelationDTO;
import com.fasterxml.jackson.annotation.*;

import java.util.Collections;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@JsonTypeInfo(use = JsonTypeInfo.Id.DEDUCTION)
// @JsonTypeInfo(include = JsonTypeInfo.As.WRAPPER_OBJECT, use = JsonTypeInfo.Id.NAME)
// @JsonTypeName("graph")
public record GraphDTO(
        @JsonProperty(value = "events") List<EventDTO> events,
        @JsonProperty(value = "relations") List<RelationDTO> relations) {
    @JsonCreator
    public GraphDTO(@JsonProperty(value = "events") List<EventDTO> events,
            @JsonProperty(value = "relations") List<RelationDTO> relations) {
        this.events = (events == null || events.isEmpty())
                ? Collections.emptyList()
                : Collections.unmodifiableList(events);
        this.relations = (relations == null || relations.isEmpty())
                ? Collections.emptyList()
                : Collections.unmodifiableList(relations);
    }
}
