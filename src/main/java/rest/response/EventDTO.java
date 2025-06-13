package rest.response;

import dcr.common.data.types.*;
import dcr.common.data.values.*;
import dcr.common.events.Event;
import dcr.runtime.elements.events.ComputationEventInstance;
import dcr.runtime.elements.events.EventInstance;
import dcr.runtime.elements.events.InputEventInstance;
import deprecated.dcr.ast.values.UnitVal;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class EventDTO {

    private final String id;
    private final String action;
    private KindDTO kind;
//    private String initiator;
//    private String receivers;
    private MarkingDTO marking;
    private TypeDTO type;

//    public EventDTO( Event e) {
//        this.id = e.remoteID();
//        this.action = "unknown";
//        this.kind = switch ()
//
//    }

    public EventDTO(ComputationEventInstance event) {
        this.action = "computation";
        this.id = event.remoteID();
        this.kind = event.receivers().isPresent() ? KindDTO.COMPUTATION_SEND:KindDTO.COMPUTATION;


    }
    public EventDTO(InputEventInstance event) {
        this.id = event.remoteID();
        this.action = "input";
        this.kind= event.receivers().isPresent() ? KindDTO.INPUT_SEND:KindDTO.INPUT;
        this.marking = new MarkingDTO(event.marking());
        this.type = new TypeDTO(event.valueType());

    }

    public KindDTO getKind() {
        return kind;
    }

    public String getAction() {
        return action;
    }

    public String getId() {
        return id;
    }

    public MarkingDTO getMarking() {
        return marking;
    }

    public TypeDTO getType() {
        return type;
    }
}

class MarkingDTO {
    private boolean executed;
    private boolean pending;
    private boolean included;
    private ValueDTO value;

    public MarkingDTO(Event.Marking m) {
        this.executed = m.hasExecuted();
        this.pending = m.isPending();
        this.included = m.isIncluded();
        this.value = ValueDTO.toValueDTO(m.value());
    }
    public ValueDTO getValue() {
        return value;
    }

    public boolean isExecuted() {
        return executed;
    }

    public boolean isPending() {
        return pending;
    }

    public boolean isIncluded() {
        return included;
    }
}



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
//class UnitTypeDTO extends TypeDTO {
//
//    UnitTypeDTO() {
//        super("UnitType");
//    }
//}


enum KindDTO {
    COMPUTATION("computation-action"),
    COMPUTATION_SEND("computation-send"),
    INPUT_SEND ( "input-send"),
    INPUT("input-action");

    private final String value;
    KindDTO( String v) {
        value = v;
    }
    public String getValue() {
        return value;
    }
}


