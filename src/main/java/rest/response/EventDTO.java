package rest.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import dcr.common.data.types.*;
import dcr.runtime.elements.events.ComputationEventInstance;
import dcr.runtime.elements.events.EventInstance;
import dcr.runtime.elements.events.InputEventInstance;

public class EventDTO {

    @JsonProperty(value = "id", required = true)
    private final String id;
    @JsonProperty(value = "action", required = true)
    private final String action;
    @JsonProperty(value = "kind", required = true)
    private final KindDTO kind;
    //    private String initiator
    @JsonProperty(value = "marking", required = true)
    private MarkingDTO marking;
    @JsonProperty(value = "typeExpr", required = true)
    private TypeDTO typeExpr;



    public EventDTO(ComputationEventInstance event) {
        this.id = event.remoteID();
        this.action = "computation";
        this.kind = event.receivers().isPresent() ? KindDTO.COMPUTATION_SEND : KindDTO.COMPUTATION;
        this.typeExpr = new TypeDTO(event.valueType());
        this.marking = Mappers.fromMarking(event.marking());
    }

    public EventDTO(InputEventInstance event) {
        this.id = event.remoteID();
        this.action = "input";
        this.kind = event.receivers().isPresent() ? KindDTO.INPUT_SEND : KindDTO.INPUT;

        this.typeExpr = new TypeDTO(event.valueType());
        this.marking = Mappers.fromMarking(event.marking());

    }

//    public KindDTO getKind() {
//        return kind;
//    }
//
//    public String getAction() {
//        return action;
//    }
//
//    public String getId() {
//        return id;
//    }
//
//    public MarkingDTO getMarking() {
//        return marking;
//    }
//
//    public TypeDTO getType() {
//        return typeExpr;
//    }
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

//class MarkingDTO {
//    private boolean executed;
//    private boolean pending;
//    private boolean included;
//    private ValueDTO value;
//
//    public MarkingDTO(Event.Marking m) {
//        this.executed = m.hasExecuted();
//        this.pending = m.isPending();
//        this.included = m.isIncluded();
//        this.value = m.value();
//    }
//    public ValueDTO getValue() {
//        return value;
//    }
//
//    public boolean isExecuted() {
//        return executed;
//    }
//
//    public boolean isPending() {
//        return pending;
//    }
//
//    public boolean isIncluded() {
//        return included;
//    }
//}


class TypeDTO {
    private String type;

    TypeDTO(Type t) {
        this.type = switch (t) {
            case BooleanType b -> "Boolean";
            case IntegerType i -> "Integer";
            case RecordType r -> "Record";
            case EventType eventType -> "EventTy";
            case StringType stringType -> "String";
            case VoidType voidType -> "Unit";
        };
    }

    public String getType() {
        return type;
    }
}

enum KindDTO {
    COMPUTATION("computation-action"),
    COMPUTATION_SEND("computation-send"),
    INPUT_SEND("input-send"),
    INPUT("input-action");

    private final String value;

    KindDTO(String v) {
        value = v;
    }

    public String getValue() {
        return value;
    }
}