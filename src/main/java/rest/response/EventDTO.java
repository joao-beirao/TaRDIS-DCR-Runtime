package rest.response;

import com.fasterxml.jackson.annotation.*;

public class EventDTO {
    private static final String COMPUTATION_ACTION = "computation";
    private static final String INPUT_ACTION = "input";

    @JsonProperty(value = "id", required = true)
    private final String id;
    @JsonProperty(value = "action", required = true)
    private final String action;
    @JsonProperty(value = "kind", required = true)
    private final KindDTO kind;
    @JsonProperty(value = "marking", required = true)
    private final MarkingDTO marking;
    @JsonProperty(value = "typeExpr", required = true)
    private final TypeDTO typeExpr;

    private EventDTO(String id, String action, TypeDTO typeExpr, KindDTO kind,
                     MarkingDTO marking) {
        this.id = id;
        this.action = action;
        this.typeExpr = typeExpr;
        this.kind = kind;
        this.marking = marking;
    }

    static EventDTO newComputationEventDTO(String id, TypeDTO typeExpr, KindDTO kind,
                                           MarkingDTO marking) {
        return new EventDTO(id, COMPUTATION_ACTION, typeExpr, kind, marking);
    }

    static EventDTO newInputEventDTO(String id, TypeDTO typeExpr, KindDTO kind,
                                     MarkingDTO marking) {
        return new EventDTO(id, INPUT_ACTION, typeExpr, kind, marking);
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
    COMPUTATION("computation-action"), COMPUTATION_SEND("computation-send"),
    INPUT_SEND("input-send"), INPUT("input-action");

    @JsonProperty(value = "value")
    private final String value;

    @JsonCreator KindDTO(@JsonProperty(value = "value") String v) {
        value = v;
    }

    @JsonValue
    public String getValue() {
        return value;
    }
}