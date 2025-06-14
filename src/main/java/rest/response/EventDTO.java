package rest.response;

import com.fasterxml.jackson.annotation.*;
import dcr.common.data.types.*;
import dcr.runtime.elements.events.ComputationEventInstance;
import dcr.runtime.elements.events.InputEventInstance;

public class EventDTO {

    @JsonProperty(value = "id", required = true)
    private final String id;
    @JsonProperty(value = "action", required = true)
    private final String action;
    @JsonProperty(value = "kind", required = true)
    private final KindDTO kind;
    @JsonProperty(value = "marking", required = true)
    private MarkingDTO marking;
    @JsonProperty(value = "typeExpr", required = true)
    private TypeDTO typeExpr;

    public EventDTO(ComputationEventInstance event) {
        this.id = event.remoteID();
        this.action = "computation";
        this.kind = event.receivers().isPresent() ? KindDTO.COMPUTATION_SEND : KindDTO.COMPUTATION;
        this.typeExpr = Mappers.fromType(event.baseElement().valueType());
        this.marking = Mappers.fromMarking(event.marking());
    }

    public EventDTO(InputEventInstance event) {
        this.id = event.remoteID();
        this.action = "input";
        this.kind = event.receivers().isPresent() ? KindDTO.INPUT_SEND : KindDTO.INPUT;

        this.typeExpr = Mappers.fromType(event.baseElement().valueType());
        this.marking = Mappers.fromMarking(event.marking());

    }
}

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
record MarkingDTO(
        @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "hasExecuted",
                required = true) boolean hasExecuted,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "isIncluded",
                required = true) boolean isIncluded,
        @JsonInclude(JsonInclude.Include.NON_EMPTY) @JsonProperty(value = "isPending",
                required = true) boolean isPending,
        @JsonProperty("value") ValueDTO value) {
}

enum KindDTO {
    COMPUTATION("computation-action"),
    COMPUTATION_SEND("computation-send"),
    INPUT_SEND("input-send"),
    INPUT("input-action");

    @JsonProperty(value = "value")
    private final String value;

    @JsonCreator
    KindDTO(@JsonProperty(value = "value") String v) {
        value = v;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}